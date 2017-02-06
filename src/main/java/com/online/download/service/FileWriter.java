package com.online.download.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

public class FileWriter {

    private final static Logger LOGGER = Logger.getLogger(FileWriter.class);

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
	    LOGGER.info("Unable to read input stream: " + ex);
	} catch (RuntimeException ex) {
	    LOGGER.info("Unexpected error ocurred: " + ex);
	} finally {
	    instream.close();
	}
	return true;

    }

    private static String createDir(String string) {

	File theDir = new File(string.substring(0, string.lastIndexOf("/")));

	if (!theDir.exists()) {
	    LOGGER.info("Creating download directory: " + theDir.getName());
	    boolean result = false;
	    try {
		result = theDir.mkdirs();
		//result = true;
	    } catch (SecurityException se) {
		LOGGER.info("There has been a secutiry error: "+ se);
	    }
	    if (result) {
		LOGGER.info("Output directory succesfully created");
	    }
	}
	return string;
    }

}
