package com.example.app;


import com.example.app.controller.OrderController;
import com.example.app.model.Order;
import com.example.app.model.Client;
import com.example.app.model.Product;
import com.example.app.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private Order order;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        Client client = Client.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();

        List<Product> products = List.of(
                Product.builder().id(1L).name("Product 1").price(50.00).build(),
                Product.builder().id(2L).name("Product 2").price(50.00).build()
        );

        order = Order.builder()
                .id(1L)
                .client(client)
                .products(products)
                .orderDate("2025-01-21")
                .build();
    }

    @Test
    void testGetAllOrders() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(order));
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].orderDate").value("2025-01-21"));
    }

    @Test
    void testGetOrderById() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderDate").value("2025-01-21"));
    }

    @Test
    void testCreateOrder() throws Exception {
        when(orderService.createOrder(any(Order.class))).thenReturn(order);
        mockMvc.perform(post("/api/orders")
                .contentType("application/json")
                .content("""
                    {
                        "clientId": 1,
                        "productIds": [1, 2],
                        "orderDate": "2025-01-21"
                    }
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderDate").value("2025-01-21"));
    }

    @Test
    void testUpdateOrder() throws Exception {
        when(orderService.updateOrder(eq(1L), any(Order.class))).thenReturn(order);
        mockMvc.perform(put("/api/orders/1")
                .contentType("application/json")
                .content("""
                    {
                        "clientId": 1,
                        "productIds": [1, 2],
                        "orderDate": "2025-01-21"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderDate").value("2025-01-21"));
    }

    @Test
    void testDeleteOrder() throws Exception {
        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());
        verify(orderService, times(1)).deleteOrder(1L);
    }
}
