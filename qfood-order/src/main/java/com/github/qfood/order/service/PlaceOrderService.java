package com.github.qfood.order.service;

import com.github.qfood.order.domain.dto.PlaceOrderDTO;
import com.github.qfood.order.domain.entity.Menu;
import com.github.qfood.order.domain.entity.Order;
import com.github.qfood.order.domain.entity.Restaurant;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Collectors;

@ApplicationScoped
public class PlaceOrderService {

    public void save(PlaceOrderDTO placeOrderDTO){
        Order order = new Order();
        order.user = placeOrderDTO.userID;
        order.menus = placeOrderDTO.itens.stream().map(Menu::from).collect(Collectors.toList());
        order.restaurant = Restaurant.from(placeOrderDTO.restaurant);
        order.persist();
    }

}
