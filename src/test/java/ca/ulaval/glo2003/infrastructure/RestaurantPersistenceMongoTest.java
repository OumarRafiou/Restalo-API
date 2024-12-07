package ca.ulaval.glo2003.infrastructure;

import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;
import com.mongodb.client.MongoClients;
import dev.morphia.Morphia;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class RestaurantPersistenceMongoTest extends RestaurantPersistenceTest{
    @Container
    private final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Override
    protected RestaurantPersistence createPersistence(){
        System.out.println(mongoDBContainer.getConnectionString());
        var mongoUrl = MongoClients.create(mongoDBContainer.getConnectionString());
        var datastore = Morphia.createDatastore(mongoUrl,"tests");
        return new RestaurantPersistenceMongo(datastore);
    }

}