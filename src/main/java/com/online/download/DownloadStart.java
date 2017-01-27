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

	if (args.length != 3) {
	    LOGGER.info("Invalid amount of arguments, expeting 3 arguments");
	    System.exit(1);
	}
	LOGGER.info("Begin download process with arguments: " + args[0] + ", " + args[1] + ", " + args[2]);

	try {
	    Map<String, List<String>> downloadLinks = linkGenerator.generateDownloadLinks(args[0], args[1], args[2]);
	    LOGGER.info("Begin downloagin images");
	    Integer pageNumber = 1;
	    for (Map.Entry<String, List<String>> entry : downloadLinks.entrySet()) {
		for (String link : entry.getValue()) {
			fileDownloader.downloadFile(link, null, null, null,args[1], args[1] + correctNumber(pageNumber++) + ".jpg");
		}

	    }

	} catch (Exception e) {

	    e.printStackTrace();
	}

    }

    private static String correctNumber(Integer pageNumber){
	String number;
	    if (pageNumber < 10)
		number =  "000" + (pageNumber);
	    else if (pageNumber >= 10 && pageNumber < 100)
		number =  "00" + (pageNumber);
	    else if (pageNumber >= 100 && pageNumber < 1000)
		number =  "0" + (pageNumber);
	    else
		number =  "" +pageNumber;
	return number;
    }
}
