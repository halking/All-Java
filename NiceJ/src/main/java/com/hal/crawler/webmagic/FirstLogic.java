package com.hal.crawler.webmagic;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class FirstLogic  implements PageProcessor{

	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Spider.create(new FirstLogic()).addUrl("https://github.com/code4craft").addPipeline(new ConsolePipeline()).run();
	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

	@Override
	public void process(Page page) {
		 // 部分二：定义如何抽取页面信息，并保存下来
        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
        if (page.getResultItems().get("name") == null) {
            //skip this page
            page.setSkip(true);
        }
        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));

        // 部分三：从页面发现后续的url地址来抓取
        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/[\\w\\-]+/[\\w\\-]+)").all());
	}

}
