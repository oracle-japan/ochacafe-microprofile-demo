package oracle.demo.country;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.helidon.microprofile.server.Server;
import oracle.demo.TestBase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CountryResourceTest extends TestBase{

    private final Client client = ClientBuilder.newClient();

    public CountryResourceTest(){
        super();
    }

    @Test
    public void testGetCountries(){
        JsonArray jsonArray = client
                .target(getConnectionString("/country"))
                .request()
                .get(JsonArray.class);
        Assertions.assertEquals(2, jsonArray.size()); // USA, Japan
    }

    @Test
    public void testGetCountry(){
        JsonObject jsonObject = client
                .target(getConnectionString("/country/1"))
                .request()
                .get(JsonObject.class);
        Assertions.assertEquals(1, jsonObject.getInt("countryId"));
        Assertions.assertEquals("USA", jsonObject.getString("countryName"));

        jsonObject = client
                .target(getConnectionString("/country/81"))
                .request()
                .get(JsonObject.class);
        Assertions.assertEquals(81, jsonObject.getInt("countryId"));
        Assertions.assertEquals("Japan", jsonObject.getString("countryName"));

        Response response = client
                .target(getConnectionString("/country/99"))
                .request()
                .get();
        Assertions.assertEquals(404, response.getStatus());
    }

}
