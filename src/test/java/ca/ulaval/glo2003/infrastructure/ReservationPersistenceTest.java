package ca.ulaval.glo2003.infrastructure;

import ca.ulaval.glo2003.domain.reservation.Reservation;
import ca.ulaval.glo2003.domain.reservation.ReservationPersistence;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.HashMap;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public abstract class ReservationPersistenceTest {
    protected abstract ReservationPersistence createPersistence();
    private ReservationPersistence reservationPersistence;
    @BeforeEach
    public void setUp(){reservationPersistence = createPersistence();
    }

    @Test
    public void givenSavedReservation_whenFindingAll_shouldReturnSavedReservation(){
        Reservation reservation = new Reservation("456", "2022-12-02","10:00:00",12,"Test","418-222","email@yahho.com","123",50,"8:00:00","23:00:00");
        reservationPersistence.save(reservation);

        var foundReservation = reservationPersistence.listAll();

        assertEquals(foundReservation.get(0).getId(),"456");
    }

    @Test
    public void givenManySavedReservation_whenFindingAll_shouldReturnAllSaveReservation() {
        Reservation firstReservation = new Reservation("456", "2022-12-02", "10:00:00", 12, "Test", "418-222", "email@yahho.com", "123", 50, "8:00:00", "23:00:00");
        Reservation secondReservation = new Reservation("444", "2022-12-02", "10:00:00", 3, "Test", "418-222", "email@yahho.com", "123", 50, "8:00:00", "23:00:00");
        reservationPersistence.save(firstReservation);
        reservationPersistence.save(secondReservation);

        var foundAllReservation = reservationPersistence.listAll();
        int sizeList = foundAllReservation.size();

        assertEquals(sizeList, 2);
    }
    @Test
    public void givenNoReservation_whenFindingAll_shouldReturnAnEmptyList(){
        var foundAllReservation = reservationPersistence.listAll();

        assertTrue(foundAllReservation.isEmpty());
    }

    @Test
    public void givenReservationIdNotValid_shouldThrowExcption(){
        Reservation firstReservation = new Reservation("456", "2022-12-02","10:00:00",12,"Test","418-222","email@yahho.com","123",50,"8:00:00","23:00:00");
        Reservation secondReservation = new Reservation("444", "2022-12-02","10:00:00",3,"Test","418-222","email@yahho.com","123",50,"8:00:00","23:00:00");
        reservationPersistence.save(firstReservation);
        reservationPersistence.save(secondReservation);


        assertThrows(NotFoundException.class,()->{
            reservationPersistence.getReservationById("Invalid");
        });
    }

    @Test
    public void givenReservationValidId_shouldReturnReservation(){
        Reservation firstReservation = new Reservation("456", "2022-12-02","10:00:00",12,"Test","418-222","email@yahho.com","123",50,"8:00:00","23:00:00");
        reservationPersistence.save(firstReservation);

        var foundReservationById = reservationPersistence.getReservationById("456");

        assertThat(foundReservationById.equals(firstReservation));
    }

    @Test
    public void givenRestaurantExist_shouldReturnTrue(){
        Reservation firstReservation = new Reservation("456", "2022-12-02","10:00:00",12,"Test","418-222","email@yahho.com","123",50,"8:00:00","23:00:00");
        reservationPersistence.save(firstReservation);

        var foundReservationExist = reservationPersistence.isReservationExist("456");
        assertTrue(foundReservationExist);
    }

    @Test
    public void givenRestaurantExist_shouldReturnFalse(){

        var foundReservationExist = reservationPersistence.isReservationExist("456");

        assertFalse(foundReservationExist);
    }


    @Test
    public void givenReservationList_ShouldBeCleared(){
        Reservation firstReservation = new Reservation("456", "2022-12-02","10:00:00",12,"Test","418-222","email@yahho.com","123",50,"8:00:00","23:00:00");
        Reservation secondReservation = new Reservation("444", "2022-12-02","10:00:00",3,"Test","418-222","email@yahho.com","123",50,"8:00:00","23:00:00");
        reservationPersistence.save(firstReservation);
        reservationPersistence.save(secondReservation);
        reservationPersistence.clear();

        assertThat(reservationPersistence.listAll().isEmpty());

    }
    @Test
    public void givenRestaurantWithId_ToBeDelete_ShouldBeDeleted(){
        Reservation firstReservation = new Reservation("456", "2022-12-02","10:00:00",12,"Test","418-222","email@yahho.com","123",50,"8:00:00","23:00:00");
        Reservation secondReservation = new Reservation("444", "2022-12-02","10:00:00",3,"Test","418-222","email@yahho.com","123",50,"8:00:00","23:00:00");
        reservationPersistence.save(firstReservation);
        reservationPersistence.save(secondReservation);
        reservationPersistence.delete("456");

        assertEquals(reservationPersistence.listAll().get(0).getId(),"444");
        assertEquals(reservationPersistence.listAll().size(),1);
    }

}