package oracle.demo.country;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CountryNotFoundExceptionMapper implements ExceptionMapper<CountryNotFoundException> {

    @Override
    public Response toResponse(CountryNotFoundException e) {
        return Response.status(Status.NOT_FOUND).build();
    }
    
}
