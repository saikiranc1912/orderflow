package com.orderflow.order_service.service;

import com.orderflow.order_service.dto.OrderRequest;
import com.orderflow.order_service.dto.OrderResponse;
import com.orderflow.order_service.entity.Order;
import com.orderflow.order_service.exception.OrderNotFoundException;
import com.orderflow.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return mapToResponse(order);
    }

    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        existingOrder.setProductName(orderRequest.getProductName());
        existingOrder.setQuantity(orderRequest.getQuantity());
        existingOrder.setPrice(orderRequest.getPrice());

        Order updatedOrder = orderRepository.save(existingOrder);

        return mapToResponse(updatedOrder);
    }

    public void deleteOrder(Long id) {

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        orderRepository.delete(existingOrder);
    }

    private OrderResponse mapToResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice()
        );
    }
}