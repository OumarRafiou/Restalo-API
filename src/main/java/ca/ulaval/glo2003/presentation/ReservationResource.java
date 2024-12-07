package ca.ulaval.glo2003.presentation;

import ca.ulaval.glo2003.controllers.ReservationCtrl;
import ca.ulaval.glo2003.controllers.RestaurantCtrl;
import ca.ulaval.glo2003.controllers.dto.ReservationDto;
import ca.ulaval.glo2003.domain.reservation.Reservation;
import ca.ulaval.glo2003.domain.reservation.ReservationPersistence;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.domain.restaurant.RestaurantPersistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("restaurants/{restaurantId}/reservations")
public class ReservationResource {
    private final ReservationCtrl reservationCtrl;
    private final RestaurantCtrl restaurantCtrl;
    private RestaurantPersistence restaurantPersistence;
    private ReservationPersistence reservationPersistence;

    public ReservationResource(
        ReservationPersistence reservationPersistence,
        RestaurantPersistence restaurantPersistence,
        ReservationCtrl reservationCtrl,
        RestaurantCtrl restaurantCtrl
    ) {
        this.reservationPersistence = reservationPersistence;
        this.restaurantPersistence = restaurantPersistence;
        this.reservationCtrl = reservationCtrl;
        this.restaurantCtrl = restaurantCtrl;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReservation(
        @PathParam("restaurantId") String restaurantId,
        Reservation newReservation
    ) {
        Restaurant restaurantSelected = restaurantCtrl.getRestaurantById(
            restaurantId
        );
        String URIReservation = reservationCtrl.createReservation(
            restaurantSelected,
            newReservation
        );

        return Response
            .status(201)
            .header(HttpHeaders.LOCATION, URIReservation)
            .build();
    }

    @DELETE
    @Path("{reservationNumber}")
    public Response deleteReservation(
        @PathParam("restaurantId") String restaurantId,
        @PathParam("reservationNumber") String reservationNumber
    ) {
        Restaurant restaurantSelected = restaurantCtrl.getRestaurantById(
            restaurantId
        );

        reservationCtrl.deleteReservation(
            restaurantSelected,
            reservationNumber
        );

        return Response.status(204).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchReservations(
            @HeaderParam("Owner") String ownerId,
            @PathParam("restaurantId") String restaurantId,
            @QueryParam("date") String date,
            @QueryParam("customerName") String customerName
    ) {
        try {
            List<ReservationDto> reservations = reservationCtrl.searchReservations(
                    ownerId,
                    restaurantId,
                    date,
                    customerName
            );
            return Response.ok(reservations).build();
        } catch (NotFoundException e) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (BadRequestException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
