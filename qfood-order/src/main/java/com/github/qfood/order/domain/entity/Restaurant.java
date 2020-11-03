package com.github.qfood.order.domain.entity;

import com.github.qfood.order.domain.dto.RestaurantDTO;

import java.util.Date;

public class Restaurant {

    public Long id;

    public String name;

    public static Restaurant from(RestaurantDTO restaurant) {
        Restaurant entity = new Restaurant();
        entity.id = restaurant.id;
        entity.name = restaurant.name;
        return entity;
    }
}
