package com.awe.onlinestore.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Cart class.
 * Tests cart operations and business logic.
 */
class CartTest {

    private Cart cart;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        cart = new Cart("C001");
        product1 = new Product("P001", "iPhone 17 Pro", 
                              "Latest smartphone", "Smartphones", 1899.00, 10);
        product2 = new Product("P002", "Samsung S25", 
                              "Android flagship", "Smartphones", 1999.00, 5);
    }

    @Test
    void testAddItem_NewItem() {
        // Act
        boolean result = cart.addItem(product1, 2);
        
        // Assert
        assertTrue(result);
        assertEquals(1, cart.getItemCount());
        assertEquals(2, cart.getItemQuantity("P001"));
        assertEquals(3798.00, cart.calculateTotal(), 0.001);
    }

    @Test
    void testAddItem_UpdateExistingItem() {
        // Arrange
        cart.addItem(product1, 2);
        
        // Act
        boolean result = cart.addItem(product1, 3);
        
        // Assert
        assertTrue(result);
        assertEquals(1, cart.getItemCount());
        assertEquals(5, cart.getItemQuantity("P001")); // 2 + 3 = 5
    }

    @Test
    void testAddItem_InsufficientStock() {
        // Act
        boolean result = cart.addItem(product1, 15); // Only 10 in stock
        
        // Assert
        assertFalse(result);
        assertTrue(cart.isEmpty());
    }

    @Test
    void testAddItem_InvalidQuantity() {
        // Act
        boolean result = cart.addItem(product1, 0);
        
        // Assert
        assertFalse(result);
        assertTrue(cart.isEmpty());
    }

    @Test
    void testRemoveItem() {
        // Arrange
        cart.addItem(product1, 2);
        cart.addItem(product2, 1);
        
        // Act
        boolean result = cart.removeItem("P001");
        
        // Assert
        assertTrue(result);
        assertEquals(1, cart.getItemCount());
        assertEquals(0, cart.getItemQuantity("P001"));
        assertEquals(1999.00, cart.calculateTotal(), 0.001);
    }

    @Test
    void testRemoveItem_NotFound() {
        // Arrange
        cart.addItem(product1, 2);
        
        // Act
        boolean result = cart.removeItem("P999"); // Non-existent product
        
        // Assert
        assertFalse(result);
        assertEquals(1, cart.getItemCount());
    }

    @Test
    void testUpdateQuantity() {
        // Arrange
        cart.addItem(product1, 2);
        
        // Act
        boolean result = cart.updateQuantity("P001", 5);
        
        // Assert
        assertTrue(result);
        assertEquals(5, cart.getItemQuantity("P001"));
        assertEquals(9495.00, cart.calculateTotal(), 0.001); // 5 * 1899.00
    }

    @Test
    void testUpdateQuantity_RemoveItem() {
        // Arrange
        cart.addItem(product1, 2);
        
        // Act
        boolean result = cart.updateQuantity("P001", 0); // Should remove item
        
        // Assert
        assertTrue(result);
        assertTrue(cart.isEmpty());
    }

    @Test
    void testUpdateQuantity_InsufficientStock() {
        // Arrange
        cart.addItem(product1, 2);
        
        // Act
        boolean result = cart.updateQuantity("P001", 15); // Only 10 in stock
        
        // Assert
        assertFalse(result);
        assertEquals(2, cart.getItemQuantity("P001")); // Quantity unchanged
    }

    @Test
    void testCalculateTotal() {
        // Arrange
        cart.addItem(product1, 2); // 2 * 1899.00 = 3798.00
        cart.addItem(product2, 1); // 1 * 1999.00 = 1999.00
        
        // Act
        double total = cart.calculateTotal();
        
        // Assert
        assertEquals(5797.00, total, 0.001);
    }

    @Test
    void testCalculateTotal_EmptyCart() {
        // Act
        double total = cart.calculateTotal();
        
        // Assert
        assertEquals(0.0, total, 0.001);
    }

    @Test
    void testIsEmpty() {
        // Assert
        assertTrue(cart.isEmpty());
        
        // Act & Assert
        cart.addItem(product1, 1);
        assertFalse(cart.isEmpty());
        
        cart.removeItem("P001");
        assertTrue(cart.isEmpty());
    }

    @Test
    void testClear() {
        // Arrange
        cart.addItem(product1, 2);
        cart.addItem(product2, 1);
        
        // Act
        cart.clear();
        
        // Assert
        assertTrue(cart.isEmpty());
        assertEquals(0, cart.calculateTotal(), 0.001);
    }

    @Test
    void testGetTotalQuantity() {
        // Arrange
        cart.addItem(product1, 2);
        cart.addItem(product2, 3);
        
        // Act
        int totalQuantity = cart.getTotalQuantity();
        
        // Assert
        assertEquals(5, totalQuantity);
    }
}
