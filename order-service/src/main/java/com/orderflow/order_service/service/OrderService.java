package com.orderflow.order_service.service;

import com.orderflow.order_service.dto.OrderRequest;
import com.orderflow.order_service.dto.OrderResponse;
import com.orderflow.order_service.entity.Order;
import com.orderflow.order_service.exception.OrderNotFoundException;
import com.orderflow.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class OrderService {

    private static final Logger logger =
            LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(Order order) {

        logger.info("Creating order for product: {}", order.getProductName());

        Order savedOrder = orderRepository.save(order);

        logger.info("Order created successfully with id: {}", savedOrder.getId());

        return mapToResponse(savedOrder);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OrderResponse getOrderById(Long id) {

        logger.info("Fetching order with id: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Order not found with id: {}", id);
                    return new OrderNotFoundException(id);
                });

        return mapToResponse(order);
    }

    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {

        logger.info("Updating order with id: {}", id);

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Order not found for update with id: {}", id);
                    return new OrderNotFoundException(id);
                });

        existingOrder.setProductName(orderRequest.getProductName());
        existingOrder.setQuantity(orderRequest.getQuantity());
        existingOrder.setPrice(orderRequest.getPrice());

        Order updatedOrder = orderRepository.save(existingOrder);

        logger.info("Order updated successfully with id: {}", updatedOrder.getId());

        return mapToResponse(updatedOrder);
    }

    public void deleteOrder(Long id) {

        logger.info("Deleting order with id: {}", id);

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Order not found for deletion with id: {}", id);
                    return new OrderNotFoundException(id);
                });

        orderRepository.delete(existingOrder);

        logger.info("Order deleted successfully with id: {}", id);
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