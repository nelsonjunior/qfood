package com.github.qfood.order.domain.dto;

import java.util.List;

public class PlaceOrderDTO {

    public List<OrderItemDTO> itens;

    public RestaurantDTO restaurant;

    public String userID;

    @Override
    public String toString() {
        return "PlaceOrderDTO{" +
                "itens=" + itens +
                ", restaurant=" + restaurant +
                ", userID='" + userID + '\'' +
                '}';
    }
}
