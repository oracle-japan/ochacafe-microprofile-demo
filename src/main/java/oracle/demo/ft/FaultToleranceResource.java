package oracle.demo.ft;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ignore) {}
    }

    // 1024 is overwited to 3 by config file: oracle.demo.ft.FaultToleranceResource/bulkhead/Bulkhead/value=3
    @Bulkhead(1024) 
    @GET @Path("/bulkhead")
    @Produces(MediaType.TEXT_PLAIN)
    public String bulkhead() {
        log("[START /ft/bulkhead]");
        sleep();
        log("[END   /ft/bulkhead]");
        return "OK";
    }

    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.75, delay = 10 * 1000, successThreshold = 5)
    @Bulkhead(3)
    @GET @Path("/circuit-breaker")
    @Produces(MediaType.TEXT_PLAIN)
    public String circuitBreaker(){
        log("[START /ft/circuit-breaker]");
        sleep();
        log("[END   /ft/circuit-breaker]");
        return "OK";
    }
    // java -cp ./target/helidon-mp-demo.jar oracle.demo.ft.FaultToleranceTester <n>
}

