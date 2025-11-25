package com.awe.onlinestore.service;

import com.awe.onlinestore.dao.Database;
import com.awe.onlinestore.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CatalogueService class.
 * Tests product search, filtering, and business logic.
 */
@ExtendWith(MockitoExtension.class)
class CatalogueServiceTest {

    @Mock
    private Database database;

    @InjectMocks
    private CatalogueService catalogueService;

    private Product smartphone;
    private Product laptop;
    private Product headphones;

    @BeforeEach
    void setUp() {
        smartphone = new Product("P001", "iPhone 17 Pro", 
                               "Apple flagship smartphone", "Smartphones", 1899.00, 10);
        laptop = new Product("P002", "MacBook Pro", 
                           "Apple laptop", "Laptops", 3299.00, 5);
        headphones = new Product("P003", "Sony WH-1000XM6", 
                               "Noise cancelling headphones", "Headphones", 549.00, 20);
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(smartphone, laptop, headphones);
        when(database.getAllProducts()).thenReturn(expectedProducts);
        
        // Act
        List<Product> result = catalogueService.getAllProducts();
        
        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(database, times(1)).getAllProducts();
    }

    @Test
    void testSearchProducts() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(smartphone);
        when(database.searchProducts("iphone")).thenReturn(expectedProducts);
        
        // Act
        List<Product> result = catalogueService.searchProducts("iphone");
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("iPhone 17 Pro", result.get(0).getName());
        verify(database, times(1)).searchProducts("iphone");
    }

    @Test
    void testSearchProducts_EmptyQuery() {
        // Arrange
        List<Product> allProducts = Arrays.asList(smartphone, laptop, headphones);
        when(database.getAllProducts()).thenReturn(allProducts);
        
        // Act
        List<Product> result = catalogueService.searchProducts("");
        
        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(database, times(1)).getAllProducts();
    }

    @Test
    void testGetProductsByCategory() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(smartphone);
        when(database.getProductsByCategory("Smartphones")).thenReturn(expectedProducts);
        
        // Act
        List<Product> result = catalogueService.getProductsByCategory("Smartphones");
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Smartphones", result.get(0).getCategory());
        verify(database, times(1)).getProductsByCategory("Smartphones");
    }

    @Test
    void testGetProductById() {
        // Arrange
        when(database.getProduct("P001")).thenReturn(smartphone);
        
        // Act
        Product result = catalogueService.getProductById("P001");
        
        // Assert
        assertNotNull(result);
        assertEquals("P001", result.getProductId());
        assertEquals("iPhone 17 Pro", result.getName());
        verify(database, times(1)).getProduct("P001");
    }

    @Test
    void testGetProductById_NotFound() {
        // Arrange
        when(database.getProduct("P999")).thenReturn(null);
        
        // Act
        Product result = catalogueService.getProductById("P999");
        
        // Assert
        assertNull(result);
        verify(database, times(1)).getProduct("P999");
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        List<Product> products = Arrays.asList(smartphone, laptop, headphones);
        when(database.getAllProducts()).thenReturn(products);
        
        // Act
        List<String> categories = catalogueService.getAllCategories();
        
        // Assert
        assertNotNull(categories);
        assertEquals(3, categories.size());
        assertTrue(categories.contains("Smartphones"));
        assertTrue(categories.contains("Laptops"));
        assertTrue(categories.contains("Headphones"));
    }

    @Test
    void testCheckStockAvailability_Sufficient() {
        // Arrange
        when(database.getProduct("P001")).thenReturn(smartphone);
        
        // Act
        boolean result = catalogueService.checkStockAvailability("P001", 5);
        
        // Assert
        assertTrue(result);
        verify(database, times(1)).getProduct("P001");
    }

    @Test
    void testCheckStockAvailability_Insufficient() {
        // Arrange
        when(database.getProduct("P001")).thenReturn(smartphone);
        
        // Act
        boolean result = catalogueService.checkStockAvailability("P001", 15);
        
        // Assert
        assertFalse(result);
    }

    @Test
    void testCheckStockAvailability_ProductNotFound() {
        // Arrange
        when(database.getProduct("P999")).thenReturn(null);
        
        // Act
        boolean result = catalogueService.checkStockAvailability("P999", 1);
        
        // Assert
        assertFalse(result);
    }

    @Test
    void testUpdateProductStock_Success() {
        // Arrange
        when(database.getProduct("P001")).thenReturn(smartphone);
        
        // Act
        boolean result = catalogueService.updateProductStock("P001", 3);
        
        // Assert
        assertTrue(result);
        assertEquals(7, smartphone.getStockQuantity()); // 10 - 3 = 7
        verify(database, times(1)).saveProduct(smartphone);
    }

    @Test
    void testUpdateProductStock_InsufficientStock() {
        // Arrange
        when(database.getProduct("P001")).thenReturn(smartphone);
        
        // Act
        boolean result = catalogueService.updateProductStock("P001", 15);
        
        // Assert
        assertFalse(result);
        assertEquals(10, smartphone.getStockQuantity()); // Stock unchanged
        verify(database, never()).saveProduct(any());
    }

    @Test
    void testGetFilteredAndSortedProducts() {
        // Arrange
        List<Product> allProducts = Arrays.asList(smartphone, laptop, headphones);
        when(database.getAllProducts()).thenReturn(allProducts);
        
        // Act
        List<Product> result = catalogueService.getFilteredAndSortedProducts(
            "Smartphones", 1000.0, 2000.0, true, "name", "asc");
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("iPhone 17 Pro", result.get(0).getName());
    }

    @Test
    void testGetPriceRange() {
        // Arrange
        List<Product> products = Arrays.asList(
            new Product("P001", "Product1", "Desc1", "Cat1", 100.0, 10),
            new Product("P002", "Product2", "Desc2", "Cat2", 500.0, 5),
            new Product("P003", "Product3", "Desc3", "Cat3", 300.0, 8)
        );
        when(database.getAllProducts()).thenReturn(products);
        
        // Act
        double[] priceRange = catalogueService.getPriceRange();
        
        // Assert
        assertNotNull(priceRange);
        assertEquals(100.0, priceRange[0], 0.001);
        assertEquals(500.0, priceRange[1], 0.001);
    }
}
