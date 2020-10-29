package com.github.qfood.marketplace.presentation;

import com.github.qfood.marketplace.domain.dto.MenuDTO;
import com.github.qfood.marketplace.service.MenuService;
import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(Paths.MENUS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MenuResource {

    @Inject
    MenuService menuService;

    @GET
    @APIResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = MenuDTO.class))
    )
    public Multi<MenuDTO> findAllMenus() {
        return menuService.findAllByRestaurant();
    }

}
