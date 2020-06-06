package oracle.demo.grpc.javaobj;

import io.helidon.microprofile.grpc.core.Grpc;
import io.helidon.microprofile.grpc.core.Unary;

@Grpc(name = "Greeter")
public interface GreeterService { 

    @Unary
    public String sayHello(String request);

}

