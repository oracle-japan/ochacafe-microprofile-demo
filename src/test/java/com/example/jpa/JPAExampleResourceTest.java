package com.example.jpa;

import jakarta.inject.Inject;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.testing.junit5.AddConfig;
import io.helidon.microprofile.testing.junit5.HelidonTest;

@HelidonTest
@AddConfig(key = "tracing.enabled", value = "false")
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
