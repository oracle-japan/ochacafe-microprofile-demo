package oracle.demo.jpa;

import java.util.List;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * JPA version of "Country", supports CRUD
 * insert Opentracing span
 */
@ApplicationScoped
@Path("/jpa/country")
@Produces(MediaType.APPLICATION_JSON)
public class CountryResource {

    private final Logger logger = Logger.getLogger(CountryResource.class.getName());

    private final CountryDAO dao;

    @Inject
    public CountryResource(CountryDAO dao){
        this.dao = dao;
    }

    @GET
    @Path("/")
    public Country[] getCountries() throws Exception {
        List<Country> countries = dao.getCountries();
        return countries.toArray(new Country[countries.size()]);
    }

    @GET
    @Path("/{countryId}")
    public Country getCountry(@PathParam("countryId") int countryId) {
        return dao.getCountry(countryId);
    }

    @POST
    @Path("/")
    public Response insertCountries(Country[] countries) {
        dao.insertCountries(countries);
        return Response.ok().build();
    }

    @POST
    @Path("/{countryId}")
    public void insertCountry(@PathParam("countryId") int countryId, @FormParam("name")String countryName) {
        logger.info(String.format("insertCountry - id=%d, name=%s", countryId, countryName));
        dao.insertCountry(countryId, countryName);
    }
    // curl -v -X POST localhost:8080/jpa/country/9 -H "Content-Type: application/x-www-form-urlencoded" -d "name=ABC" 

    @PUT
    @Path("/{countryId}")
    public void updateCountry(@PathParam("countryId") int countryId, @FormParam("name") String countryName) {
        logger.info(String.format("updateCountry - id=%d, name=%s", countryId, countryName));
        dao.updateCountry(countryId, countryName);
    }
    // curl -v -X PUT localhost:8080/jpa/country/1 -H "Content-Type: application/x-www-form-urlencoded" -d "name=XXX" 

    @DELETE
    @Path("/{countryId}")
    public void deleteCountry(@PathParam("countryId") int countryId) {
        dao.deleteCountry(countryId);
    }

}
