package com.github.qfood.order.domain.entity;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;

import java.util.List;

@MongoEntity(collection = "order", database = "order")
public class Order extends PanacheMongoEntity {

    public String user;

    public List<Menu> menus;

    public Restaurant restaurant;

    public String deliveryman;

    public Location location;
}
