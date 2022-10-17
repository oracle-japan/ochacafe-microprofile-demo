package oracle.demo.health;

import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

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

