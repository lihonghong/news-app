import urllib2 # connect url and get data
import chardet  # detect encoding character of HTML file
from bs4 import BeautifulSoup # extract data

class HotWords:
    def open_url(self, url):
        opener = urllib2.build_opener()           # create an OpenerDirector object
        content = opener.open(url).read()         # open the url and get the corresponding HTML contents
        encoding = chardet.detect(content)['encoding']  # changing character encoding
        content = content.decode(encoding, 'ignore')
        return content

    def get_hotwords(self, content):
        soup = BeautifulSoup(content,'lxml')
        keyword_list = [keyword_str.find('a').text.strip() for keyword_str in soup.find_all('td', class_='keyword')]
        return keyword_list