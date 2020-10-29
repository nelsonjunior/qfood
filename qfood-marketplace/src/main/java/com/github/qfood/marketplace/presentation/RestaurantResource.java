package com.github.qfood.marketplace.presentation;

import com.github.qfood.marketplace.domain.dto.MenuDTO;
import com.github.qfood.marketplace.service.MenuService;
import io.smallrye.mutiny.Multi;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(Paths.RESTAURANTS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestaurantResource {

    @Inject
    MenuService menuService;

    @GET
    @Path("{idRestaurant}/menus")
    public Multi<MenuDTO> findAllMenus(@PathParam("idRestaurant") Long idRestaurant) {
        return menuService.findAllByIdRestaurant(idRestaurant);
    }

}
