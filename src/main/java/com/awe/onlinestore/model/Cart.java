package com.awe.onlinestore.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<OrderItem> items;
    private String customerId;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public Cart(String customerId) {
        this();
        this.customerId = customerId;
    }

    // Getters and setters
    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Adds a product to the cart with specified quantity.
     *
     * @param product the product to add
     * @param quantity the quantity to add
     * @return true if successful, false otherwise
     */
    public boolean addItem(Product product, int quantity) {
        if (product == null || quantity <= 0 || !product.isInStock() || quantity > product.getStockQuantity()) {
            return false;
        }

        // Check if product already exists in cart
        Optional<OrderItem> existingItem = items.stream()
            .filter(item -> item.getProduct().getProductId().equals(product.getProductId()))
            .findFirst();

        if (existingItem.isPresent()) {
            // Update quantity of existing item
            OrderItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            // Add new item
            items.add(new OrderItem(product, quantity));
        }
        return true;
    }

    /**
     * Removes an item from the cart.
     *
     * @param productId the product ID to remove
     * @return true if successful, false otherwise
     */
    public boolean removeItem(String productId) {
        return items.removeIf(item -> item.getProduct().getProductId().equals(productId));
    }

    /**
     * Updates the quantity of an item in the cart.
     *
     * @param productId the product ID to update
     * @param newQuantity the new quantity
     * @return true if successful, false otherwise
     */
    public boolean updateQuantity(String productId, int newQuantity) {
        if (newQuantity <= 0) {
            return removeItem(productId);
        }

        for (OrderItem item : items) {
            if (item.getProduct().getProductId().equals(productId)) {
                if (newQuantity <= item.getProduct().getStockQuantity()) {
                    item.setQuantity(newQuantity);
                    return true;
                }
                break;
            }
        }
        return false;
    }

    /**
     * Calculates the total price of all items in the cart.
     *
     * @return total amount
     */
    public double calculateTotal() {
        return items.stream()
            .mapToDouble(OrderItem::getSubtotal)
            .sum();
    }

    /**
     * Gets the quantity of a specific product in the cart.
     *
     * @param productId the product ID
     * @return quantity, or 0 if not found
     */
    public int getItemQuantity(String productId) {
        return items.stream()
            .filter(item -> item.getProduct().getProductId().equals(productId))
            .findFirst()
            .map(OrderItem::getQuantity)
            .orElse(0);
    }

    /**
     * Checks if the cart is empty.
     *
     * @return true if cart is empty
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Clears all items from the cart.
     */
    public void clear() {
        items.clear();
    }

    /**
     * Gets the number of distinct items in the cart.
     *
     * @return item count
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * Gets the total quantity of all items in the cart.
     *
     * @return total quantity
     */
    public int getTotalQuantity() {
        return items.stream()
            .mapToInt(OrderItem::getQuantity)
            .sum();
    }

    @Override
    public String toString() {
        return String.format("Cart[Customer: %s, Items: %d, Total: $%.2f]", 
                           customerId, getItemCount(), calculateTotal());
    }
}