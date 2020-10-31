package com.github.qfood.order.domain.dto;

import java.math.BigDecimal;

public class OrderItemDTO {

    public String name;

    public String description;

    public BigDecimal price;

    public OrderItemDTO(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
