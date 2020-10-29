package com.github.qfood.marketplace.domain.dto;

import com.github.qfood.marketplace.domain.entity.Restaurant;
import io.vertx.mutiny.sqlclient.Row;

import java.math.BigDecimal;

public class MenuDTO {

    public Long id;
    public String name;
    public String description;
    public BigDecimal price;

    public static MenuDTO from(Row row) {
        MenuDTO dto = new MenuDTO();
        dto.description = row.getString("description");
        dto.name = row.getString("name");
        dto.id = row.getLong("id");
        dto.price = row.getBigDecimal("price");
        return dto;
    }
}
