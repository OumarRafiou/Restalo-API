package ca.ulaval.glo2003.infrastructure;

import ca.ulaval.glo2003.domain.reservation.Reservation;
import ca.ulaval.glo2003.domain.review.Review;
import ca.ulaval.glo2003.domain.review.ReviewPersistence;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.query.filters.Filters;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

public class ReviewPersistenceMongo implements ReviewPersistence {
    private final Datastore reviews;
    public ReviewPersistenceMongo (Datastore datastore){
        this.reviews = datastore;
    }
    public List<Review> listAll() {
        return reviews.find(ReviewMongo.class).stream().map(
                reviewMongo -> new Review(reviewMongo.restaurantId,
                        reviewMongo.name,
                        reviewMongo.date,
                        reviewMongo.rating,
                        reviewMongo.message,
                        reviewMongo.reviewId,
                        reviewMongo.username)).toList();
    }

    public void save(Review review) {
        reviews.save(new ReviewMongo(review.getRestaurantId(),
                review.getReviewId(),
                review.getName(),
                review.getDate(),
                review.getRating(),
                review.getMessage(),
                review.getUsername()));
    }

    public Review getReviewById(String id){
        List<Review> test = reviews.find(ReviewMongo.class)
                .filter(Filters.eq("reviewId", id)).stream().map(
                        reviewMongo -> new Review(reviewMongo.restaurantId,
                                reviewMongo.name,
                                reviewMongo.date,
                                reviewMongo.rating,
                                reviewMongo.message,
                                reviewMongo.reviewId,
                                reviewMongo.username)).toList();
        if(test.isEmpty()){
            throw new NotFoundException(
                    "This review does not exist: " + id
            );
        }
        return test.getFirst();
    }

    public List<Review> getReviewByRestaurantId(String restaurantId){
        List<Review> test = reviews.find(ReviewMongo.class)
                .filter(Filters.eq("restaurantId", restaurantId)).stream().map(
                        reviewMongo -> new Review(reviewMongo.restaurantId,
                                reviewMongo.name,
                                reviewMongo.date,
                                reviewMongo.rating,
                                reviewMongo.message,
                                reviewMongo.reviewId,
                                reviewMongo.username)).toList();
        if(test.isEmpty()){
            throw new NotFoundException(
                    "This review does not exist under this restaurant name: " + restaurantId
            );
        }
        return test;
    }

    public List<Review> getReviewByUsername(String name){
        List<Review> test = reviews.find(ReviewMongo.class)
                .filter(Filters.eq("username", name)).stream().map(
                        reviewMongo -> new Review(reviewMongo.restaurantId,
                                reviewMongo.name,
                                reviewMongo.date,
                                reviewMongo.rating,
                                reviewMongo.message,
                                reviewMongo.reviewId,
                                reviewMongo.username)).toList();
        if(test.isEmpty()){
            throw new NotFoundException(
                    "This review does not exist under this username: " + name
            );
        }
        return test;
    }

    public void delete(String reviewId){
        reviews.find(ReviewMongo.class)
                .filter(Filters.eq("reviewId", reviewId)).delete(new DeleteOptions());
    }
    public void clear(){
        reviews.find(ReviewMongo.class).delete(new DeleteOptions().multi(true));
    }

}
