package com.mavenproject.products.repository;

import com.mavenproject.products.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    Product findProductById(Integer id);
    Product findProductByIdAndName(Integer id, String name);
}
