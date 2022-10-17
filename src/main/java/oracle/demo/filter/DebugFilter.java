package oracle.demo.filter;

import java.io.IOException;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;

@Provider
@Debug
@Priority(Priorities.USER + 100)
public class DebugFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        System.out.println("*** debug ***");

        requestContext.getHeaders().forEach((k, v) -> {
            System.out.println(String.format("Request header: %s = %s", k, v.toString()));
        });

        final UriInfo uriInfo = requestContext.getUriInfo();
        System.out.println("Request Uri: " + uriInfo.getRequestUri());

        System.out.println("Response status: " + responseContext.getStatus());

        responseContext.getHeaders().forEach((k, v) -> {
            System.out.println(String.format("Response header: %s = %s", k, v.toString()));
        });

    }

}