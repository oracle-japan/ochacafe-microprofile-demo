package oracle.demo;

import io.helidon.microprofile.server.Server;

/**
 * Main method
 */
public final class Main {

    /**
     * Cannot be instantiated.
     */
    private Main() { }

    /**
     * Application main entry point.
     * @param args command line arguments
     */
    public static void main(final String[] args) {

        Server server = Server.builder().build();
        server.start();
        System.out.println(String.format("Server started: http://%s:%d", server.host(), server.port()));
    }

}
