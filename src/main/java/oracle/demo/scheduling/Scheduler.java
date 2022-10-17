package oracle.demo.scheduling;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;

import io.helidon.microprofile.scheduling.FixedRate;
import io.helidon.microprofile.scheduling.Scheduled;
import io.helidon.scheduling.CronInvocation;
import io.helidon.scheduling.FixedRateInvocation;
import io.helidon.scheduling.Scheduling;

@ApplicationScoped
public class Scheduler {

    private static final Logger logger = Logger.getLogger(Scheduler.class.getName());

    //@FixedRate(initialDelay = 1, value = 2, timeUnit = TimeUnit.MINUTES)
    public void fixedRate0(FixedRateInvocation inv) {
        logger.info(inv.description());
    }    

    //@Scheduled("0/30  * * ? * *")
    private void scheduled0(CronInvocation inv){
        logger.info(inv.description());
    }

    //@Scheduled("15,45 * * ? * *")
    private void scheduled1(CronInvocation inv){
        logger.info(inv.description());
    }

}
