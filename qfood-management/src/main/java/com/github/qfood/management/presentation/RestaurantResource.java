package com.github.qfood.management.presentation;

import com.github.qfood.management.domain.dto.*;
import com.github.qfood.management.domain.entity.Menu;
import com.github.qfood.management.domain.entity.Restaurant;
import com.github.qfood.management.exception.ServiceException;
import com.github.qfood.management.repository.RestaurantRepository;
import com.github.qfood.management.service.MenuService;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
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
    RestaurantMapper restaurantMapper;

    @Inject
    RestaurantRepository restaurantRepository;

    @Inject
    MenuService menuService;

    @GET
    public Response getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.listAll();
        LOGGER.debug("Total number of restaurants " + restaurants);
        List<RestaurantDTO> restaurantDTOList = restaurants.stream().map(restaurantMapper::toDTO).collect(Collectors.toList());
        return Response.ok(restaurantDTOList).build();
    }

    @GET
    @Path("/{id}")
    public Response getRestaurant(@PathParam("id") Long id) {
        Optional<Restaurant> restaurant = restaurantRepository.findByIdOptional(id);
        if (restaurant.isPresent()) {
            LOGGER.debug("Found restaurant " + restaurant);
            return Response.ok(restaurantMapper.toDTO(restaurant.get())).build();
        } else {
            LOGGER.debug("No hero found with id " + id);
            return Response.noContent().build();
        }
    }

    @POST
    @Transactional
    public Response add(AddRestaurantDTO dto, @Context UriInfo uriInfo) {
        Restaurant entity = restaurantMapper.toEntity(dto);
        entity.persist();
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(entity.id));
        LOGGER.debug("New restaurant created with URI " + builder.build().toString());
        return Response.created(builder.build()).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, UpdateRestaurantDTO dto) {
        Optional<Restaurant> restaurantEntity = Restaurant.findByIdOptional(id);
        if (restaurantEntity.isEmpty()) {
            throw new NotFoundException();
        }
        Restaurant restaurant = restaurantEntity.get();
        restaurantMapper.toRestaurant(dto, restaurant);
        restaurant.persist();
        LOGGER.debug("Restaurant updated with new valued " + restaurant);
        return Response.ok(restaurantMapper.toDTO(restaurant)).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        Optional<Restaurant> restaurantEntity = Restaurant.findByIdOptional(id);
        restaurantEntity.ifPresentOrElse(Restaurant::delete, () -> {
            throw new NotFoundException();
        });
        LOGGER.debug("Restaurant deleted with " + id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{idRestaurant}/menus")
    @Tag(name = "menu")
    public Response getAllMenus(@PathParam("idRestaurant") Long idRestaurant) {
        try {
            List<MenuDTO> menuDTOS = menuService.getMenusByRestaurantId(idRestaurant);
            LOGGER.debug("Total number of menu " + menuDTOS);
            return Response.ok(menuDTOS).build();
        } catch (ServiceException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @POST
    @Path("{idRestaurant}/menus")
    @Transactional
    @Tag(name = "menu")
    public Response addMenu(@PathParam("idRestaurant") Long idRestaurant, AddMenuDTO dto, @Context UriInfo uriInfo) {
        try {
            MenuDTO menuDTO = menuService.insert(idRestaurant, dto);
            UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(menuDTO.id));
            LOGGER.debug("New menu created with URI " + builder.build().toString());
            return Response.created(builder.build()).build();
        } catch (ServiceException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @PUT
    @Path("{idRestaurant}/menus/{id}")
    @Transactional
    @Tag(name = "menu")
    public Response updateMenu(@PathParam("idRestaurant") Long idRestaurant, @PathParam("id") Long id, UpdateMenuDTO dto) {
        try {
            MenuDTO menuDTO = menuService.update(idRestaurant, id, dto);
            return Response.ok(menuDTO).build();
        } catch (ServiceException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @DELETE
    @Path("{idRestaurant}/menus/{id}")
    @Transactional
    @Tag(name = "menu")
    public Response delete(@PathParam("idRestaurant") Long idRestaurant, @PathParam("id") Long id) {
        try {
            menuService.delete(idRestaurant, id);
            LOGGER.debug("Menu deleted with " + id);
            return Response.noContent().build();
        } catch (ServiceException e) {
            throw new NotFoundException(e.getMessage());
        }
    }
}
