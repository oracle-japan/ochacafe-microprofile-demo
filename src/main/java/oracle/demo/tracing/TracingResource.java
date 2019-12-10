package oracle.demo.tracing;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * tracing demo
 */
@ApplicationScoped
@Path("/tracing")
public class TracingResource{

    private final Logger logger = Logger.getLogger(TracingResource.class.getName());
    
    @POST
    @Path("/invoke")
    @Produces(MediaType.TEXT_PLAIN)
    public Response handleRequest(TraceOrder[] orders){

        logger.info("!!! New request");

        // SpanContext spanContext = serverRequest.spanContext(); // you can comment out this line
        // optional, you could also use GlobalTracer.get() if it is configured
        // Tracer tracer = serverRequest.webServer().configuration().tracer(); // you can comment out this line

        boolean failed = false;
        for(TraceOrder order : orders){
            Client client = ClientBuilder.newClient();
            try{
                logger.info("Invoking endpoint: " + order.endpoint);
                Response response = client.target(order.endpoint)
                            .request()
                            // .property(TRACER_PROPERTY_NAME, tracer) // you can comment out this line
                            // .property(CURRENT_SPAN_CONTEXT_PROPERTY_NAME, spanContext) // you can comment out this line
                            .post(Entity.entity(order.orders, MediaType.APPLICATION_JSON));
                logger.info("Response: " + response.toString());
            }catch(Exception e){
                logger.log(Level.WARNING, "Failed to invoke: " + e.getMessage(), e);
                failed = true;
            }finally{
                Optional.ofNullable(client).ifPresent(c -> c.close());
            }
        }
        return failed ? Response.status(Status.INTERNAL_SERVER_ERROR).build() : Response.ok().build();
    }   

    public static class TraceOrder{
        public String endpoint;
        public TraceOrder[] orders;
    }

}

/* test data
[
    {
        "endpoint" : "http://helidon-demo-mp-1:8080/tracing/invoke",
        "orders" : []
    },
    {
        "endpoint" : "http://helidon-demo-mp-2:8080/tracing/invoke",
        "orders" : [
            {
                "endpoint" : "http://helidon-demo-mp-3:8080/tracing/invoke",
                "orders" : []
            },
            {
                "endpoint" : "http://helidon-demo-mp-0:8080/tracing/invokeX",
                "orders" : []
            },
            {
                "endpoint" : "http://helidon-demo-mp-1:8080/tracing/invoke",
                "orders" : []
            }
        ]
    }
]
*/

