package com.orderflow.inventory_service.exception;

public class InsufficientInventoryException extends RuntimeException {

    public InsufficientInventoryException(
            String productName,
            Integer availableQuantity,
            Integer requestedQuantity) {

        super(
                "Insufficient inventory for product: "
                        + productName
                        + ". Available: "
                        + availableQuantity
                        + ", Requested: "
                        + requestedQuantity
        );
    }
}