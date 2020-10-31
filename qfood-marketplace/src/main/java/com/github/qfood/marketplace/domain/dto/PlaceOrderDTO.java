package com.github.qfood.marketplace.domain.dto;

import com.github.qfood.marketplace.domain.entity.Restaurant;

import java.util.List;

public class PlaceOrderDTO {

    public List<OrderItemDTO> itens;

    public RestaurantDTO restaurant;

    public String userID;
}
