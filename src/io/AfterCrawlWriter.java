package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class AfterCrawlWriter {
	private LinkedList<String> listOfLinks;
	private BufferedWriter bufferedWriter;
	
	public AfterCrawlWriter(LinkedList<String> listOfLinks, String fileName) {
		super();
		this.listOfLinks = listOfLinks;
		this.bufferedWriter = makeBufferedWriterForFile(fileName);
	}
	
	public void writeDataToFile() {
		writeListToBufferedWriter();
		closeBufferedWriter();
	}

	public BufferedWriter makeBufferedWriterForFile(String fileName) {
		try {
			File file = new File(fileName);
			FileWriter fileWriter;
			fileWriter = new FileWriter(file);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			return bufferedWriter;
		} catch (IOException e) {
			System.out.println("File Write Didn't Work");
			return null;
		}
	}

	public void writeListToBufferedWriter() {
		String blankLine = "\n";
		for (String link : listOfLinks) {
			try {
				bufferedWriter.write(link + blankLine);
			} catch (IOException e) {
				System.out.println("File Write Failed");
			}
		}
	}

	public void closeBufferedWriter() {
		try {
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			System.out.println("BufferedWriter Close Failed");
		}
	}

}
