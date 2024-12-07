package ca.ulaval.glo2003.domain.reservation;

import ca.ulaval.glo2003.domain.availability.Availability;
import ca.ulaval.glo2003.domain.availability.AvailabilityService;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;
import ca.ulaval.glo2003.infrastructure.ReservationPersistenceMemory;
import ca.ulaval.glo2003.infrastructure.RestaurantPersistenceMemory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class Reservation {
    private String id;
    private String date;
    private String startTime;
    private int groupSize;
    private Customer customer;
    private String restaurantId;
    private int reservationsDuration;
    private String open;
    private String close;
    private RestaurantPersistence restaurantPersistence;


    public Reservation() {
        this.id = UUID.randomUUID().toString();
    }

    public Reservation(String id, String date, String startTime, int groupSize, String name, String phoneNumber, String email, String restaurantId, int ReservationDuration, String openTime, String closeTime) {
        this.id = id;
        this.customer = new Customer(name,phoneNumber,email);
        this.date = date;
        this.startTime = startTime;
        this.groupSize = groupSize;
        this.restaurantId = restaurantId;
        this.reservationsDuration = ReservationDuration;
        this.open = openTime;
        this.close = closeTime;
    }
    public Reservation(Restaurant restaurant, RestaurantPersistence restaurantPersistence) {
        this.id = UUID.randomUUID().toString();
        this.restaurantPersistence = restaurantPersistence;
    }

    public int getReservationsDuration() {
        return reservationsDuration;
    }

    public String getOpen() {
        return open;
    }

    public String getClose() {
        return close;
    }

    public void setReservationsDuration(int reservationsDuration) {
        this.reservationsDuration = reservationsDuration;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getId() {
        return id;
    }


    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
    public void setRestaurant(Restaurant restaurant) {
        this.restaurantId = restaurant.getId();
        this.open = restaurant.getTime().getOpen();
        this.close = restaurant.getTime().getClose();
        this.reservationsDuration = restaurant.getReservationsDuration().get("duration");
    }

    public void validate() {
        this.validateId();
        this.validateDate();
        this.validateStartTime();
        this.validateGroupSize();
        this.validateCustomer();
    }

    private void validateId() {
            if (this.id == null) throw new IllegalStateException(
                 "The id cannot be null"
              );
    }
    public void setId(String id){ this.id = id;}


    public void validateDate() {
        if (this.date == null) throw new IllegalStateException(
            "The date cannot be null"
        );
        try {
            LocalDate.parse(this.date);
        } catch (Exception e) {
            throw new IllegalArgumentException("The date is not valid");
        }
    }

    public void validateStartTime() {
        if (this.startTime == null) throw new IllegalStateException(
            "The start time cannot be null"
        );
        try {
            LocalTime.parse(this.startTime);
        } catch (Exception e) {
            throw new IllegalArgumentException("The time is not valid");
        }

        startTimeIsNotToLate();
        LocalTime adjustedStartTime = LocalTime.parse(adjustStartTime());
        LocalTime endOfReservation = addDurationTime(adjustedStartTime);

        adjustedStartTimeIsValid(adjustedStartTime, endOfReservation);
    }

    public LocalTime addDurationTime(LocalTime ajustedStartTime) {
        return ajustedStartTime.plusMinutes(this.reservationsDuration);
    }

    public void startTimeIsNotToLate() {
        LocalTime originalStartTime = LocalTime.parse(this.startTime);
        LocalTime startTimeMaxValidTime = LocalTime.parse("23:45:00");
        if (
            originalStartTime.toSecondOfDay() >
            startTimeMaxValidTime.toSecondOfDay()
        ) {
            throw new IllegalArgumentException("The start time is to late");
        }
    }

    public String adjustStartTime() {
        LocalTime startTimeFormat = LocalTime.parse(this.startTime);

        long minutesToAddForNext15 = 0;

        if (startTimeFormat.getMinute() % 15 != 0) {
            minutesToAddForNext15 = 15 - (startTimeFormat.getMinute() % 15);
            this.startTime =
                (startTimeFormat.plusMinutes(minutesToAddForNext15)).format(
                        DateTimeFormatter.ISO_TIME
                    );
        }
        return this.startTime;
    }

    public void adjustedStartTimeIsValid(
        LocalTime adjustedStartTime,
        LocalTime endOfReservation
    ) {
        adjustedStartTimeBeginAfterOpeningTime(adjustedStartTime);
        adjustedStartTimeFinishBeforeClosingTime(endOfReservation);
        reservationFinishTheSameDay(adjustedStartTime, endOfReservation);
    }

    private void adjustedStartTimeBeginAfterOpeningTime(
        LocalTime adjustedStartTime
    ) {
        LocalTime openingTime = LocalTime.parse(
            this.open
        );
        if (adjustedStartTime.toSecondOfDay() < openingTime.toSecondOfDay()) {
            throw new IllegalArgumentException(
                "The reservation begin before opening time"
            );
        }
    }

    private void adjustedStartTimeFinishBeforeClosingTime(
        LocalTime endOfReservation
    ) {
        LocalTime closingTime = LocalTime.parse(
            this.close
        );
        if (endOfReservation.toSecondOfDay() > closingTime.toSecondOfDay()) {
            throw new IllegalArgumentException(
                "The reservation finish after closing time"
            );
        }
    }

    private void reservationFinishTheSameDay(
        LocalTime ajustedStartTime,
        LocalTime endOfReservation
    ) {
        if (
            endOfReservation.toSecondOfDay() < ajustedStartTime.toSecondOfDay()
        ) {
            throw new IllegalArgumentException(
                "The reservation finish tomorrow"
            );
        }
    }

    public void validateGroupSize() {
        if (this.groupSize < 1) throw new IllegalArgumentException(
            "The group size cannot be smaller than 1"
        );
    }

    public void validateCustomer() {
        if (customer == null) throw new IllegalStateException(
            "The customer cannot be null"
        ); else {
            customer.validate();
        }
    }
}
