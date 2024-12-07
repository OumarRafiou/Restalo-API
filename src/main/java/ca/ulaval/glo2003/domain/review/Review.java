package ca.ulaval.glo2003.domain.review;

import java.util.UUID;

public class Review {
    private String restaurantId;
    private String username;

    public String getUsername() {
        return username;
    }

    private String reviewId;
    private String name;
    private String date;
    private Integer rating;
    private String message;

    public Review(String restaurantId, String name, String date, Integer rating, String message,String username) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.date = date;
        this.rating = rating;
        this.message = message;
        this.reviewId = UUID.randomUUID().toString();
        this.username = username;
    }

    public Review() {
        this.reviewId = UUID.randomUUID().toString();
    }
    public Review(String restaurantId, String name, String date, Integer rating, String message,String reviewId,String username) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.date = date;
        this.rating = rating;
        this.message = message;
        this.reviewId = reviewId;
        this.username = username;
    }

    public Review(String name, String date, Integer rating, String message) {
        this.name = name;
        this.date = date;
        this.rating = rating;
        this.message = message;
        this.reviewId = UUID.randomUUID().toString();
    }


    public String getRestaurantId() {
        return restaurantId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }





    public void setUsername(String username) {
        this.username = username;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public Integer getRating() {
        return rating;
    }

    public String getMessage() {
        return message;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
    public void validate() {

    }

}
