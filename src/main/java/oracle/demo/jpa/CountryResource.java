package oracle.demo.jpa;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * JPA version of "Country", supports CRUD
 */
@Dependent
@Path("/jpa/country")
@Produces(MediaType.APPLICATION_JSON)
public class CountryResource {

    @PersistenceContext
    private EntityManager em;

    @GET
    @Path("/")
    @Transactional
    public Country[] getCountries() throws Exception {
        List<Country> countries = em.createQuery("select c from Country c", Country.class).getResultList();
        return countries.toArray(new Country[countries.size()]);
    }

    @POST
    @Path("/")
    @Transactional
    public Response insertCountries(Country[] countries) {
        for(int i = 0 ; i < countries.length ; i++){
            em.persist(countries[i]);
        }
        return Response.ok().build();
    }


    @GET
    @Path("/{countryId}")
    @Transactional
    public Country getCountry(@PathParam("countryId") int countryId) {
        Country country = em.find(Country.class, countryId);
        if(null == country) throw new oracle.demo.country.CountryResource.CountryNotFoundException();
        return country;
    }

    @POST
    @Path("/{countryId}")
    @Transactional
    public void insertCountry(@PathParam("countryId") int countryId, @FormParam("name")String countryName) {
        em.persist(new Country(countryId, countryName));
    }

    @PUT
    @Path("/{countryId}")
    @Transactional
    public void updateCountry(@PathParam("countryId") int countryId, @FormParam("name") String countryName) {
        Country country = em.find(Country.class, countryId);
        country.countryName = countryName;
        em.persist(country);
    }

    @DELETE
    @Path("/{countryId}")
    @Transactional
    public void deleteCountry(@PathParam("countryId") int countryId) {
        Country country = em.find(Country.class, countryId);
        em.remove(country);
    }

}
