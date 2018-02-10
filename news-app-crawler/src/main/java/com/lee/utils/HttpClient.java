package com.lee.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by honghong on 16/9/28.
 */
public class HttpClient {

  protected static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);
  private static final int CONNECT_TIME = 20000;
  private static final int TRANSFER_TIME = 10000;
  protected CloseableHttpClient httpClient;
  private RequestConfig requestConfig;

  public HttpClient() {
    httpClient = HttpClients.createSystem();
    requestConfig = RequestConfig.custom().setSocketTimeout(TRANSFER_TIME).setConnectTimeout(CONNECT_TIME).build();
  }

  public String executeHttpGet(String url) {
    try {
      LOGGER.debug("Http get url: {}", url);

      HttpGet httpGet = new HttpGet(url);
      // 伪装user agent
      httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
      httpGet.setConfig(requestConfig);

      try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
        HttpEntity entity = httpResponse.getEntity();
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
          return EntityUtils.toString(entity);
        }
      }
    } catch (Exception e) {
      LOGGER.error("http get error", e);
    }
    return null;
  }
}
