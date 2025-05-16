package com.online.download;

import java.util.List;
import java.util.Map;

import com.online.download.service.FileDownloader;
import com.online.download.service.LinkGenerator;
import com.online.download.service.ZipConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DownloadStart {

    private static LinkGenerator linkGenerator = new LinkGenerator();
    private static FileDownloader fileDownloader = new FileDownloader();

    public static void main(String[] args) {

	String globalLink = "http://www.readcomics.tv/comic/";
	String extension = "jpg";
	String[] comicNameList = {"cthulhu-williams-2016" };

	for (String comicName : comicNameList) {
	    log.info("Begin download process with arguments: " + globalLink + ", " + extension + ", " + comicName);

	    try {
			Map<String, List<String>> downloadLinks = linkGenerator.generateDownloadLinks(globalLink, comicName);
			log.info("Begin downloading images for all " + downloadLinks.size() + " comics found");
			Integer volumeNumber = 1;
			Integer pageNumber = 1;
		// TODO guardar en un archivo los links de descagaras y el
		// indice
			for (; volumeNumber <= downloadLinks.size();) {
				for (String link : downloadLinks.get(volumeNumber.toString())) {
				String fileName = comicName + correctNumber(pageNumber++) + "." + extension;
				fileDownloader.downloadFile(link, null, null, null, comicName, volumeNumber, fileName);

				}
				ZipConverter.compress(comicName, volumeNumber);
				volumeNumber++;
				pageNumber = 1;
			}

			} catch (Exception e) {

			e.printStackTrace();
			}
		}
    }

    private static String correctNumber(Integer pageNumber) {

	if (pageNumber < 10)
	    return "00" + (pageNumber);
	else if (pageNumber >= 10 && pageNumber < 100)
	    return "0" + (pageNumber);
	else 
	    return "" + (pageNumber);

    }
}
