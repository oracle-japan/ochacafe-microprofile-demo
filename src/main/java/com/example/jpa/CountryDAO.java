package com.example.jpa;

import java.util.Arrays;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.opentelemetry.context.Context;

import com.example.country.CountryNotFoundException;

@ApplicationScoped
public class CountryDAO {

    @Inject
    private Tracer tracer;

    @PersistenceContext(unitName = "Demo")
    private EntityManager em;

    // This is a standard MicroProfile annotation
    @WithSpan
    public List<Country> getCountries(){
        final List<Country> countries = em.createQuery("select c from Country c", Country.class).getResultList();
        return countries;
    }

    @WithSpan
    public Country getCountry(int countryId) {
        final Country country = em.find(Country.class, countryId);
        if(null == country) 
            throw new CountryNotFoundException(String.format("Couldn't find country, id=%d", countryId));
        return country;
    }

    @WithSpan
    @Transactional
    public void insertCountries(Country[] countries) {
        Arrays.stream(countries).forEach(em::persist);
    }

    @WithSpan
    @Transactional
    public void insertCountries(List<Country> countries) {
        countries.stream().forEach(em::persist);
    }

    @WithSpan
    @Transactional
    public void insertCountry(int countryId, String countryName) {
        em.persist(new Country(countryId, countryName));
    }

    @WithSpan
    @Transactional
    public void updateCountry(int countryId, String countryName) {

        final Span span = tracer
                            .spanBuilder("updateCountry")
                            .setParent(Context.current())
                            .setAttribute("countryId", countryId)
                            .setAttribute("countryName", countryName)
                            .startSpan();

        try{
            final Country country = em.find(Country.class, countryId);
            if(null == country) 
                throw new CountryNotFoundException(String.format("Couldn't find country, id=%d", countryId));
            country.setCountryName(countryName);
            em.persist(country);
            span.setAttribute("error", false);
        }catch(Exception e){
            span.addEvent(e.getMessage());
            span.setAttribute("error", true);
            if(e instanceof CountryNotFoundException){
                throw e;
            }else{
                throw new RuntimeException("Failed to update - " + e.getMessage(), e);
            }
        }finally{
            span.end();
        }
    }

    @WithSpan
    @Transactional
    public void deleteCountry(int countryId) {
        final Country country = em.find(Country.class, countryId);
        if(null == country) 
            throw new CountryNotFoundException(String.format("Couldn't find country, id=%d", countryId));
        em.remove(country);
    }

}