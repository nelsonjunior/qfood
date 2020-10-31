package com.github.qfood.marketplace.service;

import com.github.qfood.marketplace.domain.entity.OrderItem;
import com.github.qfood.marketplace.repository.CartRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CartService {

    @Inject
    CartRepository cartRepository;

    public Uni<Long> save(OrderItem orderItem) {
        return cartRepository.save(orderItem);
    }

    public Uni<List<OrderItem>> findCart(String userID) {
        return cartRepository.findCart(userID);
    }

    public Uni<Boolean> delete(String userID) {
        return cartRepository.delete(userID);
    }

}
