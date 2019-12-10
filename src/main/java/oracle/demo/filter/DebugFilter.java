package oracle.demo.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

@Provider
@Debug
@Priority(Priorities.AUTHENTICATION)
public class DebugFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext context) throws IOException{
        System.out.println("*** debug ***");

        context.getHeaders().forEach((k,v) -> {
            System.out.println(String.format("%s: %s", k, v.toString()));
        });
        
        UriInfo uriInfo = context.getUriInfo();
        uriInfo.getRequestUri();
    }
    
}