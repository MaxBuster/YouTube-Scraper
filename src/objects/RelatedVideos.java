package objects;

public class RelatedVideos {
	private String link;
	private String[] relatedLinks;

	public RelatedVideos(String link, String[] relatedLinks) {
		super();
		this.link = link;
		this.relatedLinks = relatedLinks;
	}
	
	public String getLink() {
		return link;
	}

	public String[] getRelatedLinks() {
		return relatedLinks;
	}
}
