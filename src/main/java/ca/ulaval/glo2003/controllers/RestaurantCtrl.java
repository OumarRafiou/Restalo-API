package ca.ulaval.glo2003.controllers;

import ca.ulaval.glo2003.controllers.assembler.RestaurantAssembler;
import ca.ulaval.glo2003.controllers.dto.RestaurantDto;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;
import ca.ulaval.glo2003.domain.restaurant.SearchRequest;
import jakarta.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;

public class RestaurantCtrl {
    private final RestaurantAssembler restaurantAssembler;
    private final RestaurantPersistence restaurantPersistence;

    public RestaurantCtrl(
        RestaurantAssembler restaurantAssembler,
        RestaurantPersistence restaurantPersistence
    ) {
        this.restaurantAssembler = restaurantAssembler;
        this.restaurantPersistence = restaurantPersistence;
    }

    public Restaurant getRestaurantById(String restaurantId) {
        return restaurantPersistence.getRestaurantById(restaurantId);
    }

    public List<RestaurantDto> searchRestaurants(SearchRequest searching) {
        List<Restaurant> restaurantList = restaurantPersistence.listAll();
        List<RestaurantDto> restaurantOwned = new ArrayList<>();
        searching.validate();
        for (Restaurant restaurant : restaurantList) {
            boolean validName = searching.verificationName(
                restaurant.getName()
            );
            boolean validTime = searching.verificationTime(
                restaurant.getHours()
            );
            if (validName && validTime) {
                restaurantOwned.add(restaurantAssembler.toDto(restaurant));
            }
        }
        return restaurantOwned;
    }

    public void createRestaurant(String ownerID, Restaurant restaurantRequest) {
        restaurantRequest.setOwner(ownerID);
        restaurantRequest.validate();
        restaurantPersistence.save(restaurantRequest);
    }

    public List<RestaurantDto> listRestaurants(String ownerID) {
        if (ownerID == null || ownerID.isEmpty()) {
            throw new IllegalArgumentException("User not logged-in");
        }

        List<RestaurantDto> restaurantOwned = new ArrayList<>();
        for (Restaurant restaurant : restaurantPersistence.listAll()) {
            if (restaurant.getOwner().equals(ownerID)) {
                restaurantOwned.add(restaurantAssembler.toDto(restaurant));
            }
        }
        return restaurantOwned;
    }

    public RestaurantDto getRestaurant(String ownerID, String restaurantId) {
        if (ownerID == null || ownerID.isEmpty()) {
            throw new IllegalArgumentException("Owner is missing.");
        }
        if (restaurantId == null || restaurantId.isEmpty()) {
            throw new IllegalArgumentException("Restaurant ID is missing.");
        }

        for (Restaurant restaurant : restaurantPersistence.listAll()) {
            if (restaurant.getId().equals(restaurantId)) {
                if (restaurant.getOwner().equals(ownerID)) {
                    return restaurantAssembler.toDto(restaurant);
                } else {
                    throw new NotFoundException(
                        "Restaurant not associated with Owner: " + ownerID
                    );
                }
            }
        }
        throw new NotFoundException(
            "This Restaurant does not exist: " + restaurantId
        );
    }

    public void deleteRestaurant(String ownerID, String restaurantId) {
        if (ownerID == null || ownerID.isEmpty()) {
            throw new IllegalArgumentException("Owner is missing.");
        }
        if (restaurantId == null || restaurantId.isEmpty()) {
            throw new IllegalArgumentException("Restaurant ID is missing.");
        }

        List<Restaurant> restaurantList = restaurantPersistence.listAll();
        List<Restaurant> restaurantListCopy = new ArrayList<>(restaurantList);
        for (Restaurant restaurant : restaurantListCopy) {
            if (restaurant.getId().equals(restaurantId)) {
                if (restaurant.getOwner().equals(ownerID)) {
                    restaurantPersistence.delete(restaurant.getId());
                    return;
                } else {
                    throw new NotFoundException(
                        "Restaurant not associated with Owner: " + ownerID
                    );
                }
            }
        }
        throw new NotFoundException(
            "This Restaurant does not exist: " + restaurantId
        );
    }


}
