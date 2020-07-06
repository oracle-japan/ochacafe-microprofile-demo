package oracle.demo.grpc.javaobj;

import io.helidon.microprofile.grpc.client.GrpcChannel;
import io.helidon.microprofile.grpc.core.Grpc;
import io.helidon.microprofile.grpc.core.Unary;

@Grpc(name = "Greeter")
@GrpcChannel(name = "myserver") // defined in application.yaml
public interface GreeterService { 

    @Unary
    public String sayHello(String request);

}

