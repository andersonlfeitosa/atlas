package com.atlas.web.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/search")
public class SearchController extends AtlasController {

    @GET
    public String index() {
        return renderView("search/index");
    }
}
