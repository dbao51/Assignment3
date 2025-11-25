package com.awe.onlinestore.model;

import java.io.Serializable;

public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String street;
    private String city;
    private String postcode;
    private String country;

    public Address() {}

    public Address(String street, String city, String postcode, String country) {
        this.street = street;
        this.city = city;
        this.postcode = postcode;
        this.country = country;
    }

    // Getters and setters
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Validates the address format.
     *
     * @return true if all required fields are present
     */
    public boolean isValid() {
        return street != null && !street.trim().isEmpty() &&
               city != null && !city.trim().isEmpty() &&
               postcode != null && !postcode.trim().isEmpty() &&
               country != null && !country.trim().isEmpty();
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s", street, city, postcode, country);
    }
}