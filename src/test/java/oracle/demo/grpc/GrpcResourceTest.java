package oracle.demo.grpc;

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

class GrpcResourceTest extends TestBase{

    private final Client client = ClientBuilder.newClient();

    public GrpcResourceTest(){
        super();
    }

    @Test
    public void testGrpcJavaobj(){
        String result = client
                .target(getConnectionString("/grpc-javaobj/client"))
                .request()
                .get(String.class);
        Assertions.assertEquals("Hello world", result);
        result = client
                .target(getConnectionString("/grpc-javaobj/client?name=Tom"))
                .request()
                .get(String.class);
                Assertions.assertEquals("Hello Tom", result);
            }

    @Test
    public void testGrpcProtobuf(){
        String result = client
                .target(getConnectionString("/grpc-protobuf/client"))
                .request()
                .get(String.class);
        Assertions.assertEquals("Hello world", result);
        result = client
                .target(getConnectionString("/grpc-protobuf/client?name=Tom"))
                .request()
                .get(String.class);
                Assertions.assertEquals("Hello Tom", result);
            }

}
