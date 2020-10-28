package oracle.demo.jpa;

import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
public class JPAExampleResourceTest{

    @Inject private WebTarget webTarget;

    @Test
    public void testGetResponse(){
        String name = webTarget.path("/jpa/example/response/Marco")
                .request()
                .get(String.class);
        Assertions.assertEquals("Polo", name);

        Response response = webTarget.path("/jpa/example/response/Paul")
                .request()
                .get();
        Assertions.assertEquals("", response.readEntity(String.class));
    }


}
