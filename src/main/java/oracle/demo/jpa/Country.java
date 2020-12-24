package oracle.demo.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * JPA version of "Country", Entity Bean
 */
@Entity
@Table(name = "COUNTRY") 
public class Country {

    @Column(name = "COUNTRY_ID") @Id 
    private int countryId;

    @Column(name = "COUNTRY_NAME") 
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
