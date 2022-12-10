package oracle.demo.grpc;

import jakarta.inject.Inject;
import jakarta.ws.rs.client.WebTarget;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.AddConfig;
import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
@AddConfig(key = "tracing.enabled", value = "false")
public class GrpcResourceTest{

    @Inject private WebTarget webTarget;

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
