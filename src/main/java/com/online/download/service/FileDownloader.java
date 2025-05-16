package com.online.download.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URI;

@Slf4j
public class FileDownloader {

    private CloseableHttpClient createConnection(String proxyHost, int proxyPort, String proxyUser, String proxyPass) {
        BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();

        if (proxyHost != null && proxyUser != null && proxyPass != null) {
            credsProvider.setCredentials(
                    new AuthScope(proxyHost, proxyPort),
                    new UsernamePasswordCredentials(proxyUser, proxyPass.toCharArray())
            );
        }

        return HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
    }

    public Boolean downloadFile(String fullUrl, String proxyHost, String proxyUserName, String proxyPassword,
                                String comicName, int volumeNumber, String fileName) throws Exception {

        URI uri = new URIBuilder(fullUrl).build();
        HttpHost target = new HttpHost(uri.getScheme(), uri.getHost(), uri.getPort() != -1 ? uri.getPort() : 80);

        int proxyPort = 8080; // Default proxy port if needed
        if (proxyHost != null && proxyHost.contains(":")) {
            String[] parts = proxyHost.split(":");
            proxyHost = parts[0];
            proxyPort = Integer.parseInt(parts[1]);
        }

        CloseableHttpClient httpClient = createConnection(proxyHost, proxyPort, proxyUserName, proxyPassword);
        HttpGet httpGet = new HttpGet(uri);

        RequestConfig config = RequestConfig.custom()
                .setRedirectsEnabled(true)
                .build();
        httpGet.setConfig(config);

        httpGet.addHeader("User-Agent", "Mozilla/5.0");

        log.info("Starting to download from: {}", fullUrl);

        try (CloseableHttpResponse response = httpClient.execute(target, httpGet)) {
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                boolean success = FileWriter.writeToFile(entity, fileName, comicName, volumeNumber);
                log.info("Writing to file success? {}", success);
                return success;
            }
        } finally {
            httpClient.close();
        }

        return false;
    }
}
