package com.github.qfood.order.worker;

import com.github.qfood.order.domain.dto.PlaceOrderDTO;
import com.github.qfood.order.service.PlaceOrderService;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class PlaceOrderWorker {

    private static final Logger LOG = Logger.getLogger(PlaceOrderWorker.class);

    @Inject
    PlaceOrderService placeOrderService;

    @Incoming("orders")
    @Blocking
    @Transactional
    public void receiverNewPlaceOrder(PlaceOrderDTO orderDTO){
        LOG.info("#### Receiver New Place Order");
        placeOrderService.save(orderDTO);
    }

}
