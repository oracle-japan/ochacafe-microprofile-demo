package oracle.demo.restclient;

import javax.inject.Inject;
import javax.json.JsonArray;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
public class MovieReviewServiceRestClientResourceTest{

    @Inject private WebTarget webTarget;

    @Test
    public void testReviews(){

        // getReview
        String result = webTarget.path("/restclient/1/submit-review")
                .queryParam("star", 5)
                .queryParam("comment", "great!")
                .request()
                .get(String.class);

        Assertions.assertEquals(
            "You submitted a review for movie [id=1,tiele=JOHN WICK: CHAPTER 2] as review [id=0,star=5,comment=great!]", 
            result);

        // submitReview
        JsonArray jsonArray = webTarget.path("/restclient/1/reviews")
                .request()
                .get(JsonArray.class);
        
        Assertions.assertEquals("0", jsonArray.getJsonObject(0).getString("id"));
        Assertions.assertEquals("great!", jsonArray.getJsonObject(0).getString("comment"));
        Assertions.assertEquals(5, jsonArray.getJsonObject(0).getInt("star"));
    }


}
