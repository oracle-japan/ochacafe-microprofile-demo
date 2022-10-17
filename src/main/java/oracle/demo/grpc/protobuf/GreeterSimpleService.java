package oracle.demo.grpc.protobuf;

import jakarta.enterprise.context.ApplicationScoped;

import io.helidon.microprofile.grpc.core.Grpc;
import io.helidon.microprofile.grpc.core.Unary;
import oracle.demo.grpc.protobuf.helloworld.Helloworld.HelloReply;
import oracle.demo.grpc.protobuf.helloworld.Helloworld.HelloRequest;

/**
 * gRPC service interoperable with https://grpc.io/docs/quickstart/java/
 * simple POJO + Annotation version 
 */
@Grpc(name = "helloworld.Greeter")
@ApplicationScoped
public class GreeterSimpleService{

    @Unary(name = "SayHello")
    public HelloReply sayHello(HelloRequest req) {
        System.out.println("gRPC GreeterSimpleService called - name: " + req.getName());
        return HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
    }
}

