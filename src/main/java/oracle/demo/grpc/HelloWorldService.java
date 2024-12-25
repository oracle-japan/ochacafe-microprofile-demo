package oracle.demo.grpc;

import java.util.logging.Logger;
import java.util.stream.Stream;

import io.helidon.grpc.api.Grpc;

import io.grpc.stub.StreamObserver;
import jakarta.enterprise.context.ApplicationScoped;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;


@Grpc.GrpcService("helloworld.Greeter")
@ApplicationScoped
public class HelloWorldService {

    private final Logger logger = Logger.getLogger(HelloWorldService.class.getName());

    @Grpc.Unary("SayHello")
    public HelloReply sayHello(HelloRequest request) {
        logger.info("gRPC SayHello - name: " + request.getName());
        String reply = "Hello " + request.getName();
        return HelloReply.newBuilder().setMessage(reply).build();
    }

    @Grpc.ServerStreaming("SayHelloStreamReply")
    public Stream<HelloReply> sayHelloStreamReply(HelloRequest request) {
        String name = request.getName();
        logger.info("gRPC SayHelloStreamReply - name: " + name);
        String[] parts = {"Hello", name};
        return Stream.of(parts).map(s -> HelloReply.newBuilder().setMessage(s).build());
    }

    @Grpc.Bidirectional("SayHelloBidiStream")
    public StreamObserver<HelloRequest> sayHelloBidiStream(StreamObserver<HelloReply> observer) {
        logger.info("gRPC SayHelloBidiStream");
        logger.info("StreamObserver: " + observer.getClass().getName());
        return new HelloRequestStreamObserver(observer);
    }   


    public class HelloRequestStreamObserver implements StreamObserver<HelloRequest>{

        private final Logger logger = Logger.getLogger(HelloRequestStreamObserver.class.getName());

        private StreamObserver<HelloReply> reply;

        public HelloRequestStreamObserver(StreamObserver<HelloReply> reply){
            this.reply = reply;
        }

        @Override
        public void onNext(HelloRequest value) {
            logger.info("onNext(): " + value.getName());
            reply.onNext(HelloReply.newBuilder().setMessage(value.getName()).build());
        }

        @Override
        public void onError(Throwable t) {
            logger.warning("onError(): " + t.getMessage());
        }

        @Override
        public void onCompleted() {
            logger.info("onCompleted()");
            reply.onCompleted();
        }
        
    }


}

