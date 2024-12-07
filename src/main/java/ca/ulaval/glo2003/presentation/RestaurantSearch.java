package ca.ulaval.glo2003.presentation;

import ca.ulaval.glo2003.controllers.RestaurantCtrl;
import ca.ulaval.glo2003.controllers.dto.RestaurantDto;
import ca.ulaval.glo2003.domain.restaurant.SearchRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/search")
public class RestaurantSearch {
    private final RestaurantCtrl restaurantCtrl;

    public RestaurantSearch(RestaurantCtrl restaurantCtrl) {
        this.restaurantCtrl = restaurantCtrl;
    }

    @POST
    @Path("/restaurants")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchRestaurant(SearchRequest searching) {
        List<RestaurantDto> restaurantOwned = restaurantCtrl.searchRestaurants(
            searching
        );
        return Response.ok(restaurantOwned).build();
    }
}
