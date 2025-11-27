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

    // Getters and Setters
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPostcode() { return postcode; }
    public void setPostcode(String postcode) { this.postcode = postcode; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    /**
     * Validates the address:
     * - Street: not empty
     * - City: letters and spaces only
     * - Postcode: digits only, 3–10 length
     * - Country: letters and spaces only
     *
     * @return true if all fields valid, false otherwise
     */
    public boolean isValid() {
        return isNotEmpty(street) &&
               isLettersOnly(city) &&
               isValidPostcode(postcode) &&
               isLettersOnly(country);
    }

    // Helper: not null / not empty
    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    // Helper: letters and spaces only
    private boolean isLettersOnly(String value) {
        return isNotEmpty(value) && value.matches("^[a-zA-Z ]+$");
    }

    // Helper: postcode = digits only, length 3–10
    private boolean isValidPostcode(String value) {
        return isNotEmpty(value) && value.matches("^[0-9]{3,10}$");
    }

    @Override
    public String toString() {
        return String.format("%s, %s %s, %s",
                street, city, postcode, country);
    }
}
