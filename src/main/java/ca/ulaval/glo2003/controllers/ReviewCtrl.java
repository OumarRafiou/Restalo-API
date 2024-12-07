package ca.ulaval.glo2003.controllers;

import ca.ulaval.glo2003.controllers.assembler.RestaurantAssembler;
import ca.ulaval.glo2003.controllers.assembler.ReviewAssembler;
import ca.ulaval.glo2003.controllers.dto.RestaurantDto;
import ca.ulaval.glo2003.controllers.dto.ReviewDto;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;
import ca.ulaval.glo2003.domain.restaurant.SearchRequest;
import ca.ulaval.glo2003.domain.review.Review;
import ca.ulaval.glo2003.domain.review.ReviewPersistence;
import jakarta.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ReviewCtrl {
    private final ReviewAssembler reviewAssembler;
    private final ReviewPersistence reviewPersistence;

    public ReviewCtrl(
            ReviewAssembler reviewAssembler,
            ReviewPersistence reviewPersistence
    ) {
        this.reviewAssembler = reviewAssembler;
        this.reviewPersistence = reviewPersistence;
    }

    public ReviewDto getReviewById(String reviewId) {
        Review laReview = reviewPersistence.getReviewById(reviewId);
        return reviewAssembler.toDto(laReview);
    }

    public void createReview(String username, Review reviewRequest, String restaurantId) {
        reviewRequest.setUsername(username);
        reviewRequest.setRestaurantId(restaurantId);
        reviewRequest.validate();
        reviewPersistence.save(reviewRequest);
    }


}
