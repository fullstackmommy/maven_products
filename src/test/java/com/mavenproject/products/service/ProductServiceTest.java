package com.mavenproject.products.service;

import com.mavenproject.products.model.Product;
import com.mavenproject.products.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    @DisplayName("Find product by id with success")
    public void testFindProductById() {
        Product mockProduct = new Product(1, "Product 1", "Product Description 1", 10, 1);

        doReturn(mockProduct).when(productRepository).findProductById(1);

        Product foundProduct = productService.findById(1);

        assertNotNull(foundProduct);
        assertSame("Product 1", foundProduct.getName());
    }

    @Test
    @DisplayName("Fail to find product with id")
    public void testFailToFindProductById(){
        doReturn(null).when(productRepository).findProductById(1);

        Product foundProduct = productService.findById(1);

        Assertions.assertNull(foundProduct);
    }
}
