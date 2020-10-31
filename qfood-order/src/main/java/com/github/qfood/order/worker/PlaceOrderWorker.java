package com.github.qfood.order.worker;

import com.github.qfood.order.domain.dto.PlaceOrderDTO;
import com.github.qfood.order.service.PlaceOrderService;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PlaceOrderWorker {

    @Inject
    PlaceOrderService placeOrderService;

    @Incoming("orders")
    public void receiverNewPlaceOrder(PlaceOrderDTO orderDTO){
        System.out.println("#### Receiver New Place Order");
        System.out.println(orderDTO);

        placeOrderService.save(orderDTO);
    }

}
