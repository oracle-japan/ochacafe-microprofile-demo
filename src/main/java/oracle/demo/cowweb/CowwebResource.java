package oracle.demo.cowweb;

import java.util.Optional;

import com.github.ricksbrown.cowsay.Cowsay;


import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@ApplicationScoped
@Path("/cowsay")
public class CowwebResource{

    @GET
    @Path("/{verb}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response handleRequest(
            @PathParam("verb") String verb,
            @QueryParam("message") String message, 
            @QueryParam("cowfile") String cowfile) {

        final String m = Optional.ofNullable(message).orElse("Moo!");
        final String c = Optional.ofNullable(cowfile).orElse("default");
        String[] params = new String[]{"-f", c, m};

        if(verb.equalsIgnoreCase("say")) return Response.ok(Cowsay.say(params)).build();
        else if(verb.equalsIgnoreCase("think")) return Response.ok(Cowsay.think(params)).build();
        
        return Response.status(Status.BAD_REQUEST).build();
    }   

}
