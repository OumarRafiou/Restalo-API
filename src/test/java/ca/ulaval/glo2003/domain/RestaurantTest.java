package ca.ulaval.glo2003.domain;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RestaurantTest {
    private Restaurant validRestaurant;

    @BeforeEach
    void setUp() {
        validRestaurant = new Restaurant();
        validRestaurant.setName("Test Restaurant");
        validRestaurant.setCapacity(12);
        validRestaurant.setOwner("Test1");
        Map<String, String> hours = new HashMap<>();
        hours.put("open", "11:00:00");
        hours.put("close", "19:30:00");
        validRestaurant.setHours(hours);
        Map<String, Integer> reservations = new HashMap<>();
        reservations.put("duration", 50);
        validRestaurant.setReservationsDuration(reservations);
    }

    @Test
    public void setName_CanSetName() {
        validRestaurant.setName("Test");

        assertThat(validRestaurant.getName()).isEqualTo("Test");
    }

    @Test
    public void setOwner_CanSetOwnerId() {
        validRestaurant.setOwner("OwnerTest");

        assertThat(validRestaurant.getOwner()).isEqualTo("OwnerTest");
    }


    @Test
    public void setCapacity_CanSetCapacity() {
        validRestaurant.setCapacity(15);

        assertThat(validRestaurant.getCapacity()).isEqualTo(15);
    }

    @Test
    public void setHours_CanSetHours() {
        Map<String, String> hours = new HashMap<>();
        hours.put("open", "12:00:00");
        hours.put("close", "20:30:00");
        validRestaurant.setHours(hours);

        assertThat(validRestaurant.getHours()).isEqualTo(hours);
    }

    @Test
    public void setReservations_CanSetReservations() {
        Map<String, Integer> reservations = new HashMap<>();
        reservations.put("duration", 60);
        validRestaurant.setReservationsDuration(reservations);

        assertThat(validRestaurant.getReservationsDuration()).isEqualTo(reservations);
    }

    @Test
    public void validate_ThrowIllegalArgumentExceptionName() {
        Restaurant restaurant = new Restaurant();
        restaurant.setOwner("Test1");
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            restaurant::validate
        );

        assertEquals("Name is missing.", exception.getMessage());
    }

    @Test
    public void validate_ThrowIllegalArgumentExceptionCapacity() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test");
        restaurant.setOwner("Test1");
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            restaurant::validate
        );

        assertEquals("Capacity must be at least 1.", exception.getMessage());
    }

    @Test
    public void validate_ThrowIllegalArgumentExceptionHours() {
        Restaurant restaurant = new Restaurant();
        restaurant.setOwner("Test1");
        restaurant.setName("Test");
        restaurant.setCapacity(12);
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            restaurant::validate
        );

        assertEquals("Hours object is missing.", exception.getMessage());
    }

    @Test
    public void validate_ThrowIllegalArgumentExceptionOpenTime() {
        Restaurant restaurant = new Restaurant();
        restaurant.setOwner("Test1");
        restaurant.setName("Test");
        restaurant.setCapacity(12);
        Map<String, String> hours = new HashMap<>();
        hours.put("open", "22:00:00");
        hours.put("close", "19:30:00");
        restaurant.setHours(hours);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            restaurant::validate
        );

        assertEquals("Invalid opening hours.", exception.getMessage());
    }

    @Test
    public void validate_ThrowIllegalArgumentExceptionCloseTime_NoClosingHours() {
        Restaurant restaurant = new Restaurant();
        restaurant.setOwner("Test1");
        restaurant.setName("Test");
        restaurant.setCapacity(12);
        Map<String, String> hours = new HashMap<>();
        hours.put("open", "11:00:00");
        hours.put("close", "");
        restaurant.setHours(hours);
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            restaurant::validate
        );

        assertEquals("Invalid close time format.", exception.getMessage());
    }

    @Test
    public void validate_DefaultReservation() {
        Restaurant restaurant = new Restaurant();
        restaurant.setOwner("Test1");
        restaurant.setName("Test");
        restaurant.setCapacity(12);
        Map<String, String> hours = new HashMap<>();
        hours.put("open", "11:00:00");
        hours.put("close", "19:30:00");
        restaurant.setHours(hours);
        Map<String, Integer> reservations = new HashMap<>();
        reservations.put("duration", 50);
        restaurant.setReservationsDuration(reservations);
        restaurant.validate();

        assertThat(restaurant.getReservationsDuration()).isEqualTo(reservations);
    }
}
