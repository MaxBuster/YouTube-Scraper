package crawler;

import io.AfterCrawlWriter;
import io.ExcelWriter;
import io.InitialLinkGetter;

import java.util.HashMap;
import java.util.LinkedList;

import org.jsoup.nodes.Document;

import objects.RelatedVideos;
import objects.VideoInfo;
import scraper.InfoGetter;
import scraper.RelatedGetter;
import scraper.SourceCodeGetter;

public class Crawler {
	private static int totalNumberCrawled;
	private static int numberToCrawl;
	private LinkedList<String> listOfCurrentLinksToCrawl;
	private LinkedList<String> listOfBadLinks;
	private LinkedList<String> listOfCrawledLinks;
	private HashMap<String, VideoInfo> linksToInfo;
	private HashMap<String, RelatedVideos> linksToRelatedVideos;

	public Crawler(int desiredNumberOfVideosToCrawl) {
		super();
		totalNumberCrawled = 0;
		numberToCrawl = desiredNumberOfVideosToCrawl;
		this.listOfCurrentLinksToCrawl = new InitialLinkGetter("linksToCrawl.txt").readLinksFromFile(); 
		this.listOfBadLinks = new InitialLinkGetter("badLinks.txt").readLinksFromFile();
		this.listOfCrawledLinks = new InitialLinkGetter("crawledLinks.txt").readLinksFromFile();
		this.linksToInfo = new HashMap<String, VideoInfo>();
		this.linksToRelatedVideos = new HashMap<String, RelatedVideos>();
	}

	public void crawl() {
		String linkToCrawl = null;
		while (totalNumberCrawled < numberToCrawl) {
			linkToCrawl = getLinkToCrawl();
			if (linkToCrawl != null) {
				scrapeLink(linkToCrawl);
			} else {
				sleepThread();
			}
		}
	}

	public synchronized String getLinkToCrawl() {
		String linkToCrawl = listOfCurrentLinksToCrawl.removeFirst();
		if (linkToCrawl == null) {
			return null;
		} else {
			totalNumberCrawled++;
			return "https://www.youtube.com" + linkToCrawl;
		}
	}
	
	public void scrapeLink(String linkToCrawl) {
		System.out.println(linkToCrawl);
		Document sourceCodeDocument = getSourceDocument(linkToCrawl);
		
		RelatedVideos relatedVideos = getRelatedVideos(linkToCrawl,
				sourceCodeDocument);
		VideoInfo videoInfo = getVideoInfo(linkToCrawl,
				sourceCodeDocument);

		String crawledLink = linkToCrawl;
		addLinkToAppropriateLists(crawledLink,
				relatedVideos,
				videoInfo);
	}

	public Document getSourceDocument(String linkToCrawl) {
		SourceCodeGetter sourceGetter = new SourceCodeGetter(linkToCrawl);
		Document sourceCodeDocument = sourceGetter.getSource();
		return sourceCodeDocument;
	}

	public RelatedVideos getRelatedVideos(String linkToCrawl,
			Document sourceCodeDocument) {
		RelatedGetter relatedGetter = new RelatedGetter(linkToCrawl, sourceCodeDocument);
		RelatedVideos relatedVideos = relatedGetter.parseDocumentForRelatedVideos();
		return relatedVideos;
	}

	public VideoInfo getVideoInfo(String linkToCrawl,
			Document sourceCodeDocument) {
		InfoGetter infoGetter = new InfoGetter(linkToCrawl, sourceCodeDocument);
		VideoInfo videoInfo = infoGetter.scrapeDocumentForInfo();
		return videoInfo;
	}

	public boolean checkForBadLink(VideoInfo videoInfo) {
		String[] info = videoInfo.getInfo();
		if (info == null) {
			return true;
		} else if (!info[7].contains("Education")) {
			return true;
		} else {
			return false;
		}
	}

	public void addLinkToAppropriateLists(String crawledLink,
			RelatedVideos relatedVideos,
			VideoInfo videoInfo) {
		boolean isBadLink = checkForBadLink(videoInfo);
		if (isBadLink) {
			addLinkToBad(crawledLink);
		} else {
			addLinkToGood(crawledLink, 
					relatedVideos,
					videoInfo);
		}
	}

	public void addLinkToGood(String crawledLink,
			RelatedVideos relatedVideos,
			VideoInfo videoInfo) {
		addLinkToCrawled(crawledLink);
		
		addAllRelatedVideosToCrawl(relatedVideos);

		mapScrapedInfo(crawledLink, videoInfo);
		mapScrapedRelated(crawledLink, relatedVideos);
	}

	public synchronized void addLinkToCrawled(String crawledLink) {
		crawledLink = crawledLink.replace("https://www.youtube.com", "");
		listOfCrawledLinks.add(crawledLink);
	}

	public synchronized void addLinkToBad(String crawledLink) {
		crawledLink = crawledLink.replace("https://www.youtube.com", "");
		listOfBadLinks.add(crawledLink);
	}


	public synchronized void addAllRelatedVideosToCrawl(RelatedVideos relatedVideos) {
		String[] relatedLinks = relatedVideos.getRelatedLinks();
		for (String link: relatedLinks) {
			addLinkToCrawl(link); 
		}
	}

	public synchronized void addLinkToCrawl(String link) {
		if (listOfBadLinks.contains(link) ||
				listOfCurrentLinksToCrawl.contains(link) ||
				listOfCrawledLinks.contains(link)) {
		} else {
			listOfCurrentLinksToCrawl.add(link);
		}
	}
	
	public synchronized void mapScrapedInfo(String link,
			VideoInfo videoInfo) {
		link = link.replace("https://www.youtube.com", "");
		linksToInfo.put(link, videoInfo);
	}

	public synchronized void mapScrapedRelated(String link,
			RelatedVideos relatedVideos) {
		link = link.replace("https://www.youtube.com", "");
		linksToRelatedVideos.put(link, relatedVideos);
	}
	
	public void sleepThread() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("Sleep Was Interrupted");
		}
	}

	public synchronized void closeListsAndWriteOut() {
		AfterCrawlWriter toCrawlWriter = new AfterCrawlWriter(listOfCurrentLinksToCrawl, 
				"linksToCrawl.txt");
		toCrawlWriter.writeDataToFile();
		
		AfterCrawlWriter crawledWriter = new AfterCrawlWriter(listOfCrawledLinks, 
				"crawledLinks.txt");
		crawledWriter.writeDataToFile();
		
		AfterCrawlWriter badWriter = new AfterCrawlWriter(listOfBadLinks, 
				"badLinks.txt");
		badWriter.writeDataToFile();
		
		closeInfoAndWriteOut();
	}
	
	public synchronized void closeInfoAndWriteOut() {
		ExcelWriter excelWriter = new ExcelWriter(linksToInfo);
		excelWriter.writeOutToExcel();
	}
}
