package ca.ulaval.glo2003.domain.availability;
import ca.ulaval.glo2003.controllers.ReservationCtrl;
import ca.ulaval.glo2003.domain.reservation.Reservation;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AvailabilityService {
    private final RestaurantPersistence restaurantPersistence;
    private final ReservationCtrl reservationCtrl;

    public AvailabilityService(RestaurantPersistence restaurantPersistence,ReservationCtrl reservationCtrl) {
        this.restaurantPersistence = restaurantPersistence;
        this.reservationCtrl = reservationCtrl;
    }

    public List<Availability> calculateAvailability(String restaurantId, LocalTime openingTime, LocalTime closingTime, int reservationDuration, String date) {
        LocalDate formatDate = validateDate(date);
        Restaurant restaurant = restaurantPersistence.getRestaurantById(restaurantId);
        int capacity = restaurant.getCapacity();
        List<Availability> availabilityList = new ArrayList<>();
        LocalTime currentIntervalStart = getNextIntervalStart(openingTime);
        LocalTime lastIntervalEnd = getLastIntervalEnd(closingTime, reservationDuration);

        while (currentIntervalStart.isBefore(lastIntervalEnd) || currentIntervalStart.equals(lastIntervalEnd)) {
            int remainingPlaces = calculateRemainingPlaces(currentIntervalStart, reservationDuration, capacity, restaurantId, formatDate);
            String formattedDateTime = LocalDateTime.of(formatDate, currentIntervalStart).toString();
            availabilityList.add(new Availability(formattedDateTime, remainingPlaces));
            currentIntervalStart = currentIntervalStart.plusMinutes(15);
        }

        return availabilityList;
    }

    public LocalDate validateDate(String date) {
        if (date == null) {
            throw new IllegalArgumentException("Date parameter is missing");
        }
        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            throw new IllegalArgumentException("Date parameter is invalid");
        }
    }

    public LocalTime getNextIntervalStart(LocalTime openingTime) {
        int intervalMinutes = 15;
        int minute = openingTime.getMinute();
        int remainder = minute % intervalMinutes;
        if (remainder == 0) {
            return openingTime;
        }
        return openingTime.plusMinutes(intervalMinutes - (openingTime.getMinute() % intervalMinutes));
    }

    private LocalTime getLastIntervalEnd(LocalTime closingTime, int reservationDuration) {
        int intervalMinutes = 15;
        return closingTime.minusMinutes((closingTime.getMinute() % intervalMinutes) + reservationDuration);
    }

    public int calculateRemainingPlaces(LocalTime intervalStart, int reservationDuration, int capacity, String restaurantId, LocalDate date) {
        List<Reservation> reservations = reservationCtrl.getReservationsForTime(restaurantId, intervalStart, date);
        int actualRemainingPlaces = capacity;

        for (Reservation reservation : reservations) {
            LocalTime reservationStartTime = LocalTime.parse(reservation.getStartTime());
            LocalTime reservationEndTime = reservationStartTime.plusMinutes(reservationDuration);

            if ((reservationStartTime.isBefore(intervalStart.plusMinutes(15)) && reservationEndTime.isAfter(intervalStart))
                    || (reservationStartTime.isAfter(intervalStart) && reservationStartTime.isBefore(intervalStart.plusMinutes(15)))) {
                actualRemainingPlaces -= reservation.getGroupSize();
            }
        }
        return Math.max(actualRemainingPlaces, 0);
    }


}
