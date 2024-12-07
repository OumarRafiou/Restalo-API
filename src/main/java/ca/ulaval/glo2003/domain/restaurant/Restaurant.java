package ca.ulaval.glo2003.domain.restaurant;

import ca.ulaval.glo2003.domain.TimeDuration;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Restaurant {
    private String id;
    private String name;
    private String owner;
    private Integer capacity;
    private Map<String, String> hours;
    private Map<String, Integer> reservationsDuration;

    public Restaurant() {this.id = UUID.randomUUID().toString();}

    public Restaurant(String name, int capacity, Map<String, String> hours) {
        this.name = name;
        this.capacity = capacity;
        this.hours = hours;
        this.id = UUID.randomUUID().toString();
    }

    public Restaurant(
        String name,
        int capacity,
        Map<String, String> hours,
        Map<String, Integer> reservations
    ) {
        this.name = name;
        this.capacity = capacity;
        this.hours = hours;
        this.reservationsDuration = reservations;
        this.id = UUID.randomUUID().toString();
    }
    public Restaurant(
            String id,
            String name,
            int capacity,
            String owner,
            Map<String, String> hours,
            Map<String, Integer> reservations
    ) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.owner = owner;
        this.hours = hours;
        this.reservationsDuration = reservations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getId() {
        return this.id;
    }


    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Map<String, String> getHours() {
        return hours;
    }

    public TimeDuration getTime() {
        if(this.hours != null){
            return new TimeDuration(this.hours.get("open"), this.hours.get("close"));
        }
        return new TimeDuration();
    }

    public void setHours(Map<String, String> hours) {
        this.hours = hours;
    }

    public Map<String, Integer> getReservationsDuration() {
        return this.reservationsDuration;
    }

    public void setReservationsDuration(Map<String, Integer> reservationsDuration) {
        this.reservationsDuration = reservationsDuration;
    }
    public void setId(String id){ this.id = id;}

    public void validate() {
        this.validateOwner();
        this.validateName();
        this.validateCapacity();
        this.validateHours();
        this.validateOpenTime();
        this.validateDefaultReservation();
    }

    private void validateOwner() {
        if (this.owner == null) throw new IllegalArgumentException(
            "Owner is missing."
        );
    }

    private void validateName() {
        if (this.name == null) throw new IllegalArgumentException(
            "Name is missing."
        );
    }

    private void validateCapacity() {
        if (
            this.capacity == null || this.capacity < 1
        ) throw new IllegalArgumentException("Capacity must be at least 1.");
    }

    private boolean isValidTimeFormat(String time) {
        try {
            LocalTime.parse(time);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void validateHours() {
        if (
            this.hours == null ||
            this.getTime().getOpen() == null ||
            this.getTime().getClose() == null
        ) throw new IllegalArgumentException("Hours object is missing.");
        if (
            !isValidTimeFormat(this.getTime().getOpen())
        ) throw new IllegalArgumentException("Invalid open time format.");
        if (
            !isValidTimeFormat(this.getTime().getClose())
        ) throw new IllegalArgumentException("Invalid close time format.");
    }

    private void validateOpenTime() {
        String openTime = this.getTime().getOpen();
        String closeTime = this.getTime().getClose();
        LocalTime open = LocalTime.parse(openTime);
        LocalTime close = LocalTime.parse(closeTime);
        boolean isOpeningAfterMidnight =
            open.isAfter(LocalTime.MIDNIGHT) || open.equals(LocalTime.MIDNIGHT);
        boolean isClosingBeforeMidnight = close.isBefore(
            LocalTime.of(23, 59, 59)
        );
        boolean isDifferenceValid =
            Duration.between(open, close).toHours() >= 1;
        if (
            !isOpeningAfterMidnight ||
            !isClosingBeforeMidnight ||
            !isDifferenceValid
        ) throw new IllegalArgumentException("Invalid opening hours.");
    }

    private void validateDefaultReservation() {
        if (
            this.reservationsDuration == null ||
            this.reservationsDuration.get("duration") == null
        ) this.setReservationsDuration(Map.of("duration", 60));
    }
}
