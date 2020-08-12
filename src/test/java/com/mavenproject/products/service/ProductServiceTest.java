package com.mavenproject.products.service;

import com.mavenproject.products.model.Product;
import com.mavenproject.products.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AssertionErrors;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

        assertNull(foundProduct);
    }

    @Test
    @DisplayName("Find all products")
    public void testFindAllProducts(){
        Product firstProduct = new Product(1, "First Product", "Product Description 1", 10, 1);
        Product secondProduct = new Product(2, "Second Product", "Product Description 2", 20, 1);

        doReturn(Arrays.asList(firstProduct, secondProduct)).when(productRepository).findAll();

        Iterable<Product> allProducts = productService.findAll();

        assertEquals(2, ((Collection<?>) allProducts).size());
    }

    @Test
    @DisplayName("Save new product successfully")
    public void testSuccessfulProductSave(){
        Product mockProduct = new Product(1, "Product 1", "Description 1", 10, 1);

        doReturn(mockProduct).when(productRepository).save(any());

        Product savedProduct = productService.save(mockProduct);

        AssertionErrors.assertNotNull("Product should not be null", savedProduct);
        assertSame("Product 1", mockProduct.getName());
        assertSame(1, savedProduct.getVersion());
    }
}
