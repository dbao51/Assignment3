// src/main/java/com/awe/onlinestore/service/PaymentService.java
package com.awe.onlinestore.service;

import com.awe.onlinestore.dao.Database;
import com.awe.onlinestore.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class PaymentService {
    
    @Autowired
    private Database database;

    /**
     * Processes a payment for an order.
     *
     * @param order the order to process payment for
     * @param paymentMethod the payment method to use
     * @return true if payment was successful
     */
    public boolean processPayment(Order order, Payment paymentMethod) {
        if (order == null || paymentMethod == null) {
            System.out.println("Invalid order or payment method.");
            return false;
        }

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            System.out.println("Order must be confirmed before payment.");
            return false;
        }

        // Set payment amount to order total
        paymentMethod.setAmount(order.getTotalAmount());
        paymentMethod.setOrderId(order.getOrderId());

        System.out.println("\n=== PROCESSING PAYMENT ===");
        System.out.printf("Order: %s%n", order.getOrderId());
        System.out.printf("Amount: $%.2f%n", order.getTotalAmount());
        System.out.printf("Method: %s%n", paymentMethod.getClass().getSimpleName());

        // Authorize payment
        if (!paymentMethod.authorize()) {
            System.out.println("Payment authorization failed.");
            return false;
        }

        // Process payment
        if (!paymentMethod.processPayment()) {
            System.out.println("Payment processing failed.");
            return false;
        }

        // Save payment and update order
        database.savePayment(paymentMethod);
        order.markAsPaid();
        database.saveOrder(order);

        System.out.println("✅ Payment processed successfully!");
        System.out.printf("Payment ID: %s%n", paymentMethod.getPaymentId());
        return true;
    }

    /**
     * Creates a card payment method.
     *
     * @param cardNumber the card number
     * @param expiryDate the expiry date (MM/YY)
     * @param cardHolderName the card holder name
     * @param cvv the CVV code
     * @return CardDetails object
     */
    public CardDetails createCardPayment(String cardNumber, String expiryDate, 
                                       String cardHolderName, String cvv) {
        String paymentId = "PAY_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new CardDetails(paymentId, 0.0, null, cardNumber, expiryDate, cardHolderName, cvv);
    }

    /**
     * Creates a cheque payment method.
     *
     * @param chequeNumber the cheque number
     * @param bankName the bank name
     * @return ChequeDetails object
     */
    public ChequeDetails createChequePayment(String chequeNumber, String bankName) {
        String paymentId = "PAY_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new ChequeDetails(paymentId, 0.0, null, chequeNumber, bankName);
    }

    /**
     * Creates a finance account payment method.
     *
     * @param accountNumber the account number
     * @param bankName the bank name
     * @param financeProvider the finance provider
     * @return FinanceAccount object
     */
    public FinanceAccount createFinancePayment(String accountNumber, String bankName, String financeProvider) {
        String paymentId = "PAY_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new FinanceAccount(paymentId, 0.0, null, accountNumber, bankName, financeProvider);
    }

    /**
     * Validates card payment details.
     *
     * @param cardNumber the card number
     * @param expiryDate the expiry date
     * @param cardHolderName the card holder name
     * @param cvv the CVV code
     * @return true if details are valid
     */
    public boolean validateCardDetails(String cardNumber, String expiryDate, 
                                     String cardHolderName, String cvv) {
        if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
            System.out.println("Invalid card number. Must be 16 digits.");
            return false;
        }

        if (expiryDate == null || !expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            System.out.println("Invalid expiry date. Format: MM/YY");
            return false;
        }

        if (cardHolderName == null || cardHolderName.trim().isEmpty()) {
            System.out.println("Card holder name is required.");
            return false;
        }

        if (cvv == null || !cvv.matches("\\d{3}")) {
            System.out.println("Invalid CVV. Must be 3 digits.");
            return false;
        }

        return true;
    }

    /**
     * Validates cheque payment details.
     *
     * @param chequeNumber the cheque number
     * @param bankName the bank name
     * @return true if details are valid
     */
    public boolean validateChequeDetails(String chequeNumber, String bankName) {
        if (chequeNumber == null || chequeNumber.trim().isEmpty()) {
            System.out.println("Cheque number is required.");
            return false;
        }

        if (bankName == null || bankName.trim().isEmpty()) {
            System.out.println("Bank name is required.");
            return false;
        }

        return true;
    }

    /**
     * Validates finance account details.
     *
     * @param accountNumber the account number
     * @param bankName the bank name
     * @param financeProvider the finance provider
     * @return true if details are valid
     */
    public boolean validateFinanceDetails(String accountNumber, String bankName, String financeProvider) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            System.out.println("Account number is required.");
            return false;
        }

        if (bankName == null || bankName.trim().isEmpty()) {
            System.out.println("Bank name is required.");
            return false;
        }

        if (financeProvider == null || financeProvider.trim().isEmpty()) {
            System.out.println("Finance provider is required.");
            return false;
        }

        return true;
    }
}