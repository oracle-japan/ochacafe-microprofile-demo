package oracle.demo.restclient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

/**
 * use RestClient in order to invoke MovieReviewServiceResource
 */
@ApplicationScoped
@Path("/restclient")
public class MovieReviewServiceRestClientResource {

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{movieId}/reviews")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Review> getReviews(@PathParam("movieId") String movieId) throws URISyntaxException {
        System.out.println(uriInfo.getRequestUri());
        
        final URI apiUri = uriInfo.getBaseUri();
        final MovieReviewService reviewSvc = RestClientBuilder.newBuilder()
            .baseUri(apiUri)
            .build(MovieReviewService.class);
        return reviewSvc.getAllReviews(movieId);
    }

    @GET
    @Path("/{movieId}/submit-review")
    @Produces(MediaType.TEXT_PLAIN)
    public String submitReview(
            @PathParam("movieId") String movieId, 
            @QueryParam("star") int star,
            @QueryParam("comment") String comment) 
            throws URISyntaxException {

        System.out.println(uriInfo.getRequestUri());
        
        final URI apiUri = uriInfo.getBaseUri();
        final MovieReviewService reviewSvc = RestClientBuilder.newBuilder()
            .baseUri(apiUri)
            .build(MovieReviewService.class);
        final Review review = new Review(star , comment);
        return reviewSvc.submitReview(movieId, review);
    }


}

