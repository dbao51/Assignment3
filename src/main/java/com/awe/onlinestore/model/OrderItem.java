package com.awe.onlinestore.model;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Product product;
    private int quantity;
    private double unitPrice;

    public OrderItem() {
    }

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
    }

    // Getters and setters
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.unitPrice = product.getPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * Calculates the subtotal for this order item.
     *
     * @return subtotal amount
     */
    public double getSubtotal() {
        return unitPrice * quantity;
    }

    @Override
    public String toString() {
        return String.format("OrderItem[Product: %s, Qty: %d, Unit Price: $%.2f, Subtotal: $%.2f]", 
                           product.getName(), quantity, unitPrice, getSubtotal());
    }
}