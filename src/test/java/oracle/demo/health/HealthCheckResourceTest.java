package oracle.demo.health;

import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
public class HealthCheckResourceTest{

    @Inject private WebTarget webTarget;

    @Test
    public void testHealthCheck(){
        webTarget.path("/mphealth").queryParam("timeToFail", 2).request().get();

        Response response = webTarget.path("/health").request().get();
        //System.out.println(response.readEntity(String.class));
        Assertions.assertEquals(200, response.getStatus()); 

        try{
            Thread.sleep(3 * 1000); // more than 2000msec
        }catch(InterruptedException ie){}
        response = webTarget.path("/health").request().get();
        //System.out.println(response.readEntity(String.class));
        Assertions.assertEquals(503, response.getStatus()); 

        webTarget.path("/mphealth").queryParam("timeToFail", 0).request().get();
        response = webTarget.path("/health").request().get();
        //System.out.println(response.readEntity(String.class));
        Assertions.assertEquals(200, response.getStatus()); 
    }



}
