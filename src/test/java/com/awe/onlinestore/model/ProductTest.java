package com.awe.onlinestore.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Product class.
 * Tests business logic methods and validation.
 */
class ProductTest {

    @Test
    void testProductCreation() {
        // Arrange & Act
        Product product = new Product("P001", "iPhone 17 Pro", 
                                    "Latest smartphone", "Smartphones", 1899.00, 40);
        
        // Assert
        assertNotNull(product);
        assertEquals("P001", product.getProductId());
        assertEquals("iPhone 17 Pro", product.getName());
        assertEquals("Smartphones", product.getCategory());
        assertEquals(1899.00, product.getPrice(), 0.001);
        assertEquals(40, product.getStockQuantity());
    }

    @Test
    void testIsInStock() {
        // Arrange
        Product inStockProduct = new Product("P001", "Test Product", 
                                           "Description", "Category", 100.0, 5);
        Product outOfStockProduct = new Product("P002", "Test Product 2", 
                                              "Description", "Category", 100.0, 0);
        
        // Act & Assert
        assertTrue(inStockProduct.isInStock());
        assertFalse(outOfStockProduct.isInStock());
    }

    @Test
    void testDecreaseStock_Success() {
        // Arrange
        Product product = new Product("P001", "Test Product", 
                                    "Description", "Category", 100.0, 10);
        
        // Act
        boolean result = product.decreaseStock(3);
        
        // Assert
        assertTrue(result);
        assertEquals(7, product.getStockQuantity());
    }

    @Test
    void testDecreaseStock_InsufficientStock() {
        // Arrange
        Product product = new Product("P001", "Test Product", 
                                    "Description", "Category", 100.0, 5);
        
        // Act
        boolean result = product.decreaseStock(10);
        
        // Assert
        assertFalse(result);
        assertEquals(5, product.getStockQuantity()); // Stock should remain unchanged
    }

    @Test
    void testDecreaseStock_InvalidQuantity() {
        // Arrange
        Product product = new Product("P001", "Test Product", 
                                    "Description", "Category", 100.0, 5);
        
        // Act
        boolean result = product.decreaseStock(-1);
        
        // Assert
        assertFalse(result);
        assertEquals(5, product.getStockQuantity());
    }

    @Test
    void testIncreaseStock() {
        // Arrange
        Product product = new Product("P001", "Test Product", 
                                    "Description", "Category", 100.0, 5);
        
        // Act
        product.increaseStock(3);
        
        // Assert
        assertEquals(8, product.getStockQuantity());
    }

    @Test
    void testIncreaseStock_InvalidQuantity() {
        // Arrange
        Product product = new Product("P001", "Test Product", 
                                    "Description", "Category", 100.0, 5);
        
        // Act
        product.increaseStock(-2); // Should not decrease stock
        
        // Assert
        assertEquals(5, product.getStockQuantity());
    }

    @Test
    void testGetDetails() {
        // Arrange
        Product product = new Product("P001", "iPhone 17 Pro", 
                                    "Latest smartphone", "Smartphones", 1899.00, 40);
        
        // Act
        String details = product.getDetails();

        // DEBUG: In ra để xem thực tế
        System.out.println("=== DEBUG getDetails() OUTPUT ===");
        System.out.println(details);
        System.out.println("=== END DEBUG ===");
        
        // Assert
        assertNotNull(details);
        assertTrue(details.contains("iPhone 17 Pro"));
        assertTrue(details.contains("$1899,00"), "Details should contain price");
        assertTrue(details.contains("Smartphones"));
        // Check if it contains either "In Stock: Yes" or "Yes" for stock status
        assertTrue(details.contains("Stock") || details.contains("In Stock"));
    }

    @Test
    void testToString() {
        // Arrange
        Product product = new Product("P001", "iPhone 17 Pro", 
                                    "Latest smartphone", "Smartphones", 1899.00, 40);
        
        // Act
        String toString = product.toString();
        
        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("Product[ID: P001"));
        assertTrue(toString.contains("iPhone 17 Pro"));
    }

    @Test
    void testProductEquality() {
        // Arrange
        Product product1 = new Product("P001", "iPhone 17 Pro", 
                                     "Description", "Smartphones", 1899.00, 40);
        Product product2 = new Product("P001", "iPhone 17 Pro", 
                                     "Description", "Smartphones", 1899.00, 40);
        
        // Act & Assert - They should be equal based on productId
        assertEquals(product1.getProductId(), product2.getProductId());
    }

    @Test
    void testProductInequality() {
        // Arrange
        Product product1 = new Product("P001", "iPhone 17 Pro", 
                                     "Description", "Smartphones", 1899.00, 40);
        Product product2 = new Product("P002", "Samsung S25", 
                                     "Description", "Smartphones", 1999.00, 35);
        
        // Act & Assert - They should not be equal
        assertNotEquals(product1.getProductId(), product2.getProductId());
    }
}
