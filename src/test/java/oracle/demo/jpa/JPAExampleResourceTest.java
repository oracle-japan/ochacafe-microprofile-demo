package oracle.demo.jpa;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import oracle.demo.TestBase;

import org.junit.jupiter.api.Assertions;
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
