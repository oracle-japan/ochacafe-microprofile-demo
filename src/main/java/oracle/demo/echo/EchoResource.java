
package oracle.demo.echo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
@Path("/echo")
public class EchoResource{

    @Inject @ConfigProperty(name = "echo.reply", defaultValue = "Hi there!") 
    private String reply;

    @POST @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response echo(Message message, @Context UriInfo uriInfo) {

        final JsonObject json = Json.createObjectBuilder()
        .add("url", uriInfo.getRequestUri().toASCIIString())
        .add("text", message.text)
        .add("reply", reply)
        .build();
        return Response.ok(json.toString()).build();
    }

    public static class Message{
        public String text;
    }

    @GET @Path("/dump")
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
            .build();
        return Response.ok(response.toString()).build();
    }


}