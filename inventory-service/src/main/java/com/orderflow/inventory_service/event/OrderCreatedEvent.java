package com.orderflow.inventory_service.event;

public class OrderCreatedEvent {

    private Long orderId;
    private String productName;
    private Integer quantity;
    private Double price;

    public OrderCreatedEvent(
            Long orderId,
            String productName,
            Integer quantity,
            Double price) {

        this.orderId = orderId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }
    public OrderCreatedEvent() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }
}