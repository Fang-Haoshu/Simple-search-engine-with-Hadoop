import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import java.io.StringReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SMFSpider {

	public static int maxPage = 10000; // 解析到《50的帖子
	public static String listUrl = "http://tieba.baidu.com/f?kw=%E4%B8%8A%E6%B5%B7%E4%BA%A4%E9%80%9A%E5%A4%A7%E5%AD%A6&ie=utf-8&pn=";
	public static String rootUrl = "http://tieba.baidu.com";
	public static String pathString1 = "f:\\craw";
	public static String pathString2 = "f:\\split";// 输出位置
	public static Queue<Bean> uUrlQueue = new LinkedList<>(); // 为解析子url存入
	public static int maxinPage = 100;//因为一个帖子最大回复页数可能3000，故自己设置一个上限

	public static void GrabPage(Bean bean, int max) {

		//判断是否超过最大回复页面
		String urlPrefix = bean.url.substring(0, bean.url.indexOf("=") + 1);
		int nowPage = (Integer
				.valueOf(bean.url.substring(bean.url.indexOf("=") + 1)));
		
		
		if (nowPage > max || nowPage > maxinPage) {
			return ;
		}
		System.out.printf("解析" + bean.url + "开始。。。");
		//本页面回复解析
		Document doc = null;
		try {
			doc = Jsoup.connect(bean.url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (doc == null) {
			return;
		}
		Elements elements = doc
				.select("div[class=d_post_content j_d_post_content]");
		for (Element element : elements) {
			bean.content.append(element.text() + " ");
			

		}

		//解析下一界面
		
		System.out.println("完成\n");
		bean.url = urlPrefix + (nowPage+1);
		GrabPage(bean,max);

	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		System.out.println("start");
		// 获取html
		
		Document document = null;

		
		for (int page = 0; page <= maxPage; page += 50) { // page = 0 50 100 150

			listUrl += page;
			document = Jsoup.connect(listUrl).get();

			if (document != null ) {

				// 每个帖子简要描述，包括：url,title,
				Elements outlineEles = document
						.select("div[class=col2_right j_threadlist_li_right]");
				Elements hotEles = document.select("div[class=t_con cleafix]");
				for (int i = 0; i < outlineEles.size(); i++) {
					Bean bean = new Bean();
					bean.url = rootUrl
							+ outlineEles.get(i).select("a[href]").first()
									.attr("href");
					bean.title = outlineEles.get(i).select("a[href]").first()
							.attr("title");
					bean.author = outlineEles.get(i).select("span").first()
							.attr("title");
					bean.hot = hotEles
							.get(i)
							.select("span[class=threadlist_rep_num center_text]")
							.first().text();
					uUrlQueue.offer(bean);
				}

				// 解析每一个子url获取 content 和 hot

				for (Bean bean : uUrlQueue) {
					
					bean.url += "?pn=1";
					// 求得最大字帖数
					Document doc = null;
					try {
						doc = Jsoup.connect(bean.url).get();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					int maxPage; //一个帖子最大回复页数
					Element hreflistElement = doc.select("li[class=l_pager pager_theme_5 pb_list_pager]").last();
					if (hreflistElement==null) {
						maxPage = 1;
					}else {
						Elements alistElements =  hreflistElement.select("a");
						if (alistElements == null) {//只有一页回复
							maxPage = 1;
						}else{
							Element lastPageHrefElement = hreflistElement.select("a").last();
							if (lastPageHrefElement == null) {
								maxPage = 1;
							}else {
								String lastPageHrefString = lastPageHrefElement.attr("href");
								maxPage = Integer.valueOf(lastPageHrefString.substring(lastPageHrefString.indexOf("=")+1));
							}
							
						}
						
					}
					
					
					
					//递归解析
					String url = bean.url;
					bean.content = new StringBuffer();

					GrabPage(bean,maxPage);
					bean.url = url;
					//System.out.println(pathString+bean.url.substring(bean.url.indexOf("/p/")+3,bean.url.indexOf('?'))+".txt");

				}
				// 输出
				while (uUrlQueue.peek() != null) {
					Bean bean = uUrlQueue.poll();
					bean.Output1(pathString1,bean.url.substring(bean.url.indexOf("/p/")+3,bean.url.indexOf('?'))+".txt");
					bean.Output2(pathString2,bean.url.substring(bean.url.indexOf("/p/")+3,bean.url.indexOf('?'))+".txt");
				}

			}
		}
		
		System.out.println("stop");
	}

}
