package oracle.demo.graphql;


import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
public class CountryGraphQLApiTest{

    @Inject private WebTarget webTarget;

    @Test
    public void testQueryCountries(){ 

        final String query = String.format("{\"query\":\"%s\"}", "query { countries { countryId countryName } }");
        
        final JsonObject json = webTarget.path("/graphql")
                .request()
                .post(Entity.json(query), JsonObject.class);
        JsonArray countries = json.getJsonObject("data").getJsonArray("countries");
        Assertions.assertEquals(
            1, 
            countries.stream().filter(v -> {
                final JsonObject country = v.asJsonObject();
                return (81 == country.getInt("countryId") && country.getString("countryName").equals("Japan"));
            }).count(), 
            "The record of Japan must exist."
        );
    }

    @Test
    public void testQueryCountry(){ 

        final String query1 = String.format("{\"query\":\"%s\"}", "query { country (countryId: 1) { countryName } }");
        
        final JsonObject json1 = webTarget.path("/graphql")
                .request()
                .post(Entity.json(query1), JsonObject.class);
        final String countryName1 = json1.getJsonObject("data").getJsonObject("country").getString("countryName");
        Assertions.assertEquals("USA", countryName1, "1:USA");

        final String query2 = String.format("{\"query\":\"%s\"}", "query { country (countryId: 81) { countryName } }");
        
        final JsonObject json2 = webTarget.path("/graphql")
                .request()
                .post(Entity.json(query2), JsonObject.class);
        final String countryName2 = json2.getJsonObject("data").getJsonObject("country").getString("countryName");
        Assertions.assertEquals("Japan", countryName2, "81:Japan");
    }

    @Test
    public void testInsertCountry(){ 

        final String mutation = String.format("{\"query\":\"%s\"}", "mutation { insertCountry(country:{countryId:86,countryName:\\\"China\\\"}) { countryId countryName } }");
        
        final Response response = webTarget.path("/graphql")
                .request()
                .post(Entity.json(mutation));
        Assertions.assertEquals(200, response.getStatus(), "Normal HTTP response");

        final String query = String.format("{\"query\":\"%s\"}", "query { country (countryId: 86) { countryName } }");
        
        final JsonObject json = webTarget.path("/graphql")
                .request()
                .post(Entity.json(query), JsonObject.class);
        final String countryName = json.getJsonObject("data").getJsonObject("country").getString("countryName");
        Assertions.assertEquals("China", countryName, "86:China");
    }



}

/*
curl -X POST -H "Content-Type: application/json" localhost:8080/graphql -d '{ "query" : "query { countries{ countryId countryName } }" }'

curl -X POST -H "Content-Type: application/json" localhost:8080/graphql -d '{ "query" : "query { country(countryId:1){ countryName } }" }'

curl -X POST -H "Content-Type: application/json" localhost:8080/graphql -d '{ "query" : "mutation { insertCountry(country:{countryId:86,countryName:\"China\"}){ countryId countryName } }" }'

curl -X POST -H "Content-Type: application/json" localhost:8080/graphql -d '{ "query" : "mutation { insertCountries(countries:[{countryId:82,countryName:\"Korea\"},{countryId:91,countryName:\"India\"}]){ countryId countryName } }" }'

curl -X POST -H "Content-Type: application/json" localhost:8080/graphql -d '{ "query" : "mutation { updateCountry(countryId:1,countryName:\"United States\"){ countryId countryName } }" }'

curl -X POST -H "Content-Type: application/json" localhost:8080/graphql -d '{ "query" : "mutation { deleteCountry(countryId:86) }" }'
*/