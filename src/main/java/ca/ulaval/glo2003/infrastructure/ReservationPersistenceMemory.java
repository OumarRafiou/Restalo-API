package ca.ulaval.glo2003.infrastructure;

import ca.ulaval.glo2003.domain.reservation.Reservation;
import ca.ulaval.glo2003.domain.reservation.ReservationPersistence;
import jakarta.ws.rs.NotFoundException;

import java.util.*;

public class ReservationPersistenceMemory implements ReservationPersistence {
    private final Map<String, Reservation> reservations = new HashMap<>();

    public List<Reservation> listAll() {
        return reservations.values().stream().toList();
    }

    public void save(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
    }
    public Reservation getReservationById(String id) {
        for(String item : reservations.keySet()){
            if(Objects.equals(item, id)){
                return reservations.get(item);
            }
        }
        throw new NotFoundException("This reservation does not exist");
    }
    public boolean isReservationExist(String idReservation){
        return reservations.containsKey(idReservation);
    }
    public void clear() { reservations.clear();}
    public void delete(String reservationsId){
        reservations.remove(reservationsId);
    }
}
