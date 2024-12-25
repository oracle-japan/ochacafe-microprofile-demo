package com.example.cowweb;

import java.util.Optional;

import com.github.ricksbrown.cowsay.Cowsay;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@ApplicationScoped
@Path("/cowsay")
public class CowwebResource{

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public Response handleRequest() {
        return handleRequest("say", null, null);
    }   

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
