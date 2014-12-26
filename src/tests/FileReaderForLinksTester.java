package tests;

import static org.junit.Assert.*;
import io.InitialLinkGetter;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileReaderForLinksTester {
	private InitialLinkGetter linkGetter;

	@Before
	public void setUp() throws Exception {
		linkGetter = new InitialLinkGetter("linksToCrawl.txt");
	}

	@After
	public void tearDown() throws Exception {
		linkGetter = null;
	}

	@Test
	public void testListNotNull() {
		LinkedList<String> links = linkGetter.readLinksFromFile();
		assertNotNull(links);
	}

}
