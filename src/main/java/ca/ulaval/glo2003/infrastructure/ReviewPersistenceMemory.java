package ca.ulaval.glo2003.infrastructure;

import ca.ulaval.glo2003.domain.reservation.Reservation;
import ca.ulaval.glo2003.domain.review.Review;
import ca.ulaval.glo2003.domain.review.ReviewPersistence;
import jakarta.ws.rs.NotFoundException;

import java.util.*;

public class ReviewPersistenceMemory implements ReviewPersistence {
    private final Map<String, Review> reviews = new HashMap<>();

    public List<Review> listAll() {
        return reviews.values().stream().toList();
    }

    public void save(Review review) {
        reviews.put(review.getReviewId(), review);
    }

    public Review getReviewById(String id){
        for(String item : reviews.keySet()){
            if(Objects.equals(item, id)){
                return reviews.get(item);
            }
        }
        throw new NotFoundException("This review does not exist");
    }

    public List<Review> getReviewByRestaurantId(String restaurantId){
        List<Review> reviewList = new ArrayList<>();
        for(Review review : reviews.values()){
            if(review.getRestaurantId().equals(restaurantId)){
                reviewList.add(review);
            }
        }
        if (reviewList.isEmpty()){
        throw new NotFoundException("This review does not exist under this restaurant name");
    }
        return reviewList;
    }

    public List<Review> getReviewByUsername(String name){
        List<Review> reviewListByUsername = new ArrayList<>();
        for(Review review : reviews.values()){
            if(review.getUsername().equals(name)){
                reviewListByUsername.add(review);
            }
        }
        if (reviewListByUsername.isEmpty()){
            throw new NotFoundException("This review does not exist under this username");
        }
        return reviewListByUsername;
    }

    public void delete(String reviewId){
        reviews.remove(reviewId);
    }

    public void clear(){
        reviews.clear();
    }

}
