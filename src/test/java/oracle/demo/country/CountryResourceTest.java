package oracle.demo.country;

import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
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
