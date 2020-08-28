package com.github.qfood.management.presentation;

import com.github.qfood.management.domain.entity.Menu;
import com.github.qfood.management.domain.entity.Restaurant;
import com.github.qfood.management.exception.ServiceException;
import com.github.qfood.management.repository.RestaurantRepository;
import com.github.qfood.management.service.MenuService;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.tags.Tags;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path(Paths.RESTAURANTS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "restaurant")
public class RestaurantResource {

    private static final Logger LOGGER = Logger.getLogger(RestaurantResource.class);

    @Inject
    RestaurantRepository restaurantRepository;

    @Inject
    MenuService menuService;

    @GET
    public Response getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.listAll();
        LOGGER.debug("Total number of restaurants " + restaurants);
        return Response.ok(restaurants).build();
    }

    @GET
    @Path("/{id}")
    public Response getRestaurant(@PathParam("id") Long id) {
        Optional<Restaurant> restaurant = restaurantRepository.findByIdOptional(id);
        if (restaurant != null) {
            LOGGER.debug("Found restaurant " + restaurant);
            return Response.ok(restaurant).build();
        } else {
            LOGGER.debug("No hero found with id " + id);
            return Response.noContent().build();
        }
    }

    @POST
    @Transactional
    public Response add(Restaurant dto, @Context UriInfo uriInfo) {
        dto.persist();
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(dto.id));
        LOGGER.debug("New restaurant created with URI " + builder.build().toString());
        return Response.created(builder.build()).build();
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
    @Tag(name = "menu")
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
    @Tag(name = "menu")
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
    @Tag(name = "menu")
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
    @Tag(name = "menu")
    public void delete(@PathParam("idRestaurant") Long idRestaurant, @PathParam("id") Long id) {
        try {
            menuService.delete(idRestaurant, id);
        } catch (ServiceException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}
