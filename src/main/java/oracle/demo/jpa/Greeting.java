package oracle.demo.jpa;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * JPA demo which Helidon provides
 */
@Access(value = AccessType.FIELD) 
@Entity(name = "Greeting") 
@Table(name = "GREETING") 
public class Greeting implements Serializable { 

    private static final long serialVersionUID = 1L;

    @Column(
        insertable = true,
        name = "SALUTATION", 
        nullable = false,
        updatable = false
    )
    @Id 
    private String salutation;

    @Basic(optional = false) 
    @Column(
        insertable = true,
        name = "RESPONSE",
        nullable = false,
        updatable = true
    )
    private String response;

    @Deprecated
    protected Greeting() { 
        super();
    }

    public Greeting(String salutation, String response) { 
        super();
        this.salutation = Objects.requireNonNull(salutation);
        this.setResponse(response);
    }

    public String getSalutation() {
        return this.salutation;
    }

    public String getResponse() {
        return this.response;
    }

    public void setResponse(String response) {
        this.response = Objects.requireNonNull(response);
    }

    @Override
    public String toString() {
        return this.getSalutation() + " " + this.getResponse();
    }

}
