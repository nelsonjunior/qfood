package com.github.qfood.order.domain.entity;

import com.github.qfood.order.domain.dto.OrderItemDTO;
import org.bson.types.Decimal128;

import java.math.BigDecimal;

public class Menu {

    public Long id;
    public String name;
    public String description;
    public Decimal128 price;

    public static Menu from(OrderItemDTO item){
        Menu menu = new Menu();
        menu.name = item.name;
        menu.description = item.description;
        menu.price = new Decimal128(item.price);
        return menu;
    }
}
