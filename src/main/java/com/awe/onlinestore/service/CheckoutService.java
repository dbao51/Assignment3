package com.awe.onlinestore.service;

import com.awe.onlinestore.dao.Database;
import com.awe.onlinestore.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CheckoutService {
    
    @Autowired
    private Database database;
    
    @Autowired
    private CatalogueService catalogueService;

    /**
     * Processes checkout for a customer's cart.
     *
     * @param cart the shopping cart
     * @param customer the customer
     * @param shippingAddress the shipping address
     * @return the created order, or null if failed
     */
    public Order processCheckout(Cart cart, Customer customer, Address shippingAddress) {
        System.out.println("🔧 Starting checkout process...");
        
        if (cart == null || cart.isEmpty()) {
            System.out.println("❌ Cart is empty. Cannot proceed with checkout.");
            return null;
        }

        if (customer == null) {
            System.out.println("❌ Customer is null.");
            return null;
        }

        if (shippingAddress == null || !shippingAddress.isValid()) {
            System.out.println("❌ Invalid shipping address.");
            return null;
        }

        System.out.println("✅ All validations passed.");
        System.out.println("📦 Cart items: " + cart.getItems().size());
        System.out.println("👤 Customer: " + customer.getName());

        // Validate cart items
        if (!validateCartItems(cart)) {
            System.out.println("❌ Checkout failed due to cart validation issues.");
            return null;
        }

        // Create order
        Order order = createOrder(cart, customer, shippingAddress);
        if (order == null) {
            System.out.println("❌ Failed to create order.");
            return null;
        }

        System.out.println("✅ Order created successfully!");
        System.out.printf("🎉 Order ID: %s%n", order.getOrderId());
        System.out.printf("💰 Total Amount: $%.2f%n", order.getTotalAmount());

        return order;
    }

    /**
     * Validates all items in the cart.
     *
     * @param cart the cart to validate
     * @return true if all items are valid
     */
    private boolean validateCartItems(Cart cart) {
        boolean allValid = true;

        for (OrderItem item : cart.getItems()) {
            Product product = item.getProduct();
            int requestedQuantity = item.getQuantity();

            if (!catalogueService.checkStockAvailability(product.getProductId(), requestedQuantity)) {
                System.out.printf("❌ Insufficient stock for: %s (Requested: %d, Available: %d)%n",
                    product.getName(), requestedQuantity, product.getStockQuantity());
                allValid = false;
            } else {
                System.out.printf("✅ Stock OK for: %s (Requested: %d, Available: %d)%n",
                    product.getName(), requestedQuantity, product.getStockQuantity());
            }
        }

        return allValid;
    }

    /**
     * Creates an order from the cart.
     *
     * @param cart the shopping cart
     * @param customer the customer
     * @param shippingAddress the shipping address
     * @return the created order
     */
    private Order createOrder(Cart cart, Customer customer, Address shippingAddress) {
        String orderId = "ORD_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        System.out.println("📝 Creating order: " + orderId);
        
        Order order = new Order(orderId, customer, shippingAddress);

        // Add all items from cart to order
        for (OrderItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem(cartItem.getProduct(), cartItem.getQuantity());
            order.addItem(orderItem);
            System.out.printf("➕ Added to order: %s x%d%n", 
                cartItem.getProduct().getName(), cartItem.getQuantity());

            // Update product stock
            boolean stockUpdated = catalogueService.updateProductStock(
                cartItem.getProduct().getProductId(), 
                cartItem.getQuantity()
            );
            System.out.printf("📊 Stock update for %s: %s%n", 
                cartItem.getProduct().getName(), stockUpdated ? "SUCCESS" : "FAILED");
        }

        // Confirm order and save
        order.confirmOrder();
        database.saveOrder(order);
        System.out.println("💾 Order saved to database.");

        return order;
    }

    /**
     * Displays order summary before confirmation (for console).
     *
     * @param cart the shopping cart
     * @param customer the customer
     * @param shippingAddress the shipping address
     */
    public void displayOrderSummary(Cart cart, Customer customer, Address shippingAddress) {
        System.out.println("\n=== ORDER SUMMARY ===");
        System.out.printf("Customer: %s (%s)%n", customer.getName(), customer.getEmail());
        System.out.printf("Shipping Address: %s%n", shippingAddress);
        System.out.println("\nItems:");

        for (OrderItem item : cart.getItems()) {
            System.out.printf("  - %s x%d - $%.2f%n", 
                item.getProduct().getName(), 
                item.getQuantity(), 
                item.getSubtotal());
        }

        double subtotal = cart.calculateTotal();
        double tax = subtotal * 0.10; // 10% GST
        double total = subtotal + tax;

        System.out.println("\nPrice Breakdown:");
        System.out.printf("  Subtotal: $%.2f%n", subtotal);
        System.out.printf("  GST (10%%): $%.2f%n", tax);
        System.out.printf("  Total: $%.2f%n", total);
    }
}
