package oracle.demo.metrics;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import oracle.demo.TestBase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
