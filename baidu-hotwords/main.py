#!/usr/bin/python
#coding: utf-8

from crawlHotWords import HotWords

# 百度风云榜实时热点词
url="http://top.baidu.com/buzz?b=1&fr=topindex"
baidu_spider = HotWords()
content = baidu_spider.open_url(url)
hotword_list = baidu_spider.get_hotwords(content)
for k in hotword_list:
    print k