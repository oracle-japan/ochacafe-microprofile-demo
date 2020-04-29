package oracle.demo.metrics;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.helidon.microprofile.server.Server;
import oracle.demo.TestBase;

import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.Meter;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.annotation.RegistryType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@ApplicationScoped
class MetricsResourceTest extends TestBase{

    private final Client client = ClientBuilder.newClient();

    public MetricsResourceTest(){
        super();
    }

    @Test
    public void testMetrics(){
        client.target(getConnectionString("/mpmetrics/apple")).request().get();
        client.target(getConnectionString("/mpmetrics/apple")).request().get();
        client.target(getConnectionString("/mpmetrics/orange")).request().get();
        client.target(getConnectionString("/mpmetrics/orange")).request().get();
        client.target(getConnectionString("/mpmetrics/orange")).request().get();

        String total = client.target(getConnectionString("/mpmetrics/count-total"))
            .request().get(String.class);

        Assertions.assertEquals(5l, Long.parseLong(total)); 
    }
}
