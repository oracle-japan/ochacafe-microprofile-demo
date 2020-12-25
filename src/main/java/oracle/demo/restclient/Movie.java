package oracle.demo.restclient;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Movie {

    private static AtomicInteger reviewIdCounter = new AtomicInteger();
    private String id;
    private String title;
    private HashMap<String, Review> reviews = new HashMap<String, Review>();

    public Movie(){}

    public Movie(String id, String title){
        this.id = id;
        this.title = title;
    }

    public Review addReview(Review review){
        final String reviewId = review.getId();
        if(Optional.ofNullable(reviewId).isPresent()){
            Review r = reviews.get(reviewId);
            if(Optional.ofNullable(r).isPresent()){
                throw new IllegalArgumentException("Review already exists: " + reviewId);
            }else{
                reviews.put(reviewId, review);
                return review;
            }
        }else{
            final String newId = Integer.toString(reviewIdCounter.getAndIncrement());
            review.setId(newId);
            reviews.put(newId, review);
            return review;
        }
    }

    public Review updateReview(String reviewId, Review review){
        Review r = reviews.get(reviewId);
        if(!Optional.ofNullable(r).isPresent()){
            throw new IllegalArgumentException("Review doesn't exist: " + reviewId);
        }else{
            reviews.put(reviewId, review);
            return review;
        }
    }

    public Review getReview(String reviewId){
        return reviews.get(reviewId);
    }

    public Set<Review> getReviews(){
        final HashSet<Review> set = new HashSet<Review>();
        reviews.values().forEach(r -> set.add(r));
        return set;
    }

    public String getId(){
        return id;
    }

    public String gettitle(){
        return title;
    }

    public String toString(){
        return String.format("[id=%s,tiele=%S]", id, title);
    }

}