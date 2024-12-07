package ca.ulaval.glo2003.infrastructure;

import ca.ulaval.glo2003.domain.reservation.ReservationPersistence;
import com.mongodb.client.MongoClients;
import dev.morphia.Morphia;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class ReservationPersistenceMongoTest extends ReservationPersistenceTest {
    @Container
    private final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");
    @Override
    protected ReservationPersistence createPersistence(){
        System.out.println(mongoDBContainer.getConnectionString());
        var mongoUrl = MongoClients.create(mongoDBContainer.getConnectionString());
        var datastore = Morphia.createDatastore(mongoUrl,"tests2");
        return new ReservationPersistenceMongo(datastore);
    }

}