package com.example.nutrismart.data.datasource

import com.example.nutrismart.domain.model.Product

object TunisianProductDataSource {
    val products = listOf(
        // Produce
        Product("1", "Tomato (الطماطم)", 1.2, "kg", 18, 0.9, 3.9, 0.2, "Produce"),
        Product("2", "Potato (البطاطا)", 1.5, "kg", 77, 2.0, 17.0, 0.1, "Produce"),
        Product("3", "Onion (البصل)", 1.0, "kg", 40, 1.1, 9.3, 0.1, "Produce"),
        Product("4", "Garlic (الثوم)", 5.0, "kg", 149, 6.4, 33.0, 0.5, "Produce"),
        Product("5", "Carrot (الجزر)", 1.3, "kg", 41, 0.9, 9.6, 0.2, "Produce"),
        Product("6", "Cucumber (الخيار)", 1.8, "kg", 15, 0.7, 3.6, 0.1, "Produce"),
        Product("7", "Green Pepper (الفلفل)", 2.5, "kg", 20, 0.9, 4.6, 0.2, "Produce"),
        Product("8", "Lemon (القارص)", 3.0, "kg", 29, 1.1, 9.0, 0.3, "Produce"),
        
        // Proteins
        Product("101", "Chicken Breast (صدر دجاج)", 16.5, "kg", 165, 31.0, 0.0, 3.6, "Proteins"),
        Product("102", "Beef (لحم بقري)", 32.0, "kg", 250, 26.0, 0.0, 15.0, "Proteins"),
        Product("103", "Eggs (عظم)", 1.4, "unit", 155, 13.0, 1.1, 11.0, "Proteins"), // price for 4 eggs approx
        Product("104", "Tuna (تن)", 4.5, "unit", 132, 28.0, 0.0, 1.0, "Proteins"), // per can
        
        // Dairy
        Product("201", "Milk (حليب)", 1.35, "L", 42, 3.4, 5.0, 1.0, "Dairy"),
        Product("202", "Yogurt (ياغورت)", 0.5, "unit", 59, 10.0, 3.6, 0.4, "Dairy"),
        Product("203", "Cheese (جبن مرحي)", 3.5, "unit", 402, 25.0, 1.3, 33.0, "Dairy"),
        
        // Grains/Pantry
        Product("301", "Couscous (كسكسي)", 0.95, "kg", 112, 3.8, 23.0, 0.2, "Grains/Pantry"),
        Product("302", "Pasta (مقرونة)", 0.8, "unit", 131, 5.0, 25.0, 1.1, "Grains/Pantry"), // 500g bag
        Product("303", "Bread (خبزة طابونة)", 0.5, "unit", 265, 9.0, 49.0, 3.2, "Grains/Pantry"),
        Product("304", "Olive Oil (زيت زيتون)", 25.0, "L", 884, 0.0, 0.0, 100.0, "Grains/Pantry"),
        Product("305", "Harissa (هريسة)", 1.2, "unit", 100, 2.0, 10.0, 6.0, "Grains/Pantry")
    )
}
