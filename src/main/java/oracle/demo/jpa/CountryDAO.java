package oracle.demo.jpa;

import java.util.Arrays;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


import org.eclipse.microprofile.opentracing.Traced;
import io.opentracing.Span;
import io.opentracing.Tracer;
import oracle.demo.country.CountryNotFoundException;

@ApplicationScoped
public class CountryDAO {

    @Inject
    private Tracer tracer;

    @PersistenceContext(unitName = "Demo")
    private EntityManager em;

    // This is a standard MicroProfile annotation
    @Traced
    public List<Country> getCountries(){
        final List<Country> countries = em.createQuery("select c from Country c", Country.class).getResultList();
        return countries;
    }

    @Traced
    public Country getCountry(int countryId) {
        final Country country = em.find(Country.class, countryId);
        if(null == country) 
            throw new CountryNotFoundException(String.format("Couldn't find country, id=%d", countryId));
        return country;
    }

    @Traced
    @Transactional
    public void insertCountries(Country[] countries) {
        Arrays.stream(countries).forEach(em::persist);
    }

    @Traced
    @Transactional
    public void insertCountries(List<Country> countries) {
        countries.stream().forEach(em::persist);
    }

    @Traced
    @Transactional
    public void insertCountry(int countryId, String countryName) {
        em.persist(new Country(countryId, countryName));
    }

    @Traced
    @Transactional
    public void updateCountry(int countryId, String countryName) {
        final Span span = tracer.buildSpan("updateCountry").asChildOf(tracer.activeSpan()).start();
        span.setTag("countryId", countryId);
        span.setTag("countryName", countryName);

        try{
            final Country country = em.find(Country.class, countryId);
            if(null == country) 
                throw new CountryNotFoundException(String.format("Couldn't find country, id=%d", countryId));
            country.setCountryName(countryName);
            em.persist(country);
            span.setTag("error", false);
        }catch(Exception e){
            span.log(e.getMessage());
            span.setTag("error", true); 
            if(e instanceof CountryNotFoundException){
                throw e;
            }else{
                throw new RuntimeException("Failed to update - " + e.getMessage(), e);
            }
        }finally{
            span.finish();
        }
    }

    @Traced
    @Transactional
    public void deleteCountry(int countryId) {
        final Country country = em.find(Country.class, countryId);
        if(null == country) 
            throw new CountryNotFoundException(String.format("Couldn't find country, id=%d", countryId));
        em.remove(country);
    }

}