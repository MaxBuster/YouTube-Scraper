package crawler;

/**
 * This program crawl YouTube for educational videos related to a list of input videos.
 * It uses jsoup to scrape the links for the video info and videos in the related tab.
 * It then write the following lists of links to text files: to crawl in the future, 
 * links that were successfully crawled, and links that were crawled but were not educational videos.
 * Finally, it writes out video information for successfully crawled links to an excel file.
 * @author Max Buster
 */

public class ConcurrentCrawler implements Runnable {
	private int numberOfLinksToCrawl;
	private Crawler crawler;

	public ConcurrentCrawler(int numberOfLinksToCrawl) {
		super();
		this.numberOfLinksToCrawl = numberOfLinksToCrawl;
		this.crawler = new Crawler(this.numberOfLinksToCrawl);
	}

	@Override
	public void run() {
		int numThreads = Runtime.getRuntime().availableProcessors();
		Thread[] arrayOfThreads = new Thread[numThreads];
		for (int i=0; i<numThreads; i++) {
			Thread newThread = newThread();
			arrayOfThreads[i] = newThread;
		}
		joinThreads(arrayOfThreads);
		crawler.closeListsAndWriteOut();
	}
	
	public Thread newThread() {
		Thread newThread = new Thread() {
			public void run() {
				crawler.crawl();
			}
		};
		newThread.start();
		return newThread;
	}
	
	public void joinThreads(Thread[] arrayOfThreads) {
		for (Thread thread : arrayOfThreads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		Runnable concurrentCrawlerRunnable = new ConcurrentCrawler(10);
		concurrentCrawlerRunnable.run();
	}
}
