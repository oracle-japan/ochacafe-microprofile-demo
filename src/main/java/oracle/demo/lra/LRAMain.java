package oracle.demo.lra;

import java.net.URI;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Response.Status;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.lra.LRAResponse;
import org.eclipse.microprofile.lra.annotation.AfterLRA;
import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.LRAStatus;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;

import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;

@Path("/lra-main")
@ApplicationScoped
public class LRAMain {

    private static final Logger logger = Logger.getLogger(LRAMain.class.getName());

    private final Client client = ClientBuilder.newBuilder().connectTimeout(5, TimeUnit.SECONDS).build();

    private final StatusMonitor statusMonitor = new StatusMonitor();

    @Inject
    public LRAMain(@ConfigProperty(name = "mp.lra.coordinator.url") String coordinatorURL){
        logger.info("mp.lra.coordinator.url: " + coordinatorURL);
    }

    @LRA(
        value = LRA.Type.REQUIRES_NEW,
        timeLimit = 3000, timeUnit = ChronoUnit.MILLIS
    )
    @POST
    @Path("start")
    public Response start(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId, String[] urls, @QueryParam("raise-error") boolean raiseError){
        logger.log(Level.INFO, "LRA id: {0} started", lraId);

        for(String url : urls){
            logger.info(url + " <- calling");
            Response response = client.target(url).request().get();
            logger.info(url + " -> " + response.getStatus() + " " + response.getStatusInfo());
            if(2 != response.getStatus() / 100){
                String message = String.format("Request failed: (%d) %s", response.getStatus(), url);
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message).build();
            }
        }

        if(raiseError)
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Error by request").build();
            
        return Response.ok("OK").build();
    }

    @PUT
    @Complete
    @Path("complete")
    public Response complete(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId){
        logger.log(Level.INFO, "LRA id: {0} completed 🎉", lraId);
        return LRAResponse.completed();
    }

    @PUT
    @Compensate
    @Path("compensate")
    public Response compensate(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId){
        logger.log(Level.SEVERE, "LRA id: {0} compensated 🚒", lraId);
        return LRAResponse.compensated();
    }

    @PUT
    @AfterLRA
    @Path("after")
    public Response notifyLRAFinished(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId, LRAStatus status) {
        logger.log(Level.INFO, "LRA id: {0} ended with status \"{1}\"", new Object[]{lraId, status});
        statusMonitor.setStatus(lraId, status);
        return Response.ok().build();
    }


    ///////

    @POST
    @Path("monitor")
    @Produces(MediaType.TEXT_PLAIN)
    public void monitor(String[] urls, 
        @QueryParam("raise-error") boolean raiseError, 
        @Context UriInfo uriInfo, 
        @Context HttpHeaders httpHeaders,
        @Suspended AsyncResponse asyncResponse){
        

        new Thread(() -> {
            URI target = uriInfo.getBaseUriBuilder().path("lra-main/start").queryParam("raise-error", raiseError).build();
            logger.info("Calling LRA initiator: " + target);
    
            // forward header values "x-b3-", "oracle-tmm-", "authorization", "refresh-"
            //Response response = client.target(target).request().post(Entity.json(urls));
            Builder builder = client.target(target).request();
            httpHeaders.getRequestHeaders().entrySet().stream().forEach(h -> {
                final String key = h.getKey().toLowerCase();
                if(key.startsWith("x-b3-") || key.startsWith("oracle-tmm-")
                     || key.startsWith("authorization") || key.startsWith("refresh-")){
                        builder.header(h.getKey(), h.getValue());
                }
            });
            builder.header(LRA_HTTP_CONTEXT_HEADER, asyncResponse);
            Response response = builder.post(Entity.json(urls));
            logger.info("LRA initiator returned with status " + response.getStatus());
    
            // handle error case
            if(response.getStatus() / 100 != 2){
                Response acyncRes = Response.status(Status.INTERNAL_SERVER_ERROR).entity("Failed to forward request: " + response.getStatus()).build();
                asyncResponse.resume(acyncRes);
            }else{
                String lraId = response.getHeaderString(LRA_HTTP_CONTEXT_HEADER);
                String status = statusMonitor.getStatus(URI.create(lraId), 10*1000);
                logger.log(Level.INFO, "LRA id: {0} final status \"{1}\"", new Object[]{lraId, status});
                Response acyncRes = status.equals(LRAStatus.Closed.name()) ?
                    Response.ok(status).build() :
                    Response.status(Status.INTERNAL_SERVER_ERROR).entity(status).build();
                asyncResponse.resume(acyncRes);
            }

        }).start();

    }

    public static class StatusMonitor{

        private final ConcurrentHashMap<URI, Optional<LRAStatus>> trans = new ConcurrentHashMap<>();

        public synchronized String getStatus(URI lraId, long timeout){
            final long startTime = System.currentTimeMillis();
            while(true){
                Optional<LRAStatus> lraStatus = trans.get(lraId);
                if(lraStatus.isPresent()){
                    trans.remove(lraId);
                    return lraStatus.get().name();
                }else{
                    trans.put(lraId, Optional.empty());
                    try{
                        wait(timeout);
                    }catch(InterruptedException ignore){}
                }
                if(System.currentTimeMillis() - startTime > timeout){
                    trans.remove(lraId);
                    return "Unknown";
                } 
            }
        }

        public synchronized void setStatus(URI lraId, LRAStatus lraStatus){
            trans.put(lraId, Optional.of(lraStatus));
            notifyAll();
        } 

    }

}