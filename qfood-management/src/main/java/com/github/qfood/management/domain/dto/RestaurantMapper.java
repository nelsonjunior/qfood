package com.github.qfood.management.domain.dto;

import com.github.qfood.management.domain.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface RestaurantMapper {

    Restaurant toEntity(AddRestaurantDTO restaurantDTO);

    @Mapping(target = "name", source = "newName")
    void toRestaurant(UpdateRestaurantDTO dto, @MappingTarget Restaurant restaurant);

    RestaurantDTO toDTO(Restaurant entity);
}
