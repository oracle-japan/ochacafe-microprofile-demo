package com.example.jpa;

import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.testing.junit5.AddConfig;
import io.helidon.microprofile.testing.junit5.HelidonTest;

@HelidonTest
@AddConfig(key = "tracing.enabled", value = "false")
public class CountryResourceTest{

    @Inject private WebTarget webTarget;

    @Test
    public void testGetCountries(){
        JsonArray jsonArray = webTarget.path("/jpa/country").request().get(JsonArray.class);
        Assertions.assertEquals(2, jsonArray.size()); // USA, Japan
    }

    @Test
    public void testGetCountry(){
        JsonObject jsonObject = webTarget.path("/jpa/country/1").request().get(JsonObject.class);
        Assertions.assertEquals(1, jsonObject.getInt("countryId"));
        Assertions.assertEquals("USA", jsonObject.getString("countryName"));

        jsonObject = webTarget.path("/jpa/country/81").request().get(JsonObject.class);
        Assertions.assertEquals(81, jsonObject.getInt("countryId"));
        Assertions.assertEquals("Japan", jsonObject.getString("countryName"));

        Response response = webTarget.path("/country/99").request().get();
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    public void testCRUDCountry(){
        
        // insert
        Country[] countries = new Country[]{ new Country(86, "China") };
        Response response = webTarget.path("/jpa/country").request().post(Entity.entity(countries, MediaType.APPLICATION_JSON));
        Assertions.assertEquals(200, response.getStatus());

        JsonObject jsonObject = webTarget.path("/jpa/country/86").request().get(JsonObject.class);
        Assertions.assertEquals(86, jsonObject.getInt("countryId"));
        Assertions.assertEquals("China", jsonObject.getString("countryName"));

        // update
        Form form = new Form().param("name", "People’s Republic of China");
        response = webTarget.path("/jpa/country/86").request().put(Entity.form(form));
        Assertions.assertEquals(204, response.getStatus());

        jsonObject = webTarget.path("/jpa/country/86").request().get(JsonObject.class);
        Assertions.assertEquals(86, jsonObject.getInt("countryId"));
        Assertions.assertEquals("People’s Republic of China", jsonObject.getString("countryName"));

        // delete
        response = webTarget.path("/jpa/country/86").request().delete();
        Assertions.assertEquals(204, response.getStatus());
        response = webTarget.path("/country/86").request().get();
        Assertions.assertEquals(404, response.getStatus());

    }

}
