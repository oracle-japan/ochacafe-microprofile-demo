package oracle.demo.grpc;

import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.grpc.StatusRuntimeException;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import io.helidon.grpc.api.Grpc;
import io.helidon.microprofile.grpc.client.GrpcConfigurablePort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/grpc")
@ApplicationScoped
public class HelloWorldResource {

    private final Logger logger = Logger.getLogger(HelloWorldResource.class.getName());

    @Inject
    @Grpc.GrpcProxy
    private HelloWorldServiceClient client;

    @GET
    @Path("/sayHello")
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHello(@QueryParam("name") String name, @QueryParam("port") Integer port) {

        if(Objects.nonNull(port) && client instanceof GrpcConfigurablePort c) {
            c.channelPort(port);
        }

        String param = Optional.ofNullable(name).orElse("world");
        logger.log(Level.INFO, "Calling SayHello with name = {0}", param);

        HelloRequest request = HelloRequest.newBuilder().setName(param).build();

        try {
            HelloReply response = client.sayHello(request);
            String msg = response.getMessage(); 
            logger.log(Level.INFO, "reply: {0}", msg);
            return msg;

        }catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            throw new RuntimeException(e.getMessage(), e); 
        }
    }

}