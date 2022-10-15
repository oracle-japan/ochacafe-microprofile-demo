package oracle.demo.lra;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.lra.LRAResponse;
import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;

import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;

@Path("/lra-service2")
@ApplicationScoped
public class LRAService2 {

    private static final Logger logger = Logger.getLogger(LRAService2.class.getName());

    @GET
    @LRA(value = LRA.Type.REQUIRED, end=false)
    @Path("serv")
    public Response serve(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId){
        logger.log(Level.INFO, "LRA id: {0} joined", lraId);
        logger.info("Done.");
        return Response.ok().build();
    }

    @GET
    @LRA(value = LRA.Type.REQUIRED, end=false)
    @Path("serv-slow")
    public Response servSlow(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId){
        logger.log(Level.INFO, "LRA id: {0} joined", lraId);

        try{
            TimeUnit.MILLISECONDS.sleep(5000);
        }catch(InterruptedException e){}
        logger.info("Done.");

        return Response.ok().build();
    }

    @GET
    @LRA(value = LRA.Type.REQUIRED, end=false)
    @Path("serv-error")
    public Response servError(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId){
        logger.log(Level.INFO, "LRA id: {0} joined", lraId);

        throw new RuntimeException("serv-error");
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

}