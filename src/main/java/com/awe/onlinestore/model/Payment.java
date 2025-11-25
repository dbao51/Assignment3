package com.awe.onlinestore.model;

import java.io.Serializable;

public abstract class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String paymentId;
    private double amount;
    private PaymentStatus status;
    private String orderId;

    public Payment() {
        this.status = PaymentStatus.PENDING;
    }

    public Payment(String paymentId, double amount, String orderId) {
        this();
        this.paymentId = paymentId;
        this.amount = amount;
        this.orderId = orderId;
    }

    // Getters and setters
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * Authorizes the payment.
     *
     * @return true if authorization successful
     */
    public abstract boolean authorize();

    /**
     * Processes the payment.
     *
     * @return true if processing successful
     */
    public abstract boolean processPayment();

    /**
     * Refunds the payment.
     *
     * @return true if refund successful
     */
    public abstract boolean refund();
}