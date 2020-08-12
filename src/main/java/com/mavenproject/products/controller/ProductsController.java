package com.mavenproject.products.controller;

import com.mavenproject.products.model.Product;
import com.mavenproject.products.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/products")
    public ResponseEntity<?> saveProduct(@RequestBody Product product){
        LOGGER.info("Adding new product with name:{}", product.getName());
        Product newProduct = productService.save(product);
        try {
            return ResponseEntity
                    .created(new URI("/products/" + newProduct.getId()))
                    .eTag(Integer.toString(newProduct.getVersion()))
                    .body(newProduct);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id,
                                           @RequestBody Product product,
                                           @RequestHeader("If-Match") Integer ifMatch){
        Product existingProduct = productService.findById(id);
        if (existingProduct == null){
            return ResponseEntity.notFound().build();
        } else {
            if (!existingProduct.getVersion().equals(ifMatch)){
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } else {
                LOGGER.info("Updating product with name:{}", existingProduct.getName());

                existingProduct.setName(product.getName());
                existingProduct.setDescription(product.getDescription());
                existingProduct.setQuantity(product.getQuantity());
                existingProduct.setVersion(existingProduct.getVersion() + 1);

                try {
                    existingProduct = productService.update(existingProduct);
                    return ResponseEntity
                            .ok()
                            .eTag(Integer.toString(existingProduct.getVersion()))
                            .location(new URI("/products/" + existingProduct.getId()))
                            .body(existingProduct);
                } catch (URISyntaxException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
        }
    }
}
