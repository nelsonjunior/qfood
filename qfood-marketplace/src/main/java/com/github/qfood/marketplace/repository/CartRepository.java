package com.github.qfood.marketplace.repository;

import com.github.qfood.marketplace.domain.entity.OrderItem;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CartRepository {

    @Inject
    PgPool pgPool;

    public Uni<Long> save(OrderItem orderItem) {
        return pgPool.preparedQuery("INSERT INTO menu_item (userID, menuID) VALUES ($1, $2) RETURNING (menuid)")
                .execute(Tuple.of(orderItem.user, orderItem.menuID))
                .map(pgRowSet -> pgRowSet.iterator().next().getLong("menuid"));
    }

    public Uni<List<OrderItem>> findCart(String userID) {
        return pgPool.preparedQuery("select * from menu_item where userid = $1 ")
                .execute(Tuple.of(userID))
                .map(pgRowSet -> {
                    List<OrderItem> list = new ArrayList<>(pgRowSet.size());
                    for (Row row : pgRowSet) {
                        list.add(OrderItem.from(row));
                    }
                    return list;
                });
    }

    public Uni<Boolean> delete(String userID) {
        return pgPool.preparedQuery("DELETE FROM menu_item WHERE userid = $1")
                .execute(Tuple.of(userID))
                .map(pgRowSet -> pgRowSet.rowCount() == 1);

    }

}
