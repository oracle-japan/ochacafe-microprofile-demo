package oracle.demo.lra;

import java.net.URI;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

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
        return Response.ok().build();
    }

}