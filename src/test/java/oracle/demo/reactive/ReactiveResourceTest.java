package oracle.demo.reactive;

import java.util.concurrent.TimeUnit;

import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.AddConfig;
import io.helidon.microprofile.tests.junit5.HelidonTest;
import oracle.demo.jpa.Country;

@HelidonTest
@AddConfig(key = "tracing.enabled", value = "false")
public class ReactiveResourceTest{

    @Inject private WebTarget webTarget;

    @Test
    public void testCRUDCountry(){
        
        // warm-up
        JsonObject jsonObject = webTarget.path("/jpa/country/1").request().get(JsonObject.class);
        Assertions.assertEquals(1, jsonObject.getInt("countryId"));
        Assertions.assertEquals("USA", jsonObject.getString("countryName"));

        // insert
        Country[] countries = new Country[]{ new Country(86, "China") };
        Response response = webTarget.path("/reactive/country").request().post(Entity.entity(countries, MediaType.APPLICATION_JSON));
        Assertions.assertEquals(204, response.getStatus());
        delay(1000);
        jsonObject = webTarget.path("/jpa/country/86").request().get(JsonObject.class);
        Assertions.assertEquals(86, jsonObject.getInt("countryId"));
        Assertions.assertEquals("China", jsonObject.getString("countryName"));

        countries = new Country[]{ new Country(61, "Australia") };
        response = webTarget.path("/reactive/country").request().post(Entity.entity(countries, MediaType.APPLICATION_JSON));
        Assertions.assertEquals(204, response.getStatus());
        delay(1000);
        jsonObject = webTarget.path("/jpa/country/61").request().get(JsonObject.class);
        Assertions.assertEquals(61, jsonObject.getInt("countryId"));
        Assertions.assertEquals("Australia", jsonObject.getString("countryName"));

        // update
        Form form = new Form().param("name", "People's Republic of China");
        response = webTarget.path("/reactive/country/86").request().put(Entity.form(form));
        Assertions.assertEquals(204, response.getStatus());
        delay(1000);
        jsonObject = webTarget.path("/jpa/country/86").request().get(JsonObject.class);
        Assertions.assertEquals(86, jsonObject.getInt("countryId"));
        Assertions.assertEquals("People's Republic of China", jsonObject.getString("countryName"));

        countries = new Country[]{ new Country(61, "Commonwealth of Australia") };
        response = webTarget.path("/reactive/country").request().put(Entity.entity(countries, MediaType.APPLICATION_JSON));
        Assertions.assertEquals(204, response.getStatus());
        delay(1000);
        jsonObject = webTarget.path("/jpa/country/61").request().get(JsonObject.class);
        Assertions.assertEquals(61, jsonObject.getInt("countryId"));
        Assertions.assertEquals("Commonwealth of Australia", jsonObject.getString("countryName"));

        // delete
        response = webTarget.path("/reactive/country/86").request().delete();
        Assertions.assertEquals(204, response.getStatus());
        delay(1000);
        response = webTarget.path("/country/86").request().get();
        Assertions.assertEquals(404, response.getStatus());

        response = webTarget.path("/reactive/country/61").request().delete();
        Assertions.assertEquals(204, response.getStatus());
        delay(1000);
        response = webTarget.path("/country/61").request().get();
        Assertions.assertEquals(404, response.getStatus());

        // error update - CountryNotFoundException: Couldn't find country, id=86
        response = webTarget.path("/reactive/country/86").request().put(Entity.form(form));
        Assertions.assertEquals(204, response.getStatus()); 
        delay(1000);
        response = webTarget.path("/country/86").request().get();
        Assertions.assertEquals(404, response.getStatus());


    }

    private void delay(long ms){
        try{
            TimeUnit.MILLISECONDS.sleep(ms);
        }catch(Exception e){}
    }

}
