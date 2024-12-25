package com.example.grpc;

import jakarta.inject.Inject;
import jakarta.ws.rs.client.WebTarget;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.testing.junit5.HelidonTest;

@HelidonTest
public class HelloWorldResourceTest{

    @Inject private WebTarget webTarget;

    @Test
    void sayHello() {

        String reply = webTarget.path("/grpc/sayHello")
                .queryParam("port", webTarget.getUri().getPort())
                .request()
                .get(String.class);
        Assertions.assertEquals("Hello world", reply, "default message");

        reply = webTarget.path("/grpc/sayHello")
                .queryParam("name", "Bob")
                .queryParam("port", webTarget.getUri().getPort())
                .request()
                .get(String.class);
        Assertions.assertEquals("Hello Bob", reply);

    }

}
