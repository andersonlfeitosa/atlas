package com.atlas.web.controller;

import com.atlas.core.ejb.SearchServiceEJB;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Date;

@Path("/")
public class HomeController extends AtlasController {
    @Inject
    private SearchServiceEJB searchService;

    @GET
    public String index() {
        viewModel.put("today", new Date());

        searchService.initialize();

        return renderView("home");
    }
}
