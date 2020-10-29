package com.github.qfood.marketplace.domain.entity;

import java.util.Date;

public class Restaurant {

    public Long id;

    public String owner;
    public String documentID;
    public String name;

    public Location location;

    public Date dateCreatedAt;

    public Date dataUpdatedAt;
}
