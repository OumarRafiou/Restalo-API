package ca.ulaval.glo2003.infrastructure;

import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;
import jakarta.ws.rs.NotFoundException;

import java.util.*;

public class RestaurantPersistenceMemory implements RestaurantPersistence {
    private final Map<String, Restaurant> restaurants = new HashMap<>();

    public List<Restaurant> listAll() {
        return restaurants.values().stream().toList();
    }

    public void save(Restaurant restaurant) {
        restaurants.put(restaurant.getId(), restaurant);
    }
    public Restaurant getRestaurantById(String id) {
        for(String item : restaurants.keySet()){
            if(Objects.equals(item, id)){
                return restaurants.get(item);
            }
        }
        throw new NotFoundException("This restaurant does not exist");
    }
    public boolean isRestaurantExist(String idRestaurant){
        return restaurants.containsKey(idRestaurant);
    }

    public void delete(String restaurantId){
        restaurants.remove(restaurantId);
    }
    public void clear() {restaurants.clear();}
}
