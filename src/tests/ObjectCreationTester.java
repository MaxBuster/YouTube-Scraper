package tests;

import static org.junit.Assert.*;
import objects.RelatedVideos;
import objects.VideoInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ObjectCreationTester {
	private RelatedVideos related;
	private VideoInfo info;

	@Before
	public void setUp() throws Exception {
		related = new RelatedVideos(null,null);
		info = new VideoInfo(null,null);
	}

	@After
	public void tearDown() throws Exception {
		related = null;
		info = null;
	}

	@Test
	public void testForCreation() {
		assertNotNull(related);
		assertNotNull(info);
	}

}
