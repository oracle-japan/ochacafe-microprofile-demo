package oracle.demo.graphql;


import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
public class CountryGraphQLApiTest{

    @Inject private WebTarget webTarget;

    @Test
    public void testQuery(){ // TODO
/*
        String result = webTarget.path("/grpc-javaobj/client")
                .request()
                .get(String.class);
        Assertions.assertEquals("Hello world", result);
        result = webTarget.path("/grpc-javaobj/client")
                .queryParam("name", "Tom")
                .request()
                .get(String.class);
                Assertions.assertEquals("Hello Tom", result);
*/
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