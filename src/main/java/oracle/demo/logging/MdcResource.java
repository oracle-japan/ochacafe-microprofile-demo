package oracle.demo.logging;

import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


/**
 * JPA version of "Country", supports CRUD
 * insert Opentracing span
 */
@ApplicationScoped
@Path("/logging")
@Produces(MediaType.APPLICATION_JSON)
public class MdcResource {

    private final Logger logger = Logger.getLogger(MdcResource.class.getName());

    @Inject private Sub sub;

    @GET @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String nomdc(){
        return call();
    }

    @Mdc
    @GET @Path("/mdc")
    @Produces(MediaType.TEXT_PLAIN)
    public String mdc(){
        return call();
    }

    public String call(){
        logger.info("Invoking Sub#get()");
        final String result = sub.get();
        logger.info("Ended Sub#get()");
        return result;
    }

}
