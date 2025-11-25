package com.awe.onlinestore.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String orderId;
    private LocalDateTime orderDate;
    private Customer customer;
    private List<OrderItem> items;
    private double totalAmount;
    private OrderStatus status;
    private Address shippingAddress;

    public Order() {
        this.items = new ArrayList<>();
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }

    public Order(String orderId, Customer customer, Address shippingAddress) {
        this();
        this.orderId = orderId;
        this.customer = customer;
        this.shippingAddress = shippingAddress;
    }

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
        calculateTotal();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    /**
     * Adds an item to the order.
     *
     * @param item the order item to add
     */
    public void addItem(OrderItem item) {
        if (item != null) {
            items.add(item);
            calculateTotal();
        }
    }

    /**
     * Calculates the total amount including tax.
     */
    public void calculateTotal() {
        double subtotal = items.stream()
            .mapToDouble(OrderItem::getSubtotal)
            .sum();
        this.totalAmount = subtotal * 1.10; // 10% GST
    }

    /**
     * Checks if order can be cancelled.
     *
     * @return true if order can be cancelled
     */
    public boolean canBeCancelled() {
        return status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED;
    }

    /**
     * Cancels the order.
     *
     * @return true if successful
     */
    public boolean cancelOrder() {
        if (canBeCancelled()) {
            this.status = OrderStatus.CANCELLED;
            return true;
        }
        return false;
    }

    /**
     * Confirms the order.
     */
    public void confirmOrder() {
        this.status = OrderStatus.CONFIRMED;
    }

    /**
     * Marks order as paid.
     */
    public void markAsPaid() {
        this.status = OrderStatus.PAID;
    }

    /**
     * Gets formatted order date for display.
     *
     * @return formatted date string
     */
    public String getFormattedOrderDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return orderDate.format(formatter);
    }

    @Override
    public String toString() {
        return String.format("Order[ID: %s, Date: %s, Customer: %s, Total: $%.2f, Status: %s]", 
                           orderId, getFormattedOrderDate(), customer.getName(), totalAmount, status);
    }
}
