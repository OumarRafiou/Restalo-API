package ca.ulaval.glo2003.controllers.assembler;

import ca.ulaval.glo2003.controllers.dto.ReservationDto;
import ca.ulaval.glo2003.controllers.dto.RestaurantDto;
import ca.ulaval.glo2003.domain.reservation.Reservation;
import ca.ulaval.glo2003.domain.reservation.TimeReservation;

public class ReservationAssembler {

    public ReservationDto toDto(
        Reservation reservation,
        RestaurantDto restaurantDto
    ) {
        return new ReservationDto(
            reservation.getId(),
            reservation.getDate(),
            reservation.getGroupSize(),
            reservation.getCustomer(),
            new TimeReservation(reservation),
            restaurantDto
        );
    }
}
