package com.atlas.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNClientManager;

import com.atlas.core.beans.SvnItem;

public class Main {

	public static void main(String[] args) throws Exception {
		SolrServer server = new HttpSolrServer("http://localhost:8983/solr/atlas");		
		server.deleteByQuery("*:*");

		SVNClientManager clientManager = SVNClientManager.newInstance();
		SVNRepository svnRepository = clientManager.createRepository(SVNURL.parseURIEncoded("file:///Users/dilas/atlas-repo"), true);
		
		List<SvnItem> svnItemList = new ArrayList<SvnItem>();
		listEntries(svnItemList, svnRepository, "");
		
		server.addBeans(svnItemList);
		server.commit();
		
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		query.setParam("rows", "1000");		
		QueryResponse rsp = server.query(query);
		
		List<SvnItem> resultList = rsp.getBeans(SvnItem.class);
		
		for (SvnItem svnItem : resultList) {
			System.out.println(svnItem);
		}
	}

	@SuppressWarnings("rawtypes")
	public static void listEntries(List<SvnItem> svnItemList, SVNRepository repository, String path) throws SVNException {
		Collection entries = repository.getDir(path, -1, null, (Collection) null);
		Iterator iterator = entries.iterator();
		while (iterator.hasNext()) {
			SVNDirEntry entry = (SVNDirEntry) iterator.next();
			
			SvnItem item = new SvnItem();
			item.setLastCommitter(entry.getAuthor());
			item.setPath("/" + (path.equals("") ? "" : path));
			item.setFileName(entry.getName());
			item.setLastRevision(entry.getRevision());
			item.setUpdatedAt(entry.getDate());
			item.setId("/" + (path.equals("") ? "" : path + "/") + item.getFileName());
			item.setType(entry.getKind() == SVNNodeKind.DIR ? "D" : "F");
			
			svnItemList.add(item);

			if (entry.getKind() == SVNNodeKind.DIR) {
				listEntries(svnItemList, repository, (path.equals("")) ? entry.getName() : path + "/" + entry.getName());
			}
		}
	}

}
