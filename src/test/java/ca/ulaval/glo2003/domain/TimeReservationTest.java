package ca.ulaval.glo2003.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ca.ulaval.glo2003.domain.reservation.Reservation;
import ca.ulaval.glo2003.domain.reservation.TimeReservation;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TimeReservationTest {
    private TimeReservation timeReservation;

    @Mock
    private Reservation reservation;

    @Mock
    private Restaurant restaurant;

    @BeforeEach
    public void setUp() {
        Map<String, String> hours = new HashMap<>();
        hours.put("open", "11:00:00");
        hours.put("close", "19:30:00");

        Map<String, Integer> reservations = new HashMap<>();
        reservations.put("duration", 50);
        when(reservation.getRestaurantId()).thenReturn("abacaba");
        when(reservation.getStartTime()).thenReturn("14:00");
        when(reservation.getReservationsDuration()).thenReturn(50);

        timeReservation = new TimeReservation(reservation);
    }

    @Test
    public void calculateEndTime_ReturnsEndTime() {
        String endTime = timeReservation.calculateEndTime(reservation);
        assertEquals("14:50:00", endTime);
    }

    @Test
    public void calculateEndTime_ThrowIllegalArgumentException() {
        when(reservation.getRestaurantId()).thenReturn(null);
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> {
                timeReservation.calculateEndTime(reservation);
            }
        );

        assertEquals(
            "Restaurant cannot be null in Reservation.",
            exception.getMessage()
        );
    }
}
