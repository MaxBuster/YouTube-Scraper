package scraper;

import objects.VideoInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class InfoGetter {
	private String videoLink;
	private Document sourceCodeDocument;
	private String[] arrayOfInfo;

	public InfoGetter(String videoLink, Document sourceCodeDocument) {
		super();
		this.videoLink = videoLink;
		this.sourceCodeDocument = sourceCodeDocument;
		this.arrayOfInfo = new String[8];
	}

	public VideoInfo scrapeDocumentForInfo() {
		Document firstInfoContainer = getElementDocumentByID("watch-header");
		Document secondInfoContainer = getElementDocumentByID("action-panel-details");

		getInfoFromFirstContainerDocument(firstInfoContainer);
		getInfoFromSecondContainerDocument(secondInfoContainer);

		if (checkForAllCleanData() == false) {
			VideoInfo videoInfo = new VideoInfo(videoLink, null);
			return videoInfo;
		} else {
			VideoInfo videoInfo = new VideoInfo(videoLink, arrayOfInfo);
			return videoInfo;
		}
	}

	public boolean checkForAllCleanData() {
		for (String stringOfInfo : arrayOfInfo) {
			if (stringOfInfo == null) {
				return false;
			}
		}
		return true;
	}

	public Document getElementDocumentByID(String elementID) {
		Element elementFromID = sourceCodeDocument.getElementById(elementID);
		Document documentForElement = parseElementIntoDocument(elementFromID);
		return documentForElement;
	}

	public Document parseElementIntoDocument(Element element) {
		String elementHTML = element.html();
		Document elementDocument = Jsoup.parse(elementHTML);
		return elementDocument;
	}

	public String[] getArrayOfInfo() {
		return arrayOfInfo;
	}

	public void getInfoFromFirstContainerDocument(Document firstContainerDocument) {
		getTitleFromFirstContainer(firstContainerDocument);
		getPosterNameFromFirstContainer(firstContainerDocument);
		getPosterSubscribersFromFirstContainer(firstContainerDocument);
		getViewsFromFirstContainer(firstContainerDocument);
		getLikesFromFirstContainer(firstContainerDocument);
		getDislikesFromFirstContainer(firstContainerDocument);
	}

	public void getInfoFromSecondContainerDocument(Document secondContainerDocument) {
		getPostDateFromSecondContainer(secondContainerDocument);
		getCategoryFromSecondContainer(secondContainerDocument);
	}

	public void getTitleFromFirstContainer(Document firstContainerDocument) {
		String titleElementID = "watch-headline-title";
		String pageTitle = firstContainerDocument.select("#" + titleElementID + " span")
				.text();
		arrayOfInfo[0] = testForCleanData(pageTitle);
	}

	public void getPosterNameFromFirstContainer(Document firstContainerDocument) {
		String posterNameElementID = "watch7-user-header";
		Element posterNameElement = firstContainerDocument.getElementById(posterNameElementID);
		String posterName = posterNameElement.select("a").text();
		arrayOfInfo[1] = testForCleanData(posterName);
	}

	public void getPosterSubscribersFromFirstContainer(Document firstContainerDocument) {
		String className = "yt-subscription-button-subscriber-count-branded-horizontal";
		String posterSubscribers = firstContainerDocument.select("span." + className)
				.attr("title");
		arrayOfInfo[2] = testForCleanData(posterSubscribers);
	}

	public void getViewsFromFirstContainer(Document firstContainerDocument) {
		String className = "watch-view-count";
		String numViews = firstContainerDocument.select("div." + className)
				.text();
		arrayOfInfo[3] = testForCleanData(numViews);
	}

	public void getLikesFromFirstContainer(Document firstContainerDocument) {
		String idName = "watch-like";
		String numLikes = firstContainerDocument.select("#" + idName + " span")
				.text();
		arrayOfInfo[4] = testForCleanData(numLikes);
	}

	public void getDislikesFromFirstContainer(Document firstContainerDocument) {
		String idName = "watch-dislike";
		String numDislikes = firstContainerDocument.select("#" + idName + " span")
				.text();
		arrayOfInfo[5] = testForCleanData(numDislikes);
	}

	public void getPostDateFromSecondContainer(Document secondContainerDocument) {
		String idName = "watch-uploader-info";
		String postDate = secondContainerDocument.select("#" + idName + " strong")
				.text();
		arrayOfInfo[6] = testForCleanData(postDate);
	}

	public void getCategoryFromSecondContainer(Document secondContainerDocument) {
		String idName = "watch-description-extras";
		String category = secondContainerDocument.select("#" + idName + " a")
				.text();
		arrayOfInfo[7] = testForCleanData(category);
	}

	public String testForCleanData(String infoString) {
		if (infoString.isEmpty()
				|| infoString == null) {
			return null;
		} else {
			return infoString;
		}
	}
}
