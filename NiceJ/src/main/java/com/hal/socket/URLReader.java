package com.hal.socket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

/**
 * @author hal
 */
public class URLReader {

	/**
	 *2016年4月26日
	 * @param args
	 */
	public static void main(String[] args)  throws Exception{
       URL   url =  new URL("http://www.nowcoder.com/523171");
       BufferedReader  br = new BufferedReader(new InputStreamReader(url.openStream()));
       String path="e://tmp/out.txt";
       OutputStreamWriter   osw = new OutputStreamWriter(new FileOutputStream(path));
       String line;
       while((line=br.readLine())!=null){
    	   osw.write(line);
       }
       osw.flush();
       osw.close();
       br.close();
   }

}
