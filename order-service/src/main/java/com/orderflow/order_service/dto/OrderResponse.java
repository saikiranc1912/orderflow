package com.orderflow.order_service.dto;

public class OrderResponse {

    private Long id;
    private String productName;
    private Integer quantity;
    private Double price;

    public OrderResponse() {
    }

    public OrderResponse(Long id, String productName, Integer quantity, Double price) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
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