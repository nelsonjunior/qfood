package com.github.qfood.marketplace.repository;

import com.github.qfood.marketplace.domain.entity.Restaurant;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class RestaurantRepository {

    @Inject
    PgPool pgPool;

    public void persist(Restaurant restaurant) {
        pgPool.preparedQuery("insert into location (id, latitude, longitude) values ($1, $2, $3)").execute(
                Tuple.of(restaurant.location.id, restaurant.location.latitude, restaurant.location.longitude)).await().indefinitely();

        pgPool.preparedQuery("insert into restaurant (id, name, location_id) values ($1, $2, $3)").execute(
                Tuple.of(restaurant.id, restaurant.name, restaurant.location.id)).await().indefinitely();

    }
}
