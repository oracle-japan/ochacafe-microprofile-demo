package oracle.demo.graphql;

import org.eclipse.microprofile.graphql.NonNull;

public class Country {

    private int countryId;
    @NonNull
    private String countryName;

    public Country(){}

    public Country(int countryId, String countryName){
        this.countryId = countryId;
        this.countryName = countryName;
    }

    public int getCountryId() {
        return countryId;
    }
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
    public String getCountryName() {
        return countryName;
    }
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

}
