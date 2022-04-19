package oracle.demo.lra;

import java.net.URI;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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
        }

        if(raiseError)
            throw new RuntimeException();        
            
        return Response.ok().build();
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
        statusMonitor.setStauts(lraId, status);
        return Response.ok().build();
    }


    ///////

    @POST
    @Path("monitor")
    @Produces(MediaType.TEXT_PLAIN)
    public String monitor(String[] urls, @QueryParam("raise-error") boolean raiseError, @Context UriInfo uriInfo){

        URI target = uriInfo.getBaseUriBuilder().path("lra-main/start").queryParam("raise-error", raiseError).build();
        logger.info("Calling LRA initiator: " + target);

        Response response = client.target(target).request().post(Entity.json(urls));
        logger.info("LRA initiator returned with status " + response.getStatus());

        String lraId = response.getHeaderString(LRA_HTTP_CONTEXT_HEADER);
        String status = statusMonitor.getStatus(URI.create(lraId), 10*1000);
        logger.log(Level.INFO, "LRA id: {0} final status \"{1}\"", new Object[]{lraId, status});

        return status;
    }

    public static class StatusMonitor{

        private final ConcurrentHashMap<URI, LRAStatus> trans = new ConcurrentHashMap<>();

        public synchronized String getStatus(URI lraId, long timeout){
            //logger.info("getStatus()");
            final long startTime = System.currentTimeMillis();
            while(true){
                LRAStatus lraStatus = trans.get(lraId);
                logger.info("Status: " + lraStatus);
                if(Objects.nonNull(lraStatus)){
                    trans.remove(lraId);
                    return lraStatus.name();
                }else{
                    try{
                        //logger.info("Sleeping");
                        wait(timeout);
                    }catch(InterruptedException ignore){}
                }
                final long currentTime = System.currentTimeMillis(); 
                if(currentTime - startTime > timeout) return "Unknown";
            }
        }

        public synchronized void setStauts(URI lraId, LRAStatus lraStatus){
            //logger.info("setStatus()");
            trans.put(lraId, lraStatus);
            notifyAll();
        } 

    }

}