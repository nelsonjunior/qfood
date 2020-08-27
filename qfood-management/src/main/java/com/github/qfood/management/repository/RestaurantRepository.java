package com.github.qfood.management.repository;

import com.github.qfood.management.domain.entity.Restaurant;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RestaurantRepository implements PanacheRepository<Restaurant> {
}
