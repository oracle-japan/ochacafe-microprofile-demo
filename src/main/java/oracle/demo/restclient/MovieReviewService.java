package oracle.demo.restclient;

import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * interface for both server and client implementation
 */
@Path("/movies")
public interface MovieReviewService {

     @GET
     @Path("/")
     public Set<Movie> getAllMovies();

     @GET
     @Path("/{movieId}/reviews")
     public Set<Review> getAllReviews( @PathParam("movieId") String movieId );

     @GET
     @Path("/{movieId}/reviews/{reviewId}")
     public Review getReview( @PathParam("movieId") String movieId, @PathParam("reviewId") String reviewId );

     @POST
     @Path("/{movieId}/reviews")
     public String submitReview( @PathParam("movieId") String movieId, Review review );

     @PUT
     @Path("/{movieId}/reviews/{reviewId}")
     public Review updateReview( @PathParam("movieId") String movieId, @PathParam("reviewId") String reviewId, Review review );
 }
 