package com.orderflow.inventory_service.consumer;

import com.orderflow.inventory_service.event.OrderCreatedEvent;
import com.orderflow.inventory_service.service.InventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderCreatedConsumer {

    private final InventoryService inventoryService;

    public OrderCreatedConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(
            topics = "order-created",
            groupId = "inventory-service"
    )
    public void consumeOrderCreatedEvent(OrderCreatedEvent event) {

        inventoryService.processOrder(event);
    }
}