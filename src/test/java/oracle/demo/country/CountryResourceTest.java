package oracle.demo.country;

import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.AddConfig;
import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
@AddConfig(key = "tracing.enabled", value = "false")
public class CountryResourceTest{

    @Inject private WebTarget webTarget;

    @Test
    public void testGetCountries(){
        JsonArray jsonArray = webTarget.path("/country")
                .request()
                .get(JsonArray.class);
        Assertions.assertEquals(2, jsonArray.size()); // USA, Japan
    }

    @Test
    public void testGetCountry(){
        JsonObject jsonObject = webTarget.path("/country/1")
                .request()
                .get(JsonObject.class);
        Assertions.assertEquals(1, jsonObject.getInt("countryId"));
        Assertions.assertEquals("USA", jsonObject.getString("countryName"));

        jsonObject = webTarget.path("/country/81")
                .request()
                .get(JsonObject.class);
        Assertions.assertEquals(81, jsonObject.getInt("countryId"));
        Assertions.assertEquals("Japan", jsonObject.getString("countryName"));

        Response response = webTarget.path("/country/99")
                .request()
                .get();
        Assertions.assertEquals(404, response.getStatus());
    }

}
