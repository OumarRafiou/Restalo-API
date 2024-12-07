package ca.ulaval.glo2003.controllers.dto;

import ca.ulaval.glo2003.domain.reservation.Customer;
import ca.ulaval.glo2003.domain.reservation.TimeReservation;

public record ReservationDto(String number,
                             String date,
                             int groupSize,
                             Customer customer,
                             TimeReservation time,
                             RestaurantDto restaurant) {
}
