package ca.ulaval.glo2003.domain;

import static com.google.common.truth.Truth.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import ca.ulaval.glo2003.domain.reservation.Customer;
import ca.ulaval.glo2003.domain.reservation.Reservation;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReservationTest {
    private Reservation validReservation;
    private Reservation invalidReservation;
    private Reservation testReservation1;
    private Reservation testReservation2;
    private Reservation testReservation3;

    @Mock
    Customer customer;

    @Mock
    Restaurant restaurant;

    @Mock
    RestaurantPersistence restaurantPersistence;

    @BeforeEach
    public void setup() {
        validReservation = new Reservation();
        validReservation.setDate("2023-01-01");
        validReservation.setStartTime("12:00:00");
        validReservation.setGroupSize(12);
        validReservation.setCustomer(customer);
        when(restaurant.getTime()).thenReturn(new TimeDuration("10:00:00", "22:00:00"));
        Map<String, Integer> reservationDuration = new HashMap<>();
        reservationDuration.put("duration", 40);
        when(restaurant.getReservationsDuration()).thenReturn(reservationDuration);
        when(restaurant.getId()).thenReturn("Abacaba");
        validReservation.setRestaurant(restaurant);

        invalidReservation = new Reservation();
        invalidReservation.setDate(null);
        invalidReservation.setStartTime("25:00:00");
        invalidReservation.setGroupSize(0);
        invalidReservation.setCustomer(null);

        testReservation1 = new Reservation();
        testReservation2 = new Reservation();
        testReservation3 = new Reservation();
    }

    @Test
    public void setDate_CanSetDate() {
        validReservation.setDate("2024-03-16");

        assertThat(validReservation.getDate()).isEqualTo("2024-03-16");
    }

    @Test
    public void setStartTime_CanSetStartTime() {
        validReservation.setStartTime("11:30:55");

        assertThat(validReservation.getStartTime()).isEqualTo("11:30:55");
    }

    @Test
    public void setGroupSize_CanSetGroupSize() {
        validReservation.setGroupSize(1);

        assertThat(validReservation.getGroupSize()).isEqualTo(1);
    }

    @Test
    public void setCustomer_CanSetCustomer() {
        validReservation.setCustomer(customer);

        assertThat(validReservation.getCustomer()).isEqualTo(customer);
    }

    @Test
    public void setRestaurant_CanSetRestaurant() {
        validReservation.setRestaurant(restaurant);


        assertThat(validReservation.getOpen()).isEqualTo("10:00:00");
        assertThat(validReservation.getClose()).isEqualTo("22:00:00");
        assertThat(validReservation.getReservationsDuration()).isEqualTo(40);
        assertThat(validReservation.getRestaurantId()).isEqualTo("Abacaba");
    }


    @Test
    public void validateDate_ThrowIfInvalidDate() {
        assertDoesNotThrow(() -> validReservation.validateDate());
    }

    @Test
    public void adjustStartTime_TimeAdjustedToNext15MinuteLapsBackTo0() {
        testReservation3.setStartTime("23:55:00");

        testReservation3.adjustStartTime();

        assertThat(testReservation3.getStartTime()).isEqualTo("00:00:00");
    }

    @Test
    public void adjustStartTime_TimeNotAdjustedIfAlreadyIn15Minutes() {
        testReservation1.setStartTime("13:00:00");

        testReservation1.adjustStartTime();

        assertThat(testReservation1.getStartTime()).isEqualTo("13:00:00");
    }

    @Test
    public void adjustStartTime_TimeAdjustedToNext15Minute() {
        testReservation1.setStartTime("12:12:00");

        testReservation1.adjustStartTime();

        assertThat(testReservation1.getStartTime()).isEqualTo("12:15:00");
    }

    @Test
    public void validateStartTime_ThrowIfInvalidStartTime() {
        testReservation1.setStartTime("23:55:00");

        assertThrows(
            IllegalArgumentException.class,
            () -> testReservation1.validateStartTime()
        );
    }

    @Test
    public void validateStartTime_NoThrowIfValidStartTime() {
        validReservation.setStartTime("11:55:00");

        assertDoesNotThrow(() -> validReservation.validateStartTime());
    }

    @Test
    public void addDurationTime_ReturnTheEndOfTheReservation() {
        LocalTime adjustedStartTime = LocalTime.parse("10:00:00");
        LocalTime endOfReservationIntendedValue = LocalTime.parse("10:40:00");

        LocalTime returnedEndOfReservation = validReservation.addDurationTime(
            adjustedStartTime
        );

        assertThat(returnedEndOfReservation)
            .isEqualTo(endOfReservationIntendedValue);
    }

    @Test
    public void startTimeIsNotToLate_ThrowIfTooLate() {
        testReservation2.setStartTime("23:46:00");

        assertThrows(
            IllegalArgumentException.class,
            () -> testReservation2.startTimeIsNotToLate()
        );
    }

    @Test
    public void startTimeIsNotToLate_NoThrowIfNotTooLate() {
        assertDoesNotThrow(() -> validReservation.startTimeIsNotToLate());
    }

    @Test
    public void adjustStartTimeIsValid_NoThrowIfStartTimeAndCloseTimeSameRestaurant() {
        LocalTime adjustedStartTimeSameOpen = LocalTime.parse("10:00:00");
        LocalTime endOfReservationSameClose = LocalTime.parse("22:00:00");

        assertDoesNotThrow(
            () ->
                validReservation.adjustedStartTimeIsValid(
                    adjustedStartTimeSameOpen,
                    endOfReservationSameClose
                )
        );
    }

    @Test
    public void adjustStartTimeIsValid_NoThrowIfValidStartTimeAndCloseTime() {
        LocalTime adjustedStartTimeAfterOpen = LocalTime.parse("20:00:00");
        LocalTime endOfReservationBeforeClose = LocalTime.parse("21:00:00");
        assertDoesNotThrow(
            () ->
                validReservation.adjustedStartTimeIsValid(
                    adjustedStartTimeAfterOpen,
                    endOfReservationBeforeClose
                )
        );
    }

    @Test
    public void adjustStartTimeIsValid_ThrowIfInvalidStartTime() {
        LocalTime adjustedStartTimeBeforeOpen = LocalTime.parse("01:00:00");
        LocalTime endOfReservationBeforeClose = LocalTime.parse("21:00:00");

        assertThrows(
            IllegalArgumentException.class,
            () ->
                validReservation.adjustedStartTimeIsValid(
                    adjustedStartTimeBeforeOpen,
                    endOfReservationBeforeClose
                )
        );
    }

    @Test
    public void adjustStartTimeIsValid_ThrowIfInvalidCloseTime() {
        LocalTime adjustedStartTimeAfterOpen = LocalTime.parse("10:00:00");
        LocalTime endOfReservationAfterClose = LocalTime.parse("23:00:00");

        assertThrows(
            IllegalArgumentException.class,
            () ->
                validReservation.adjustedStartTimeIsValid(
                    adjustedStartTimeAfterOpen,
                    endOfReservationAfterClose
                )
        );
    }

    @Test
    public void validateGroupSize_NoThrowIfValidGroupSize() {
        validReservation.setGroupSize(1);

        assertDoesNotThrow(() -> validReservation.validateGroupSize());
    }

    @Test
    public void validateGroupSize_ThrowIfInvalidGroupSize() {
        testReservation1.setGroupSize(0);

        assertThrows(
            IllegalArgumentException.class,
            () -> testReservation1.validateGroupSize()
        );
    }

    @Test
    public void validateCustomer_ThrowIfInvalidCustomer() {
        assertDoesNotThrow(() -> validReservation.validateCustomer());
    }
}
