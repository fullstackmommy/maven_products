package com.mavenproject.products.service;

import com.mavenproject.products.model.Product;
import com.mavenproject.products.repository.ProductRepository;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private static final Logger LOGGER = LogManager.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    public Product findById(Integer id) {
        LOGGER.info("Finding product by id: {}", id);
        return productRepository.findProductById(id);
    }

    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    public Product save(Product product) {
        LOGGER.info("Saving new product with name:{}", product.getName());
        return product;
    }

    public Product update(Product product) {
        LOGGER.info("Updating product with id:{}", product.getId());
        product.setVersion(1);
        return productRepository.save(product);
    }

    public void delete(Integer id) {
        LOGGER.info("Deleting product with id:{}", id);
    }
}
