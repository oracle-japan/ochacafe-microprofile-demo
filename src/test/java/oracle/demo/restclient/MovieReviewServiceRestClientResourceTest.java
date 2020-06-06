package oracle.demo.restclient;

import javax.json.JsonArray;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import oracle.demo.TestBase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MovieReviewServiceRestClientResourceTest extends TestBase{

    private final Client client = ClientBuilder.newClient();

    public MovieReviewServiceRestClientResourceTest(){
        super();
    }

    @Test
    public void testReviews(){

        // getReview
        String result = client
                .target(getConnectionString("/restclient/1/submit-review?star=5&comment=great!"))
                .request()
                .get(String.class);

        Assertions.assertEquals(
            "You submitted a review for movie [id=1,tiele=JOHN WICK: CHAPTER 2] as review [id=0,star=5,comment=great!]", 
            result);

        // submitReview
        JsonArray jsonArray = client
                .target(getConnectionString("/restclient/1/reviews"))
                .request()
                .get(JsonArray.class);
        
        Assertions.assertEquals("0", jsonArray.getJsonObject(0).getString("id"));
        Assertions.assertEquals("great!", jsonArray.getJsonObject(0).getString("comment"));
        Assertions.assertEquals(5, jsonArray.getJsonObject(0).getInt("star"));
    }


}
