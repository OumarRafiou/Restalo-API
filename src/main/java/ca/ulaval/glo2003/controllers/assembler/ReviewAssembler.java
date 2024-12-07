package ca.ulaval.glo2003.controllers.assembler;

import ca.ulaval.glo2003.controllers.dto.RestaurantDto;
import ca.ulaval.glo2003.controllers.dto.ReviewDto;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.domain.review.Review;

public class ReviewAssembler {

    public ReviewDto toDto(Review review) {
        return new ReviewDto(
                review.getName(),
                review.getDate(),
                review.getRating(),
                review.getMessage()
        );
    }
}
