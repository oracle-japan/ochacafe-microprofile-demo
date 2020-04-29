package oracle.demo.ft;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.helidon.microprofile.server.Server;
import oracle.demo.TestBase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class FaultToleranceResourceTest extends TestBase{

    private final Client client = ClientBuilder.newClient();

    public FaultToleranceResourceTest(){
        super();
    }

    @Test
    public void testBulkhead(){

        final int numCalls = 3;
        ExecutorService service = Executors.newFixedThreadPool(numCalls);
        for(int i = 0 ; i < numCalls ; i++){
            service.submit(() -> {
                final Client client = ClientBuilder.newClient();
                client.target(getConnectionString("/ft/bulkhead")).request().get();
                client.close();
            });
        }

        try{
            Thread.sleep(1000); // wait 1 sec to kick the 4th request
        }catch(InterruptedException e){}    
        Response response = client.target(getConnectionString("/ft/bulkhead")).request().get();
        Assertions.assertEquals(500, response.getStatus());

        service.shutdown();
        try{
            service.awaitTermination(10, TimeUnit.SECONDS);
        }catch(InterruptedException e){}    
    }

    @Test
    public void testCircuitBreaker(){

        final int numCalls = 6; // OK->OK->(OK->NG->NG->NG) = 75% failure 
        ExecutorService service = Executors.newFixedThreadPool(numCalls);
        for(int i = 0 ; i < numCalls ; i++){
            service.submit(() -> {
                final Client client = ClientBuilder.newClient();
                client.target(getConnectionString("/ft/circuit-breaker")).request().get();
                client.close();
            });
        }

        try{
            Thread.sleep(2000); // wait 2 secs to turn to open
        }catch(InterruptedException e){}    
        Response response = client.target(getConnectionString("/ft/circuit-breaker")).request().get();
        Assertions.assertEquals(500, response.getStatus());

        try{
            Thread.sleep(10 * 1000); // wait 10 secs to turn to half-open
        }catch(InterruptedException e){}    
        response = client.target(getConnectionString("/ft/circuit-breaker")).request().get();
        Assertions.assertEquals(200, response.getStatus());

        service.shutdown();
        try{
            service.awaitTermination(10, TimeUnit.SECONDS);
        }catch(InterruptedException e){}    
    }


}
