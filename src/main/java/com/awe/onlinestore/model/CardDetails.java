package com.awe.onlinestore.model;

import java.io.Serializable;

public class CardDetails extends Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String cardNumber;
    private String expiryDate;
    private String cardHolderName;
    private String cvv;

    public CardDetails() {
        super();
    }

    public CardDetails(String paymentId, double amount, String orderId, 
                      String cardNumber, String expiryDate, String cardHolderName, String cvv) {
        super(paymentId, amount, orderId);
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cardHolderName = cardHolderName;
        this.cvv = cvv;
    }

    // Getters and setters
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public boolean authorize() {
        // Simulate card validation
        if (isValidCard()) {
            setStatus(PaymentStatus.AUTHORIZED);
            return true;
        }
        setStatus(PaymentStatus.FAILED);
        return false;
    }

    @Override
    public boolean processPayment() {
        if (getStatus() == PaymentStatus.AUTHORIZED) {
            // Simulate payment processing
            setStatus(PaymentStatus.PROCESSED);
            return true;
        }
        return false;
    }

    @Override
    public boolean refund() {
        if (getStatus() == PaymentStatus.PROCESSED) {
            setStatus(PaymentStatus.REFUNDED);
            return true;
        }
        return false;
    }

    /**
     * Validates card details.
     *
     * @return true if card details are valid
     */
    private boolean isValidCard() {
        return cardNumber != null && cardNumber.matches("\\d{16}") &&
               expiryDate != null && expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}") &&
               cardHolderName != null && !cardHolderName.trim().isEmpty() &&
               cvv != null && cvv.matches("\\d{3}");
    }

    /**
     * Gets masked card number for security.
     *
     * @return masked card number
     */
    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }

    @Override
    public String toString() {
        return String.format("CardPayment[ID: %s, Amount: $%.2f, Card: %s]", 
                           getPaymentId(), getAmount(), getMaskedCardNumber());
    }
}