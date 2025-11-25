package com.awe.onlinestore.model;

import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String productId;
    private String name;
    private String description;
    private String category;
    private double price;
    private int stockQuantity;

    // Default constructor
    public Product() {
    }

    /**
     * Constructs a new Product with specified details.
     *
     * @param productId the unique identifier for the product
     * @param name the name of the product
     * @param description the description of the product
     * @param category the category of the product
     * @param price the price of the product
     * @param stockQuantity the available stock quantity
     */
    public Product(String productId, String name, String description, 
                   String category, double price, int stockQuantity) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // Getters and setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    /**
     * Checks if the product is in stock.
     *
     * @return true if stock quantity is greater than 0, false otherwise
     */
    public boolean isInStock() {
        return stockQuantity > 0;
    }

    /**
     * Decreases the stock quantity by specified amount.
     *
     * @param quantity the quantity to decrease
     * @return true if successful, false if insufficient stock
     */
    public boolean decreaseStock(int quantity) {
        if (quantity > 0 && quantity <= stockQuantity) {
            stockQuantity -= quantity;
            return true;
        }
        return false;
    }

    /**
     * Increases the stock quantity by specified amount.
     *
     * @param quantity the quantity to increase
     */
    public void increaseStock(int quantity) {
        if (quantity > 0) {
            stockQuantity += quantity;
        }
    }

    @Override
    public String toString() {
        return String.format("Product[ID: %s, Name: %s, Category: %s, Price: $%.2f, Stock: %d]", 
                           productId, name, category, price, stockQuantity);
    }

    /**
     * Returns detailed product information.
     *
     * @return formatted string with product details
     */
    public String getDetails() {
        return String.format(
            "=== Product Details ===\n" +
            "ID: %s\n" +
            "Name: %s\n" +
            "Description: %s\n" +
            "Category: %s\n" +
            "Price: $%.2f\n" +
            "Stock Available: %d\n" +
            "In Stock: %s",
            productId, name, description, category, price, stockQuantity, 
            isInStock() ? "Yes" : "No"
        );
    }
}
