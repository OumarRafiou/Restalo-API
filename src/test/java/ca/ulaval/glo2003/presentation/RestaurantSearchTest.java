package ca.ulaval.glo2003.presentation;

import static com.google.common.truth.Truth.assertThat;

import ca.ulaval.glo2003.controllers.RestaurantCtrl;
import ca.ulaval.glo2003.controllers.assembler.RestaurantAssembler;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;
import ca.ulaval.glo2003.infrastructure.RestaurantPersistenceMemory;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RestaurantSearchTest extends JerseyTest {
    private RestaurantPersistence restaurantPersistence;
    private String restaurant1Id;
    private String restaurant2Id;

    @Override
    protected Application configure() {
        restaurantPersistence = new RestaurantPersistenceMemory();
        return new ResourceConfig()
        .register(
                new RestaurantSearch(
                    new RestaurantCtrl(
                        new RestaurantAssembler(),
                            restaurantPersistence
                    )
                )
            );
    }

    @BeforeEach
    public void setup() {
        restaurantPersistence.clear();

        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("Test Restaurant 1");
        restaurant1.setOwner("Owner 1");
        restaurant1.setCapacity(12);
        Map<String, String> hours1 = new HashMap<>();
        hours1.put("open", "10:00:00");
        hours1.put("close", "22:00:00");
        restaurant1.setHours(hours1);
        restaurant1Id = restaurant1.getId();
        restaurantPersistence.save(restaurant1);

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setName("Test Restaurant 2");
        restaurant2.setOwner("Owner 2");
        restaurant2.setCapacity(12);
        Map<String, String> hours2 = new HashMap<>();
        hours2.put("open", "09:00:00");
        hours2.put("close", "20:00:00");
        restaurant2.setHours(hours2);
        restaurant2Id = restaurant2.getId();
        restaurantPersistence.save(restaurant2);
    }

    @Test
    public void searchRestaurant_ReturnsMatchingRestaurantsByHour()
        throws Exception {
        JSONObject searchJson = new JSONObject();
        searchJson.put("name", "Test");
        JSONObject hoursJson = new JSONObject();
        hoursJson.put("from", "10:00:00");
        hoursJson.put("to", "21:00:00");
        searchJson.put("opened", hoursJson);

        Response response = target("/search/restaurants")
            .request()
            .post(Entity.json(searchJson));

        assertThat(response.getStatus()).isEqualTo(200);

        String responseBody = response.readEntity(String.class);
        JSONArray restaurants = (JSONArray) new JSONParser()
        .parse(responseBody);

        assertThat(restaurants.size()).isEqualTo(1);

        JSONObject restaurantJson = (JSONObject) restaurants.get(0);
        assertThat(restaurantJson.get("id")).isEqualTo(restaurant1Id);
        assertThat(restaurantJson.get("name")).isEqualTo("Test Restaurant 1");
        assertThat(restaurantJson.get("hours"))
            .isEqualTo(Map.of("open", "10:00:00", "close", "22:00:00"));
    }

    @Test
    public void searchRestaurant_ReturnsMatchingRestaurantsByName()
        throws Exception {
        JSONObject searchJson = new JSONObject();
        searchJson.put("name", "Test Restaurant 2");
        JSONObject hoursJson = new JSONObject();
        hoursJson.put("from", "10:00:00");
        hoursJson.put("to", "19:00:00");
        searchJson.put("opened", hoursJson);

        Response response = target("/search/restaurants")
            .request()
            .post(Entity.json(searchJson));

        assertThat(response.getStatus()).isEqualTo(200);

        String responseBody = response.readEntity(String.class);
        JSONArray restaurants = (JSONArray) new JSONParser()
        .parse(responseBody);

        assertThat(restaurants.size()).isEqualTo(1);

        JSONObject restaurantJson = (JSONObject) restaurants.get(0);
        assertThat(restaurantJson.get("id")).isEqualTo(restaurant2Id);
        assertThat(restaurantJson.get("name")).isEqualTo("Test Restaurant 2");
        assertThat(restaurantJson.get("hours"))
            .isEqualTo(Map.of("open", "09:00:00", "close", "20:00:00"));
    }

    @Test
    public void searchRestaurant_ReturnsMultipleRestaurant() throws Exception {
        JSONObject searchJson = new JSONObject();
        searchJson.put("name", "Test");
        JSONObject hoursJson = new JSONObject();
        hoursJson.put("to", "18:00:00");
        searchJson.put("opened", hoursJson);

        Response response = target("/search/restaurants")
            .request()
            .post(Entity.json(searchJson));

        assertThat(response.getStatus()).isEqualTo(200);

        String responseBody = response.readEntity(String.class);
        JSONArray restaurants = (JSONArray) new JSONParser()
        .parse(responseBody);

        assertThat(restaurants.size()).isEqualTo(2);
    }

    @Test
    public void searchRestaurant_ReturnsMultipleRestaurantWithNoArgument()
        throws Exception {
        JSONObject searchJson = new JSONObject();

        Response response = target("/search/restaurants")
            .request()
            .post(Entity.json(searchJson));

        assertThat(response.getStatus()).isEqualTo(200);

        String responseBody = response.readEntity(String.class);
        JSONArray restaurants = (JSONArray) new JSONParser()
        .parse(responseBody);

        assertThat(restaurants.size()).isEqualTo(2);
    }

    @Test
    public void searchRestaurant_ReturnsNoRestaurantByHour() throws Exception {
        JSONObject searchJson = new JSONObject();
        searchJson.put("name", "Test");
        JSONObject hoursJson = new JSONObject();
        hoursJson.put("to", "23:30:00");
        searchJson.put("opened", hoursJson);

        Response response = target("/search/restaurants")
            .request()
            .post(Entity.json(searchJson));

        assertThat(response.getStatus()).isEqualTo(200);

        String responseBody = response.readEntity(String.class);
        JSONArray restaurants = (JSONArray) new JSONParser()
        .parse(responseBody);

        assertThat(restaurants.size()).isEqualTo(0);
    }

    @Test
    public void searchRestaurant_ReturnsNoRestaurantByName() throws Exception {
        JSONObject searchJson = new JSONObject();
        searchJson.put("name", "Barack");
        JSONObject hoursJson = new JSONObject();
        hoursJson.put("to", "17:30:00");
        searchJson.put("opened", hoursJson);

        Response response = target("/search/restaurants")
            .request()
            .post(Entity.json(searchJson));

        assertThat(response.getStatus()).isEqualTo(200);

        String responseBody = response.readEntity(String.class);
        JSONArray restaurants = (JSONArray) new JSONParser()
        .parse(responseBody);

        assertThat(restaurants.size()).isEqualTo(0);
    }
}
