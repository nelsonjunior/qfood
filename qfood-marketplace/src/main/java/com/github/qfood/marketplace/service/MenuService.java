package com.github.qfood.marketplace.service;

import com.github.qfood.marketplace.domain.dto.MenuDTO;
import com.github.qfood.marketplace.repository.MenuRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MenuService {

    @Inject
    MenuRepository menuRepository;

    public Multi<MenuDTO> findAllByRestaurant() {
        return menuRepository.findAll();
    }

    public Multi<MenuDTO> findAllByIdRestaurant(Long idRestaurant) {
        return menuRepository.findAllByIdRestaurant(idRestaurant);
    }

    public Uni<MenuDTO> findById(Long id) {
        return menuRepository.findById(id);
    }

}
