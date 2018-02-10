package com.lee.utils;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by honghong on 16/12/24.
 */
public class Constants {
  public static final String UNKNOWN = "unknown";
  public static final String NEWS = "news";
  public static final String WEIBO = "weibo";

  public static final Map<String, String> TYPE_URL_HTML_MAP = new LinkedHashMap<String, String>() {
    {
      put("ent", "http://top.baidu.com/buzz?b=344&c=513&fr=topbuzz_b342_c513");
      put("society", "http://top.baidu.com/buzz?b=342&c=513&fr=topbuzz_b42_c513");
      put("unknown", "http://top.baidu.com/buzz?b=1&c=513&fr=topbuzz_b341_c513");
    }
  };

  public static final Set<String> SOGOU_HTML_SET = new LinkedHashSet<String>() {
    {
      add("http://top.sogou.com/hot/shishi_1.html");
      add("http://top.sogou.com/hot/shishi_2.html");
      add("http://top.sogou.com/hot/shishi_3.html");
    }
  };

  public static final Set<String> SHEN_MA_JSON_SET = new LinkedHashSet<String>() {
    {
      add("http://api.m.sm.cn/rest?method=tools.hot&source=home&start=1");
      add("http://api.m.sm.cn/rest?method=tools.hot&source=home&start=2");
      add("http://api.m.sm.cn/rest?method=tools.hot&source=home&start=3");
    }
  };

  public static final Set<String> WEIXIN_HTML_SET = new LinkedHashSet<String>() {
    {
      add("http://weixin.sogou.com/");
    }
  };
}
