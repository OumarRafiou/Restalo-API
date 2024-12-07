package ca.ulaval.glo2003.infrastructure;

import ca.ulaval.glo2003.domain.reservation.Reservation;
import ca.ulaval.glo2003.domain.reservation.ReservationPersistence;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.query.filters.Filters;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

public class ReservationPersistenceMongo implements ReservationPersistence {
    public ReservationPersistenceMongo(Datastore datastore){
        this.reservations = datastore;
    }
    private final Datastore reservations;

    public List<Reservation> listAll() {
        return reservations.find(ReservationMongo.class).stream().map(
                reservationMongo -> new Reservation(reservationMongo.id,
                        reservationMongo.date,
                        reservationMongo.startTime,
                        reservationMongo.groupSize,
                        reservationMongo.name,
                        reservationMongo.phoneNumber,
                        reservationMongo.email,
                        reservationMongo.restaurantId,
                        reservationMongo.reservationDuration,
                        reservationMongo.openTime,
                        reservationMongo.closeTime)).toList();
    }
    public Reservation getReservationById(String id) {
        List<Reservation> test =  reservations.find(ReservationMongo.class)
                .filter(Filters.eq("id", id)).stream().map(
                        reservationMongo -> new Reservation(reservationMongo.id,
                                reservationMongo.date,
                                reservationMongo.startTime,
                                reservationMongo.groupSize,
                                reservationMongo.name,
                                reservationMongo.phoneNumber,
                                reservationMongo.email,
                                reservationMongo.restaurantId,
                                reservationMongo.reservationDuration,
                                reservationMongo.openTime,
                                reservationMongo.closeTime)).toList();
        if(test.isEmpty()){
            throw new NotFoundException(
                    "This Restaurant does not exist: " + id
            );
        }
        return test.getFirst();
    }
    public boolean isReservationExist(String id) {
        List<Reservation> test = reservations.find(ReservationMongo.class)
                .filter(Filters.eq("id", id)).stream().map(
                        reservationMongo -> new Reservation(reservationMongo.id,
                                reservationMongo.date,
                                reservationMongo.startTime,
                                reservationMongo.groupSize,
                                reservationMongo.name,
                                reservationMongo.phoneNumber,
                                reservationMongo.email,
                                reservationMongo.restaurantId,
                                reservationMongo.reservationDuration,
                                reservationMongo.openTime,
                                reservationMongo.closeTime)).toList();
        if(test.isEmpty()){
            return false;
        }
        return true;
    }

    public void save(Reservation reservation) {
        reservations.save(new ReservationMongo(reservation.getId(),
                reservation.getDate(), reservation.getStartTime(),
                reservation.getGroupSize(), reservation.getCustomer().getName(),
                reservation.getCustomer().getPhoneNumber(),
                reservation.getCustomer().getEmail(), reservation.getRestaurantId(),
                reservation.getReservationsDuration(), reservation.getOpen(), reservation.getClose()));
    }
    public void clear() {reservations.find(ReservationMongo.class).delete(new DeleteOptions().multi(true));
    }
    public void delete(String reservationId){
        reservations.find(ReservationMongo.class)
                .filter(Filters.eq("id",reservationId))
                .delete(new DeleteOptions());
    }
}
