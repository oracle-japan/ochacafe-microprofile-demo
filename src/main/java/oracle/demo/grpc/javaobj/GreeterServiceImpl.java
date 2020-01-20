package oracle.demo.grpc.javaobj;

import javax.enterprise.context.ApplicationScoped;

import io.helidon.microprofile.grpc.core.RpcService;
import io.helidon.microprofile.grpc.core.Unary;

@RpcService(name = "Greeter")
@ApplicationScoped
public class GreeterServiceImpl implements GreeterService { 

    @Unary
    @Override
    public String sayHello(String request) {
        System.out.println("gRPC GreeterServiceImpl called - name: " + request);
        return "Hello " + request;
    }

}

