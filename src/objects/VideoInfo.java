package objects;

public class VideoInfo {
	private String link;
	private String[] infoArray;

	public VideoInfo(String link, 
			String[] infoArray) {
		super();
		this.link = link;
		this.infoArray = infoArray;
	}
	
	public String getLink() {
		return link;
	}

	public String[] getInfo() {
		return infoArray;
	}
}
