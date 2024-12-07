package ca.ulaval.glo2003.presentation;

import static com.google.common.truth.Truth.*;

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
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservationRessourceTest extends JerseyTest {
    private  String restaurantId;
    JSONObject inputJsonPost;
    Restaurant restaurant;
    private Map<String, String> hours;
    Reservation reservation = new Reservation();
    private ReservationPersistence reservationPersistence;
    private RestaurantPersistence restaurantPersistence;
    private ReservationCtrl reservationCtrl;
    private RestaurantCtrl restaurantCtrl;

    @Override
    protected Application configure() {
        reservationPersistence = new ReservationPersistenceMemory();
        restaurantPersistence = new RestaurantPersistenceMemory();
        reservationCtrl =
            new ReservationCtrl(
                new ReservationAssembler(),
                new RestaurantAssembler(),
                reservationPersistence,restaurantPersistence
            );
        restaurantCtrl =
            new RestaurantCtrl(new RestaurantAssembler(), restaurantPersistence);
        final ResourceConfig rc = new ResourceConfig()
        .register(
                new ReservationResource(
                        reservationPersistence,
                        restaurantPersistence,
                    reservationCtrl,
                    restaurantCtrl
                )
            );

        return rc;
    }

    @BeforeEach
    public void setup() {
        reservationPersistence.clear();
        restaurantPersistence.clear();
        restaurant = new Restaurant();
        restaurant.setName("Test");
        restaurant.setOwner("Joe");
        restaurant.setCapacity(32);
        Map reservationDuration = new HashMap<>();
        reservationDuration.put("duration", 60);
        restaurant.setReservationsDuration(reservationDuration);
        Map hoursRestaurant = new HashMap<>();
        hoursRestaurant.put("open", "10:00:00");
        hoursRestaurant.put("close", "22:00:00");
        restaurant.setHours(hoursRestaurant);
        restaurantId = restaurant.getId();
        restaurantPersistence.save(restaurant);


        hours = new HashMap<>();
        hours.put("open", "8:00:00");
        hours.put("close", "23:00:00");
        Map<String, Integer> reservationsDuration = new HashMap<>();
        reservationsDuration.put("duration", 50);
        reservation.setDate("2023-01-01");
        reservation.setStartTime("12:00:00");
        reservation.setGroupSize(12);
        Customer customer = new Customer();
        customer.setName("George");
        customer.setPhoneNumber("418-666-6666");
        customer.setEmail("george@gmail.fr");
        reservation.setCustomer(customer);
        reservation.setRestaurant(restaurant);

        reservationPersistence.save(reservation);

        JSONObject customerJsonInput = new JSONObject();
        customerJsonInput.put("name", "John Deer");
        customerJsonInput.put("email", "john.deer@gmail.com");
        customerJsonInput.put("phoneNumber", "1234567890");

        inputJsonPost = new JSONObject();
        inputJsonPost.put("date", "2024-03-16");
        inputJsonPost.put("startTime", "13:15:00");
        inputJsonPost.put("groupSize", 2);
        inputJsonPost.put("customer", customerJsonInput);
    }

    @Test
    public void createReservation_ReturnResponse201() {
        System.out.println(restaurantPersistence.listAll().get(0).getId());
        System.out.println(restaurantPersistence.listAll().get(0).getOwner());
        System.out.println(restaurantId);
        Response response = target(
                "/restaurants/" + restaurantId + "/reservations"
            )
            .request()
            .post(Entity.json(inputJsonPost));

        assertThat(response.getStatus()).isEqualTo(201);
    }

    @Test
    public void createReservation_ReturnLocationIDReservation() {
        reservationPersistence.clear();
        Response response = target(
                "/restaurants/" + restaurantId + "/reservations/"
            )
            .request()
            .post(Entity.json(inputJsonPost));

        String idLastReservationCreated = reservationPersistence
            .listAll()
            .getLast()
            .getId();
        String expectedHeader =
            "http://localhost:9998/reservations/" + idLastReservationCreated;

        assertThat(response.getLocation().toString()).isEqualTo(expectedHeader);
    }

    @Test
    public void searchReservations_ReturnResponse200() {
        String ownerId = "Joe";
        String date = "2024-03-16";
        String customerName = "John";

        Response response = target("/restaurants/" + restaurantId + "/reservations")
                .queryParam("date", date)
                .queryParam("customerName", customerName)
                .request()
                .header("Owner", ownerId)
                .get();

        assertThat(response.getStatus()).isEqualTo(200);
    }
    @Test
    public void searchReservations_ReturnResponse200NoQueryParam() {
        String ownerId = "Joe";

        Response response = target("/restaurants/" + restaurantId + "/reservations")
                .request()
                .header("Owner", ownerId)
                .get();

        assertThat(response.getStatus()).isEqualTo(200);
    }
    @Test
    public void searchReservations_UnauthorizedAccess_Returns404() {
        String ownerId = "Not Joe";

        Response response = target("/restaurants/" + restaurantId + "/reservations")
                .request()
                .header("Owner", ownerId)
                .get();

        assertThat(response.getStatus()).isEqualTo(404);
    }
}
