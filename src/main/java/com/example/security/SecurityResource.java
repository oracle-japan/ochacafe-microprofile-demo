package com.example.security;

import java.security.Principal;
import java.util.Optional;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

import jakarta.ws.rs.core.MediaType;

import io.helidon.security.annotations.Authenticated;
import io.helidon.security.annotations.Authorized;

/**
 * built-in basic auth provider demo
 */
@ApplicationScoped
@Path("/security/basic")
public class SecurityResource {

    @GET @Path("/public")
    @Authenticated(optional = true) // any one can access
    @Produces(MediaType.TEXT_PLAIN)
    public String getPublic(@Context SecurityContext securityContext) {
        return dumpSecurityContext(securityContext);
    }

    @GET @Path("/guest")
    @Authenticated // needs log-in
    @Produces(MediaType.TEXT_PLAIN)
    public String getGuest(@Context SecurityContext securityContext) {
        return dumpSecurityContext(securityContext);
    }

    @GET @Path("/admin")
    @Authenticated @Authorized @RolesAllowed("admin") // needs admin role
    @Produces(MediaType.TEXT_PLAIN)
    public String getAdmin(@Context SecurityContext securityContext) {
        return dumpSecurityContext(securityContext);
    }

    @GET @Path("/user")
    @Authenticated @Authorized @RolesAllowed("user") // needs user role
    @Produces(MediaType.TEXT_PLAIN)
    public String getUser(@Context SecurityContext securityContext) {
        return dumpSecurityContext(securityContext);
    }

    private String dumpSecurityContext(SecurityContext securityContext){
        final StringBuilder builder = new StringBuilder();
        if(Optional.ofNullable(securityContext).isPresent()){
            builder.append(String.format("SecurityContext class: %s\n", securityContext.getClass().getSimpleName()));
            builder.append(String.format("AuthenticationScheme: %s\n", securityContext.getAuthenticationScheme()));
            builder.append(String.format("Secure(= via HTTPS or other secure channel): %b\n", securityContext.isSecure()));

            final Principal principal = securityContext.getUserPrincipal();
            builder.append(String.format("Principal: %s\n", principal.toString()));
        }else{
            builder.append("No SecurityContext.\n");
        }
        return builder.toString();
    }

}
