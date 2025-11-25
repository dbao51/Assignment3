package com.awe.onlinestore.dao;

import com.awe.onlinestore.model.*;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;

@Repository
public class Database {
    private Map<String, Product> products;
    private Map<String, Customer> customers;
    private Map<String, Order> orders;
    private Map<String, Payment> payments;
    
    private final String DATA_DIR = "data/";
    private final String PRODUCTS_FILE = DATA_DIR + "products.dat";
    private final String CUSTOMERS_FILE = DATA_DIR + "customers.dat";
    private final String ORDERS_FILE = DATA_DIR + "orders.dat";
    private final String PAYMENTS_FILE = DATA_DIR + "payments.dat";

    @PostConstruct
    public void init() {
        // Create data directory
        new File(DATA_DIR).mkdirs();
        
        this.products = new HashMap<>();
        this.customers = new HashMap<>();
        this.orders = new HashMap<>();
        this.payments = new HashMap<>();
        
        loadDataFromFiles();
        
        // Create sample data if no data exists
        if (products.isEmpty()) {
            createSampleData();
        }
    }

    // Product operations
    public void saveProduct(Product product) {
        products.put(product.getProductId(), product);
        saveProductsToFile();
    }

    public Product getProduct(String productId) {
        return products.get(productId);
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public List<Product> searchProducts(String query) {
        return products.values().stream()
            .filter(product -> product.getName().toLowerCase().contains(query.toLowerCase()) ||
                             product.getDescription().toLowerCase().contains(query.toLowerCase()) ||
                             product.getCategory().toLowerCase().contains(query.toLowerCase()))
            .toList();
    }

    public List<Product> getProductsByCategory(String category) {
        return products.values().stream()
            .filter(product -> product.getCategory().equalsIgnoreCase(category))
            .toList();
    }

    // Customer operations
    public void saveCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
        saveCustomersToFile();
    }

    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }

    // Order operations
    public void saveOrder(Order order) {
        orders.put(order.getOrderId(), order);
        saveOrdersToFile();
    }

    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    // Payment operations
    public void savePayment(Payment payment) {
        payments.put(payment.getPaymentId(), payment);
        savePaymentsToFile();
    }

    public Payment getPayment(String paymentId) {
        return payments.get(paymentId);
    }

    // File operations
    @SuppressWarnings("unchecked")
    private void loadDataFromFiles() {
        try {
            // Load products
            if (new File(PRODUCTS_FILE).exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PRODUCTS_FILE))) {
                    products = (Map<String, Product>) ois.readObject();
                }
            }
            
            // Load customers
            if (new File(CUSTOMERS_FILE).exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CUSTOMERS_FILE))) {
                    customers = (Map<String, Customer>) ois.readObject();
                }
            }
            
            // Load orders
            if (new File(ORDERS_FILE).exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ORDERS_FILE))) {
                    orders = (Map<String, Order>) ois.readObject();
                }
            }
            
            // Load payments
            if (new File(PAYMENTS_FILE).exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PAYMENTS_FILE))) {
                    payments = (Map<String, Payment>) ois.readObject();
                }
            }
            
        } catch (Exception e) {
            System.out.println("⚠️  Could not load data from files: " + e.getMessage());
            // Continue with empty data
        }
    }

    private void saveProductsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PRODUCTS_FILE))) {
            oos.writeObject(products);
        } catch (IOException e) {
            System.err.println("❌ Error saving products: " + e.getMessage());
        }
    }

    private void saveCustomersToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CUSTOMERS_FILE))) {
            oos.writeObject(customers);
        } catch (IOException e) {
            System.err.println("❌ Error saving customers: " + e.getMessage());
        }
    }

    private void saveOrdersToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ORDERS_FILE))) {
            oos.writeObject(orders);
        } catch (IOException e) {
            System.err.println("❌ Error saving orders: " + e.getMessage());
        }
    }

    private void savePaymentsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PAYMENTS_FILE))) {
            oos.writeObject(payments);
        } catch (IOException e) {
            System.err.println("❌ Error saving payments: " + e.getMessage());
        }
    }

    // Sample data - UPDATED WITH NEW PRODUCTS
    private void createSampleData() {
        System.out.println("📦 Creating sample data with latest electronics...");
        
        // Updated sample products with 2024-2025 models
        Product[] sampleProducts = {
            // --- Smartphones ---
            new Product("P001", "iPhone 17 Pro", 
                "Apple's latest flagship with A19 chip and upgraded camera system", 
                "Smartphones", 1899.00, 40),

            new Product("P002", "Samsung Galaxy S25 Ultra", 
                "Top-tier Android with 200MP camera and S Pen support", 
                "Smartphones", 1999.00, 35),

            new Product("P009", "Samsung Galaxy S25+", 
                "Bigger 6.7\" model with enhanced battery life and upgraded camera", 
                "Smartphones", 1699.00, 40),

            new Product("P010", "iPhone 17 Pro Max", 
                "Apple's top-tier model with A19 Pro chip and advanced zoom system", 
                "Smartphones", 2199.00, 30),

            new Product("P011", "Google Pixel 9 Pro", 
                "Google flagship with Tensor G4 chip and industry-leading AI camera", 
                "Smartphones", 1699.00, 28),

            new Product("P012", "Google Pixel Fold 2", 
                "Second-gen foldable with stronger hinge and brighter display", 
                "Smartphones", 2499.00, 15),

            new Product("P013", "Samsung Galaxy Z Fold 6", 
                "Newest foldable with slimmer design and Snapdragon 8 Elite", 
                "Smartphones", 2799.00, 20),

            new Product("P014", "Samsung Galaxy Z Flip 6", 
                "Compact foldable with improved durability and better battery", 
                "Smartphones", 1599.00, 25),

            // --- Laptops ---
            new Product("P003", "MacBook Pro 2024 (M3 Pro)", 
                "High-performance laptop with M3 Pro chip", 
                "Laptops", 3299.00, 20),

            new Product("P007", "Dell XPS 14 (2024)", 
                "Premium 14-inch laptop with Intel Ultra processors", 
                "Laptops", 2499.00, 18),

            new Product("P015", "MacBook Air 2024 (M3)", 
                "Ultra-portable laptop with M3 chip and longer battery life", 
                "Laptops", 1899.00, 22),

            new Product("P016", "Razer Blade 16 (2024)", 
                "High-end gaming laptop with RTX 4090 and 240Hz display", 
                "Laptops", 5399.00, 10),

            // --- Tablets ---
            new Product("P005", "iPad Pro 2024 (M4)", 
                "Ultra-powerful tablet with OLED display and M4 chip", 
                "Tablets", 1699.00, 25),

            // --- Headphones / Audio ---
            new Product("P004", "Sony WH-1000XM6", 
                "Next-gen ANC wireless headphones with improved sound", 
                "Headphones", 549.00, 50),

            new Product("P008", "Bose QuietComfort Ultra", 
                "Flagship ANC headphones with immersive audio", 
                "Headphones", 499.00, 30),

            // --- Wearables ---
            new Product("P006", "Apple Watch Series 10", 
                "Latest smartwatch with redesigned thin body and advanced health sensors", 
                "Wearables", 799.00, 45),

            new Product("P018", "Apple Vision Pro 2 (2025)", 
                "Next-gen spatial computing headset with lighter design", 
                "Wearables", 5899.00, 8)
        };

        for (Product product : sampleProducts) {
            saveProduct(product);
        }

        // Sample customer
        Customer customer = new Customer("C001", "john_doe", "password123", "John Doe", "john.doe@email.com");
        customer.addAddress(new Address("123 Main St", "Melbourne", "3000", "Australia"));
        saveCustomer(customer);
        
        System.out.println("✅ Sample data created successfully with " + sampleProducts.length + " products!");
        System.out.println("📊 Product distribution:");
        System.out.println("   - Smartphones: " + getProductsByCategory("Smartphones").size() + " models");
        System.out.println("   - Laptops: " + getProductsByCategory("Laptops").size() + " models");
        System.out.println("   - Tablets: " + getProductsByCategory("Tablets").size() + " models");
        System.out.println("   - Headphones: " + getProductsByCategory("Headphones").size() + " models");
        System.out.println("   - Wearables: " + getProductsByCategory("Wearables").size() + " models");
    }
}
