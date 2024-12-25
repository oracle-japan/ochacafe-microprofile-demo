package com.example.filter;

import java.io.IOException;
import java.net.InetAddress;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;


@Provider
@CORS
public class CORSFilter implements ContainerResponseFilter {

    @Inject @ConfigProperty(name="demo.cors.serverName", defaultValue="__UNDIFINED__")
    private String serverName;
    private static final String X_SERVER_NAME = "X-SERVER-NAME";
    private static final String HOSTNAME = "HOSTNAME";

    public CORSFilter(){

        if(null == serverName || serverName.equals("__UNDIFINED__")) {
            serverName = System.getenv(HOSTNAME);
            if(null == serverName) {
                try {
                    serverName = InetAddress.getLocalHost().getHostName();
                }catch(Exception e){
                    serverName = "unknown";
                }
            }
        }

    }
    
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, PATCH, DELETE");
        headers.add("Access-Control-Allow-Headers", "X-Requested-With,content-type");
        headers.add("Access-Control-Allow-Credentials", true);
        
        headers.add(X_SERVER_NAME, serverName);

    }
}