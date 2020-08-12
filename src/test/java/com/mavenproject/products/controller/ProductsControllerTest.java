package com.mavenproject.products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mavenproject.products.model.Product;
import com.mavenproject.products.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductsControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test all products found - GET /products")
    public void testAllProductsFound() throws Exception {
        Product firstProduct = new Product(1, "First Product", "First Product Description", 5, 1);
        Product secondProduct = new Product(1, "Second Product", "Second Product Description", 15, 1);

        List<Product> products = new ArrayList<>();
        products.add(firstProduct);
        products.add(secondProduct);

        doReturn(products).when(productService).findAll();

        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].name", is("First Product")))
                .andExpect(jsonPath("$[1].name", is("Second Product")));
    }

    @Test
    @DisplayName("Test product found - GET /products/1")
    public void testGetProductByIdFindsProduct() throws Exception {
        Product mockProduct = new Product(1, "Product 1", "Product Description 1", 10, 1);

        doReturn(mockProduct).when(productService).findById(mockProduct.getId());

        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/products/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Product 1")))
                .andExpect(jsonPath("$.description", is("Product Description 1")))
                .andExpect(jsonPath("$.quantity", is(10)))
                .andExpect(jsonPath("$.version", is(1)));

    }

    @Test
    @DisplayName("Add a new product - POST /products")
    public void testAddNewProduct() throws Exception {
        Product newProduct = new Product("New Product", "New Product Description", 8);
        Product mockProduct = new Product(1, "New Product", "New Product Description", 8, 1);

        doReturn(mockProduct).when(productService).save(ArgumentMatchers.any());

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/products/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Product")))
                .andExpect(jsonPath("$.quantity", is(8)))
                .andExpect(jsonPath("$.version", is(1)));
    }

    @Test
    @DisplayName("Update an existing product with success - PUT /products/1")
    public void testUpdatingProductWithSuccess() throws Exception {
        Product productToUpdate = new Product("New name", "New description", 20);
        Product mockProduct = new Product(1, "Mock product", "Mock product desc", 10, 1);

        doReturn(mockProduct).when(productService).findById(1);
        doReturn(mockProduct).when(productService).update(ArgumentMatchers.any());

        mockMvc.perform(put("/products/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.IF_MATCH, 1)
                .content(new ObjectMapper().writeValueAsString(productToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string(HttpHeaders.ETAG, "\"2\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/products/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New name")))
                .andExpect(jsonPath("$.description", is("New description")))
                .andExpect(jsonPath("$.quantity", is(20)));
    }

    @Test
    @DisplayName("Version mismatch while updating existing product - PUT /products/1")
    public void testVersionMismatchWhileUpdating() throws Exception {
        Product productToUpdate = new Product("New name", "New description", 20);
        Product mockProduct = new Product(1, "Mock product", "Mock product desc", 10, 2);

        doReturn(mockProduct).when(productService).findById(1);

        mockMvc.perform(put("/products/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.IF_MATCH, 1)
                .content(new ObjectMapper().writeValueAsString(productToUpdate)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Product not found while updating - PUT /products/1")
    public void testProductNotFoundWhileUpdating() throws Exception{
        Product productToUpdate = new Product("New name", "New description", 20);

        doReturn(null).when(productService).findById(1);

        mockMvc.perform(put("/products/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.IF_MATCH, 1)
                .content(new ObjectMapper().writeValueAsString(productToUpdate)))

                // Validate 404 NOT_FOUND received
                .andExpect(status().isNotFound());
    }

}
