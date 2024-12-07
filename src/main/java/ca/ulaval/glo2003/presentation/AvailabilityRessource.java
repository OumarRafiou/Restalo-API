package ca.ulaval.glo2003.presentation;

import ca.ulaval.glo2003.controllers.AvailabilityController;
import ca.ulaval.glo2003.controllers.RestaurantCtrl;
import ca.ulaval.glo2003.controllers.dto.AvailabilityDto;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Path("/restaurants")
public class AvailabilityRessource {
    private final RestaurantCtrl restaurantCtrl;
    private final AvailabilityController availabilityController;
    public AvailabilityRessource(RestaurantCtrl restaurantCtrl, AvailabilityController availabilityController){
        this.restaurantCtrl = restaurantCtrl;
        this.availabilityController = availabilityController;
    }

    @GET
    @Path("/{restaurantId}/availabilities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailabilities(
            @QueryParam("date") String date,
            @PathParam("restaurantId") String restaurantId
    ) {
        try {
            Restaurant restaurant = restaurantCtrl.getRestaurantById(restaurantId);
            LocalTime openingTime = LocalTime.parse(restaurant.getHours().get("open"));
            LocalTime closingTime = LocalTime.parse(restaurant.getHours().get("close"));
            List<AvailabilityDto> availabilities = availabilityController.getAvailabilityDtoList(
                    restaurantId,
                    openingTime,
                    closingTime,
                    restaurant.getReservationsDuration().get("duration"),
                    date
            );
            return Response.ok(availabilities).build();

        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorMessage)
                    .build();
        }
    }
}
