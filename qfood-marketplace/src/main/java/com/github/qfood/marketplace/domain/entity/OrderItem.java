package com.github.qfood.marketplace.domain.entity;

import io.vertx.mutiny.sqlclient.Row;

public class OrderItem {

    public String user;

    public Long menuID;

    public static OrderItem from(Row row) {
        OrderItem item = new OrderItem();
        item.user = row.getString("userid");
        item.menuID = row.getLong("menuid");
        return item;
    }
}
