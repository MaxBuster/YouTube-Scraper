package tests;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import scraper.InfoGetter;
import scraper.SourceCodeGetter;

public class InfoScrapeTester {
	private SourceCodeGetter sourceCodeGetter;
	private Document sourceCodeDocument;
	private InfoGetter infoGetter;

	@Before
	public void setUp() throws Exception {
		this.sourceCodeGetter = new SourceCodeGetter("https://www.youtube.com/watch?v=CU1k1UvM60Y");
		this.sourceCodeDocument = sourceCodeGetter.getSource();
		this.infoGetter = new InfoGetter("", sourceCodeDocument);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testContainerSeparation() {
		Document firstInfoContainer = infoGetter.getElementDocumentByID("watch-header");
		Document secondInfoContainer = infoGetter.getElementDocumentByID("action-panel-details");
		assertNotNull(firstInfoContainer);
		assertNotNull(secondInfoContainer);
	}
	
	@Test
	public void testTitleGetter() {
		Document firstInfoContainer = infoGetter.getElementDocumentByID("watch-header");
		infoGetter.getTitleFromFirstContainer(firstInfoContainer);
		assertNotNull(infoGetter.getArrayOfInfo()[0]);
	}

	@Test
	public void testForInfoCollection() {
		infoGetter.scrapeDocumentForInfo();
		for (String bitOfInfo : infoGetter.getArrayOfInfo()) {
			assertTrue(!bitOfInfo.isEmpty());
			assertNotNull(bitOfInfo);
		}
	}

}
