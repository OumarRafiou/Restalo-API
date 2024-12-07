package ca.ulaval.glo2003.domain;

import ca.ulaval.glo2003.controllers.ReservationCtrl;
import ca.ulaval.glo2003.domain.availability.Availability;
import ca.ulaval.glo2003.domain.availability.AvailabilityService;
import ca.ulaval.glo2003.domain.reservation.Reservation;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvailabilityServiceTest {
    @Mock
    ReservationCtrl reservationCtrl;
    @Mock
    RestaurantPersistence restaurantPersistence;

    private Restaurant restaurant;

    @InjectMocks
    AvailabilityService availabilityService;
    List<Reservation> reservations = new ArrayList<>();
    private final String restaurantId = "123";
    private final LocalTime openingTime = LocalTime.of(9, 0);
    private final LocalTime closingTime = LocalTime.of(21, 0);
    private final int reservationDuration = 60;
    private final String date = "2024-04-02";

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setCapacity(12);
        Reservation reservation1 = new Reservation();
        reservation1.setStartTime("12:00");
        reservation1.setGroupSize(5);
        Reservation reservation2 = new Reservation();
        reservation2.setStartTime("12:00");
        reservation2.setGroupSize(10);
        reservations.add(reservation1);
        reservations.add(reservation2);

    }

    @Test
    public void validateDate_DateNull_ThrowIllegalArgumentException(){
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> availabilityService.validateDate(null)
        );

        assertEquals("Date parameter is missing", exception.getMessage());
    }

    @Test
    public void validateDate_DateInvalid_ThrowIllegalArgumentException(){
        String invalidDate = "invalidDate";
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> availabilityService.validateDate(invalidDate)
        );

        assertEquals("Date parameter is invalid", exception.getMessage());
    }

    @Test
    public void validateDate_ValidDate_ReturnsLocalDate() {
        LocalDate result = availabilityService.validateDate(date);
        assertEquals(LocalDate.parse(date), result);
    }

    @Test
    public void getNextIntervalStart_returnValidNextInterval(){
        LocalTime openingTimeValid = LocalTime.of(9, 7);
        assertEquals(LocalTime.of(9, 15), availabilityService.getNextIntervalStart(openingTimeValid));
    }

    @Test
    public void getNextIntervalStart_RoundTime_returnValidTime(){
        LocalTime openingTimeValid = LocalTime.of(9, 0);
        assertEquals(LocalTime.of(9, 0), availabilityService.getNextIntervalStart(openingTimeValid));
    }

    @Test
    public void calculateRemainingPlaces_ReturnEmptyPlacesLeft(){
        when(reservationCtrl.getReservationsForTime(restaurantId, LocalTime.of(12, 0), LocalDate.parse(date))).thenReturn(reservations);
        int remainingPlaces = availabilityService.calculateRemainingPlaces(
                LocalTime.of(12, 0),
                reservationDuration,
                12,
                restaurantId,
                LocalDate.parse(date)
        );
        assertEquals(0, remainingPlaces);
    }

    @Test
    public void calculateRemainingPlaces_ReturnValidPlacesLeft(){
        when(reservationCtrl.getReservationsForTime(restaurantId, LocalTime.of(12, 0), LocalDate.parse(date))).thenReturn(reservations);
        int remainingPlaces = availabilityService.calculateRemainingPlaces(
                LocalTime.of(12, 0),
                reservationDuration,
                18,
                restaurantId,
                LocalDate.parse(date)
        );
        assertEquals(18-10-5, remainingPlaces);
    }

    @Test
    public void calculateAvailability_ReturnListofAvailability(){
        LocalDate formattedDate = LocalDate.parse(date);

        when(restaurantPersistence.getRestaurantById(restaurantId)).thenReturn(restaurant);
        when(reservationCtrl.getReservationsForTime(restaurantId, openingTime, formattedDate)).thenReturn(reservations);

        List<Availability> availabilityList = availabilityService.calculateAvailability(restaurantId, openingTime, closingTime, reservationDuration, date);

        assertEquals(availabilityList.size(),45);
    }

}
