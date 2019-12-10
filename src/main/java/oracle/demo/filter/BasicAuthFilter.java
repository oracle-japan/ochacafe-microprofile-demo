package oracle.demo.filter;

import java.io.IOException;
import java.util.Base64;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
@Auth
@Priority(Priorities.AUTHENTICATION)
public class BasicAuthFilter implements ContainerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";
    public static final String AUTH_BASIC = "Basic";
    public static final String AUTH_ERROR = "Basic realm=\"OchaCafe\"";

    public static final String AUTH_USERNAME = "oracle";
    public static final String AUTH_PASSWORD = "Welcome1";
    
    
    /**
     * In order to let this sample work on WebLogig Server, you must set the enforce-valid-basic-auth-credentials Flag to false
     * Please refer to https://docs.oracle.com/middleware/1221/wls/SCPRG/thin_client.htm#SCPRG150
     */
    @Override
    public void filter(ContainerRequestContext context) throws IOException{
        // System.out.println("*** auth ***");

        // Authorization: Basic dGVzdDoxMjPCow== (username:password)
        
        String authHeader = context.getHeaderString(AUTHORIZATION);
        System.out.println(">> auth header: " + authHeader);
        if (null == authHeader || 0 == authHeader.length()) {
            throw new NotAuthorizedException(AUTH_ERROR);
        }

        String[] headerElements = authHeader.split("\\s");
        if(2 != headerElements.length 
                || !headerElements[0].equalsIgnoreCase(AUTH_BASIC)) {
            throw new NotAuthorizedException(AUTH_ERROR);
        }
        
        String decoded = new String(Base64.getDecoder().decode(headerElements[1]));
        // System.out.println(">> decoded: " + decoded);

        String[] userpw = decoded.split(":");
        if(2 != userpw.length || null == userpw[0] || null == userpw[1] 
                || !userpw[0].equals(AUTH_USERNAME) || !userpw[1].equals(AUTH_PASSWORD)) {
            throw new NotAuthorizedException(AUTH_ERROR);
        }
    }
    
}