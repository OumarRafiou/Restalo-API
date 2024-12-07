package ca.ulaval.glo2003.presentation;

import ca.ulaval.glo2003.controllers.ReservationCtrl;
import ca.ulaval.glo2003.controllers.RestaurantCtrl;
import ca.ulaval.glo2003.controllers.ReviewCtrl;
import ca.ulaval.glo2003.controllers.dto.RestaurantDto;
import ca.ulaval.glo2003.controllers.dto.ReviewDto;
import ca.ulaval.glo2003.domain.reservation.ReservationPersistence;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;
import ca.ulaval.glo2003.domain.review.Review;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/review")
public class ReviewRessource {
    private final ReviewCtrl reviewCtrl;
    private final RestaurantCtrl restaurantCtrl;
    private RestaurantPersistence restaurantPersistence;
    private ReservationPersistence reservationPersistence;

    public ReviewRessource(
            ReservationPersistence reservationPersistence,
            RestaurantPersistence restaurantPersistence,
            ReviewCtrl reviewCtrl,
            RestaurantCtrl restaurantCtrl
    ) {
        this.reservationPersistence = reservationPersistence;
        this.restaurantPersistence = restaurantPersistence;
        this.reviewCtrl = reviewCtrl;
        this.restaurantCtrl = restaurantCtrl;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRestaurant(
            @HeaderParam("User") String username,
            @HeaderParam("RestaurantId") String restaurantId,
            Review reviewRequest
    ) {
        reviewCtrl.createReview(username, reviewRequest, restaurantId);

        return Response
                .status(Response.Status.CREATED)
                .header(
                        HttpHeaders.LOCATION,
                        "http://localhost:8080/review/" + reviewRequest.getReviewId()
                )
                .build();
    }

    @GET
    @Path("/{reviewId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRestaurant(
            @PathParam("reviewId") String reviewId
    ) {
        ReviewDto reviewFormat = reviewCtrl.getReviewById(
                reviewId
        );
        return Response.ok(reviewFormat).build();
    }

}
