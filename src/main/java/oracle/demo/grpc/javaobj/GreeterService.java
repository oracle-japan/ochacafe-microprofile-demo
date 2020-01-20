package oracle.demo.grpc.javaobj;

import io.helidon.microprofile.grpc.core.RpcService;
import io.helidon.microprofile.grpc.core.Unary;

@RpcService(name = "Greeter")
public interface GreeterService { 

    @Unary
    public String sayHello(String request);

}

