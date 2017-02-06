package com.online.download.service;

import org.apache.log4j.Logger;
import java.util.zip.*;
import java.io.*;

public class ZipConverter 
{
	private static final int BUFFER = 2048;
	private final static Logger LOGGER = Logger.getLogger(ZipConverter.class);
	
	public static void compress(String comicName, int volumeNumber)
	{
		try 
		{
			LOGGER.info("Init compression");
			
			String filePath = "comics/" + comicName +"/";
			String folder = comicName + volumeNumber;
			String zipName = folder+".zip";
			File zipFile = new File(filePath+zipName);
			FileOutputStream out = new FileOutputStream(zipFile);
			ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(out));
			byte[] data = new byte[BUFFER];
			File f = new File(filePath+folder);
			String files [] = f.list();
			
			for(int i = 0; i < files.length; i++)
			{
				LOGGER.info("Adding file: " + files[i] +" to: "+zipName);
				File file = new File(filePath+folder+"/"+files[i]);
				FileInputStream fi = new FileInputStream(file);
				BufferedInputStream in = new BufferedInputStream(fi,BUFFER);
				ZipEntry entry = new ZipEntry(files[i]);
				zout.putNextEntry(entry);
				int count;
				while((count = in.read(data, 0, BUFFER)) != -1)
				{
					zout.write(data, 0, count);
				}
				
				in.close();
				LOGGER.info("¿Deleting file: "+file.getName()+" from: "+file.getPath()+"? "+file.delete());
			}
			zout.close();
			LOGGER.info("¿Deleting directory: "+f.getPath()+"? "+f.delete());
			
			
		} catch (Exception e) 
		{
			LOGGER.info(e.getMessage());
		}
		
	}
}
