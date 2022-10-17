package oracle.demo.graphql;


import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
public class CountryGraphQLApiTest{

    @Inject private WebTarget webTarget;

    @Test
    public void testQueryCountries(){ 

        final String query = String.format("{\"query\":\"%s\"}", 
            "query {" +
            "  countries {" + 
            "    countryId" +
            "    countryName" +
            "  }" +
            "}");
        
        final JsonObject json = webTarget.path("/graphql")
                .request().post(Entity.json(query), JsonObject.class);
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

        // select USA
        final String query1 = String.format("{\"query\":\"%s\"}", 
            "query {" +
            "  country (countryId: 1) {" +
            "    countryName" +
            "  }" +
            "}");
        
        final JsonObject json1 = webTarget.path("/graphql")
                .request().post(Entity.json(query1), JsonObject.class);
        final String countryName1 = json1.getJsonObject("data").getJsonObject("country").getString("countryName");
        Assertions.assertTrue(countryName1.equals("USA") || countryName1.equals("United States"), "1: USA or United States");

        final String query2 = String.format("{\"query\":\"%s\"}", 
            "query {" +
            "  country (countryId: 81) {" +
            "    countryName" +
            "  }" +
            "}");
        
        // select Japan
        final JsonObject json2 = webTarget.path("/graphql")
                .request().post(Entity.json(query2), JsonObject.class);
        final String countryName2 = json2.getJsonObject("data").getJsonObject("country").getString("countryName");
        Assertions.assertEquals("Japan", countryName2, "81: Japan");
    }

    @Test
    public void testInsertDeleteCountry(){ 

        // Insert China
        final String mutationInsert = String.format("{\"query\":\"%s\"}", 
            "mutation {" +
            "  insertCountry (" +
            "    country: {countryId: 86, countryName: \\\"China\\\"}" +
            "  )" +
            "  { countryId countryName }" +
            "}");
        
        final Response responseInsert = webTarget.path("/graphql")
                .request().post(Entity.json(mutationInsert));
        Assertions.assertEquals(200, responseInsert.getStatus(), "Normal HTTP response");

        final String query = String.format("{\"query\":\"%s\"}", 
            "query {" +
            "  country (countryId: 86) {" +
            "    countryName" +
            "  }" +
            "}");
        
        final JsonObject json = webTarget.path("/graphql")
                .request().post(Entity.json(query), JsonObject.class);
        final String countryName = json.getJsonObject("data").getJsonObject("country").getString("countryName");
        Assertions.assertEquals("China", countryName, "86: China");

        // Delete China
        final String mutationDelete = String.format("{\"query\":\"%s\"}", 
            "mutation {" +
            "  deleteCountry(countryId: 86)" +
            "}");
        
        final Response responseDelete = webTarget.path("/graphql")
                .request().post(Entity.json(mutationDelete));
        Assertions.assertEquals(200, responseDelete.getStatus(), "Normal HTTP response");

        final JsonObject response = webTarget.path("/graphql")
                .request().post(Entity.json(query), JsonObject.class);
        // {
        //    "data":{"country":null},
        //    "errors":[{"path":["country"],"locations":[{"column":10,"line":1}],"message":"Server Error"}]
        // }
        final JsonValue country = response.getJsonObject("data").get("country");
        Assertions.assertEquals(JsonValue.NULL, country, "Record must be deleted");
    }

    @Test
    public void testInsertCountries(){ 

        // Insert Korea and India
        final String mutation = String.format("{\"query\":\"%s\"}", 
            "mutation {" +
            "  insertCountries(" +
            "    countries: [" +
            "      {countryId: 82, countryName: \\\"Korea\\\"}," +
            "      {countryId: 91, countryName: \\\"India\\\"}" +
            "    ]" +
            "  )" +
            "  { countryId countryName }" +
            "}");
        
        final Response response = webTarget.path("/graphql")
                .request().post(Entity.json(mutation));
        Assertions.assertEquals(200, response.getStatus(), "Normal HTTP response");

        final String query = String.format("{\"query\":\"%s\"}", 
            "query {" +
            "  countries {" + 
            "    countryId" +
            "    countryName" +
            "  }" +
            "}");
        
        final JsonObject json = webTarget.path("/graphql")
                .request().post(Entity.json(query), JsonObject.class);
        JsonArray countries = json.getJsonObject("data").getJsonArray("countries");
        Assertions.assertEquals(
            2, 
            countries.stream().filter(v -> {
                final JsonObject country = v.asJsonObject();
                return (82 == country.getInt("countryId") && country.getString("countryName").equals("Korea"))
                    || (91 == country.getInt("countryId") && country.getString("countryName").equals("India"));
            }).count(), 
            "Records of Korea and India must exist."
        );
    }

    @Test
    public void testUpdateCountry(){ 

        // Update USA
        final String mutation = String.format("{\"query\":\"%s\"}", 
            "mutation {" +
            "  updateCountry (" +
            "    countryId: 1, " +
            "    countryName: \\\"United States\\\"" +
            "  )" +
            "  { countryId countryName }" +
            "}");

        final Response response = webTarget.path("/graphql")
                .request().post(Entity.json(mutation));
        Assertions.assertEquals(200, response.getStatus(), "Normal HTTP response");

        final String query = String.format("{\"query\":\"%s\"}", 
            "query {" +
            "  country (countryId: 1) {" +
            "    countryName" +
            "  }" +
            "}");
        
        final JsonObject json = webTarget.path("/graphql")
                .request().post(Entity.json(query), JsonObject.class);
        final String countryName = json.getJsonObject("data").getJsonObject("country").getString("countryName");
        Assertions.assertEquals("United States", countryName, "1: United States");
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