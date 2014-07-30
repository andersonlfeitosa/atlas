package com.atlas.core.es;

import com.atlas.core.util.Constants;
import com.atlas.core.svn.SubversionCrawler;
import org.elasticsearch.node.Node;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

public class SubversionIndexerTest {

    @Test
    public void indexItems() throws Exception {
        Node node = nodeBuilder().local(true).node();

        SubversionCrawler crawler = new SubversionCrawler(new URL(Constants.SVN_REPO), "", "");
        SubversionIndexer indexer = new SubversionIndexer(crawler, Constants.INDEX_BATCH_SIZE, node);

        indexer.start();

        Assert.assertTrue("Indexed items must be greater than 0", indexer.getIndexedItems() > 0);
    }
}

