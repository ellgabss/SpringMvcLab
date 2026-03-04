package com.example.springmvclab.controller;

import com.example.springmvclab.model.Product;
import com.example.springmvclab.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class StatisticsController {

    private final ProductService productService;

    public StatisticsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/statistics")
    public String showStatistics(Model model) {

        List<Product> products = productService.findAll();

        model.addAttribute("totalProducts", products.size());

        List<Product> lowStockProducts = products.stream()
                .filter(p -> p.getStock() <= 20)
                .collect(Collectors.toList());

        model.addAttribute("lowStockProducts", lowStockProducts);
        model.addAttribute("lowStockCount", lowStockProducts.size());

        Product expensive = products.stream()
                .max(Comparator.comparing(Product::getPrice))
                .orElse(null);

        Product cheapest = products.stream()
                .min(Comparator.comparing(Product::getPrice))
                .orElse(null);

        model.addAttribute("expensive", expensive);
        model.addAttribute("cheapest", cheapest);

        double avg = products.stream()
                .mapToDouble(Product::getPrice)
                .average()
                .orElse(0.0);

        model.addAttribute("avgPrice", avg);

        Map<String, Long> categoryStats = products.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.counting()
                ));

        model.addAttribute("categoryStats", categoryStats);

        return "statistics";
    }
}