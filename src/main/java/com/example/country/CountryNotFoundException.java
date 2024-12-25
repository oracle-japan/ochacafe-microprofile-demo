
package com.example.country;

@SuppressWarnings("serial")
public class CountryNotFoundException extends RuntimeException {
    public CountryNotFoundException() {
        super();
    }

    public CountryNotFoundException(String message) {
        super(message);
    }
}