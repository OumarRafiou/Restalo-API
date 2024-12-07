package ca.ulaval.glo2003.presentation.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class IllegalArgumentExceptionMapper
    implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        return Response
            .status(Response.Status.BAD_REQUEST)
            .entity(
                "{\"error\":\"INVALID_PARAMETER\",\"description\":\"" +
                exception.getMessage() +
                "\"}"
            )
            .build();
    }
}
