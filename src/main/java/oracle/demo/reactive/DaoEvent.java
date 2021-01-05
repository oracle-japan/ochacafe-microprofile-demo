package oracle.demo.reactive;

import java.io.Serializable;
import java.util.UUID;
import oracle.demo.jpa.Country;

public class DaoEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Operation {INSERT, UPDATE, DELETE}

    private String id;
    private Operation operation;
    private Country[] countries;

    public DaoEvent(){}

    public DaoEvent(Operation op, Country[] countries){
        this.operation = op;
        this.countries = countries;
        this.id = UUID.randomUUID().toString();
    }

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
    public Operation getOperation() {
        return operation;
    }
    public void setOperation(Operation operation) {
        this.operation = operation;
    }
    public Country[] getCountries() {
        return countries;
    }
    public void setCountries(Country[] countries) {
        this.countries = countries;
    }
}
