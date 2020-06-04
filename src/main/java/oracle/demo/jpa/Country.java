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

    @Id 
    @Column(name = "COUNTRY_ID")
    public int countryId;

    @Column(name = "COUNTRY_NAME") 
    public String countryName;

    public Country(){}

    public Country(int countryId, String countryName){
        this.countryId = countryId;
        this.countryName = countryName;
    }

}
