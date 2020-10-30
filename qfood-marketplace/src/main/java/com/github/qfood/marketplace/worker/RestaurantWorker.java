package com.github.qfood.marketplace.worker;


import com.github.qfood.marketplace.service.RestaurantService;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import com.github.qfood.marketplace.domain.entity.Restaurant;

@ApplicationScoped
public class RestaurantWorker {

    @Inject
    RestaurantService restaurantService;

    @Incoming("restaurants")
    public void receiverCreatedRestaurant(String jsonRestaurant){
        Jsonb create = JsonbBuilder.create();
        Restaurant rest = create.fromJson(jsonRestaurant, Restaurant.class);
        System.out.println("########### Receiver message!");
        System.out.println(rest);
        restaurantService.persist(rest);
    }

}
