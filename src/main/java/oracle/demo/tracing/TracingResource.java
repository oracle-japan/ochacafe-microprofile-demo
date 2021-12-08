package oracle.demo.tracing;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * tracing demo
 */
@ApplicationScoped
@Path("/tracing")
public class TracingResource{

    private final Logger logger = Logger.getLogger(TracingResource.class.getName());

    private final Client client = ClientBuilder.newBuilder().connectTimeout(5, TimeUnit.SECONDS).build();
    
    @POST
    @Path("/invoke")
    @Produces(MediaType.TEXT_PLAIN)
    public Response handleRequest(TraceOrder[] orders, @Context HttpHeaders headers){

        logger.info("!!! New request");

        headers.getRequestHeaders().forEach((key, values) -> {
            values.forEach(val -> logger.info(String.format("Header: %s=%s", key, val)));
        });

        boolean failed = false;
        for(TraceOrder order : orders){

            // first handle calls
            if(Objects.nonNull(order.calls)){
                for(Call call : order.calls){
                    logger.info("Invoking call: " + call.method + " " + call.endpoint);
                    Response response = null;
                    if(call.method.equalsIgnoreCase("GET")){
                        response = client.target(call.endpoint).request().get();
                    }else if(call.method.equalsIgnoreCase("POST")){
                        response = client.target(call.endpoint).request()
                        .post(Entity.entity(call.body, MediaType.APPLICATION_JSON));
                    }
                    logger.info("Response: " + response.toString());
                    String responseBody = response.readEntity(String.class);
                    if(responseBody.length() > 512){
                        responseBody = responseBody.substring(0, 512).concat("\n...");
                    }
                    logger.info("Response body:\n" + responseBody);
                }
            }

            // then route to another order
            logger.info("Invoking endpoint: " + order.endpoint);
            final Response response = client.target(order.endpoint).request()
                        .post(Entity.entity(order.orders, MediaType.APPLICATION_JSON));
            logger.info("Response: " + response.toString());
        }
        return Response.ok().build();
    }   

    public static class TraceOrder{
        public String endpoint;
        public TraceOrder[] orders;
        public Call[] calls;
    }

    public static class Call{
        public String method;
        public String endpoint;
        public Object body; // json
    }

}

/* test data
[
    {
        "endpoint" : "http://helidon-demo-mp-1:8080/tracing/invoke",
        "orders" : [],
        "calls" : [
            {
                "method" : "GET",
                "endpoint" : "http://helidon-demo-mp-1:8080/jpa/country/1"
            }
        ]
    },
    {
        "endpoint" : "http://helidon-demo-mp-2:8080/tracing/invoke",
        "orders" : [
            {
                "endpoint" : "http://helidon-demo-mp-3:8080/tracing/invoke",
                "orders" : [],
                "calls" : [
                    {
                        "method" : "GET",
                        "endpoint" : "http://helidon-demo-mp-3:8080/jpa/country?error=true"
                    },
                    {
                        "method" : "GET",
                        "endpoint" : "https://api.weather.gov/alerts/active?area=CA"
                    }                    
                ]
            }
        ],
        "calls" : [
            {
                "method" : "POST",
                "endpoint" : "http://helidon-demo-mp-2:8080/echo",
                "body" : { "text" : "Hello World!" }
            }
        ]
    }
]
*/

