package oracle.demo.scheduling;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

import io.helidon.microprofile.scheduling.FixedRate;
import io.helidon.microprofile.scheduling.Scheduled;

@ApplicationScoped
public class Scheduler {

    private static final Logger logger = Logger.getLogger(Scheduler.class.getName());

    private void log(){
        final String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        for(Method method : Scheduler.class.getDeclaredMethods()){
            if(method.getName().equals(methodName)){
                final Scheduled scheduled = method.getAnnotation(Scheduled.class);
                if(null != scheduled){
                    final String scheduledValue = scheduled.value();
                    logger.info(String.format("Scheduled event (%s) - @Scheduled(\"%s\")", methodName, scheduledValue));
                    return;
                }
                final FixedRate fixedRate = method.getAnnotation(FixedRate.class);
                if(null != fixedRate){
                    final long interval = fixedRate.value();
                    final long initialDelay = fixedRate.initialDelay();
                    final TimeUnit timeUnit = fixedRate.timeUnit();
                    logger.info(String.format("Scheduled event (%s) - @FixedRate(initialDelay=%d, value(interval)=%d, timeUnit=%s)",
                                        methodName, initialDelay, interval, timeUnit));
                    return;
                }
            }
        }
    }

    //@FixedRate(initialDelay = 2, value = 3, timeUnit = TimeUnit.MINUTES)
    public void fixedRate0() {
        log();
    }    

    //@Scheduled("0/30  * * ? * *")
    private void scheduled0(){
        log();
    }

    //@Scheduled("15,45 * * ? * *")
    private void scheduled1(){
        log();
    }


}
