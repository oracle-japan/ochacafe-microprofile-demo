package oracle.demo.metrics;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
public class MetricsResourceTest{

    @Inject private WebTarget webTarget;

    @Test
    public void testMetrics(){
        webTarget.path("/mpmetrics/apple").request().get();
        webTarget.path("/mpmetrics/apple").request().get();
        webTarget.path("/mpmetrics/orange").request().get();
        webTarget.path("/mpmetrics/orange").request().get();
        webTarget.path("/mpmetrics/orange").request().get();

        String total = webTarget.path("/mpmetrics/count-total")
            .request().get(String.class);

        Assertions.assertEquals(5l, Long.parseLong(total)); 
    }
}
