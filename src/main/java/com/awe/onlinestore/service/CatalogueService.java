package com.awe.onlinestore.service;

import com.awe.onlinestore.dao.Database;
import com.awe.onlinestore.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing product catalogue operations.
 * Provides functionality for browsing, searching, filtering, and sorting products.
 * 
 * <p>This service acts as the main entry point for all product-related operations
 * in the AWE Online Electronics Store system.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Product browsing and search</li>
 *   <li>Advanced filtering by category, price range, and stock status</li>
 *   <li>Multiple sorting options (name, price, category)</li>
 *   <li>Stock management and validation</li>
 * </ul>
 * 
 * @author AWE Development Team
 * @version 1.0
 * @since 2024
 */
@Service
public class CatalogueService {
    
    @Autowired
    private Database database;

    /**
     * Retrieves all available products in the catalogue.
     *
     * @return a list of all products, ordered by product ID
     * @see Product
     */
    public List<Product> getAllProducts() {
        return database.getAllProducts();
    }

    /**
     * Searches products by name, description, or category using the provided query.
     * Performs case-insensitive partial matching.
     *
     * @param query the search term to match against product names, descriptions, and categories
     * @return a list of products matching the search criteria, or all products if query is empty
     * @throws IllegalArgumentException if the query is null
     */
    public List<Product> searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllProducts();
        }
        return database.searchProducts(query.trim());
    }

    /**
     * Retrieves products belonging to a specific category.
     *
     * @param category the category to filter by (case-insensitive)
     * @return a list of products in the specified category, or all products if category is empty
     */
    public List<Product> getProductsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return getAllProducts();
        }
        return database.getProductsByCategory(category.trim());
    }

    /**
     * Retrieves a specific product by its unique identifier.
     *
     * @param productId the unique product identifier
     * @return the product with the specified ID, or null if not found
     */
    public Product getProductById(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            return null;
        }
        return database.getProduct(productId.trim());
    }

    /**
     * Retrieves all distinct product categories available in the catalogue.
     *
     * @return a list of unique category names
     */
    public List<String> getAllCategories() {
        return database.getAllProducts().stream()
            .map(Product::getCategory)
            .distinct()
            .collect(Collectors.toList());
    }

    /**
     * Checks if sufficient stock is available for a product purchase.
     *
     * @param productId the product identifier
     * @param quantity the requested quantity
     * @return true if the product exists and has sufficient stock, false otherwise
     */
    public boolean checkStockAvailability(String productId, int quantity) {
        Product product = getProductById(productId);
        return product != null && product.isInStock() && product.getStockQuantity() >= quantity;
    }

    /**
     * Updates product stock quantity after a successful purchase.
     * Decreases the stock by the specified quantity if sufficient stock is available.
     *
     * @param productId the product identifier
     * @param quantity the quantity to deduct from stock
     * @return true if stock was successfully updated, false if insufficient stock or product not found
     */
    public boolean updateProductStock(String productId, int quantity) {
        Product product = getProductById(productId);
        if (product != null && product.decreaseStock(quantity)) {
            database.saveProduct(product);
            return true;
        }
        return false;
    }

    /**
     * Applies multiple filters and sorting criteria to the product catalogue.
     * Supports combination of category filter, price range, stock status, and various sort options.
     *
     * @param category filter by specific category (optional)
     * @param minPrice minimum price filter (optional)
     * @param maxPrice maximum price filter (optional)
     * @param inStockOnly if true, only returns products with available stock
     * @param sortBy field to sort by: "name", "price", "category", or "stock" (optional)
     * @param sortOrder sort direction: "asc" for ascending, "desc" for descending (optional)
     * @return a filtered and sorted list of products
     */
    public List<Product> getFilteredAndSortedProducts(String category, Double minPrice, Double maxPrice, 
                                                     boolean inStockOnly, String sortBy, String sortOrder) {
        List<Product> products = getAllProducts();
        
        // Apply filters
        products = products.stream()
            .filter(product -> category == null || category.isEmpty() || product.getCategory().equalsIgnoreCase(category))
            .filter(product -> minPrice == null || product.getPrice() >= minPrice)
            .filter(product -> maxPrice == null || product.getPrice() <= maxPrice)
            .filter(product -> !inStockOnly || product.isInStock())
            .collect(Collectors.toList());
        
        // Apply sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            Comparator<Product> comparator = getComparator(sortBy);
            if ("desc".equalsIgnoreCase(sortOrder)) {
                comparator = comparator.reversed();
            }
            products = products.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
        }
        
        return products;
    }

    /**
     * Creates a comparator for sorting products based on the specified field.
     *
     * @param sortBy the field to sort by: "name", "price", "category", or "stock"
     * @return a comparator for the specified field
     * @throws IllegalArgumentException if an invalid sort field is provided
     */
    private Comparator<Product> getComparator(String sortBy) {
        switch (sortBy.toLowerCase()) {
            case "name":
                return Comparator.comparing(Product::getName);
            case "price":
                return Comparator.comparing(Product::getPrice);
            case "category":
                return Comparator.comparing(Product::getCategory);
            case "stock":
                return Comparator.comparing(Product::getStockQuantity);
            default:
                return Comparator.comparing(Product::getName);
        }
    }

    /**
     * Calculates the minimum and maximum prices across all products.
     * Useful for setting price range filters in the UI.
     *
     * @return an array where [0] is minimum price and [1] is maximum price
     */
    public double[] getPriceRange() {
        List<Product> products = getAllProducts();
        if (products.isEmpty()) {
            return new double[]{0, 0};
        }
        
        double minPrice = products.stream()
            .mapToDouble(Product::getPrice)
            .min()
            .orElse(0);
        
        double maxPrice = products.stream()
            .mapToDouble(Product::getPrice)
            .max()
            .orElse(0);
            
        return new double[]{minPrice, maxPrice};
    }

    // Console display methods (for testing/demo purposes)
    
    /**
     * Displays all products in a formatted console output.
     * Primarily used for testing and demonstration.
     */
    public void displayAllProducts() {
        List<Product> products = getAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        System.out.println("\n=== PRODUCT CATALOGUE ===");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.printf("%d. %s - $%.2f (Stock: %d)%n", 
                i + 1, product.getName(), product.getPrice(), product.getStockQuantity());
        }
    }

    /**
     * Displays products from a specific category in formatted console output.
     *
     * @param category the category to display
     */
    public void displayProductsByCategory(String category) {
        List<Product> products = getProductsByCategory(category);
        if (products.isEmpty()) {
            System.out.println("No products found in category: " + category);
            return;
        }

        System.out.println("\n=== " + category.toUpperCase() + " ===");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.printf("%d. %s - $%.2f (Stock: %d)%n", 
                i + 1, product.getName(), product.getPrice(), product.getStockQuantity());
        }
    }

    /**
     * Displays detailed information for a specific product.
     *
     * @param productId the product identifier
     */
    public void displayProductDetails(String productId) {
        Product product = getProductById(productId);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.println(product.getDetails());
    }
}
