package oracle.demo.grpc.protobuf;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import oracle.demo.grpc.protobuf.helloworld.GreeterGrpc;
import oracle.demo.grpc.protobuf.helloworld.Helloworld.HelloReply;
import oracle.demo.grpc.protobuf.helloworld.Helloworld.HelloRequest;

@Path("/grpc-protobuf")
@ApplicationScoped
public class GrpcResource {

    private final Logger logger = Logger.getLogger(GrpcResource.class.getName());

    @Inject @ConfigProperty(name = "grpc.port", defaultValue = "50051")
    private int port;

    @GET
    @Path("/client")
    @Produces(MediaType.TEXT_PLAIN)
    public String invoke(@QueryParam("name") String name) {

        final ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build();
        final GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel);

        HelloRequest request = HelloRequest.newBuilder().setName(Optional.ofNullable(name).orElse("world")).build();
        HelloReply response;
        try {
            response = blockingStub.sayHello(request);
            return response.getMessage();
        }catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            throw new RuntimeException(e.getMessage(), e); 
        }finally{
            try{
                channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            }catch(InterruptedException ignore){}
        }
    }
}
