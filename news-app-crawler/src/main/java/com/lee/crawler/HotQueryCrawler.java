package com.lee.crawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lee.dao.HotQueryDao;
import com.lee.model.HotQuery;
import com.lee.utils.Constants;
import com.lee.utils.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by honghong on 16/9/26.
 */
public class HotQueryCrawler {

  private static final Logger LOGGER = LoggerFactory.getLogger(HotQueryCrawler.class);
  private HttpClient httpClient = new HttpClient();

  public static void main(String[] args) {
    HotQueryCrawler hotQueryCrawler = new HotQueryCrawler();
    List<HotQuery> list = hotQueryCrawler.crawlShenMaJson();
    for (HotQuery hotQuery : list) {
      LOGGER.info(hotQuery.toString());
    }
    LOGGER.info("count:" + list.size());

    HotQueryDao.getInstance().updateHotQueryList(list);
  }

  public List<HotQuery> getAllHotQuery() {
    LOGGER.info("start crawl realtime hotquery");
    List<HotQuery> hotQueryList = new ArrayList<>();
    hotQueryList.addAll(crawlBaiduHtml());
    hotQueryList.addAll(crawlSogouHtml());
    hotQueryList.addAll(crawlShenMaJson());
    hotQueryList.addAll(crawlWeixinHtml());
    return hotQueryList;
  }

  public List<HotQuery> crawlBaiduHtml() {
    List<HotQuery> hotQueryList = new ArrayList<>();
    for (Map.Entry<String, String> entry : Constants.TYPE_URL_HTML_MAP.entrySet()) {
      String type = entry.getKey();
      String url = entry.getValue();
      LOGGER.info("craw url: {}", url);
      hotQueryList.addAll(parseBaiduHtml(url, type));
    }
    return hotQueryList;
  }

  public List<HotQuery> crawlSogouHtml() {
    List<HotQuery> hotQueryList = new ArrayList<>();
    int n = 0;
    for (String url : Constants.SOGOU_HTML_SET) {
      LOGGER.info("craw url: {}", url);
      try {
        Document doc = Jsoup.parse(new URL(url).openStream(), "utf-8", url);
        Elements items = doc.getElementsByAttributeValue("class", "p1");
        items.addAll(doc.getElementsByAttributeValue("class", "p3"));
        for (Element item : items) {
          Element a = item.getElementsByTag("a").get(0);
          String keyword = a.text();
          HotQuery hotQuery = new HotQuery();
          hotQuery.setName(keyword);
          hotQuery.setSource("sogou");
          hotQuery.setType(Constants.NEWS);
          hotQuery.setCategory(Constants.UNKNOWN);
          hotQuery.setPosition(n);
          hotQueryList.add(hotQuery);
          n++;
        }
      } catch (Exception e) {
        LOGGER.error("jsoup fail {}", url, e);
      }
    }
    return hotQueryList;
  }

  public List<HotQuery> parseBaiduHtml(String url, String category) {
    List<HotQuery> hotQueryList = new ArrayList<>();
    try {
      Document doc = Jsoup.parse(new URL(url).openStream(), "gbk", url);
      Elements items = doc.getElementsByAttributeValue("class", "list-title");
      for (Element item : items) {
        Element a = item.getElementsByTag("a").get(0);
        String keyword = a.text();
        HotQuery hotQuery = new HotQuery();
        hotQuery.setName(keyword);
        hotQuery.setSource("baidu_html");
        hotQuery.setType(Constants.NEWS);
        hotQuery.setCategory(category);
        hotQuery.setPosition(hotQueryList.size());
        hotQueryList.add(hotQuery);
      }
    } catch (Exception e) {
      LOGGER.error("jsoup fail {}", url, e);
    }

    return hotQueryList;
  }

  public List<HotQuery> crawlWeixinHtml() {
    List<HotQuery> hotQueryList = new ArrayList<>();
    String type = Constants.UNKNOWN;
    for (String url : Constants.WEIXIN_HTML_SET) {
      LOGGER.info("craw url: {}", url);
      hotQueryList.addAll(parseWeixinHtml(url, type));
    }
    return hotQueryList;
  }

  public List<HotQuery> parseWeixinHtml(String url, String category) {
    List<HotQuery> hotQueryList = new ArrayList<>();
    try {
      Document doc = Jsoup.parse(new URL(url).openStream(), "utf-8", url);
      Elements items = doc.getElementsByAttributeValueMatching("uigs", "hotnews*");
      for (Element item : items) {
        Element a = item.getElementsByTag("a").get(0);
        String keyword = a.text();
        HotQuery hotQuery = new HotQuery();
        hotQuery.setName(keyword);
        hotQuery.setSource("weixin");
        hotQuery.setType(Constants.NEWS);
        hotQuery.setCategory(category);
        hotQuery.setPosition(hotQueryList.size());
        hotQueryList.add(hotQuery);
      }
    } catch (Exception e) {
      LOGGER.error("jsoup fail {}", url, e);
    }

    return hotQueryList;
  }

  public List<HotQuery> crawlShenMaJson() {
    List<HotQuery> hotQueryList = new ArrayList<>();
    int n = 0;
    String category = Constants.UNKNOWN;
    for (String url : Constants.SHEN_MA_JSON_SET) {
      try {
        String result = httpClient.executeHttpGet(url);
        JSONArray dataArray = JSON.parseArray(result);
        for (int i = 0; i < dataArray.size(); i++) {
          HotQuery hotQuery = new HotQuery();
          JSONObject itemObject = dataArray.getJSONObject(i);
          hotQuery.setName(itemObject.getString("title"));
          hotQuery.setSource("shenma");
          hotQuery.setType(Constants.NEWS);
          hotQuery.setCategory(category);
          hotQuery.setPosition(n);
          hotQueryList.add(hotQuery);
          n++;
        }
      } catch (Exception e) {
        LOGGER.error("crawl shen ma {}, json fail {}", url, e);
      }
    }
    return hotQueryList;
  }
}
