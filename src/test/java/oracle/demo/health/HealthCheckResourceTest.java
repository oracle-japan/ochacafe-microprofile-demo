package oracle.demo.health;

import java.util.concurrent.TimeUnit;

import jakarta.inject.Inject;
import jakarta.json.JsonValue;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.AddConfig;
import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
@AddConfig(key = "tracing.enabled", value = "false")
@AddConfig(key = "demo.healthcheck.time-to-fail", value = "60") // seconds, need to keep enough time to start-up
public class HealthCheckResourceTest{

    @Inject private WebTarget webTarget;

    @Test
    public void testHealthCheck(){

        // check initial status
        Response response = webTarget.path("/health/live").request().get();
        check(response, 200, "UP");

        // wait for timeout
        System.out.println("Waiting for timeout...");
        long uptime = HealthCheckHelper.getUptime();
        sleep((60 * 1000L - uptime) / 1000 + 1L);
        response = webTarget.path("/health/live").request().get();
        check(response, 503, "DOWN");

        // reset timeout to 2 seconds
        webTarget.path("/myhealth").queryParam("timeToFail", 2).request().get();
        response = webTarget.path("/health/live").request().get();
        check(response, 200, "UP");

        // wait for timeout
        System.out.println("Waiting for timeout...");
        sleep(3);
        response = webTarget.path("/health/live").request().get();
        check(response, 503, "DOWN");

        // reset timeout
        webTarget.path("/myhealth").queryParam("timeToFail", 0).request().get();
        response = webTarget.path("/health/live").request().get();
        check(response, 200, "UP");
    }

    public void check(Response response, int status, String updown){
        JsonValue json = response.readEntity(JsonValue.class);
        System.out.println(json.toString());
        Assertions.assertEquals(status, response.getStatus()); 
        Assertions.assertEquals(updown, json.asJsonObject().getString("status")); 
    }

    private void sleep(long sec){
        System.out.format("Will sleep %d sec.%n", sec);
        try{
            TimeUnit.SECONDS.sleep(sec);
        }catch(Exception e){}
    }


}
