Kode Eksperimen 3 — StatisticsController + template

- StatisticsController

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

    // Constructor Injection sesuai konsep Week 3
    public StatisticsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/statistics")
    public String showStatistics(Model model) {
        List<Product> products = productService.findAll();

        // 1. Total semua produk
        model.addAttribute("totalProducts", products.size());

        // 2. Total produk per kategori (Map)
        Map<String, Long> categoryStats = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));
        model.addAttribute("categoryStats", categoryStats);

        // 3. Produk termahal & termurah
        Product expensive = products.stream()
                .max(Comparator.comparing(Product::getPrice)).orElse(null);
        Product cheapest = products.stream()
                .min(Comparator.comparing(Product::getPrice)).orElse(null);
        model.addAttribute("expensive", expensive);
        model.addAttribute("cheapest", cheapest);

        // 4. Rata-rata harga
        double avg = products.stream().mapToDouble(Product::getPrice).average().orElse(0.0);
        model.addAttribute("avgPrice", avg);

        // 5. Jumlah produk stok < 20
        long lowStock = products.stream().filter(p -> p.getStock() < 20).count();
        model.addAttribute("lowStockCount", lowStock);

        return "statistics";
    }
}

- Template
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Statistik Gudang - Ellen</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" th:href="@{/css/style.css}">
</head>
<body class="bg-light">
    <div th:replace="~{fragments/layout :: navbar}"></div>

    <div class="container py-5" style="min-height: 75vh;">
        <h2 class="text-center mb-5 fw-bold">Statistik Gudang Produk</h2>

        <div class="row g-4">
            <div class="col-md-6">
                <div class="card h-100 border-0 shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title border-bottom pb-2">📦 Overview</h5>
                        <p>Total Produk: <b th:text="${totalProducts}">0</b></p>
                        <p class="text-danger">Stok Menipis (< 20): <b th:text="${lowStockCount}">0</b></p>
                        <p>Rata-rata Harga: <b th:text="'Rp ' + ${#numbers.formatDecimal(avgPrice, 0, 'POINT', 0, 'COMMA')}">0</b></p>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="card h-100 border-0 shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title border-bottom pb-2">💰 Produk Unggulan</h5>
                        <p>Termahal: <span th:text="${expensive?.name + ' (Rp ' + expensive?.price + ')'}">-</span></p>
                        <p>Termurah: <span th:text="${cheapest?.name + ' (Rp ' + cheapest?.price + ')'}">-</span></p>
                    </div>
                </div>
            </div>
        </div>

        <div class="card mt-4 border-0 shadow-sm">
            <div class="card-body">
                <h5 class="card-title border-bottom pb-2">📊 Rincian Per Kategori</h5>
                <table class="table table-hover">
                    <thead><tr><th>Kategori</th><th>Jumlah</th></tr></thead>
                    <tbody>
                        <tr th:each="entry : ${categoryStats}">
                            <td th:text="${entry.key}">Kategori</td>
                            <td><span class="badge bg-info text-dark" th:text="${entry.value}">0</span></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div th:replace="~{fragments/layout :: footer}"></div>
</body>
</html>