package ca.ulaval.glo2003.controllers.assembler;

import ca.ulaval.glo2003.controllers.dto.RestaurantDto;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;

public class RestaurantAssembler {

    public RestaurantDto toDto(Restaurant restaurant) {
        return new RestaurantDto(
            restaurant.getId(),
            restaurant.getName(),
            restaurant.getCapacity(),
            restaurant.getHours(),
            restaurant.getReservationsDuration()
        );
    }
}
