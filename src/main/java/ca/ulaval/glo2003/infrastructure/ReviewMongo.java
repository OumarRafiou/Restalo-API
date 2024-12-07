package ca.ulaval.glo2003.infrastructure;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("reviews")
public class ReviewMongo {
    @Id
    public String reviewId;
    public String username;

    public String restaurantId;
    public String name;
    public String date;
    public Integer rating;
    public String message;

    public ReviewMongo() {}

    public ReviewMongo(String restaurantId, String reviewId, String name, String date, Integer rating, String message,String username) {
        this.restaurantId = restaurantId;
        this.reviewId = reviewId;
        this.name = name;
        this.date = date;
        this.rating = rating;
        this.message = message;
        this.username = username;
    }
}
