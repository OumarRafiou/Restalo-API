package ca.ulaval.glo2003.infrastructure;

import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.query.filters.Filters;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

public class RestaurantPersistenceMongo implements RestaurantPersistence {
    public RestaurantPersistenceMongo(Datastore datastore){
        this.restaurants = datastore;
    }
    private final Datastore restaurants;

    public List<Restaurant> listAll() {
        return restaurants.find(RestaurantMongo.class).stream().map(
                restaurantMongo -> new Restaurant(restaurantMongo.id,
                        restaurantMongo.name,
                        restaurantMongo.capacity,
                        restaurantMongo.owner,
                        restaurantMongo.hours,
                        restaurantMongo.reservationsDuration)).toList();
    }
    public Restaurant getRestaurantById(String id) {
        List<Restaurant> test =  restaurants.find(RestaurantMongo.class)
                .filter(Filters.eq("id", id)).stream().map(
                restaurantMongo -> new Restaurant(restaurantMongo.id,
                        restaurantMongo.name,
                        restaurantMongo.capacity,
                        restaurantMongo.owner,
                        restaurantMongo.hours,
                        restaurantMongo.reservationsDuration)).toList();
        if(test.isEmpty()){
            throw new NotFoundException(
                    "This Restaurant does not exist: " + id
            );
        }
        return test.getFirst();
    }
    public boolean isRestaurantExist(String id) {
        List<Restaurant> test = restaurants.find(RestaurantMongo.class)
                .filter(Filters.eq("id", id)).stream().map(
                        restaurantMongo -> new Restaurant(restaurantMongo.id,
                                restaurantMongo.name,
                                restaurantMongo.capacity,
                                restaurantMongo.owner,
                                restaurantMongo.hours,
                                restaurantMongo.reservationsDuration)).toList();
        if(test.isEmpty()){
            return false;
        }
        return true;
    }

    public void save(Restaurant restaurant) {
       restaurants.save(new RestaurantMongo(restaurant.getId(),restaurant.getName(), restaurant.getCapacity(), restaurant.getHours(), restaurant.getOwner(), restaurant.getReservationsDuration()));
    }
    public void delete(String restaurantId){
        restaurants.find(RestaurantMongo.class)
                .filter(Filters.eq("id",restaurantId))
                .delete(new DeleteOptions());
    }
    public void clear(){restaurants.find(RestaurantMongo.class)
            .delete(new DeleteOptions()
                    .multi(true));
}
}
