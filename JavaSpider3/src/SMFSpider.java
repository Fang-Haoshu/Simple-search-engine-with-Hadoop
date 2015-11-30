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

	public static int maxPage = 10000; // ��������50������
	public static String listUrl = "http://tieba.baidu.com/f?kw=%E4%B8%8A%E6%B5%B7%E4%BA%A4%E9%80%9A%E5%A4%A7%E5%AD%A6&ie=utf-8&pn=";
	public static String rootUrl = "http://tieba.baidu.com";
	public static String pathString1 = "f:\\craw";
	public static String pathString2 = "f:\\split";// ���λ��
	public static Queue<Bean> uUrlQueue = new LinkedList<>(); // Ϊ������url����
	public static int maxinPage = 100;//��Ϊһ���������ظ�ҳ������3000�����Լ�����һ������

	public static void GrabPage(Bean bean, int max) {

		//�ж��Ƿ񳬹����ظ�ҳ��
		String urlPrefix = bean.url.substring(0, bean.url.indexOf("=") + 1);
		int nowPage = (Integer
				.valueOf(bean.url.substring(bean.url.indexOf("=") + 1)));
		
		
		if (nowPage > max || nowPage > maxinPage) {
			return ;
		}
		System.out.printf("����" + bean.url + "��ʼ������");
		//��ҳ��ظ�����
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

		//������һ����
		
		System.out.println("���\n");
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
		// ��ȡhtml
		
		Document document = null;

		
		for (int page = 0; page <= maxPage; page += 50) { // page = 0 50 100 150

			listUrl += page;
			document = Jsoup.connect(listUrl).get();

			if (document != null ) {

				// ÿ�����Ӽ�Ҫ������������url,title,
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

				// ����ÿһ����url��ȡ content �� hot

				for (Bean bean : uUrlQueue) {
					
					bean.url += "?pn=1";
					// ������������
					Document doc = null;
					try {
						doc = Jsoup.connect(bean.url).get();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					int maxPage; //һ���������ظ�ҳ��
					Element hreflistElement = doc.select("li[class=l_pager pager_theme_5 pb_list_pager]").last();
					if (hreflistElement==null) {
						maxPage = 1;
					}else {
						Elements alistElements =  hreflistElement.select("a");
						if (alistElements == null) {//ֻ��һҳ�ظ�
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
					
					
					
					//�ݹ����
					String url = bean.url;
					bean.content = new StringBuffer();

					GrabPage(bean,maxPage);
					bean.url = url;
					//System.out.println(pathString+bean.url.substring(bean.url.indexOf("/p/")+3,bean.url.indexOf('?'))+".txt");

				}
				// ���
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
