package oracle.demo.grpc.javaobj;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.helidon.microprofile.grpc.client.GrpcChannel;
import io.helidon.microprofile.grpc.client.GrpcProxy;

@Path("/grpc-javaobj")
@ApplicationScoped
public class GrpcResource {

    @Inject 
    @GrpcProxy
    private GreeterService greeterService;

    @GET
    @Path("/client")
    @Produces(MediaType.TEXT_PLAIN)
    public String invoke(@QueryParam("name") String name) {
        return greeterService.sayHello(Optional.ofNullable(name).orElse("world"));
    }

}
