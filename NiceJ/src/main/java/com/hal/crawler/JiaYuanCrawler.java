package com.hal.crawler;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class JiaYuanCrawler extends WebCrawler {
    /** 爬取数据保存文件路径 */
    private final static String CSV_PATH = "E:\\test\\jiayuan.csv";
    /** 爬取匹配原则 */
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g|ico"
            + "|png|tiff?|mid|mp2|mp3|mp4" + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
 
    private static final Logger logger = LoggerFactory.getLogger(JiaYuanCrawler.class);

    private final static Pattern URL_PREFIX =  Pattern.compile("^http://www.youyuan.com/find/beijing/mm18-28/advance-0-0-0-0-0-0-0/p.*");

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    CrawlStatJiaYuan myCrawlStat;

    public JiaYuanCrawler() throws IOException {
        myCrawlStat = new CrawlStatJiaYuan();
    }
    
    public void dumpMyData() {
        final int id = getMyId();
        // You can configure the log to output to file
        logger.info("Crawler {} > Processed Pages: {}", id, myCrawlStat.getTotalProcessedPages());
        logger.info("Crawler {} > Total Links Found: {}", id, myCrawlStat.getTotalLinks());
        logger.info("Crawler {} > Total Text Size: {}", id, myCrawlStat.getTotalTextSize());
    }

    @Override
    public Object getMyLocalData() {
        return myCrawlStat;
    }

    @Override
    public void onBeforeExit() {
        dumpMyData();
    }

    /*
     * 这个方法决定了要抓取的URL及其内容，例子中只允许抓取“http://sh.ziroom.com/z/nl/”这个域的页面,
     * 不允许.css、.js和多媒体等文件
     *
     * @see edu.uci.ics.crawler4j.crawler.WebCrawler#shouldVisit(edu.uci.ics.
     * crawler4j.crawler.Page, edu.uci.ics.crawler4j.url.WebURL)
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        final String href = url.getURL().toLowerCase();
        if (FILTERS.matcher(href).matches() || !URL_PREFIX.matcher(href).matches()) {
            return false;
        }
        return true;
    }

    /*
     * 当URL下载完成会调用这个方法。你可以轻松获取下载页面的url, 文本, 链接, html,和唯一id等内容。
     *
     * @see
     * edu.uci.ics.crawler4j.crawler.WebCrawler#visit(edu.uci.ics.crawler4j.
     * crawler.Page)
     */
    @Override
    public void visit(Page page) {
        final String url = page.getWebURL().getURL();
        System.out.println("-----------爬取路径：" + url);
        myCrawlStat.incProcessedPages();
        if (page.getParseData() instanceof HtmlParseData) {
            final HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            try {
                myCrawlStat.incTotalTextSize(htmlParseData.getText().getBytes("UTF-8").length);
            } catch (final UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            final String html = htmlParseData.getHtml();

            final Document doc = Jsoup.parse(html);

            final Elements elements = doc.select(".mian.search_list").select(".search_user_item,.last_li search_user_item"); 

            downimage d = new downimage();

            for (int i = 0; i < elements.size(); i++) {
				String img = elements.get(i).select("a img").first().attr("src");
				String name = elements.get(i).select("a strong").first().text();
				d.saveToFile(img,name);
			}
            
        }
    }
    
    public static class downimage {

		public void saveToFile(String destUrl,String name) {
			FileOutputStream fos = null;
			BufferedInputStream bis = null;
			HttpURLConnection httpUrl = null;
			URL url = null;
			int BUFFER_SIZE = 1024;
			byte[] buf = new byte[BUFFER_SIZE];
			int size = 0;
			try {
				url = new URL(destUrl);
				httpUrl = (HttpURLConnection) url.openConnection();
				httpUrl.connect();
				bis = new BufferedInputStream(httpUrl.getInputStream());
				fos = new FileOutputStream("E:\\test\\img\\" + name + destUrl.substring(destUrl.lastIndexOf(".")));
				while ((size = bis.read(buf)) != -1) {
					fos.write(buf, 0, size);
				}
				fos.flush();
			} catch (IOException e) {
			} catch (ClassCastException e) {
			} finally {
				try {
					fos.close();
					bis.close();
					httpUrl.disconnect();
				} catch (IOException e) {
				} catch (NullPointerException e) {
				}
			}
		}
	}

}