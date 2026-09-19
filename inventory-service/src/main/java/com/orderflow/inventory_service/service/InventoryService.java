package com.orderflow.inventory_service.service;

import com.orderflow.inventory_service.entity.Inventory;
import com.orderflow.inventory_service.event.OrderCreatedEvent;
import com.orderflow.inventory_service.exception.InsufficientInventoryException;
import com.orderflow.inventory_service.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private static final Logger logger =
            LoggerFactory.getLogger(InventoryService.class);

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void processOrder(OrderCreatedEvent event) {

        logger.info(
                "Processing inventory for order id: {}",
                event.getOrderId()
        );

        Inventory inventory = inventoryRepository
                .findByProductName(event.getProductName())
                .orElse(null);

        if (inventory == null) {
            logger.warn(
                    "No inventory found for product: {}",
                    event.getProductName()
            );
            return;
        }

        if (inventory.getAvailableQuantity() < event.getQuantity()) {

            logger.warn(
                    "Insufficient inventory for product: {}. Available: {}, Requested: {}",
                    event.getProductName(),
                    inventory.getAvailableQuantity(),
                    event.getQuantity()
            );

            throw new InsufficientInventoryException(
                    event.getProductName(),
                    inventory.getAvailableQuantity(),
                    event.getQuantity()
            );
        }

        int updatedQuantity =
                inventory.getAvailableQuantity() - event.getQuantity();

        inventory.setAvailableQuantity(updatedQuantity);

        inventoryRepository.save(inventory);

        logger.info(
                "Inventory updated for product: {}. Remaining quantity: {}",
                event.getProductName(),
                updatedQuantity
        );
    }
}