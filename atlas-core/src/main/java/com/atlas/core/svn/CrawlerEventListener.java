package com.atlas.core.svn;

public interface CrawlerEventListener {
    void onCrawlerStart();
    void onCrawlerNewItem(SVNItem item);
    void onCrawlerEnd();
}
