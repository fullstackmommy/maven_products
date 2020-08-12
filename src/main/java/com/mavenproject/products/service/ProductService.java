package com.mavenproject.products.service;

import com.mavenproject.products.model.Product;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private static final Logger LOGGER = LogManager.getLogger(ProductService.class);

    public Product findById(Integer id) {
        LOGGER.info("Finding product by id: {}", id);
        return new Product();
    }

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        return products;
    }
}
