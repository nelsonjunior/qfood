package com.github.qfood.order.presentation;

import com.github.qfood.order.domain.entity.Location;
import com.github.qfood.order.domain.entity.Order;
import io.vertx.core.Vertx;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.mutiny.core.eventbus.EventBus;
import org.bson.types.ObjectId;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    Vertx vertx;

    @Inject
    EventBus eventBus;

    void startup(@Observes Router router) {
        router.route("/locations*").handler(locationHandler());
    }

    private SockJSHandler locationHandler() {
        SockJSHandler handler = SockJSHandler.create(vertx);
        PermittedOptions permitted = new PermittedOptions();
        permitted.setAddress("newLocation");

        //Alterado na versoa 1.9
        //        BridgeOptions bridgeOptions = new BridgeOptions().addOutboundPermitted(permitted);
        //        handler.bridge(bridgeOptions);

        SockJSBridgeOptions bridgeOptions = new SockJSBridgeOptions().addOutboundPermitted(permitted);
        handler.bridge(bridgeOptions);
        return handler;
    }


    @GET
    public List<Order> listAllOrders() {
        return Order.listAll();
    }

    @GET
    @Path("{idOrder}")
    public Order getOrderById(@PathParam("idOrder") String idOrder) {
        Order order = Order.findById(new ObjectId(idOrder));
        return order;
    }

    @POST
    @Path("{idOrder}/location")
    public Order newLocation(@PathParam("idOrder") String idOrder, Location location) {
        Order order = Order.findById(new ObjectId(idOrder));
        order.setLocation(location);
        order.persistOrUpdate();
        eventBus.sendAndForget("newLocation", JsonbBuilder.create().toJson(location));
        return order;
    }
}
