package oracle.demo.jpa;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.xml.namespace.QName;

import org.eclipse.microprofile.opentracing.Traced;

import oracle.demo.country.CountryNotFoundException;
import oracle.demo.tracing.interceptor.Trace;
import oracle.demo.tracing.interceptor.TraceTag;

@Dependent
public class CountryDAO {

    static {
        // in case of Oracle, disable fan to avoid waiting for fan initialization timeout
        System.setProperty("oracle.jdbc.fanEnabled", "false");
    }

    @PersistenceContext(unitName = "CountryDS")
    private EntityManager em;

    // This is a standard MicroProfile annotation
    @Traced
    public List<Country> getCountries(){
        List<Country> countries = em.createQuery("select c from Country c", Country.class).getResultList();
        return countries;
    }

    // This is my original annotations for tracing
    @Trace("JPA") 
    @TraceTag(key = "JPQL", value = "select c from Countries c")
    @TraceTag(key = "comment", value = "An error is expected by the wrong jpql statement.")
    public List<Country> getCountriesWithError(){ // will intentionally cause an error
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
        Arrays.stream(countries).forEach(em::persist);
    }

    @Trace("JPA")
    @Transactional
    public void insertCountries(List<Country> countries) {
        countries.stream().forEach(em::persist);
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
        country.setCountryName(countryName);
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