package ca.ulaval.glo2003.presentation;

import ca.ulaval.glo2003.controllers.ReservationCtrl;
import ca.ulaval.glo2003.controllers.dto.ReservationDto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/reservations")
public class SearchReservation {
    private final ReservationCtrl reservationCtrl;

    public SearchReservation(ReservationCtrl reservationCtrl) {
        this.reservationCtrl = reservationCtrl;
    }


    @GET
    @Path("/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservation(@PathParam("number") String number) {
        try {
            ReservationDto reservationDto = reservationCtrl.getReservationFormatByNumber(
                number
            );
            return Response.ok(reservationDto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


}
