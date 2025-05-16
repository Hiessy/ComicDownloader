package com.online.download.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.core5.http.HttpEntity;


@Slf4j
public class FileWriter {

    public static boolean writeToFile(HttpEntity entity, String fileName, String comicName, int volumeNumber) throws ClientProtocolException, IOException {

	InputStream instream = entity.getContent();
	try {
	    BufferedInputStream bis = new BufferedInputStream(instream);
	    String filePath = createDir("comics/"+comicName+"/"+comicName+volumeNumber+"/") + fileName;
	    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
	    int inByte;
	    while ((inByte = bis.read()) != -1) {
		bos.write(inByte);
	    }
	    bis.close();
	    bos.close();
	} catch (IOException ex) {
	    log.info("Unable to read input stream: " + ex);
	} catch (RuntimeException ex) {
		log.info("Unexpected error ocurred: " + ex);
	} finally {
	    instream.close();
	}
	return true;

    }

    private static String createDir(String string) {

	File theDir = new File(string.substring(0, string.lastIndexOf("/")));

	if (!theDir.exists()) {
		log.info("Creating download directory: " + theDir.getName());
	    boolean result = false;
	    try {
		result = theDir.mkdirs();
		//result = true;
	    } catch (SecurityException se) {
			log.info("There has been a secutiry error: "+ se);
	    }
	    if (result) {
			log.info("Output directory succesfully created");
	    }
	}
	return string;
    }

}
