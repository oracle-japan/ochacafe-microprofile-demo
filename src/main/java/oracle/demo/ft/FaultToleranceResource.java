package oracle.demo.ft;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;

/**
 * FaultTlerance demo
 */
@ApplicationScoped
@Path("/ft")
public class FaultToleranceResource {

    private final Logger logger = Logger.getLogger(FaultToleranceResource.class.getName());

    private void log(String message){
        final String thread = Thread.currentThread().getName();
        logger.info("[" + thread +"]" + message);
    }

    private void sleep(){
        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException ignore) {}
    }

    @Bulkhead(1024) // - will change value with Config property
    @GET
    @Path("/bulkhead")
    @Produces(MediaType.TEXT_PLAIN)
    public String bulkhead() {
        log("[START /ft/bulkhead]");
        sleep();
        log("[END   /ft/bulkhead]");
        return "OK";
    }

    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.75, delay = 10 * 1000, successThreshold = 5)
    @Bulkhead(3)
    @GET
    @Path("/circuit-breaker")
    @Produces(MediaType.TEXT_PLAIN)
    public String circuitBreaker(){
        log("[START /ft/circuit-breaker]");
        sleep();
        log("[END   /ft/circuit-breaker]");
        return "OK";
    }
    // java -cp ./target/helidon-demo-mp.jar oracle.demo.ft.FaultToleranceTester <n>
}

