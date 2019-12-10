package oracle.demo.country;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import oracle.demo.country.CountryResource.CountryNotFoundException;

@Provider
public class CountryNotFoundExceptionMapper implements ExceptionMapper<CountryNotFoundException> {

    @Override
    public Response toResponse(CountryNotFoundException e) {
        return Response.status(Status.NOT_FOUND).build();
    }
    
}
