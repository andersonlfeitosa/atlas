package com.atlas.core.svn;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SubversionCrawler {
    private URL url;
    private String username;
    private String password;

    private List<SVNItem> svnItemList;
    private List<CrawlerEventListener> listeners;

    private SVNRepository repository;

    static {
        FSRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        DAVRepositoryFactory.setup();
    }

    private void navigateRepository(String path) throws SVNException {
        Collection<SVNDirEntry> dirEntries = new ArrayList<>();

        repository.getDir(path, SVNRevision.HEAD.getNumber(), null, dirEntries);

        for (SVNDirEntry svnDirEntry: dirEntries) {
            SVNItem item = new SVNItem();
            item.setFilename(svnDirEntry.getName());
            item.setLastCommitter(svnDirEntry.getAuthor());
            item.setPath(path.equals("/") ? path + svnDirEntry.getRelativePath() : path + "/" + svnDirEntry.getRelativePath());
            item.setId(item.getPath());
            item.setUpdatedAt(svnDirEntry.getDate());
            item.setLastRevision(svnDirEntry.getRevision());
            item.setType(svnDirEntry.getKind() == SVNNodeKind.DIR ? "D" : "F");
            item.setUrl(svnDirEntry.getURL().toDecodedString());
            item.setSize(svnDirEntry.getSize());

            fireNewItemEvent(item);

            if (svnDirEntry.getKind() == SVNNodeKind.DIR) {
                navigateRepository(item.getPath());
            }
        }
    }

    private void fireStartEvent() {
        for (CrawlerEventListener listener : listeners) {
            listener.onCrawlerStart();
        }
    }

    private void fireEndEvent() {
        for (CrawlerEventListener listener : listeners) {
            listener.onCrawlerEnd();
        }
    }

    private void fireNewItemEvent(SVNItem item) {
        for (CrawlerEventListener listener : listeners) {
            listener.onCrawlerNewItem(item);
        }
    }

    public SubversionCrawler(URL url, String username, String password) throws SVNException, URISyntaxException {
        this.url = url;
        this.username = username;
        this.password = password;
        this.svnItemList = new ArrayList<>();
        this.listeners = new ArrayList<>();

        SVNURL svnUrl;

        if(url.getProtocol().equalsIgnoreCase("file")) {
            svnUrl = SVNURL.fromFile(new File(url.toURI()));
        } else {
            svnUrl = SVNURL.create(url.getProtocol(), "", url.getHost(), url.getPort(), "/", false);
        }

        repository = SVNRepositoryFactory.create(svnUrl);
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
        repository.setAuthenticationManager(authManager);
    }

    public void execute(String path) throws SVNException {
        fireStartEvent();
        navigateRepository(path);
        fireEndEvent();
    }

    public void addListener(CrawlerEventListener listener) {
        this.listeners.add(listener);
    }

    public List<SVNItem> getSvnItemList() {
        return svnItemList;
    }
}
