package ca.ulaval.glo2003.infrastructure;

import ca.ulaval.glo2003.domain.review.ReviewPersistence;
import com.mongodb.client.MongoClients;
import dev.morphia.Morphia;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
@Testcontainers

class ReviewPersistenceMongoTest extends ReviewPersistenceTest {
    @Container
    private final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Override
    protected ReviewPersistence createPersistence() {
        System.out.println(mongoDBContainer.getConnectionString());
        var mongoUrl = MongoClients.create(mongoDBContainer.getConnectionString());
        var datastore = Morphia.createDatastore(mongoUrl, "testsReview");
        return new ReviewPersistenceMongo(datastore);
    }

}