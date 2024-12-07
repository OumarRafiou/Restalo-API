package ca.ulaval.glo2003.presentation.mappers;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class NotFoundExceptionMapper
    implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        return Response.status(404).build();
    }
}
