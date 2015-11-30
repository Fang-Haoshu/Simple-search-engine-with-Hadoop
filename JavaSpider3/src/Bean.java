import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Bean {
	public String url;
	public String hot;
	public String author;
	public StringBuffer content;
	public String title;
	
	
	public void Output1(String rootpath,String file){
		//目录新建
		File rootPathFile = new File(rootpath);
		if (!rootPathFile.exists()) {
			rootPathFile.mkdir();
		}
		//文件新建
		File file2 = new File(rootpath,file);
		if (!file2.exists()) {
			try {
				file2.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//写入文件
		FileWriter writer;
		try {
			writer = new FileWriter(file2);
			
			writer.write(title+"\n"+content.toString()+"\n"+author+"\n"+hot+"\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public void Output2(String rootpath,String file){
		//目录新建
		File rootPathFile = new File(rootpath);
		if (!rootPathFile.exists()) {
			rootPathFile.mkdir();
		}
		//文件新建
		File file2 = new File(rootpath,file);
		if (!file2.exists()) {
			try {
				file2.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//写入文件
		FileWriter writer;
		try {
			writer = new FileWriter(file2);
			
			Analyzer anal=new IKAnalyzer(true);		
			StringReader reader=new StringReader(title);
			//分词
			TokenStream ts=anal.tokenStream("", reader);
			CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);
			//遍历分词数据
			while(ts.incrementToken()){
				writer.write(term.toString()+" ");
			}
			
			anal=new IKAnalyzer(true);		
			reader=new StringReader(content.toString());
			//分词
			ts=anal.tokenStream("", reader);
			term=ts.getAttribute(CharTermAttribute.class);
			//遍历分词数据
			while(ts.incrementToken()){
				writer.write(term.toString()+" ");
			}
					
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
