package oracle.demo.health;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Simply keep the value of requested timeToFail, 
 *  accessed by MyHealthCheck and HealthCheckResource
 */
public class TimeToFail {

    private final static AtomicLong timeToFail = new AtomicLong(-1);

    public static void set(long timeToFail){
        TimeToFail.timeToFail.set(timeToFail);
    }

    public static long get(){
        return timeToFail.getAndSet(-1);
    }

}

