package oracle.demo.health;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

/**
 * HealthCheck demo, health check reports NG after timeToFail sec
 */
@ApplicationScoped
public class MyHealthCheck {

    private final Logger logger = Logger.getLogger(MyHealthCheck.class.getName());

    private final String nameLiveness;
    private final String nameReadiness;
    private long timeToFail;

    @Inject
    public MyHealthCheck(
        @ConfigProperty(name="demo.healthcheck.liveness.name", defaultValue="my-health-check-liveness") String nameLiveness,
        @ConfigProperty(name="demo.healthcheck.readiness.name", defaultValue="my-health-check-readiness") String nameReadiness,
        @ConfigProperty(name="demo.healthcheck.time-to-fail", defaultValue="0") int timeToFail
        ) {
        this.nameLiveness = nameLiveness;
        this.nameReadiness = nameReadiness;
        this.timeToFail = timeToFail * 1000;
    }

    @Produces
    @Liveness
    public HealthCheck checkLivenss() {

        return () -> {
            final long uptime = HealthCheckHelper.getUptime();
            final long requestedTimeToFail = HealthCheckHelper.get();
            if(requestedTimeToFail != -1) timeToFail = requestedTimeToFail;
    
            logger.info(String.format("time-to=fail=%d, uptime=%d", timeToFail, uptime));
    
            return  HealthCheckResponse
                .named(nameLiveness)
                .withData("uptime", uptime)
                .withData("time-to-fail", timeToFail)
                .state(0 == timeToFail ? true : timeToFail > uptime)
                .build();
        };
    }

    @Produces
    @Readiness
    public HealthCheck checkReadiness() {
        return () -> HealthCheckResponse.named(nameReadiness).up().build();
    }


}

/* output example
        {
            "name": "my-health-check-liveness",
            "state": "DOWN",
            "status": "DOWN",
            "data": {
                "process": "20273@base-instance.sub01150600002.basevcn.oraclevcn.com",
                "time-to-fail": 30000,
                "uptime": 76291
            }
        }
*/