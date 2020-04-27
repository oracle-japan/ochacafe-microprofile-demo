package oracle.demo.ft;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * FaultTolerance demo tester
 */
public class FaultToleranceTester {
    public static void main(String[] args) {
        final AtomicReference<String> endpoint = new AtomicReference<>("http://localhost:8080/ft/circuit-breaker");
        final AtomicInteger counter = new AtomicInteger();
        int numCalls = 1;

        final Options options = new Options();
        options.addOption(Option.builder("e").longOpt("endpoint").hasArg(true).desc("endpoint url").build());
        try{
            final CommandLineParser parser = new DefaultParser();
            final CommandLine cmd = parser.parse(options, args);
            if(cmd.hasOption("e")) endpoint.set(cmd.getOptionValue("e"));
            final String[] cmdArgs = cmd.getArgs();
            if(cmdArgs.length > 0) numCalls = Integer.parseInt(cmdArgs[0]);
        }catch(Exception e) {
            System.err.println(e.getMessage());
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp(FaultToleranceTester.class.getName() + " [options] [times to call]", options);
            System.exit(1);
        }

        ExecutorService service = Executors.newFixedThreadPool(numCalls);
        for(int i = 0 ; i < numCalls ; i++){
            service.submit(() -> {
                int count = counter.incrementAndGet();
                try {
                    System.out.println(String.format("%03d", count) + ": Calling " + endpoint.get());
                    Client client = ClientBuilder.newClient();
                    Response response = client.target(endpoint.get()).request().get();
                    System.out.println(String.format("%03d", count) + ": Response code: " + response.getStatus() + " " + response.getStatusInfo());
                } catch (Exception e) {
                    System.out.println(String.format("%03d", count) + ": Error: " + e.getMessage());
                }
            });
        }
        service.shutdown();
        try{
            service.awaitTermination(30, TimeUnit.SECONDS);
        }catch(InterruptedException e){}

    }
}
