package com.atlas.core.ejb;

import com.atlas.core.es.SubversionIndexer;
import com.atlas.core.svn.SVNItem;
import com.atlas.core.svn.SubversionCrawler;
import com.atlas.core.util.Constants;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.IOException;
import java.net.URL;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@Startup
@Singleton
public class IndexServiceEJB {
    private static final Logger logger = LoggerFactory.getLogger(IndexServiceEJB.class);
    private SubversionIndexer indexer;
    private Node node;

    @PostConstruct
    void init() {
        initializeElasticSearch();
    }

    @PreDestroy
    void cleanUp() {
        logger.info("Elastic Search cleanup...");
        node.close();
    }

    @Asynchronous
    public void startIndexJob() {
        SubversionCrawler crawler = null;
        SubversionIndexer indexer = null;

        try {
            crawler = new SubversionCrawler(new URL(Constants.SVN_REPO), "", "");
            indexer = new SubversionIndexer(crawler, Constants.INDEX_BATCH_SIZE, node);

            logger.info("Starting indexer job...");
            indexer.start();

        } catch (Exception e) {
            logger.error("Failed to index SVN repository", e);
        }
    }

    public Node getNode() {
        return node;
    }

    private void initializeElasticSearch() {
        logger.info("Initializing Elastic Search...");

        ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder();
        settings.put("node.name", "atlas-node");
        settings.put("path.data", "/tmp/index");

        node = nodeBuilder().settings(settings).clusterName(Constants.ES_CLUSTER_NAME).data(true).local(true).node();
    }
}
