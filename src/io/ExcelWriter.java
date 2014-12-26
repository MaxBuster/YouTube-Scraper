package io;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import objects.VideoInfo;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelWriter {
	private HashMap<String, VideoInfo> linksToInfo;
	private LinkedList<String> listOfLinks;
	private WritableWorkbook workbook;

	public ExcelWriter(HashMap<String, VideoInfo> linksToInfo) {
		super();
		this.linksToInfo = linksToInfo;
		this.listOfLinks = makeListFromHashMap();
		this.workbook = createNewWorkbook();
	}

	public LinkedList<String> makeListFromHashMap() {
		Set<String> setOfLinks = linksToInfo.keySet();
		LinkedList<String> allLinks = new LinkedList<String>();
		for (String link : setOfLinks) {
			allLinks.add(link);
		}
		return allLinks;
	}

	public WritableWorkbook createNewWorkbook() {
		try {
			String date = getDate();		
			String fileName = date + "YouTubeInfo.xls"; 
			WritableWorkbook workbook = Workbook.createWorkbook(new File(fileName));
			return workbook;
		} catch (IOException e) {
			System.out.println("Excel Write Failed");
			return null;
		}
	}

	public String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("d-M-y");
		Calendar calendarInstance = Calendar.getInstance();
		String date = dateFormat.format(calendarInstance.getTime()); 
		return date;
	}

	public void writeLabels(WritableSheet sheet) {
		String[] labels = new String[] {"Link", "Title", "Poster", "Poster Subscribers",
				"Views", "Likes", "Dislikes", "Post Data", "Category"};
		int column = 1;
		for (String label : labels) {
			try {
				Label currentLabel = new Label(column, 1, label);
				sheet.addCell(currentLabel);
				column++;
			} catch (RowsExceededException e) {
				System.out.println("Too Many Rows For Excel Writeout");
			} catch (WriteException e) {
				System.out.println("Problem With Excel Writing");
			}
		} 
	}

	public void addRow(String link,
			WritableSheet sheet, 
			String[] info, 
			int rowsDown) {
		try {
			Label linkCellText = new Label(1, rowsDown, link);
			sheet.addCell(linkCellText);
			for (int colsOver = 2; colsOver < 10; colsOver++){
				Label cellText = new Label(colsOver, rowsDown, info[colsOver-2]);
				sheet.addCell(cellText);
			}
		} catch (WriteException e) {
			System.out.println("Write Exception");
		}
	}

	public void writeOutToExcel() {
		int numLinks = linksToInfo.size();
		int rowMax = 50000;
		int numSheets = getNumSheets(numLinks);

		for (int i=0; i<numSheets; i++) {
			String sheetName = "Sheet- " + i;
			WritableSheet currentSheet = workbook.createSheet(sheetName, i);
			writeLabels(currentSheet);
			for (int rowsDown=2; rowsDown<rowMax; rowsDown++) {
				if (!linksToInfo.isEmpty()) {
					String linkToAdd = getLink();
					String[] info = getInfoFromLink(linkToAdd);
					addRow(linkToAdd, currentSheet, info, rowsDown);
				} else {
					break;
				}
			}
		}

		closeAndWriteWorkbook();
	}

	public int getNumSheets(int numLinks) {
		int rowMax = 50000;
		long unroundedNumSheets = numLinks/rowMax;
		int numSheets = (int) unroundedNumSheets;
		int realNumSheets = numSheets + 1;
		return realNumSheets;
	}
	
	public String getLink() {
		String linkToAdd = listOfLinks.removeFirst();
		return linkToAdd;
	}

	public String[] getInfoFromLink(String linkToAdd) {
		VideoInfo videoInfo = linksToInfo.remove(linkToAdd);
		String[] info = videoInfo.getInfo();
		return info;
	}

	public synchronized void closeAndWriteWorkbook() {
		try {
			workbook.write();
			workbook.close();
		} catch (IOException e) {
			System.out.println("IO Exception");
		} catch (WriteException e) {
			System.out.println("Write failed");
		}		
	}

}
