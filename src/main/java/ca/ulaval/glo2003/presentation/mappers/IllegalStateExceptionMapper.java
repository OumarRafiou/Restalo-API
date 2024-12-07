package ca.ulaval.glo2003.presentation.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class IllegalStateExceptionMapper
    implements ExceptionMapper<IllegalStateException> {

    @Override
    public Response toResponse(IllegalStateException exception) {
        return Response
            .status(Response.Status.BAD_REQUEST)
            .entity(
                "{\"error\":\"MISSING_PARAMETER\",\"description\":\"" +
                exception.getMessage() +
                "\"}"
            )
            .build();
    }
}
