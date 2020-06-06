package oracle.demo;

import io.helidon.microprofile.server.Server;

public class TestBase {
    private static Server server;

    private static void startUp(){
        if(null == server){
            server = Server.builder().build();
            server.start();
            System.out.println("Server started - " + server.host() + ":" + server.port());
        }
    }

    public TestBase(){
        startUp();
    }
    
    protected String getConnectionString(String path) {
        return "http://localhost:" + server.port() + path;
    }
    
}