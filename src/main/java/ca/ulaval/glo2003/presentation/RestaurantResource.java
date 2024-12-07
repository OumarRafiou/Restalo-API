package ca.ulaval.glo2003.presentation;

import ca.ulaval.glo2003.controllers.RestaurantCtrl;
import ca.ulaval.glo2003.controllers.dto.RestaurantDto;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.*;

@Path("/restaurants")
public class RestaurantResource {
    private final RestaurantCtrl restaurantCtrl;

    public RestaurantResource(RestaurantCtrl restaurantCtrl) {
        this.restaurantCtrl = restaurantCtrl;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRestaurant(
        @HeaderParam("Owner") String ownerID,
        Restaurant restaurantRequest
    ) {
        restaurantCtrl.createRestaurant(ownerID, restaurantRequest);
        return Response
            .status(Response.Status.CREATED)
            .header(
                HttpHeaders.LOCATION,
                "http://localhost:8080/restaurants/" + restaurantRequest.getId()
            )
            .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listRestaurant(@HeaderParam("Owner") String ownerID) {
        List<RestaurantDto> restaurantOwned = restaurantCtrl.listRestaurants(
            ownerID
        );
        return Response.ok(restaurantOwned).build();
    }

    @GET
    @Path("/{restaurantId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRestaurant(
        @HeaderParam("Owner") String ownerID,
        @PathParam("restaurantId") String restaurantId
    ) {
        RestaurantDto restaurantFormat = restaurantCtrl.getRestaurant(
            ownerID,
            restaurantId
        );
        return Response.ok(restaurantFormat).build();
    }

    @DELETE
    @Path("/{restaurantId}")
    public Response deleteRestaurant(
        @HeaderParam("Owner") String ownerID,
        @PathParam("restaurantId") String restaurantId
    ) {
        restaurantCtrl.deleteRestaurant(ownerID, restaurantId);
        return Response.noContent().build();
    }
}
