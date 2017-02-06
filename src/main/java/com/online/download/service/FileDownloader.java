package com.online.download.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

public class FileDownloader {

    private final static Logger LOGGER = Logger.getLogger(FileDownloader.class);

    private CloseableHttpClient createConnection(String proxyUrl, String proxyUserName, String proxyPassword) {

	Registry<AuthSchemeProvider> r = RegistryBuilder.<AuthSchemeProvider> create().register(AuthSchemes.BASIC, new BasicSchemeFactory()).register(AuthSchemes.DIGEST, new DigestSchemeFactory())
		.build();

	CredentialsProvider credsProvider = new BasicCredentialsProvider();
	if (proxyUrl != null)
	    credsProvider.setCredentials(new AuthScope(proxyUrl, 80), new UsernamePasswordCredentials(proxyUserName, proxyPassword));

	return HttpClients.custom().setDefaultAuthSchemeRegistry(r).setDefaultCredentialsProvider(credsProvider).build(); //
    }

    public Boolean downloadFile(String url, String proxyUrl, String proxyUserName, String proxyPassword, String comicName, int volumeNumber, String fileName) throws Exception {

	url = url.replace("http://", "").replace("https://", "");
	String uri = getUri(url);
	url = getUrl(url);
	LOGGER.info("Connecting to begin download...");
	CloseableHttpClient httpclient = createConnection(proxyUrl, proxyUserName, proxyPassword);

	try {

	    LOGGER.info("Starting to download from: " + url + uri);

	    HttpHost target = new HttpHost(url, 80, "http");

	    RequestConfig config = RequestConfig.custom().build();
	    HttpGet httpget = new HttpGet(uri);

	    httpget.setConfig(config);

	    LOGGER.info("Executing request " + httpget.getRequestLine() + " to " + target + " via " + proxyUserName);

	    CloseableHttpResponse response = httpclient.execute(target, httpget);

	    try {
		HttpEntity entity = response.getEntity();

		if (entity != null) {

		    LOGGER.info("¿Writing to file success? " + FileWriter.writeToFile(entity, fileName, comicName, volumeNumber));

		}
	    } finally {
		response.close();
	    }
	} finally {
	    httpclient.close();
	}
	return true;
    }

    private String getUri(String url) {

	return url.substring(url.indexOf("/"), url.length());
    }

    private String getUrl(String url) {

	url = url.substring(0, url.indexOf("/"));

	return url;
    }

}
