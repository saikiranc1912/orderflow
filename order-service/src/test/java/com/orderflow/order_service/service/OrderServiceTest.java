package com.orderflow.order_service.service;

import com.orderflow.order_service.dto.OrderRequest;
import com.orderflow.order_service.entity.Order;
import com.orderflow.order_service.dto.OrderResponse;
import com.orderflow.order_service.repository.OrderRepository;
import com.orderflow.order_service.service.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.orderflow.order_service.exception.OrderNotFoundException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_shouldSaveAndReturnOrderResponse() {

        Order order = new Order();
        order.setProductName("Laptop");
        order.setQuantity(2);
        order.setPrice(1299.99);

        Order savedOrder = new Order();
        savedOrder.setProductName("Laptop");
        savedOrder.setQuantity(2);
        savedOrder.setPrice(1299.99);

        when(orderRepository.save(order)).thenReturn(savedOrder);

        OrderResponse response = orderService.createOrder(order);

        verify(kafkaProducerService).publishOrderCreatedEvent(
                org.mockito.ArgumentMatchers.any()
        );

        assertEquals("Laptop", response.getProductName());
        assertEquals(2, response.getQuantity());
        assertEquals(1299.99, response.getPrice());
    }

    @Test
    void getOrderById_shouldReturnOrderResponse() {

        Order order = new Order();
        order.setProductName("Laptop");
        order.setQuantity(2);
        order.setPrice(1299.99);

        when(orderRepository.findById(1L))
                .thenReturn(java.util.Optional.of(order));

        OrderResponse response = orderService.getOrderById(1L);

        assertNotNull(response);
        assertEquals("Laptop", response.getProductName());
        assertEquals(2, response.getQuantity());
        assertEquals(1299.99, response.getPrice());
    }

    @Test
    void getOrderById_shouldThrowExceptionWhenOrderDoesNotExist() {

        when(orderRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.getOrderById(99L)
        );

        assertEquals("Order not found with id: 99", exception.getMessage());
    }

    @Test
    void getAllOrders_shouldReturnAllOrders() {

        Order order1 = new Order();
        order1.setProductName("Laptop");
        order1.setQuantity(2);
        order1.setPrice(1299.99);

        Order order2 = new Order();
        order2.setProductName("Mouse");
        order2.setQuantity(3);
        order2.setPrice(29.99);

        when(orderRepository.findAll())
                .thenReturn(java.util.List.of(order1, order2));

        java.util.List<OrderResponse> responses = orderService.getAllOrders();

        assertEquals(2, responses.size());
        assertEquals("Laptop", responses.get(0).getProductName());
        assertEquals("Mouse", responses.get(1).getProductName());
    }

    @Test
    void updateOrder_shouldUpdateAndReturnOrderResponse() {

        Order existingOrder = new Order();
        existingOrder.setProductName("Laptop");
        existingOrder.setQuantity(1);
        existingOrder.setPrice(1000.00);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProductName("Gaming Laptop");
        orderRequest.setQuantity(2);
        orderRequest.setPrice(1500.00);

        when(orderRepository.findById(1L))
                .thenReturn(java.util.Optional.of(existingOrder));

        when(orderRepository.save(existingOrder))
                .thenReturn(existingOrder);

        OrderResponse response = orderService.updateOrder(1L, orderRequest);

        assertEquals("Gaming Laptop", response.getProductName());
        assertEquals(2, response.getQuantity());
        assertEquals(1500.00, response.getPrice());
    }

    @Test
    void updateOrder_shouldThrowExceptionWhenOrderDoesNotExist() {

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProductName("Gaming Laptop");
        orderRequest.setQuantity(2);
        orderRequest.setPrice(1500.00);

        when(orderRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.updateOrder(99L, orderRequest)
        );

        assertEquals("Order not found with id: 99", exception.getMessage());
    }

    @Test
    void deleteOrder_shouldDeleteExistingOrder() {

        Order existingOrder = new Order();
        existingOrder.setProductName("Laptop");
        existingOrder.setQuantity(2);
        existingOrder.setPrice(1299.99);

        when(orderRepository.findById(1L))
                .thenReturn(java.util.Optional.of(existingOrder));

        orderService.deleteOrder(1L);

        org.mockito.Mockito.verify(orderRepository)
                .delete(existingOrder);
    }

    @Test
    void deleteOrder_shouldThrowExceptionWhenOrderDoesNotExist() {

        when(orderRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.deleteOrder(99L)
        );

        assertEquals("Order not found with id: 99", exception.getMessage());
    }
}