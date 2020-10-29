package com.github.qfood.marketplace.repository;

import com.github.qfood.marketplace.domain.dto.MenuDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class MenuRepository {

    @Inject
    PgPool pgPool;

    public Multi<MenuDTO> findAll() {
        Uni<RowSet<Row>> preparedQuery = pgPool.query("select * from menu").execute();
        return unitToMulti(preparedQuery);
    }

    public Multi<MenuDTO> findAllByIdRestaurant(Long idRestaurant) {
        Uni<RowSet<Row>> preparedQuery = pgPool
                .preparedQuery("SELECT * FROM menu where menu.restaurant_id = $1 ORDER BY name ASC").execute(
                        Tuple.of(idRestaurant));
        return unitToMulti(preparedQuery);
    }

    public Uni<MenuDTO> findById(Long id) {
        return pgPool.preparedQuery("SELECT * FROM menu WHERE id = $1").execute(Tuple.of(id))
                .map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? MenuDTO.from(iterator.next()) : null);
    }

    private Multi<MenuDTO> unitToMulti(Uni<RowSet<Row>> queryResult) {
        return queryResult.onItem()
                .produceMulti(set -> Multi.createFrom().items(() -> {
                    return StreamSupport.stream(set.spliterator(), false);
                }))
                .onItem().apply(MenuDTO::from);
    }
}
