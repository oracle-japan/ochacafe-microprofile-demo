package oracle.demo.security;

import java.util.Base64;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import io.helidon.config.Config;
import io.helidon.security.Principal;
import io.helidon.security.SecurityContext;
// import io.helidon.security.abac.role.RoleValidator;
import io.helidon.security.abac.scope.ScopeValidator;
import io.helidon.security.annotations.Authenticated;

/**
 * IDCS integration demo
 */
@Path("/security/idcs")
@ApplicationScoped
public class IdcsResource {

    private final String cookieName;
    private final String cookiePath;
    private final String cookieDomain;

    public IdcsResource(){
        final Config config = Config.create();
        cookieName = config.get("security.providers.oidc.cookie-name").asString().orElse("JSESSIONID");
        cookiePath = config.get("security.providers.oidc.cookie-path").asString().orElse("/");
        cookieDomain = config.get("security.providers.oidc.cookie-domain").asString().orElse(null);
    }

    @GET
    @Path("/login")
    @Authenticated
    @Produces(MediaType.TEXT_PLAIN)
    public String login(@Context SecurityContext context, @Context ContainerRequestContext request) {

        // dump SecurityContext
        final String ctx = dumpSecurityContext(context);
        final StringBuilder sb = new StringBuilder(ctx);

        final String token = getToken(request); // Access Token (NOT a ID Token)
        System.out.println(token);
        final String[] tokenParts = token.split("\\.");
        final Base64.Decoder decoder = Base64.getDecoder();
        final String jwtHeader = new String(decoder.decode(tokenParts[0]));
        final String jwtPayload = new String(decoder.decode(tokenParts[1]));

        // dump Cookie (= Access Token)
        sb.append(String.format("cookie.jwt.header=%s\n", jwtHeader));
        sb.append(String.format("cookie.jwt.payload=%s\n", jwtPayload));

        return sb.toString();
    }

    @GET
    @Path("/login2")
    @Authenticated
    public Response login(@Context SecurityContext context, @QueryParam("target") String redirectTo) {
        return Response.status(Response.Status.TEMPORARY_REDIRECT).header("Location", redirectTo).build();
    }

    /**
     * log out - simply invalidate cookie 
     */
    @GET
    @Path("/logout")
    @Authenticated
    public Response logout(){
        final NewCookie newCookie = new NewCookie(cookieName, "", cookiePath, cookieDomain, "invalidated", 0, false);
        return Response
            .ok("Logged out.", MediaType.TEXT_PLAIN)
            .cookie(newCookie)
            .build();
    }


    /**
     * test exisitng scope
     */
    @GET
    @Path("/scopes")
    @Authenticated
    @ScopeValidator.Scope("first_scope")
    @ScopeValidator.Scope("second_scope")
    @Produces(MediaType.TEXT_PLAIN)
    public String scopes(@Context SecurityContext context) {
        return dumpSecurityContext(context);
    }

    /**
     * test non-exisitng scope
     */
    @GET
    @Path("/third-scope")
    @Authenticated
    @ScopeValidator.Scope("third_scope")
    @Produces(MediaType.TEXT_PLAIN)
    public String thirdScope(@Context SecurityContext context) {
        return dumpSecurityContext(context);
    }

    /**
     * test roles = IDCS groups
     */
    @GET
    @Path("/user-role")
    @Authenticated
    // this also works - @RoleValidator.Roles("user")
    @RolesAllowed("user")
    @Produces(MediaType.TEXT_PLAIN)
    public String roleA(@Context SecurityContext context) {
        return dumpSecurityContext(context);
    }

    @GET
    @Path("/admin-role")
    @Authenticated
    // this also works - @RoleValidator.Roles("admin")
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    public String roleB(@Context SecurityContext context) {
        return dumpSecurityContext(context);
    }

    
    ///////////////

    private String getToken(ContainerRequestContext request){
        final Cookie cookie = request.getCookies().get(cookieName);
        return cookie.getValue();
    }

    private String dumpSecurityContext(SecurityContext context){
        final StringBuilder sb = new StringBuilder();

        context.user().ifPresent(user ->{
            user.abacAttributeNames().forEach(key ->{
                //System.out.println("Key: " + key);
                user.abacAttribute(key).ifPresent(value -> {
                    //System.out.println("Value: " + value + "/" + value.getClass().getName());
                    if(value instanceof Principal){
                        Principal principal = (Principal)value;
                        principal.abacAttributeNames().forEach(pKey ->{
                            principal.abacAttribute(pKey).ifPresent(pValue ->{
                                sb.append(String.format("principal.%s=%s\n", pKey, pValue));
                            });
                        });
                    }else{
                        sb.append(String.format("%s=%s\n", key, value));
                    }
                });
            });
        });

        return sb.toString();
    }


}
