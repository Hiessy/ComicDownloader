/**
 * 
 */
/**
 * @author Tincho
 *
 */
package com.online.download.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Doicuemtacion https://jsoup.org/cookbook/
@Slf4j
public class LinkGenerator {

    public Map<String, List<String>> generateDownloadLinks(String uri, String comicName) throws Exception {

	log.debug("Generating download link for URL: " + uri + comicName);
	Map<String, List<String>> downloadLinks = getDownloadURL(uri, comicName);

	log.debug("finished genetaring map with download links");

	return downloadLinks;
    }

    private Map<String, List<String>> getDownloadURL(String url, String comicName) throws IOException {

	Document doc = Jsoup.connect(url + comicName).userAgent("Mozilla").get();

	log.debug("Got document for total amount of comics: " + doc.toString());

	Map<String, List<String>> comicLinks = getComicLinks(doc, comicName);

	log.debug("Total comics found: " + comicLinks.size());

	return comicLinks;

    }

    private Map<String, List<String>> getComicLinks(Document doc, String comicName) {

	Map<String, List<String>> comicsList = new HashMap<String, List<String>>();

	Elements links = doc.select("a[href]");
	Integer volumenNumber = 0;
	log.info("Getting comic book links");

	for (Element element : links) {
		log.debug("Analizing link: " + element);
	    if (element.toString().contains("chapter-") && element.toString().contains(comicName) && !element.toString().contains("single")) {
		String link = (element.attr("href"));
		log.info(link);
		volumenNumber = Integer.valueOf(link.substring(link.lastIndexOf("chapter-") + 8, link.length()));
		try {
			log.info("Storing page links for volume number: " + volumenNumber);
		    if (comicsList.get(volumenNumber.toString()) == null)
			comicsList.put(volumenNumber.toString(), getPageLinks(Jsoup.connect(link).userAgent("Mozilla").get(), comicName, volumenNumber.toString()));
		    else
			log.info("volumen number: " + volumenNumber +", already inserted");
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

	return comicsList;
    }

    private List<String> getPageLinks(Document doc, String comicName, String volumeNumber) {

	List<String> pagesLinks = new ArrayList<String>();

	Elements links = doc.select("div[class=label]");
	int totalPages = 0;
	LOGGER.debug("Creating download page link");

	for (Element element : links) {
	    if (element.text().contains("of ")) {
		totalPages = Integer.valueOf(element.text().replace("of ", ""));
		LOGGER.debug("The volumen" + volumeNumber + " for the comicbook " + comicName + " has a total of ");
		break;
	    }
	}

	for (int i = 0; i < totalPages; i++) {
	    String page = ("http://www.readcomics.tv/images/manga/" + comicName + "/" + volumeNumber + "/" + (i + 1) + ".jpg");
	    LOGGER.debug("Creating page link: " + page);
	    pagesLinks.add(page);
	}

	return pagesLinks;
    }

}
