package oracle.demo.jpa;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * JPA version of "Country", supports CRUD
 * insert Opentracing span
 */
@ApplicationScoped
@Path("/jpa/country")
@Produces(MediaType.APPLICATION_JSON)
public class CountryResource {

    @Inject
    CountryDAO dao;

    @GET
    @Path("/")
    public Country[] getCountries(@QueryParam("error") boolean error) throws Exception {
        List<Country> countries = error ? dao.getCountriesWithError() : dao.getCountries();
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
        dao.insertCountry(countryId, countryName);
    }

    @PUT
    @Path("/{countryId}")
    public void updateCountry(@PathParam("countryId") int countryId, @FormParam("name") String countryName) {
        dao.updateCountry(countryId, countryName);
    }

    @DELETE
    @Path("/{countryId}")
    public void deleteCountry(@PathParam("countryId") int countryId) {
        dao.deleteCountry(countryId);
    }

}
