/*
 * Copyright (c) 2018, 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package oracle.demo;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
public class MainTest{

    @Inject private WebTarget webTarget;

    @Test
    void testHelloWorld() {

        Client client = ClientBuilder.newClient();

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
