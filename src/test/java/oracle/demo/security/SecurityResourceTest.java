package oracle.demo.security;

import java.util.Base64;

import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
public class SecurityResourceTest{

    @Inject private WebTarget webTarget;

    private Base64.Encoder encoder = Base64.getEncoder();

    @Test
    public void testReviews(){

        Response response = webTarget.path("/security/basic/public")
                .request()
                .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeaderVal("hoge", "bar"))
                .get();
        Assertions.assertEquals(200, response.getStatus());

        response = webTarget.path("/security/basic/guest")
                .request()
                .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeaderVal("hoge", "bar"))
                .get();
        Assertions.assertEquals(401, response.getStatus(), "login is required");

        response = webTarget.path("/security/basic/guest")
                .request()
                .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeaderVal("ken", "password3"))
                .get();
        Assertions.assertEquals(200, response.getStatus());

        response = webTarget.path("/security/basic/guest")
                .request()
                .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeaderVal("ken", "passwordx"))
                .get();
        Assertions.assertEquals(401, response.getStatus(), "wrong password");

        response = webTarget.path("/security/basic/admin")
                .request()
                .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeaderVal("ken", "password3"))
                .get();
        Assertions.assertEquals(403, response.getStatus(), "no privilege");

        response = webTarget.path("/security/basic/user")
                .request()
                .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeaderVal("ken", "password3"))
                .get();
        Assertions.assertEquals(403, response.getStatus(), "no privilege");

        response = webTarget.path("/security/basic/admin")
                .request()
                .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeaderVal("mary", "password2"))
                .get();
        Assertions.assertEquals(403, response.getStatus(), "no privilege");

        response = webTarget.path("/security/basic/user")
                .request()
                .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeaderVal("mary", "password2"))
                .get();
        Assertions.assertEquals(200, response.getStatus());

        response = webTarget.path("/security/basic/admin")
                .request()
                .header(HttpHeaders.AUTHORIZATION, getBasicAuthHeaderVal("john", "password1"))
                .get();
        Assertions.assertEquals(200, response.getStatus());

    }

    private String getBasicAuthHeaderVal(String username, String password){
        return String.format("Basic %s", encoder.encodeToString(new String(username + ":" + password).getBytes()));
    }

}

