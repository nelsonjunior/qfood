package com.github.qfood.marketplace.domain.entity;

import com.github.qfood.marketplace.domain.dto.MenuDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

import java.math.BigDecimal;
import java.util.stream.StreamSupport;

public class Menu {

    public Long id;

    public String name;
    public String description;
    public BigDecimal price;

    public Restaurant restaurant;

    public static Multi<MenuDTO> findAll(PgPool pgPool) {
        Uni<RowSet<Row>> preparedQuery = pgPool.query("select * from menu").execute();
        return unitToMulti(preparedQuery);
    }

    public static Multi<MenuDTO> findAll(PgPool client, Long idRestaurante) {
        Uni<RowSet<Row>> preparedQuery = client
                .preparedQuery("SELECT * FROM menu where menu.restaurant_id = $1 ORDER BY name ASC").execute(
                        Tuple.of(idRestaurante));
        return unitToMulti(preparedQuery);
    }

    private static Multi<MenuDTO> unitToMulti(Uni<RowSet<Row>> queryResult) {
        return queryResult.onItem()
                .produceMulti(set -> Multi.createFrom().items(() -> {
                    return StreamSupport.stream(set.spliterator(), false);
                }))
                .onItem().apply(MenuDTO::from);
    }

    public static Uni<MenuDTO> findById(PgPool client, Long id) {
        return client.preparedQuery("SELECT * FROM menu WHERE id = $1").execute(Tuple.of(id))
                .map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? MenuDTO.from(iterator.next()) : null);
    }
}
