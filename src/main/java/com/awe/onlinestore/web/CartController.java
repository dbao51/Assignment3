// src/main/java/com/awe/onlinestore/web/CartController.java
package com.awe.onlinestore.web;

import com.awe.onlinestore.model.Cart;
import com.awe.onlinestore.model.Product;
import com.awe.onlinestore.service.CatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CatalogueService catalogueService;
    
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Cart cart = getOrCreateCart(session);
        model.addAttribute("cart", cart);
        model.addAttribute("total", cart.calculateTotal());
        return "cart";
    }
    
    @PostMapping("/add")
    public String addToCart(@RequestParam String productId, 
                           @RequestParam int quantity,
                           HttpSession session,
                           Model model) {
        Cart cart = getOrCreateCart(session);
        Product product = catalogueService.getProductById(productId);
        
        if (product != null) {
            boolean success = cart.addItem(product, quantity);
            if (!success) {
                model.addAttribute("error", "Failed to add product to cart. Check stock availability.");
            }
        } else {
            model.addAttribute("error", "Product not found.");
        }
        
        return "redirect:/cart";
    }
    
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam String productId, HttpSession session) {
        Cart cart = getOrCreateCart(session);
        cart.removeItem(productId);
        return "redirect:/cart";
    }
    
    @PostMapping("/update")
    public String updateCart(@RequestParam String productId, 
                            @RequestParam int quantity,
                            HttpSession session) {
        Cart cart = getOrCreateCart(session);
        cart.updateQuantity(productId, quantity);
        return "redirect:/cart";
    }
    
    private Cart getOrCreateCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}