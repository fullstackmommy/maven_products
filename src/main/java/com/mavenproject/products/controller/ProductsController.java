package com.mavenproject.products.controller;

import com.mavenproject.products.model.Product;
import com.mavenproject.products.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsController {

    private static final Logger LOGGER = LogManager.getLogger(ProductsController.class);

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public Iterable<Product> getAllProducts(){
        return productService.findAll();
    }

}
