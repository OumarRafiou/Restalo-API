package ca.ulaval.glo2003.infrastructure;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity("restaurants")
public class RestaurantMongo {
    @Id
    public String id;
    public String name;
    public String owner;
    public Integer capacity;
    public Map<String, String> hours;
    public Map<String, Integer> reservationsDuration = new HashMap<>();

    public RestaurantMongo() {}
    public RestaurantMongo(String id, String name, int capacity, Map<String, String> hours, String owner, Map<String, Integer> reservationsDuration) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.hours = hours;
        this.owner = owner;
        this.reservationsDuration = reservationsDuration;
    }
}
