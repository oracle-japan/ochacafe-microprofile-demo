package com.example.tracing;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

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
                    }else{
                        return Response.status(Status.BAD_REQUEST).entity("Unsupported method: " + call.method).build();
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

