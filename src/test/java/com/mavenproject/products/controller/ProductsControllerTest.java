package com.mavenproject.products.controller;

import com.mavenproject.products.model.Product;
import com.mavenproject.products.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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


}
