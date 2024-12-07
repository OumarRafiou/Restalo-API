package ca.ulaval.glo2003.domain.reservation;

import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeReservation {
    private final String start;
    private final String end;
    private final int groupSize;

    public TimeReservation(Reservation reservation) {
        this.start = reservation.getStartTime();
        this.end = calculateEndTime(reservation);
        this.groupSize = reservation.getGroupSize();
    }

    public String calculateEndTime(Reservation reservation) {
        String restaurant = reservation.getRestaurantId();
        if (restaurant == null) {
            throw new IllegalArgumentException(
                "Restaurant cannot be null in Reservation."
            );
        }

        LocalTime startTime = LocalTime.parse(reservation.getStartTime());
        LocalTime endTime = startTime.plusMinutes(
            reservation.getReservationsDuration()
        );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return endTime.format(formatter);
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public int getGroupSize() {
        return groupSize;
    }
}
