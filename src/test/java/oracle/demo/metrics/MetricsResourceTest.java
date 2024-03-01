package oracle.demo.metrics;

import jakarta.inject.Inject;
import jakarta.ws.rs.client.WebTarget;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.testing.junit5.AddConfig;
import io.helidon.microprofile.testing.junit5.HelidonTest;

@HelidonTest
@AddConfig(key = "tracing.enabled", value = "false")
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
