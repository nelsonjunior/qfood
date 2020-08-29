package com.github.qfood.management.domain.dto;

import com.github.qfood.management.domain.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface MenuMapper {

    Menu toEntity(MenuDTO dto);

    Menu toEntity(AddMenuDTO dto);

    MenuDTO toDTO(Menu entity);

    @Mapping(target = "price", source = "newPrice")
    void toMenu(UpdateMenuDTO dto, @MappingTarget Menu entity);

}
