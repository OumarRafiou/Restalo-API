package ca.ulaval.glo2003.shared;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class DatastoreProvider {
    public Datastore provide() {
        var mongoUrl = System.getenv("MONGO_CLUSTER_URL");
        return Morphia.createDatastore(MongoClients.create(mongoUrl), "restalo");
    }
}
