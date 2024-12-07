package ca.ulaval.glo2003.domain.review;

import java.util.List;

public interface ReviewPersistence {
    List<Review> listAll();
    void save(Review review);
    Review getReviewById(String id);
    List<Review> getReviewByRestaurantId(String restaurantId);
    List<Review> getReviewByUsername(String name);
    void delete(String reviewId);
    void clear();

}
