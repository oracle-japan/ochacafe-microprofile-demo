
package com.example.filter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@ApplicationScoped
@Path("/filter")
public class FilterResource{

    @POST @Path("/")
    @Auth @CORS @Debug
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response dump(Message message, @Context UriInfo uriInfo, @Context HttpHeaders headers /*, @Context Request request*/) {

        final JsonObjectBuilder builder = Json.createObjectBuilder();
        headers.getRequestHeaders().forEach((s,l) -> {
            l.forEach(x -> {
                builder.add(s, x);
            });
        });
        JsonObject jsonHeaders = builder.build();

        final JsonObject response = Json.createObjectBuilder()
            .add("url", uriInfo.getRequestUri().toASCIIString())
            .add("headers", jsonHeaders)
            .add("message", message.text)
            .build();
        return Response.ok(response.toString()).build();
    }
    
    public static class Message{
        public String text;
    }

}