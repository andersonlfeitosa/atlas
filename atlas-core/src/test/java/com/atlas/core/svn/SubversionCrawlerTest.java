package com.atlas.core.svn;

import com.atlas.core.util.Constants;
import org.junit.Assert;
import org.junit.Test;
import org.tmatesoft.svn.core.SVNException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class SubversionCrawlerTest {
    @Test
    public void repositoryFoundTest() {
        SubversionCrawler crawler = null;

        try {
            crawler = new SubversionCrawler(new URL(Constants.SVN_REPO), "", "");
            crawler.execute("/");
        } catch (SVNException | MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull("Items cannot be null", crawler.getSvnItemList());
    }
}
