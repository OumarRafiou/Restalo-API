package ca.ulaval.glo2003;

import ca.ulaval.glo2003.controllers.AvailabilityController;
import ca.ulaval.glo2003.controllers.ReservationCtrl;
import ca.ulaval.glo2003.controllers.RestaurantCtrl;
import ca.ulaval.glo2003.controllers.ReviewCtrl;
import ca.ulaval.glo2003.controllers.assembler.AvailabilityAssembler;
import ca.ulaval.glo2003.controllers.assembler.ReservationAssembler;
import ca.ulaval.glo2003.controllers.assembler.RestaurantAssembler;
import ca.ulaval.glo2003.controllers.assembler.ReviewAssembler;
import ca.ulaval.glo2003.domain.reservation.ReservationPersistence;
import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;
import ca.ulaval.glo2003.domain.availability.AvailabilityService;
import ca.ulaval.glo2003.domain.review.ReviewPersistence;
import ca.ulaval.glo2003.presentation.*;
import ca.ulaval.glo2003.presentation.mappers.IllegalArgumentExceptionMapper;
import ca.ulaval.glo2003.presentation.mappers.IllegalStateExceptionMapper;
import ca.ulaval.glo2003.presentation.mappers.NotFoundExceptionMapper;
import ca.ulaval.glo2003.shared.DatastoreProvider;
import java.net.URI;
import java.util.Objects;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import ca.ulaval.glo2003.infrastructure.*;

public class Main {
    public static String BASE_URI;

    public static HttpServer startServer() {
        DatastoreProvider datastoreProvider = new DatastoreProvider();
        RestaurantPersistence restaurantPersistence;
        ReservationPersistence reservationPersistence;
        ReviewPersistence reviewPersistence;
        String port = System.getenv("PORT");
        if(port == null)
        {
            port = "8080";
        }
        if(Objects.equals(System.getProperty("persistence"), "mongo")){
            restaurantPersistence = new RestaurantPersistenceMongo(datastoreProvider.provide());
            reservationPersistence = new ReservationPersistenceMongo(datastoreProvider.provide());
            reviewPersistence = new ReviewPersistenceMongo(datastoreProvider.provide());
        }
        else{
            restaurantPersistence = new RestaurantPersistenceMemory();
            reservationPersistence = new ReservationPersistenceMemory();
            reviewPersistence = new ReviewPersistenceMemory();
            port = "8080";
        }
        BASE_URI = "https://0.0.0.0:" + port + "/";
        RestaurantAssembler restaurantAssembler = new RestaurantAssembler();
        ReservationAssembler reservationAssembler = new ReservationAssembler();
        AvailabilityAssembler availabilityAssembler = new AvailabilityAssembler();
        ReviewAssembler reviewAssembler = new ReviewAssembler();

        ReservationCtrl reservationCtrl = new ReservationCtrl(
                reservationAssembler,
                restaurantAssembler,
                reservationPersistence,
                restaurantPersistence
        );
        ReviewCtrl reviewCtrl = new ReviewCtrl(
                reviewAssembler,
                reviewPersistence
        );
        AvailabilityService availabilityService = new AvailabilityService(restaurantPersistence,reservationCtrl);
        AvailabilityController availabilityController = new AvailabilityController(
                availabilityAssembler,restaurantPersistence,availabilityService);;
        RestaurantCtrl restaurantCtrl = new RestaurantCtrl(
            restaurantAssembler,
                restaurantPersistence

        );


        ReservationResource reservationResource = new ReservationResource(
                reservationPersistence,
                restaurantPersistence,
            reservationCtrl,
            restaurantCtrl
        );
        RestaurantSearch restaurantSearch = new RestaurantSearch(
            restaurantCtrl
        );
        SearchReservation searchReservation = new SearchReservation(
            reservationCtrl
        );
        AvailabilityRessource availabilityRessource = new AvailabilityRessource(restaurantCtrl,availabilityController);
        ReviewRessource reviewRessource = new ReviewRessource(reservationPersistence, restaurantPersistence, reviewCtrl, restaurantCtrl);

        final ResourceConfig rc = new ResourceConfig()
            .register((new NotFoundExceptionMapper()))
            .register(new IllegalArgumentExceptionMapper())
            .register(new IllegalStateExceptionMapper())
            .register(new HealthResource())
            .register(new RestaurantResource(restaurantCtrl))
            .register(new SearchReservation(reservationCtrl), 0)
            .register(reservationResource)
                .register(reviewRessource)
            .register(restaurantSearch)
                .register(availabilityRessource);

        return GrizzlyHttpServerFactory.createHttpServer(
            URI.create(BASE_URI),
            rc
        );
    }

    public static void main(String[] args) {
        startServer();
        System.out.printf(
            "Jersey app started with endpoints available at %s%n",
            BASE_URI
        );
    }
}
