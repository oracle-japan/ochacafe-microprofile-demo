package oracle.demo.ft;

import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

/**
 * FaultTolerance demo tester
 */
public class FaultToleranceTester {
    public static void main(String[] args) {
        String endpoint = "http://localhost:8080/ft/circuit-breaker";
        int numCalls = -1;

        for(int i = 0 ; i < args.length ; i++){
            if(args[i].equals("--endpoint")){
                endpoint = args[++i];
            }else{
                numCalls = Integer.parseInt(args[i]);
            }
        }

        final AtomicInteger counter = new AtomicInteger();
        final String serverUri = endpoint;
        for (int i = 0 ; i < numCalls ; i++) {
            new Thread(new Runnable() {
                int count = counter.incrementAndGet();
                public void run() {
                    try {
                        System.out.println(count + ": Calling " + serverUri);
                        Client client = ClientBuilder.newClient();
                        Response response = client.target(serverUri).request().get();
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