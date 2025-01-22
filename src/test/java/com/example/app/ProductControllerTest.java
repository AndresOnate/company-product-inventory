package com.example.app;

import com.example.app.controller.ProductController;
import com.example.app.model.Product;
import com.example.app.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        product = new Product(1L, "P001", "Product Name", "Description", 100.00, 10, null, null, null);
    }

    @Test
    void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(product));
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product Name"))
                .andExpect(jsonPath("$[0].price").value(100.00));
    }

    @Test
    void testGetProductById() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product Name"))
                .andExpect(jsonPath("$.price").value(100.00));
    }

    @Test
    void testCreateProduct() throws Exception {
        when(productService.createProduct(any(Product.class))).thenReturn(product);
        mockMvc.perform(post("/api/products")
                .contentType("application/json")
                .content("{ \"code\": \"P001\", \"name\": \"Product Name\", \"description\": \"Description\", \"price\": 100.00, \"stock\": 10 }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Product Name"))
                .andExpect(jsonPath("$.price").value(100.00));
    }

    @Test
    void testUpdateProduct() throws Exception {
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(product);
        mockMvc.perform(put("/api/products/1")
                .contentType("application/json")
                .content("{ \"code\": \"P001\", \"name\": \"Product Name\", \"description\": \"Description\", \"price\": 100.00, \"stock\": 10 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product Name"))
                .andExpect(jsonPath("$.price").value(100.00));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
        verify(productService, times(1)).deleteProduct(1L);
    }
}
