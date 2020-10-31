package com.github.qfood.marketplace.presentation;

import com.github.qfood.marketplace.domain.dto.MenuDTO;
import com.github.qfood.marketplace.domain.dto.OrderItemDTO;
import com.github.qfood.marketplace.domain.dto.PlaceOrderDTO;
import com.github.qfood.marketplace.domain.dto.RestaurantDTO;
import com.github.qfood.marketplace.domain.entity.OrderItem;
import com.github.qfood.marketplace.service.CartService;
import com.github.qfood.marketplace.service.MenuService;
import com.github.qfood.marketplace.service.RestaurantService;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path(Paths.CARTS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {

    private static final String USER_ID = "qfood";

    @Inject
    @Channel("orders")
    Emitter<PlaceOrderDTO> emitterOrder;

    @Inject
    CartService cartService;

    @Inject
    MenuService menuService;

    @Inject
    RestaurantService restaurantService;

    @GET
    public Uni<List<OrderItem>> findCart() {
        return cartService.findCart(USER_ID);
    }

    @POST
    @Path("/menu/{idMenu}")
    public Uni<Long> addMenu(@PathParam("idMenu") Long idMenu) {
        //poderia retornar o pedido atual
        OrderItem orderItem = new OrderItem();
        orderItem.user = USER_ID;
        orderItem.menuID = idMenu;
        return cartService.save(orderItem);
    }

    @POST
    @Path("/place-order")
    public Uni<Boolean> finish() {

        PlaceOrderDTO pedido = new PlaceOrderDTO();
        pedido.userID = USER_ID;
        List<OrderItem> pratoCarrinho = cartService.findCart(USER_ID).await().indefinitely();

        //Utilizar mapstruts
        List<OrderItemDTO> pratos = pratoCarrinho.stream().map(pc -> from(pc)).collect(Collectors.toList());
        pedido.itens = pratos;

        RestaurantDTO restaurante = new RestaurantDTO();
        restaurante.id = 1L;
        restaurante.name = "nome restaurante";
        pedido.restaurant = restaurante;

        emitterOrder.send(pedido);

        return cartService.delete(USER_ID);
    }

    private OrderItemDTO from(OrderItem orderItem) {
        MenuDTO dto = menuService.findById(orderItem.menuID).await().indefinitely();
        return new OrderItemDTO(dto.name, dto.description, dto.price);
    }
}
