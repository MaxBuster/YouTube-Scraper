package tests;

import static org.junit.Assert.*;
import objects.RelatedVideos;

import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import scraper.RelatedGetter;
import scraper.SourceCodeGetter;

public class RelatedVideoScrapeTester {
	private Document sourceCode;
	private RelatedVideos relatedVideos;

	@Before
	public void setUp() throws Exception {
		String link = "https://www.youtube.com/watch?v=UPzRv5VVctA";
		sourceCode = new SourceCodeGetter(link).getSource();
		relatedVideos = new RelatedGetter(link,sourceCode).parseDocumentForRelatedVideos();
	}

	@After
	public void tearDown() throws Exception {
		sourceCode = null;
		relatedVideos = null;
	}

	@Test
	public void test() {
		assertNotNull(sourceCode.html());
		assertTrue(relatedVideos.getRelatedLinks().length == 20);
	}

}
