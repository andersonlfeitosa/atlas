package com.atlas.web.app;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
public class AtlasApplication extends Application {
    private static TemplateEngine templateEngine;

    private static TemplateEngine initializeTemplateEngine() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheable(false);

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine;
    }

    public AtlasApplication() {
        initializeTemplateEngine();
    }

    public static TemplateEngine getTemplateEngine() {
        if (templateEngine == null) {
            templateEngine = initializeTemplateEngine();
        }

        return templateEngine;
    }
}
