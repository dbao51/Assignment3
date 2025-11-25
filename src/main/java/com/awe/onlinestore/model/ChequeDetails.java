package com.awe.onlinestore.model;

import java.io.Serializable;

public class ChequeDetails extends Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String chequeNumber;
    private String bankName;

    public ChequeDetails() {
        super();
    }

    public ChequeDetails(String paymentId, double amount, String orderId, 
                        String chequeNumber, String bankName) {
        super(paymentId, amount, orderId);
        this.chequeNumber = chequeNumber;
        this.bankName = bankName;
    }

    // Getters and setters
    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public boolean authorize() {
        // Cheque payments are always authorized (manual processing)
        setStatus(PaymentStatus.AUTHORIZED);
        return true;
    }

    @Override
    public boolean processPayment() {
        if (getStatus() == PaymentStatus.AUTHORIZED) {
            // Simulate cheque processing
            setStatus(PaymentStatus.PROCESSED);
            return true;
        }
        return false;
    }

    @Override
    public boolean refund() {
        // Cheque refunds require manual processing
        return false;
    }

    /**
     * Validates cheque details.
     *
     * @return true if cheque details are valid
     */
    public boolean isValidCheque() {
        return chequeNumber != null && !chequeNumber.trim().isEmpty() &&
               bankName != null && !bankName.trim().isEmpty();
    }

    @Override
    public String toString() {
        return String.format("ChequePayment[ID: %s, Amount: $%.2f, Cheque: %s, Bank: %s]", 
                           getPaymentId(), getAmount(), chequeNumber, bankName);
    }
}