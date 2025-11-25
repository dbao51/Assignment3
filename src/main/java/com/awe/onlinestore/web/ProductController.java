package com.awe.onlinestore.web;

import com.awe.onlinestore.model.Product;
import com.awe.onlinestore.service.CatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    private CatalogueService catalogueService;
    
    @GetMapping
    public String listProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean inStockOnly,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            Model model) {
        
        // Default values
        if (inStockOnly == null) inStockOnly = false;
        if (sortBy == null) sortBy = "name";
        if (sortOrder == null) sortOrder = "asc";
        
        List<Product> products = catalogueService.getFilteredAndSortedProducts(
            category, minPrice, maxPrice, inStockOnly, sortBy, sortOrder);
        
        double[] priceRange = catalogueService.getPriceRange();
        
        model.addAttribute("products", products);
        model.addAttribute("categories", catalogueService.getAllCategories());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("inStockOnly", inStockOnly);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("minPriceRange", priceRange[0]);
        model.addAttribute("maxPriceRange", priceRange[1]);
        
        return "products";
    }
    
    @GetMapping("/search")
    public String searchProducts(@RequestParam String query, Model model) {
        List<Product> products = catalogueService.searchProducts(query);
        double[] priceRange = catalogueService.getPriceRange();
        
        model.addAttribute("products", products);
        model.addAttribute("searchQuery", query);
        model.addAttribute("categories", catalogueService.getAllCategories());
        model.addAttribute("minPriceRange", priceRange[0]);
        model.addAttribute("maxPriceRange", priceRange[1]);
        
        return "products";
    }
    
    @GetMapping("/category/{category}")
    public String productsByCategory(@PathVariable String category, Model model) {
        List<Product> products = catalogueService.getProductsByCategory(category);
        double[] priceRange = catalogueService.getPriceRange();
        
        model.addAttribute("products", products);
        model.addAttribute("category", category);
        model.addAttribute("categories", catalogueService.getAllCategories());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("minPriceRange", priceRange[0]);
        model.addAttribute("maxPriceRange", priceRange[1]);
        
        return "products";
    }
}
