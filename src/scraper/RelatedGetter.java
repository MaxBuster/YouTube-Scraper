package scraper;

import java.util.ArrayList;

import objects.RelatedVideos;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RelatedGetter {
	private String originalLink;
	private Document sourceCodeDocument;
	private ArrayList<String> relatedVideoLinks;
	
	public RelatedGetter(String originalLink, Document sourceCodeDocument) {
		super();
		this.originalLink = originalLink;
		this.sourceCodeDocument = sourceCodeDocument;
		this.relatedVideoLinks = new ArrayList<String>();
	}
	
	public RelatedVideos parseDocumentForRelatedVideos() {
		Elements relatedVideoSections = getElementsFromDocument();
		extractLinksFromElements(relatedVideoSections); 
		
		String[] relatedVideoLinksArray = listToArray();
		RelatedVideos relatedVideos = new RelatedVideos(originalLink, relatedVideoLinksArray);
		return relatedVideos;
	}
	
	public String[] listToArray() {
		String[] relatedVideoLinksArray = new String[relatedVideoLinks.size()];
		for (String relatedLink: relatedVideoLinks) {
			relatedVideoLinksArray[relatedVideoLinks.indexOf(relatedLink)] = relatedLink;
		}
		return relatedVideoLinksArray;
	}
	
	public Elements getElementsFromDocument() {
		Element relatedVideoContainer = sourceCodeDocument.getElementById("watch-related");
		Document containerDocument = parseElementIntoDocument(relatedVideoContainer);
		
		Elements relatedVideoSections = containerDocument.select("a");
		return relatedVideoSections;
	}
	
	public void extractLinksFromElements(Elements relatedVideoSections) {
		for (Element relatedVideoCode: relatedVideoSections) {
			getLinkByAttribute(relatedVideoCode);
		}
	}
	
	public void getLinkByAttribute(Element relatedVideoCode) {
		String relatedVideoLink = relatedVideoCode.attr("href");
		addLinkToList(relatedVideoLink);
	}
	
	public void addLinkToList(String link) {
		relatedVideoLinks.add(link);
	}
	
	public Document parseElementIntoDocument(Element element) {
		String elementHTML = element.html();
		Document elementDocument = Jsoup.parse(elementHTML);
		return elementDocument;
	}
	
}
