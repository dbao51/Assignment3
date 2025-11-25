package com.awe.onlinestore.model;

import java.io.Serializable;

public class FinanceAccount extends Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String accountNumber;
    private String bankName;
    private String financeProvider;

    public FinanceAccount() {
        super();
    }

    public FinanceAccount(String paymentId, double amount, String orderId, 
                         String accountNumber, String bankName, String financeProvider) {
        super(paymentId, amount, orderId);
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.financeProvider = financeProvider;
    }

    // Getters and setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getFinanceProvider() {
        return financeProvider;
    }

    public void setFinanceProvider(String financeProvider) {
        this.financeProvider = financeProvider;
    }

    @Override
    public boolean authorize() {
        // Simulate finance account authorization
        if (isValidFinanceAccount()) {
            setStatus(PaymentStatus.AUTHORIZED);
            return true;
        }
        setStatus(PaymentStatus.FAILED);
        return false;
    }

    @Override
    public boolean processPayment() {
        if (getStatus() == PaymentStatus.AUTHORIZED) {
            // Simulate finance payment processing
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
     * Validates finance account details.
     *
     * @return true if finance account details are valid
     */
    private boolean isValidFinanceAccount() {
        return accountNumber != null && !accountNumber.trim().isEmpty() &&
               bankName != null && !bankName.trim().isEmpty() &&
               financeProvider != null && !financeProvider.trim().isEmpty();
    }

    /**
     * Gets masked account number for security.
     *
     * @return masked account number
     */
    public String getMaskedAccountNumber() {
        if (accountNumber == null || accountNumber.length() < 4) {
            return "****";
        }
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }

    @Override
    public String toString() {
        return String.format("FinancePayment[ID: %s, Amount: $%.2f, Account: %s, Provider: %s]", 
                           getPaymentId(), getAmount(), getMaskedAccountNumber(), financeProvider);
    }
}