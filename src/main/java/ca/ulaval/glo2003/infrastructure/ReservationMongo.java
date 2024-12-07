package ca.ulaval.glo2003.infrastructure;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("reservations")
public class ReservationMongo {
    @Id
    public String id;
    public String date;

    public String startTime;
    public int groupSize;
    public String name;
    public String phoneNumber;
    public String email;
    public String restaurantId;
    public int reservationDuration;
    public String openTime;
    public String closeTime;
    public ReservationMongo() {}

    public ReservationMongo(String id, String date, String startTime, int groupSize, String name, String phoneNumber, String email, String restaurantId, int reservationDuration, String openTime, String closeTime) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.groupSize = groupSize;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.restaurantId = restaurantId;
        this.reservationDuration = reservationDuration;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
