package oracle.demo.grpc.protobuf;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.helidon.config.Config;
import oracle.demo.grpc.protobuf.helloworld.GreeterGrpc;
import oracle.demo.grpc.protobuf.helloworld.Helloworld.HelloReply;
import oracle.demo.grpc.protobuf.helloworld.Helloworld.HelloRequest;

@Path("/grpc-protobuf")
@ApplicationScoped
public class GrpcResource {

    private final Logger logger = Logger.getLogger(GrpcResource.class.getName());

    @GET
    @Path("/client")
    @Produces(MediaType.TEXT_PLAIN)
    public String invoke(@QueryParam("name") String name) {

        final int port = Config.create().get("grpc.port").asInt().get();
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
