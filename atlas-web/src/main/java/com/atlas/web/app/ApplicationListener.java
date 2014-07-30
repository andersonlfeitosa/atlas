package com.atlas.web.app;

import com.atlas.core.ejb.IndexServiceEJB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationListener.class);

    @Inject
    private AtlasApplication application;

    @Inject
    private IndexServiceEJB indexService;

    public ApplicationListener() {

    }

    public void contextInitialized(ServletContextEvent sce) {
        application.init();
        indexService.startIndexJob();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        // TODO check and stop for any pending index job
    }
}
