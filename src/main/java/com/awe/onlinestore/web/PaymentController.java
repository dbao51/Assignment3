// src/main/java/com/awe/onlinestore/web/PaymentController.java
package com.awe.onlinestore.web;

import com.awe.onlinestore.model.*;
import com.awe.onlinestore.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    @PostMapping("/process")
    public String processPayment(@RequestParam String orderId,
                                @RequestParam String paymentMethod,
                                @RequestParam(required = false) String cardNumber,
                                @RequestParam(required = false) String expiryDate,
                                @RequestParam(required = false) String cardHolderName,
                                @RequestParam(required = false) String cvv,
                                @RequestParam(required = false) String chequeNumber,
                                @RequestParam(required = false) String bankName,
                                Model model) {
        
        // In a real app, you would get the order from database
        // For demo, we'll create a mock order
        Order order = new Order(orderId, new Customer(), new Address());
        order.setTotalAmount(100.0); // Mock amount
        
        Payment payment = null;
        boolean paymentSuccess = false;
        
        try {
            switch (paymentMethod) {
                case "card":
                    if (paymentService.validateCardDetails(cardNumber, expiryDate, cardHolderName, cvv)) {
                        payment = paymentService.createCardPayment(cardNumber, expiryDate, cardHolderName, cvv);
                        paymentSuccess = paymentService.processPayment(order, payment);
                    }
                    break;
                    
                case "cheque":
                    if (paymentService.validateChequeDetails(chequeNumber, bankName)) {
                        payment = paymentService.createChequePayment(chequeNumber, bankName);
                        paymentSuccess = paymentService.processPayment(order, payment);
                    }
                    break;
                    
                default:
                    model.addAttribute("error", "Invalid payment method");
                    return "payment-error";
            }
            
            if (paymentSuccess) {
                model.addAttribute("orderId", orderId);
                model.addAttribute("payment", payment);
                return "payment-success";
            } else {
                model.addAttribute("error", "Payment processing failed");
                return "payment-error";
            }
            
        } catch (Exception e) {
            model.addAttribute("error", "Payment error: " + e.getMessage());
            return "payment-error";
        }
    }
}