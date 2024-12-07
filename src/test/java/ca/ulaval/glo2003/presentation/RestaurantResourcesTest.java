package ca.ulaval.glo2003.presentation;

import static com.google.common.truth.Truth.*;

import ca.ulaval.glo2003.controllers.RestaurantCtrl;
import ca.ulaval.glo2003.controllers.assembler.RestaurantAssembler;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;
import ca.ulaval.glo2003.infrastructure.RestaurantPersistenceMemory;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RestaurantResourcesTest extends JerseyTest {
    private RestaurantPersistence restaurantPersistence;
    private String restaurantId;

    @Override
    protected Application configure() {
        restaurantPersistence = new RestaurantPersistenceMemory();
        RestaurantAssembler restaurantAssembler = new RestaurantAssembler();
        RestaurantCtrl restaurantCtrl = new RestaurantCtrl(
            restaurantAssembler,
                restaurantPersistence
        );
        RestaurantResource restaurantResource = new RestaurantResource(
            restaurantCtrl
        );
        return new ResourceConfig().register(restaurantResource);
    }

    @BeforeEach
    public void setup() {
        restaurantPersistence.clear();
        Map<String, String> hoursMap = new HashMap<>();
        hoursMap.put("open", "11:00:00");
        hoursMap.put("close", "19:30:00");
        Restaurant rest = new Restaurant("Fictive", 10, hoursMap);
        rest.setOwner("John");
        restaurantId = rest.getId();
        restaurantPersistence.save(rest);
    }

    @Test
    public void createRestaurant_ReturnsResponse201() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Sucess");
        restaurant.setCapacity(10);
        Map<String, String> hoursMap = new HashMap<>();
        hoursMap.put("open", "11:00:00");
        hoursMap.put("close", "19:30:00");
        restaurant.setHours(hoursMap);

        try (
            Response response = target("/restaurants")
                .request()
                .header("Owner", "John")
                .post(Entity.json(restaurant))
        ) {
            assertThat(response.getStatus()).isEqualTo(201);
            assertThat(null != response.getHeaderString(HttpHeaders.LOCATION))
                .isTrue();
        }
    }

    @Test
    public void createRestaurant_argOwnerMissing() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("No Owner");
        restaurant.setCapacity(10);
        Map<String, String> hoursMap = new HashMap<>();
        hoursMap.put("open", "11:00:00");
        hoursMap.put("close", "19:30:00");
        restaurant.setHours(hoursMap);

        Response response = target("/restaurants")
            .request()
            .post(Entity.json(restaurant));
        assertThat(response.getStatus()).isEqualTo(500);
    }

    @Test
    public void createRestaurant_argNameMissing() {
        Restaurant restaurant = new Restaurant();
        restaurant.setCapacity(10);
        Map<String, String> hoursMap = new HashMap<>();
        hoursMap.put("open", "11:00:00");
        hoursMap.put("close", "19:30:00");
        restaurant.setHours(hoursMap);

        Response response = target("/restaurants")
            .request()
            .header("Owner", "John")
            .post(Entity.json(restaurant));
        assertThat(response.getStatus()).isEqualTo(500);
    }

    @Test
    public void createRestaurant_argCapacityMissing() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("No capacity");
        Map<String, String> hoursMap = new HashMap<>();
        hoursMap.put("open", "11:00:00");
        hoursMap.put("close", "19:30:00");
        restaurant.setHours(hoursMap);

        Response response = target("/restaurants")
            .request()
            .header("Owner", "John")
            .post(Entity.json(restaurant));
        assertThat(response.getStatus()).isEqualTo(500);
    }

    @Test
    public void createRestaurant_argHourMissing() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("No Hours");
        restaurant.setCapacity(10);

        Response response = target("/restaurants")
            .request()
            .header("Owner", "John")
            .post(Entity.json(restaurant));
        assertThat(response.getStatus()).isEqualTo(500);
    }

    @Test
    public void createRestaurant_argHourError() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Error hours");
        restaurant.setCapacity(10);
        Map<String, String> hoursMap = new HashMap<>();
        hoursMap.put("open", "11:00:00");
        hoursMap.put("close", "129:30:00");
        restaurant.setHours(hoursMap);

        Response response = target("/restaurants")
            .request()
            .header("Owner", "John")
            .post(Entity.json(restaurant));
        assertThat(response.getStatus()).isEqualTo(500);
    }

    @Test
    public void createRestaurant_argDefaultDurationSet() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("TEST avec le temps");
        restaurant.setCapacity(10);
        Map<String, String> hoursMap = new HashMap<>();
        hoursMap.put("open", "11:00:00");
        hoursMap.put("close", "19:30:00");
        restaurant.setHours(hoursMap);
        Map<String, Integer> reservations = new HashMap<>();
        reservations.put("duration", 120);
        restaurant.setReservationsDuration(reservations);

        Response response = target("/restaurants")
            .request()
            .header("Owner", "John")
            .post(Entity.json(restaurant));
        assertThat(response.getStatus()).isEqualTo(201);
        String locationHeader = response.getHeaderString(HttpHeaders.LOCATION);
        if (locationHeader != null) {
            final String id = locationHeader.substring(
                locationHeader.lastIndexOf('/') + 1
            );
            System.out.println("RESTO ID :" + id);
        }
    }

    @Test
    public void getRestaurant_ReturnsResponse200() {
        Response response = target(
                "/restaurants/" + restaurantId
            )
            .request(MediaType.APPLICATION_JSON)
            .header("Owner", "John")
            .get();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void getRestaurant_argOwnerMissing() {
        Response response = target(
                "/restaurants/d51e10c1-a037-4519-9b75-f0368656bd17"
            )
            .request(MediaType.APPLICATION_JSON)
            .get();
        assertThat(response.getStatus()).isEqualTo(500);
    }

    @Test
    public void getRestaurants_ReturnsResponse200() {
        Response response = target("/restaurants")
            .request(MediaType.APPLICATION_JSON)
            .header("Owner", "John")
            .get();
        assertThat(response.getStatus()).isEqualTo(200);
    }
}
