package oracle.demo.health;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

/**
 * HealthCheck demo, health check reports NG after timeToFail sec
 */
// @Health - depricated, this is for Health Check 1.0
@Liveness @Readiness
@ApplicationScoped
public class MyHealthCheck implements HealthCheck {

    private final Logger logger = Logger.getLogger(MyHealthCheck.class.getName());

    @Inject @ConfigProperty(name="demo.healthcheck.name", defaultValue="my-health-check")
    private /*final*/ String name;

    @Inject @ConfigProperty(name="demo.healthcheck.time-to-fail", defaultValue="0")
    private /*final*/ long timeToFail;

    /* antoher way to get values from config
    @Inject
    public MyHealthCheck(
        @ConfigProperty(name="healthcheck.name", defaultValue="my-health-check") String name,
        @ConfigProperty(name="healthcheck.time-to-fail", defaultValue="0") int timeToFail
        ) {
        this.name = name;
        this.timeToFail = timeToFail;
    }
    */

    @Override
    public HealthCheckResponse call() {

        final RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        final String process = runtimeMXBean.getName();
        final long uptime = runtimeMXBean.getUptime();

        final long requestedTimeToFail = TimeToFail.get();
        if(requestedTimeToFail != -1) timeToFail = (0 == requestedTimeToFail) ? 0 : requestedTimeToFail;

        //return HealthCheckResponse.named(name).up().build();
        logger.fine("process: " + process);
        logger.info(String.format("time-to=fail=%d, uptime=%d", uptime, timeToFail));

        return  HealthCheckResponse
            .named(name) // since Health 2.1 & MP 3.2
            .withData("process", process)
            .withData("uptime", uptime)
            .withData("time-to-fail", timeToFail)
            //.up()
            .state(0 == timeToFail ? true : uptime < timeToFail)
            .build();
    }

}

/* output example
        {
            "name": "my-health-check",
            "state": "DOWN",
            "status": "DOWN",
            "data": {
                "process": "20273@base-instance.sub01150600002.basevcn.oraclevcn.com",
                "time-to-fail": 30000,
                "uptime": 76291
            }
        }
*/