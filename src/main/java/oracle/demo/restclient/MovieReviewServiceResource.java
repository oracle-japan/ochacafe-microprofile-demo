package oracle.demo.restclient;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

/**
 * server implementation of MovieReviewService interface 
 */
@ApplicationScoped
@Provider // let helidon know this is a JAX-RS resource
public class MovieReviewServiceResource implements MovieReviewService {

    private final ConcurrentHashMap<String, Movie> movies = new ConcurrentHashMap<String, Movie>();

    public MovieReviewServiceResource(){
        movies.put("0", new Movie("0", "John Wick"));
        movies.put("1", new Movie("1", "John Wick: Chapter 2"));
        movies.put("2", new Movie("2", "John Wick: Chapter 3 - Parabellum"));
    }

    @Override
    public Set<Movie> getAllMovies() {
        HashSet<Movie> set = new HashSet<Movie>();
        movies.values().forEach(m -> set.add(m));
        return set;
    }

    @Override
    public Set<Review> getAllReviews(final String movieId) {
        return movies.get(movieId).getReviews();
    }

    @Override
    public Review getReview(final String movieId, final String reviewId) {
        return movies.get(movieId).getReview(reviewId);
    }

    @Override
    public String submitReview(final String movieId, final Review review) {
        final Movie m = movies.get(movieId);
        if(!Optional.ofNullable(m).isPresent()) throw new IllegalArgumentException("Movie doesn't exist: " + movieId);
        final Review r = m.addReview(review);
        final String out = "You submitted a review for movie " + m.toString() + " as review " + r.toString();
        System.out.println(out);
        return out;
    }

    @Override
    public Review updateReview(final String movieId, final String reviewId, final Review review) {
        final Movie m = movies.get(movieId);
        if(!Optional.ofNullable(m).isPresent()) throw new IllegalArgumentException("Movie doesn't exist: " + movieId);
        return m.updateReview(reviewId, review);
    }

}
