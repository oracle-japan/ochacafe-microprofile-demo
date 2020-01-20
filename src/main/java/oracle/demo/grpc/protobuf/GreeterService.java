package oracle.demo.grpc.protobuf;

import io.grpc.stub.StreamObserver;
import oracle.demo.grpc.protobuf.helloworld.Helloworld.HelloReply;
import oracle.demo.grpc.protobuf.helloworld.Helloworld.HelloRequest;
import oracle.demo.grpc.protobuf.helloworld.GreeterGrpc;

/**
 * gRPC service interoperable with https://grpc.io/docs/quickstart/java/
 * normal implmentation with GrpcMpExtension version
 */
class GreeterService extends GreeterGrpc.GreeterImplBase { 

    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> observer) {
        System.out.println("gRPC GreeterService called - name: " + req.getName());
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
        observer.onNext(reply);
        observer.onCompleted();        
    }

}

