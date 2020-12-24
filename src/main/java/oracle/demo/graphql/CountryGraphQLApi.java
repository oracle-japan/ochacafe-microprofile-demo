package oracle.demo.graphql;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import oracle.demo.jpa.Country;
import oracle.demo.jpa.CountryDAO;

/**
 * GraphQL version of "Country"
 */
@GraphQLApi
@ApplicationScoped
public class CountryGraphQLApi {

    @Inject
    CountryDAO dao;

    @Query
    public List<Country> getCountries() {
        return dao.getCountries();
    }

    @Query
    public Country getCountry(@Name("countryId") int countryId) {
        oracle.demo.jpa.Country c = dao.getCountry(countryId);
        return new Country(c.getCountryId(), c.getCountryName());
    }

    @Mutation
    public Country insertCountry(@Name("country") Country country) {
        dao.insertCountry(country.getCountryId(), country.getCountryName());
        return country;
    }

    @Mutation
    public List<Country> insertCountries(@Name("countries") List<Country> countries) {
        dao.insertCountries(countries);
        return countries;
    }

    @Mutation
    public Country updateCountry(@Name("countryId") int countryId, @Name("countryName") String countryName) {
        dao.updateCountry(countryId, countryName);
        return new Country(countryId, countryName);
    }

    @Mutation
    public int deleteCountry(@Name("countryId") int countryId) {
        dao.deleteCountry(countryId);
        return countryId;
    }

}
