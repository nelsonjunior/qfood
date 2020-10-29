package com.github.qfood.marketplace.domain.entity;

import java.math.BigDecimal;

public class Menu {

    public Long id;

    public String name;
    public String description;
    public BigDecimal price;

    public Restaurant restaurant;

}
