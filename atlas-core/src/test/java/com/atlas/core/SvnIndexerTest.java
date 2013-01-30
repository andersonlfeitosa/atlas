package com.atlas.core;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNClientManager;

public class SvnIndexerTest extends SvnUnitTest {
	private static SVNURL repositoryRoot;
	private static SVNClientManager clientManager;
	private static SVNRepository svnRepository;
	
	@BeforeClass
	public static void setup() throws SVNException, IOException {
		repositoryRoot = createTempSvnRepository();
		clientManager = SVNClientManager.newInstance();
		svnRepository = clientManager.createRepository(repositoryRoot, true);
		populateSvnRepository(svnRepository, clientManager, loadSvnData(new File("src/test/resources/svn-data.yaml")));
	}
		
	@Test
	public void testCreateTempRepository() throws SVNException {
		Assert.assertTrue(svnRepository.getLatestRevision() == 1);
	}
}
