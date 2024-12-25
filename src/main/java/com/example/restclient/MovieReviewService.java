package com.example.restclient;

import java.util.Set;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

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
 