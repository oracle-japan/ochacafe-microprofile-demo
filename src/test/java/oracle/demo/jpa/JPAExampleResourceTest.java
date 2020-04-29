package oracle.demo.jpa;

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

class JPAExampleResourceTest extends TestBase{

    private final Client client = ClientBuilder.newClient();

    public JPAExampleResourceTest(){
        super();
    }

    @Test
    public void testGetResponse(){
        String name = client
                .target(getConnectionString("/jpa/example/response/Marco"))
                .request()
                .get(String.class);
        Assertions.assertEquals("Polo", name);

        Response response = client
                .target(getConnectionString("/jpa/example/response/Paul"))
                .request()
                .get();
        Assertions.assertEquals("", response.readEntity(String.class));
    }


}
