package ca.ulaval.glo2003.presentation;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import ca.ulaval.glo2003.controllers.ReservationCtrl;
import ca.ulaval.glo2003.controllers.RestaurantCtrl;
import ca.ulaval.glo2003.controllers.assembler.ReservationAssembler;
import ca.ulaval.glo2003.controllers.assembler.RestaurantAssembler;
import ca.ulaval.glo2003.domain.reservation.Customer;
import ca.ulaval.glo2003.domain.reservation.Reservation;
import ca.ulaval.glo2003.domain.reservation.ReservationPersistence;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;
import ca.ulaval.glo2003.infrastructure.ReservationPersistenceMemory;
import ca.ulaval.glo2003.infrastructure.RestaurantPersistenceMemory;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchReservationTest extends JerseyTest {
    private String reservationId;
    private Restaurant restaurant;
    private Reservation reservation;
    private RestaurantAssembler restaurantAssembler;
    private ReservationResource reservationResource;
    private Map<String, String> hours;
    private ReservationPersistence reservationPersistence;
    private RestaurantPersistence restaurantPersistence;
    private RestaurantCtrl restaurantCtrl;
    private ReservationCtrl reservationCtrl;

    @Override
    protected Application configure() {
        reservationPersistence = new ReservationPersistenceMemory();
        restaurantPersistence = new RestaurantPersistenceMemory();
        restaurantAssembler = new RestaurantAssembler();
        reservationCtrl =
            new ReservationCtrl(
                new ReservationAssembler(),
                restaurantAssembler,
                    reservationPersistence,
                    restaurantPersistence
            );
        restaurantCtrl =
            new RestaurantCtrl(restaurantAssembler, restaurantPersistence);
        reservationResource =
            new ReservationResource(
                    reservationPersistence,
                    restaurantPersistence,
                reservationCtrl,
                restaurantCtrl
            );
        SearchReservation searchReservation = new SearchReservation(
            reservationCtrl
        );
        return new ResourceConfig().register(searchReservation);
    }

    @BeforeEach
    public void setup() {
        reservationPersistence.clear();
        restaurantPersistence.clear();
        reservation = new Reservation();
        hours = new HashMap<>();
        hours.put("open", "8:00:00");
        hours.put("close", "23:00:00");
        restaurant = new Restaurant("RestaurantTest", 10, hours);
        Map<String, Integer> reservationsDuration = new HashMap<>();
        reservationsDuration.put("duration", 50);
        restaurant.setReservationsDuration(reservationsDuration);
        reservation.setDate("2023-01-01");
        reservation.setStartTime("12:00:00");
        reservation.setGroupSize(12);
        reservationId = reservation.getId();
        Customer customer = new Customer();
        customer.setName("George");
        customer.setPhoneNumber("418-666-6666");
        customer.setEmail("george@gmail.fr");
        reservation.setCustomer(customer);
        reservation.setRestaurant(restaurant);
        restaurantPersistence.save(restaurant);
        reservationPersistence.save(reservation);
    }

    @Test
    public void getReservation_ReturnResponse200() {
        Response response = target("/reservations/" + reservationId).request().get();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void getReservation_ReturnResponde404() {
        Response response = target("/reservations/" + "wsduoyvvchjldawlbicdshjlv").request().get();
        assertThat(response.getStatus()).isEqualTo(404);
    }
}
