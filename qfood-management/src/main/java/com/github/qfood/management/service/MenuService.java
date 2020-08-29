package com.github.qfood.management.service;

import com.github.qfood.management.domain.dto.AddMenuDTO;
import com.github.qfood.management.domain.dto.MenuDTO;
import com.github.qfood.management.domain.dto.MenuMapper;
import com.github.qfood.management.domain.dto.UpdateMenuDTO;
import com.github.qfood.management.domain.entity.Menu;
import com.github.qfood.management.domain.entity.Restaurant;
import com.github.qfood.management.exception.ServiceException;
import com.github.qfood.management.repository.MenuRepository;
import com.github.qfood.management.repository.RestaurantRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class MenuService {

    @Inject
    RestaurantRepository restaurantRepository;

    @Inject
    MenuMapper menuMapper;

    @Inject
    MenuRepository menuRepository;

    public List<MenuDTO> getMenusByRestaurantId(Long idRestaurant) throws ServiceException {
        Optional<Restaurant> restaurantEntity = restaurantRepository.findByIdOptional(idRestaurant);
        if (restaurantEntity.isEmpty()) {
            throw new ServiceException("Restaurant does not exist");
        }
        Stream<Menu> menus = Menu.stream("restaurant", restaurantEntity.get());
        return menus.map(menuMapper::toDTO).collect(Collectors.toList());
    }

    public MenuDTO insert(Long idRestaurant, AddMenuDTO dto) throws ServiceException {
        Optional<Restaurant> restaurantEntity = restaurantRepository.findByIdOptional(idRestaurant);
        if (restaurantEntity.isEmpty()) {
            throw new ServiceException("Restaurant does not exist");
        }
        Menu menu = menuMapper.toEntity(dto);
        menu.restaurant = restaurantEntity.get();
        menu.persist();
        return menuMapper.toDTO(menu);
    }

    public MenuDTO update(Long idRestaurant, Long idMenu, UpdateMenuDTO dto) throws ServiceException {
        Optional<Restaurant> restaurantEntity = restaurantRepository.findByIdOptional(idRestaurant);
        if (restaurantEntity.isEmpty()) {
            throw new ServiceException("Restaurant does not exist");
        }

        Optional<Menu> menuEntity = menuRepository.findByIdOptional(idMenu);
        if (menuEntity.isEmpty()) {
            throw new ServiceException("Menu does not exist");
        }
        Menu menu = menuEntity.get();
        menuMapper.toMenu(dto, menu);
        menu.persist();
        return menuMapper.toDTO(menu);
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
