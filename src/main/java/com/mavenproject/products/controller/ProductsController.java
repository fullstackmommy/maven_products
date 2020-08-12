package com.mavenproject.products.controller;

import com.mavenproject.products.model.Product;
import com.mavenproject.products.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class ProductsController {

    private static final Logger LOGGER = LogManager.getLogger(ProductsController.class);

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public Iterable<Product> getAllProducts(){
        return productService.findAll();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Integer id) {
        Product product = productService.findById(id);

        if(product != null) {
            try {
                return ResponseEntity
                        .ok()
                        .eTag(Integer.toString(product.getId()))
                        .location(new URI("/products/" + product.getId()))
                        .body(product);
            } catch (URISyntaxException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
