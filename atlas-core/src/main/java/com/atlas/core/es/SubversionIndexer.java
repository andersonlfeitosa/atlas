package com.atlas.core.es;

import com.atlas.core.svn.CrawlerEventListener;
import com.atlas.core.svn.SVNItem;
import com.atlas.core.svn.SubversionCrawler;
import com.atlas.core.util.Constants;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.node.Node;
import org.tmatesoft.svn.core.SVNException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.client.Requests.indexRequest;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class SubversionIndexer implements CrawlerEventListener {
    private SubversionCrawler crawler;
    private Node node;

    private List<SVNItem> svnItemList;
    private int indexBatchSize;
    private long indexedItems;

    public SubversionIndexer(SubversionCrawler crawler, int indexBatchSize, Node node) {
        this.crawler = crawler;
        this.indexBatchSize = indexBatchSize;
        this.node = node;
        this.svnItemList = new ArrayList<>();
    }

    private void updateIndex() {
        Client client = node.client();
        BulkRequestBuilder bulk = client.prepareBulk();

        try {
            for (SVNItem item : svnItemList) {
                XContentBuilder document = createDocument(item);
                bulk.add(indexRequest(Constants.ES_INDEX_NAME).type(Constants.ES_SVN_ITEM_TYPE_NAME).id(item.getId()).source(document));
                System.out.println("Indexing.. " + item);
            }

            BulkResponse response =  bulk.execute().actionGet();

            if (response.hasFailures()) {
                System.out.println("Failed to execute: " + response.buildFailureMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private XContentBuilder createDocument(SVNItem item) throws IOException {
        return jsonBuilder()
                .startObject()
                .field("filename", item.getFilename())
                .field("path", item.getPath())
                .field("lastCommitter", item.getLastCommitter())
                .field("lastRevision", item.getLastRevision())
                .field("updatedAt", item.getUpdatedAt())
                .field("type", item.getType())
                .field("url", item.getUrl())
                .field("size", item.getSize())
                .endObject();
    }

    @Override
    public void onCrawlerStart() {
        // do nothing
    }

    @Override
    public void onCrawlerNewItem(SVNItem item) {
        indexedItems++;

        if (svnItemList.size() >= indexBatchSize) {
            updateIndex();
            svnItemList.clear();
        } else {
            svnItemList.add(item);
        }
    }

    @Override
    public void onCrawlerEnd() {
        if (svnItemList.size() > 0) {
            updateIndex();
            svnItemList.clear();
        }
    }

    public void start() throws SVNException {
        crawler.addListener(this);
        crawler.execute("/");
    }

    public long getIndexedItems() {
        return indexedItems;
    }
}
