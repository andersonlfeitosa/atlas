package com.atlas.web.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class HomeController extends AtlasController {
    @GET
    public String index() {
        return renderView("home");
    }
}
