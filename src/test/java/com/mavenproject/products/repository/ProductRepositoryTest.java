package com.mavenproject.products.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mavenproject.products.model.Product;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private static File DATA_JSON = Paths.get("src", "test", "resources", "products.json").toFile();

    @BeforeEach
    public void setup() throws IOException {
        // Deserialize products from JSON file to Product array
        Product[] products = new ObjectMapper().readValue(DATA_JSON, Product[].class);

        // Save each product to database
        Arrays.stream(products).forEach(productRepository::save);
    }

    @AfterEach
    public void cleanup(){
        // Cleanup the database after each test
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("Test product not found with non-existing id")
    public void testProductNotFoundForNonExistingId() {
        Product retrievedProduct = productRepository.findProductById(100);

        assertNull(retrievedProduct, "Product with id 100 should not exist");
    }

    @Test
    @DisplayName("Test product saved successfully")
    public void testProductSavedSuccessfully() {
        Product newProduct = new Product("New Product", "New Product Description", 8);

        Product savedProduct = productRepository.save(newProduct);

        assertNotNull(savedProduct, "Product should be saved");
        assertNotNull(savedProduct.getId(), "Product should have an id when saved");
        assertEquals(newProduct.getName(), savedProduct.getName());
    }

    @Test
    @DisplayName("Test product updated successfully")
    public void testProductUpdatedSuccessfully() {
        Product productToUpdate = new Product(1, "Updated Product", "New Product Description", 20, 2);

        Product updatedProduct = productRepository.save(productToUpdate);

        assertEquals(productToUpdate.getName(), updatedProduct.getName());
        assertEquals(2, updatedProduct.getVersion());
        assertEquals(20, updatedProduct.getQuantity());
    }
}
