package ca.ulaval.glo2003.domain.restaurant;

import java.util.List;

public interface RestaurantPersistence {
    List<Restaurant> listAll();
    void save(Restaurant restaurant);
    Restaurant getRestaurantById(String id);
    boolean isRestaurantExist(String idRestaurant);
    void delete(String restaurantId);
    void clear();
}
