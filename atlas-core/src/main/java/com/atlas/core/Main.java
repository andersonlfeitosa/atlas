package com.atlas.core;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		SolrServer server = new HttpSolrServer("http://localhost:8983/solr/");
		
		

	}

}
