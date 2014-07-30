package com.atlas.web.controller;

import com.atlas.web.app.AtlasApplication;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestScoped
@Produces("text/html")
public abstract class AtlasController {
    @Inject
    private AtlasApplication application;

    @Context
    protected HttpServletRequest request;

    @Context
    protected HttpServletResponse response;

    @Context
    protected ServletContext context;

    protected Map<String, Object> viewModel = new HashMap<>();

    public String renderView(String viewName) {
        WebContext ctx = new WebContext(request, response, context, request.getLocale(), viewModel);
        return application.getTemplateEngine().process(viewName, ctx);
    }

    public void redirectToView(String viewName) {
        try {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
            requestDispatcher.forward(request, response);
        } catch (IOException | ServletException ex) {
            ex.printStackTrace();
        }
    }
}
