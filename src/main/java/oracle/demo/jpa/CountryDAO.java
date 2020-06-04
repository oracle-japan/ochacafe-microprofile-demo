package oracle.demo.jpa;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import oracle.demo.country.CountryNotFoundException;
import oracle.demo.tracing.interceptor.Trace;
import oracle.demo.tracing.interceptor.TraceTag;


/**
 * JPA with transaction control
 * Support OpenTracing Span with annotaions - @trace @TraceConfig @TraceTag
 */
@Dependent
public class CountryDAO {

    static {
        // in case of Oracle, disable fan to avoid waiting for fan initialization timeout
        System.setProperty("oracle.jdbc.fanEnabled", "false");
    }

    @PersistenceContext(unitName = "jpa_country")
    private EntityManager em;

    @Trace("JPA") @TraceTag(key = "JPQL", value = "select c from Country c")
    public List<Country> getCountries(){
        List<Country> countries = em.createQuery("select c from Country c", Country.class).getResultList();
        return countries;
    }

    // will intentionally cause an error
    @Trace("JPA") 
    @TraceTag(key = "JPQL", value = "select c from Countries c")
    @TraceTag(key = "comment", value = "An error is expected by the wrong jpql statement.")
    public List<Country> getCountriesWithError(){
        List<Country> countries = em.createQuery("select c from Countries c", Country.class).getResultList();
        return countries;
    }

    @Trace(value = "JPA", stackTrace = true)
    public Country getCountry(int countryId) {
        Country country = em.find(Country.class, countryId);
        if(null == country) 
            throw new CountryNotFoundException(String.format("Couldn't find country, id=%d", countryId));
        return country;
    }

    @Trace("JPA")
    @Transactional
    public void insertCountries(Country[] countries) {
        for(int i = 0 ; i < countries.length ; i++){
            em.persist(countries[i]);
        }
    }

    @Trace("JPA")
    @Transactional
    public void insertCountry(int countryId, String countryName) {
        em.persist(new Country(countryId, countryName));
    }

    @Trace("JPA")
    @Transactional
    public void updateCountry(int countryId, String countryName) {
        Country country = em.find(Country.class, countryId);
        if(null == country) 
            throw new CountryNotFoundException(String.format("Couldn't find country, id=%d", countryId));
        country.countryName = countryName;
        em.persist(country);
    }

    @Trace("JPA")
    @Transactional
    public void deleteCountry(int countryId) {
        Country country = em.find(Country.class, countryId);
        if(null == country) 
            throw new CountryNotFoundException(String.format("Couldn't find country, id=%d", countryId));
        em.remove(country);
    }

}