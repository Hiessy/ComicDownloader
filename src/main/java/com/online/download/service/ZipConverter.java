package com.online.download.service;

import lombok.extern.slf4j.Slf4j;

import java.util.zip.*;
import java.io.*;


@Slf4j
public class ZipConverter 
{
	private static final int BUFFER = 2048;

	public static void compress(String comicName, int volumeNumber)
	{
		try 
		{
			log.info("Init compression");
			
			String filePath = "comics/" + comicName +"/";
			String folder = comicName + volumeNumber;
			String zipName = folder+".cbr";
			File zipFile = new File(filePath+zipName);
			FileOutputStream out = new FileOutputStream(zipFile);
			ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(out));
			byte[] data = new byte[BUFFER];
			File f = new File(filePath+folder);
			String files [] = f.list();
			
			for(int i = 0; i < files.length; i++)
			{
				log.info("Adding file: " + files[i] +" to: "+zipName);
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
				log.info("�Deleting file: "+file.getName()+" from: "+file.getPath()+"? "+file.delete());
			}
			zout.close();
			log.info("�Deleting directory: "+f.getPath()+"? "+f.delete());
			
			
		} catch (Exception e) 
		{
			log.info(e.getMessage());
		}
		
	}
}
