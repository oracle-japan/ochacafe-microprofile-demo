package oracle.demo.grpc;

import java.util.stream.Stream;

import io.helidon.grpc.api.Grpc;
import io.grpc.stub.StreamObserver;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;

@Grpc.GrpcService("helloworld.Greeter")
@Grpc.GrpcChannel("helloworld-channel")
public interface HelloWorldServiceClient {

    @Grpc.Unary("SayHello")
    HelloReply sayHello(HelloRequest request);

    @Grpc.ServerStreaming("SayHelloStreamReply")
    Stream<HelloReply> sayHelloStreamReply(HelloRequest request);

    @Grpc.Bidirectional("SayHelloBidiStream")
    public StreamObserver<HelloRequest> sayHelloBidiStream(StreamObserver<HelloReply> observer);
    
}

