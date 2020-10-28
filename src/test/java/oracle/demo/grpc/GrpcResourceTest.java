package oracle.demo.grpc;

import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
public class GrpcResourceTest{

    @Inject private WebTarget webTarget;

    @Test
    public void testGrpcJavaobj(){
        String result = webTarget.path("/grpc-javaobj/client")
                .request()
                .get(String.class);
        Assertions.assertEquals("Hello world", result);
        result = webTarget.path("/grpc-javaobj/client")
                .queryParam("name", "Tom")
                .request()
                .get(String.class);
                Assertions.assertEquals("Hello Tom", result);
            }

    @Test
    public void testGrpcProtobuf(){
        String result = webTarget.path("/grpc-protobuf/client")
                .request()
                .get(String.class);
        Assertions.assertEquals("Hello world", result);
        result = webTarget.path("/grpc-protobuf/client")
                .queryParam("name", "Tom")
                .request()
                .get(String.class);
                Assertions.assertEquals("Hello Tom", result);
            }

}
