/**
 * 
 */
/**
 * @author Tincho
 *
 */
package com.online.download.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// Doicuemtacion https://jsoup.org/cookbook/

public class LinkGenerator {

    private final static Logger LOGGER = Logger.getLogger(LinkGenerator.class);

    public Map<String, List<String>> generateDownloadLinks(String uri, String comicName, String extension) throws Exception {

	LOGGER.info("Generating download link for URL: " + uri + comicName + ", with extension: " + extension);
	Map<String, List<String>> downloadLinks = getDownloadURL(uri, comicName, extension);

	LOGGER.info("finished genetaring map with download links");

	return downloadLinks;
    }

    private Map<String, List<String>> getDownloadURL(String url, String comicName, String extension) throws IOException {

	Document doc = Jsoup.connect(url + comicName).userAgent("Mozilla").get();

	LOGGER.debug("Got documento for total amount of comics: " + doc.toString());

	Map<String, List<String>> comicLinks = getComicLinks(doc, comicName);

	LOGGER.info("Total comics found: " + comicLinks.size());

	return comicLinks;

    }

    private Map<String, List<String>> getComicLinks(Document doc, String comicName) {

	Map<String, List<String>> comicsList = new HashMap<String, List<String>>();

	Elements links = doc.select("a[href]");
	String key;
	String volumenNumber;
	LOGGER.info("Getting comic book links");

	for (Element element : links) {
	    if (element.toString().contains("chapter-") && element.toString().contains(comicName)) {
		String link = (element.attr("href"));
		LOGGER.info(link);
		try {
		    volumenNumber = link.substring(link.lastIndexOf("chapter-"), link.length()).replace("chapter-", "");
		    key = volumenNumber.length() == 1 ? "0" + volumenNumber : volumenNumber;
		    LOGGER.debug("Storing page links using key: " + key);
		    comicsList.put(key, getPageLinks(Jsoup.connect(link).userAgent("Mozilla").get(),comicName ,volumenNumber));
		} catch (IOException e) {

		    e.printStackTrace();
		}
	    }
	}

	if (comicsList.size() == 0) {
	    LOGGER.info("Found no comics in URL, exiting app");
	    System.exit(0);
	}

	LOGGER.info("Finished getting comic book links, sorting..");
	Map<String, List<String>> treeMap = new TreeMap<String, List<String>>(comicsList);
	LOGGER.info("Finished sorting map... " + treeMap);
	return treeMap;
    }

    private List<String> getPageLinks(Document doc,String comicName ,String volumeNumber) {
	
	List<String> pagesLinks = new ArrayList<String>();
	
	Elements links = doc.select("div[class=label]");
	int totalPages = 0;
	LOGGER.info("Creating download page link");

	for (Element element : links) {
	    if (element.text().contains("of ")) {
		totalPages = Integer.valueOf(element.text().replace("of ", ""));
		LOGGER.info("The volumen" + volumeNumber + " for the comicbook " + comicName + " has a total of ");
		
	    }
	}

	for(int i = 0; i < totalPages; i++){
	    String page = ("http://www.readcomics.tv/images/manga/"+comicName+"/"+volumeNumber+"/"+(i+1)+".jpg");
	    LOGGER.info("Creating page link: " + page);
	    pagesLinks.add(page); 
	}
	    
	return pagesLinks;
    }

}
