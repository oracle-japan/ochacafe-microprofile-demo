package oracle.demo.health;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST endpoint to change timeToFail value
 */
@Path("/myhealth")
@ApplicationScoped
public class HealthCheckResource {

    private final Logger logger = Logger.getLogger(HealthCheckResource.class.getName());

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String resetTimeToFail(@QueryParam("timeToFail") long timeToFail){
        logger.info(String.format("timeToFail: %d (sec)", timeToFail));
        if(timeToFail < 0){
            throw new IllegalArgumentException("timeToFail must be zero or positive.");
        }
        HealthCheckHelper.set(timeToFail);
        return Long.toString(timeToFail);
    }

}

