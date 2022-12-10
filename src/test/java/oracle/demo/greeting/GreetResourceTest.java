package oracle.demo.greeting;

import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.AddConfig;
import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
@AddConfig(key = "tracing.enabled", value = "false")
public class GreetResourceTest{

    @Inject private WebTarget webTarget;

    @Test
    void testHelloWorld() {

        JsonObject jsonObject = webTarget.path("/greet")
                .request()
                .get(JsonObject.class);
        Assertions.assertEquals("Hello World!", jsonObject.getString("message"),
                "default message");

        jsonObject = webTarget.path("/greet/Joe")
                .request()
                .get(JsonObject.class);
        Assertions.assertEquals("Hello Joe!", jsonObject.getString("message"),
                "hello Joe message");

        Response r = webTarget.path("/greet/greeting")
                .request()
                .put(Entity.entity("{\"greeting\" : \"Hola\"}", MediaType.APPLICATION_JSON));
        Assertions.assertEquals(204, r.getStatus(), "PUT status code");

        jsonObject = webTarget.path("/greet/Jose")
                .request()
                .get(JsonObject.class);
        Assertions.assertEquals("Hola Jose!", jsonObject.getString("message"),
                "hola Jose message");

        r = webTarget.path("/metrics")
                .request()
                .get();
        Assertions.assertEquals(200, r.getStatus(), "GET metrics status code");

        r = webTarget.path("/health")
                .request()
                .get();
        Assertions.assertEquals(200, r.getStatus(), "GET health status code");
    }

}
