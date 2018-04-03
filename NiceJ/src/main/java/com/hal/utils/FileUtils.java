package com.hal.utils;

import java.io.File;

public class FileUtils {
     public  static final  String  PATH  = "D://tmp//MyXml01.xml";
     public  static final  String  PATH_2  = "D:/test/template.xml";
     
     public static  File  getOneFiles(String  str){
    	 File  file = new File(str);
    	 return file;
     }
     
}
