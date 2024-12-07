package ca.ulaval.glo2003.controllers;

import ca.ulaval.glo2003.controllers.assembler.AvailabilityAssembler;
import ca.ulaval.glo2003.controllers.dto.AvailabilityDto;
import ca.ulaval.glo2003.domain.availability.Availability;
import ca.ulaval.glo2003.domain.availability.AvailabilityService;
import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;
import java.time.LocalTime;
import java.util.List;

public class AvailabilityController {
    private AvailabilityAssembler availabilityAssembler;
    private RestaurantPersistence restaurantPersistence;
    private AvailabilityService availabilityService;


    public AvailabilityController(AvailabilityAssembler availabilityAssembler, RestaurantPersistence restaurantPersistence,AvailabilityService availabilityService) {
        this.availabilityAssembler = availabilityAssembler;
        this.restaurantPersistence = restaurantPersistence;
        this.availabilityService = availabilityService;
    }

    public List<AvailabilityDto> getAvailabilityDtoList(String restaurantId, LocalTime openingTime, LocalTime closingTime, int reservationDuration, String date) {
            List<Availability> availabilities = availabilityService.calculateAvailability(restaurantId, openingTime, closingTime, reservationDuration, date);
            return availabilityAssembler.toDtoList(availabilities);
    }

}
