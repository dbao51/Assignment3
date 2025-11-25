package com.awe.onlinestore.web;

import com.awe.onlinestore.model.*;
import com.awe.onlinestore.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    
    @Autowired
    private CheckoutService checkoutService;
    
    @GetMapping
    public String checkoutPage(HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");
        
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }
        
        // Create guest customer for demo
        Customer guestCustomer = new Customer("GUEST_" + UUID.randomUUID().toString().substring(0, 8),
                                            "guest", "guest", "Guest User", "guest@example.com");
        guestCustomer.addAddress(new Address("", "", "", ""));
        
        model.addAttribute("cart", cart);
        model.addAttribute("customer", guestCustomer);
        double subtotal = cart.calculateTotal();
        double tax = subtotal * 0.10;
        double total = subtotal + tax;
        
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("tax", tax);
        model.addAttribute("total", total);
        
        return "checkout";
    }
    
    @PostMapping("/process")
    public String processCheckout(@RequestParam String name,
                                 @RequestParam String email,
                                 @RequestParam String street,
                                 @RequestParam String city,
                                 @RequestParam String postcode,
                                 @RequestParam String country,
                                 HttpSession session,
                                 Model model) {
        Cart cart = (Cart) session.getAttribute("cart");
        
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }
        
        // Create customer with form data
        Customer customer = new Customer("GUEST_" + UUID.randomUUID().toString().substring(0, 8),
                                       "guest", "guest", name, email);
        
        // Set customer address
        Address address = new Address(street, city, postcode, country);
        customer.addAddress(address);
        
        // Process checkout
        Order order = checkoutService.processCheckout(cart, customer, address);
        
        if (order != null) {
            model.addAttribute("order", order);
            model.addAttribute("success", true);
            
            // Clear cart after successful order
            session.removeAttribute("cart");
            
            return "order-confirmation";
        } else {
            model.addAttribute("error", "Checkout failed. Please try again.");
            return "redirect:/checkout";
        }
    }
}
