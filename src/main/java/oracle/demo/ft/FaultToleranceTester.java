package oracle.demo.ft;

import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

/**
 * FaultTlerance demo tester
 */
public class FaultToleranceTester {
    public static void main(String[] args) {
        final String serverUrl = (args.length > 1) ? args[1] : "http://localhost:8080/ft/circuit-breaker";
        final int numCalls = Integer.parseInt(args[0]);
        final AtomicInteger counter = new AtomicInteger();
        for (int i = 0 ; i < numCalls ; i++) {
            new Thread(new Runnable() {
                int count = counter.incrementAndGet();
                public void run() {
                    try {
                        System.out.println(count + ": Calling " + serverUrl);
                        Client client = ClientBuilder.newClient();
                        Response response = client.target(serverUrl).request().get();
                        System.out.println(count + ": Response code: " + response.getStatusInfo());
                    } catch (Exception e) {
                        System.out.println(count + ": Error: " + e.getMessage());
                    }
                }
            }).start();
        }
    }
}

/*
java -cp target/helidon-demo-mp.jar oracle.demo.ft.FaultToleranceTester 1 http://localhost:8080/ft/bulkhead

java -cp target/helidon-demo-mp.jar oracle.demo.ft.FaultToleranceTester 1

 */