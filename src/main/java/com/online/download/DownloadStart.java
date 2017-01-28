package com.online.download;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.online.download.service.FileDownloadar;
import com.online.download.service.LinkGenerator;

public class DownloadStart {

    private final static Logger LOGGER = Logger.getLogger(DownloadStart.class);
    private static LinkGenerator linkGenerator = new LinkGenerator();
    private static FileDownloadar fileDownloader = new FileDownloadar();

    public static void main(String[] args) {

	String globalLink = "http://www.readcomics.tv/comic/";
	String extension = "jpg";
	String comicName = "bone";

	LOGGER.info("Begin download process with arguments: " + globalLink + ", " + extension + ", " + comicName);

	try {
	    Map<String, List<String>> downloadLinks = linkGenerator.generateDownloadLinks(globalLink, comicName);
	    LOGGER.info("Begin downloagin images");
	    Integer pageNumber = 1;
	    // TODO guardar en un archivo los links de descagaras y el indice
	    for (; pageNumber <= downloadLinks.size(); ) {
		for (String link : downloadLinks.get(pageNumber.toString())) {
		    String fileName = comicName + correctNumber(pageNumber++) + "." + extension;
		    fileDownloader.downloadFile(link, null, null, null, comicName, fileName);

		}
	    }

	} catch (Exception e) {

	    e.printStackTrace();
	}

    }

    private static String correctNumber(Integer pageNumber) {
	String number;
	if (pageNumber < 10)
	    number = "000" + (pageNumber);
	else if (pageNumber >= 10 && pageNumber < 100)
	    number = "00" + (pageNumber);
	else if (pageNumber >= 100 && pageNumber < 1000)
	    number = "0" + (pageNumber);
	else
	    number = "" + pageNumber;
	return number;
    }
}
