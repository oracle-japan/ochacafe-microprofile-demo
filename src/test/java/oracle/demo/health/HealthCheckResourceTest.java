package oracle.demo.health;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import oracle.demo.TestBase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HealthCheckResourceTest extends TestBase{

    private final Client client = ClientBuilder.newClient();

    public HealthCheckResourceTest(){
        super();
    }

    @Test
    public void testHealthCheck(){
        client.target(getConnectionString("/mphealth?timeToFail=2")).request().get();

        Response response = client.target(getConnectionString("/health")).request().get();
        //System.out.println(response.readEntity(String.class));
        Assertions.assertEquals(200, response.getStatus()); 

        try{
            Thread.sleep(3 * 1000); // more than 2000msec
        }catch(InterruptedException ie){}
        response = client.target(getConnectionString("/health")).request().get();
        //System.out.println(response.readEntity(String.class));
        Assertions.assertEquals(503, response.getStatus()); 

        client.target(getConnectionString("/mphealth?timeToFail=0")).request().get();
        response = client.target(getConnectionString("/health")).request().get();
        //System.out.println(response.readEntity(String.class));
        Assertions.assertEquals(200, response.getStatus()); 
    }



}
