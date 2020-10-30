package com.github.qfood.marketplace.service;

import com.github.qfood.marketplace.domain.entity.Restaurant;
import com.github.qfood.marketplace.repository.RestaurantRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class RestaurantService {

    @Inject
    RestaurantRepository restaurantRepository;

    public void persist(Restaurant restaurant){
        restaurantRepository.persist(restaurant);
    }

}
