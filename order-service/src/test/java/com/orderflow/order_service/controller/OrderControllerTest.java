package com.orderflow.order_service.controller;

import com.orderflow.order_service.dto.OrderResponse;
import com.orderflow.order_service.exception.OrderNotFoundException;
import com.orderflow.order_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    void createOrder_shouldReturnCreatedOrder() throws Exception {

        OrderResponse response = new OrderResponse(
                1L,
                "Laptop",
                2,
                1299.99
        );

        when(orderService.createOrder(any()))
                .thenReturn(response);

        String requestBody = """
                {
                    "productName": "Laptop",
                    "quantity": 2,
                    "price": 1299.99
                }
                """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productName").value("Laptop"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.price").value(1299.99));
    }

    @Test
    void createOrder_shouldReturnBadRequestForInvalidInput() throws Exception {

        String requestBody = """
            {
                "productName": "",
                "quantity": 0,
                "price": -10
            }
            """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status")
                        .value(400))
                .andExpect(jsonPath("$.message")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.errors.productName")
                        .value("Product name is required"))
                .andExpect(jsonPath("$.errors.quantity")
                        .value("Quantity must be at least 1"))
                .andExpect(jsonPath("$.errors.price")
                        .value("Price must be greater than 0"));
    }
    @Test
    void getOrderById_shouldReturnOrder() throws Exception {

        OrderResponse response = new OrderResponse(
                1L,
                "Laptop",
                2,
                1299.99
        );

        when(orderService.getOrderById(1L))
                .thenReturn(response);

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/orders/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productName").value("Laptop"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.price").value(1299.99));
    }

    @Test
    void getAllOrders_shouldReturnAllOrders() throws Exception {

        java.util.List<OrderResponse> responses = java.util.List.of(
                new OrderResponse(1L, "Laptop", 2, 1299.99),
                new OrderResponse(2L, "Mouse", 3, 29.99)
        );

        when(orderService.getAllOrders())
                .thenReturn(responses);

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/orders")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].productName").value("Laptop"))
                .andExpect(jsonPath("$[1].productName").value("Mouse"));
    }

    @Test
    void updateOrder_shouldReturnUpdatedOrder() throws Exception {

        OrderResponse response = new OrderResponse(
                1L,
                "Gaming Laptop",
                2,
                1500.00
        );

        when(orderService.updateOrder(
                org.mockito.ArgumentMatchers.eq(1L),
                any()
        )).thenReturn(response);

        String requestBody = """
            {
                "productName": "Gaming Laptop",
                "quantity": 2,
                "price": 1500.00
            }
            """;

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/api/orders/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productName").value("Gaming Laptop"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.price").value(1500.00));
    }

    @Test
    void deleteOrder_shouldReturnNoContent() throws Exception {

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/orders/1")
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void getOrderById_shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {

        when(orderService.getOrderById(99L))
                .thenThrow(new OrderNotFoundException(99L));

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .get("/api/orders/99")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Order not found with id: 99"));
    }
}