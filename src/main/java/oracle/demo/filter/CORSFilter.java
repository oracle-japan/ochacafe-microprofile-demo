package oracle.demo.filter;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;


@Provider
@CORS
public class CORSFilter implements ContainerResponseFilter {

    private String serverName;
    private static final String X_SERVER_NAME = "X-SERVER-NAME";
    private static final String HOSTNAME = "HOSTNAME";

    public CORSFilter(){

        try (InputStream in = CORSFilter.class.getResourceAsStream("/apisample.properties")){
            Properties p = new Properties();
            p.load(in);
            serverName = p.getProperty("serverName");
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        if(null == serverName) {
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