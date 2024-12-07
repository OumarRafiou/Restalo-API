package ca.ulaval.glo2003.infrastructure;

import ca.ulaval.glo2003.domain.reservation.ReservationPersistence;

import static org.junit.jupiter.api.Assertions.*;

class ReservationPersistenceMemoryTest extends ReservationPersistenceTest {
    private final ReservationPersistence reservationPersistence = new ReservationPersistenceMemory();

    @Override
    protected ReservationPersistence createPersistence(){
        return  new ReservationPersistenceMemory();
    }

}