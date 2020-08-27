package com.github.qfood.management.service;

import com.github.qfood.management.domain.entity.Menu;
import com.github.qfood.management.domain.entity.Restaurant;
import com.github.qfood.management.exception.ServiceException;
import com.github.qfood.management.repository.MenuRepository;
import com.github.qfood.management.repository.RestaurantRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Stream;

@ApplicationScoped
public class MenuService {

    @Inject
    RestaurantRepository restaurantRepository;

    @Inject
    MenuRepository menuRepository;

    public Stream<Menu> getMenusByRestaurantId(Long idRestaurant) throws ServiceException {
        Optional<Restaurant> restauranteEntity = restaurantRepository.findByIdOptional(idRestaurant);
        if (restauranteEntity.isEmpty()) {
            throw new ServiceException("Restaurant does not exist");
        }
        return Menu.stream("restaurant", restauranteEntity.get());
    }

    public void insert(Long idRestaurant, Menu menu) throws ServiceException {
        Optional<Restaurant> restaurantEntity = restaurantRepository.findByIdOptional(idRestaurant);
        if (restaurantEntity.isEmpty()) {
            throw new ServiceException("Restaurant does not exist");
        }
        menu.restaurant = restaurantEntity.get();
        menu.persist();
    }

    public void update(Long idRestaurant, Long idMenu, Menu menu) throws ServiceException {
        Optional<Restaurant> restaurantEntity = restaurantRepository.findByIdOptional(idRestaurant);
        if (restaurantEntity.isEmpty()) {
            throw new ServiceException("Restaurant does not exist");
        }

        Optional<Menu> menuEntity = menuRepository.findByIdOptional(idMenu);
        if (menuEntity.isEmpty()) {
            throw new ServiceException("Menu does not exist");
        }
        menu.restaurant = restaurantEntity.get();
        menu.persist();
    }

    public void delete(Long idRestaurant, Long idMenu) throws ServiceException {
        Optional<Restaurant> restaurantEntity = restaurantRepository.findByIdOptional(idRestaurant);
        if (restaurantEntity.isEmpty()) {
            throw new ServiceException("Restaurant does not exist");
        }

        Optional<Menu> menuEntity = Menu.findByIdOptional(idMenu);
        if (menuEntity.isEmpty()) {
            throw new ServiceException("Menu does not exist");
        }
        menuEntity.ifPresent(Menu::delete);
    }
}
