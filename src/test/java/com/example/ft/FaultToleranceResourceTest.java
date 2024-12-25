package com.example.ft;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jakarta.inject.Inject;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.testing.junit5.AddConfig;
import io.helidon.microprofile.testing.junit5.HelidonTest;

@HelidonTest
@AddConfig(key = "tracing.enabled", value = "false")
public class FaultToleranceResourceTest{

    @Inject private WebTarget webTarget;

    @Test
    public void testBulkhead(){

        final int numCalls = 3;
        ExecutorService service = Executors.newFixedThreadPool(numCalls);
        for(int i = 0 ; i < numCalls ; i++){
            service.submit(() -> {
                webTarget.path("/ft/bulkhead").request().get();
            });
        }

        try{
            Thread.sleep(1000); // wait 1 sec to kick the 4th request
        }catch(InterruptedException e){}    
        Response response = webTarget.path("/ft/bulkhead").request().get();
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
                webTarget.path("/ft/circuit-breaker").request().get();
            });
        }

        try{
            Thread.sleep(2000); // wait 2 secs to turn to open
        }catch(InterruptedException e){}    
        Response response = webTarget.path("/ft/circuit-breaker").request().get();
        Assertions.assertEquals(500, response.getStatus());

        try{
            Thread.sleep(10 * 1000); // wait 10 secs to turn to half-open
        }catch(InterruptedException e){}    
        response = webTarget.path("/ft/circuit-breaker").request().get();
        Assertions.assertEquals(200, response.getStatus());

        service.shutdown();
        try{
            service.awaitTermination(10, TimeUnit.SECONDS);
        }catch(InterruptedException e){}    
    }


}
