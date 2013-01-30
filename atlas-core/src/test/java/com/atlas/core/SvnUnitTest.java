package com.atlas.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.atlas.core.beans.SvnItem;

public abstract class SvnUnitTest {
	protected static SVNURL createTempSvnRepository() throws SVNException {
		FSRepositoryFactory.setup();
		File localRepository = new File(System.getProperty("user.home") + "/atlas-repo");		
		return SVNRepositoryFactory.createLocalRepository(localRepository, true, true);				
	}
	
	@SuppressWarnings("rawtypes")
	protected static Iterable loadSvnData(File dataFile) throws FileNotFoundException {
		Yaml yaml = new Yaml(new Constructor(SvnItem.class));
		InputStream inputStream = new FileInputStream("src/test/resources/svn-data.yaml");		
		return yaml.loadAll(inputStream);
	}
	
	@SuppressWarnings("rawtypes")
	protected static void populateSvnRepository(SVNRepository repo, SVNClientManager clientManager, Iterable svnItemList) throws IOException, SVNException {
		SVNCommitClient svnCommitClient = clientManager.getCommitClient();
		
		File tempDir = new File("/tmp/atlas-wc");
		FileUtils.deleteDirectory(tempDir);
		
		File completePath = null;
		File dummyFile = null;
		
		while (svnItemList.iterator().hasNext()) {
			SvnItem svnItem = (SvnItem) svnItemList.iterator().next();
			System.out.println(svnItem);
			completePath = new File(tempDir + File.separator + svnItem.getPath());
			dummyFile = new File(completePath, svnItem.getFileName());
			FileUtils.forceMkdir(completePath);
			FileUtils.writeStringToFile(dummyFile, svnItem.getFileName());
		}
		
		svnCommitClient.doImport(tempDir, repo.getRepositoryRoot(true), "Initial import", null, true, false, SVNDepth.INFINITY);

		FileUtils.deleteDirectory(tempDir);
	}
}
