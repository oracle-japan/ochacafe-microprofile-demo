package oracle.demo.restclient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

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

