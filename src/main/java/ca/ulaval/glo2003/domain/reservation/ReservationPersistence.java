package ca.ulaval.glo2003.domain.reservation;

import ca.ulaval.glo2003.domain.reservation.Reservation;

import java.util.List;

public interface ReservationPersistence {
    List<Reservation> listAll();
    void save(Reservation reservation);
    Reservation getReservationById(String id);
    boolean isReservationExist(String idReservation);
    void delete(String reservationNumber);
    void clear();
}
