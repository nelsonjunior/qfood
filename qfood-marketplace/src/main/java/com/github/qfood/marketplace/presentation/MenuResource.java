package com.github.qfood.marketplace.presentation;

import com.github.qfood.marketplace.domain.dto.MenuDTO;
import com.github.qfood.marketplace.domain.entity.Menu;
import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;

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
    PgPool pgPool;

    @GET
    public Multi<MenuDTO> findAllMenus(){
        return Menu.findAll(pgPool);
    }

}
