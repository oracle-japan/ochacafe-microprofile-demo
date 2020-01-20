package oracle.demo.grpc.protobuf;

import io.helidon.microprofile.grpc.server.spi.GrpcMpContext;
import io.helidon.microprofile.grpc.server.spi.GrpcMpExtension;
/**
 * work with META-INF/services/io.helidon.microprofile.grpc.server.spi.GrpcMpExtension
 */
public class GrpcExtension implements GrpcMpExtension {

    @Override
    public void configure(GrpcMpContext context) {  
        context.routing().register(new GreeterService());     
    }
}