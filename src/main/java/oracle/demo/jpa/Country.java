package oracle.demo.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA version of "Country", Entity Bean
 */
@Entity
@Table(name = "COUNTRY") 
public class Country implements Serializable{

	private static final long serialVersionUID = 1L;

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
