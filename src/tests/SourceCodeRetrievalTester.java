package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import scraper.SourceCodeGetter;

public class SourceCodeRetrievalTester {
	private SourceCodeGetter sourceGetter;

	@Before
	public void setUp() throws Exception {
		sourceGetter = new SourceCodeGetter("https://www.youtube.com/watch?v=OhzVI-hw3S0");
	}

	@After
	public void tearDown() throws Exception {
		sourceGetter = null;
	}

	@Test
	public void test() {
		assertNotNull(sourceGetter.getSource());
	}

}
