package com.github.qfood.management.presentation;

import com.github.qfood.management.domain.entity.Menu;
import com.github.qfood.management.domain.entity.Restaurant;
import com.github.qfood.management.exception.ServiceException;
import com.github.qfood.management.repository.RestaurantRepository;
import com.github.qfood.management.service.MenuService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path(Paths.RESTAURANTS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestaurantResource {

    @Inject
    RestaurantRepository restaurantRepository;

    @Inject
    MenuService menuService;

    @GET
    public List<Restaurant> index() {
        return restaurantRepository.listAll();
    }

    @POST
    @Transactional
    public Response add(Restaurant dto) {
        dto.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void update(@PathParam("id") Long id, Restaurant dto) {
        Optional<Restaurant> restaurantEntity = Restaurant.findByIdOptional(id);
        if (restaurantEntity.isEmpty()) {
            throw new NotFoundException();
        }
        Restaurant restaurant = restaurantEntity.get();
        restaurant.name = dto.name;
        restaurant.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Optional<Restaurant> restaurantEntity = Restaurant.findByIdOptional(id);
        restaurantEntity.ifPresentOrElse(Restaurant::delete, () -> {
            throw new NotFoundException();
        });
    }

    @GET
    @Path("/{idRestaurant}/menus")
    public List<Menu> indexMenu(@PathParam("idRestaurant") Long idRestaurant) {
        try {
            return menuService.getMenusByRestaurantId(idRestaurant).collect(Collectors.toList());
        } catch (ServiceException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @POST
    @Path("{idRestaurant}/menus")
    @Transactional
    public Response addMenu(@PathParam("idRestaurant") Long idRestaurant, Menu dto) {
        try {
            menuService.insert(idRestaurant, dto);
            return Response.status(Response.Status.CREATED).build();
        } catch (ServiceException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @PUT
    @Path("{idRestaurant}/menus/{id}")
    @Transactional
    public Response updateMenu(@PathParam("idRestaurant") Long idRestaurant, @PathParam("id") Long id, Menu dto) {
        try {
            menuService.update(idRestaurant, id, dto);
            return Response.status(Response.Status.CREATED).build();
        } catch (ServiceException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @DELETE
    @Path("{idRestaurant}/menus/{id}")
    @Transactional
    public void delete(@PathParam("idRestaurant") Long idRestaurant, @PathParam("id") Long id) {
        try {
            menuService.delete(idRestaurant, id);
        } catch (ServiceException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}
