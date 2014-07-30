package com.atlas.web.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AtlasApplication {
    private static final Logger logger = LoggerFactory.getLogger(AtlasApplication.class);

    private TemplateEngine templateEngine;

    private TemplateEngine initializeTemplateEngine() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheable(false);

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine;
    }

    void init() {
        logger.info("Atlas application starting...");
        initializeTemplateEngine();
    }

    public TemplateEngine getTemplateEngine() {
        if (templateEngine == null) {
            templateEngine = initializeTemplateEngine();
        }

        return templateEngine;
    }
}
