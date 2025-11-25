package com.awe.onlinestore.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String customerId;
    private String username;
    private String password; // In real application, this should be hashed
    private String name;
    private String email;
    private List<Address> addresses;

    public Customer() {
        this.addresses = new ArrayList<>();
    }

    public Customer(String customerId, String username, String password, String name, String email) {
        this();
        this.customerId = customerId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    // Getters and setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    /**
     * Adds a new address to the customer.
     *
     * @param address the address to add
     */
    public void addAddress(Address address) {
        if (address != null && address.isValid()) {
            this.addresses.add(address);
        }
    }

    /**
     * Validates customer information.
     *
     * @return true if all required fields are valid
     */
    public boolean isValid() {
        return customerId != null && !customerId.trim().isEmpty() &&
               username != null && !username.trim().isEmpty() &&
               password != null && !password.trim().isEmpty() &&
               email != null && email.contains("@") &&
               !addresses.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("Customer[ID: %s, Name: %s, Email: %s, Addresses: %d]", 
                           customerId, name, email, addresses.size());
    }
}