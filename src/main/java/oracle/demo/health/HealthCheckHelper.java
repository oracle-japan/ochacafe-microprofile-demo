package oracle.demo.health;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simply keep the value of requested timeToFail, 
 *  accessed by MyHealthCheck and HealthCheckResource
 */
public class HealthCheckHelper {

    private final static AtomicLong timeToFail = new AtomicLong(-1);

    public static void set(long timeToFail){
        final RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        final long uptime = runtimeMXBean.getUptime();
        final long requestedTimeToFail = (0 == timeToFail) ? 0 : uptime + timeToFail * 1000;
        HealthCheckHelper.timeToFail.set(requestedTimeToFail);
    }

    public static long get(){
        return timeToFail.get();
    }

    // helper method
    public static long getUptime(){
        final RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        //final String process = runtimeMXBean.getName();
        return runtimeMXBean.getUptime();
    }

}

