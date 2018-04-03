package com.hal.booleans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class CheckDataExport {
    
    public static void main(String[] args) throws ParseException {
        
//        String fromDate = "2017-03-30 00:00:00";
//        String toDate = "2017-04-04 23:59:59";
//        
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//        
//        System.out.println("fromDate:" +sdf.parse(fromDate).getTime());
//        System.out.println("endDate:" +sdf.parse(toDate).getTime());
//        System.out.println("getSAReport(" + sdf.parse(fromDate).getTime() + "," + sdf.parse(toDate).getTime() + ");");
        
        
//        fromDate ="2017-03-23";
//        toDate ="2017-03-26";
//        Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT+8"));
//        System.out.println(fromDate.substring(0, 4));
//        System.out.println(fromDate.substring(5, 7));
//        System.out.println(fromDate.substring(8));
//        int y = Integer.valueOf(fromDate.substring(0, 4));
//        int m = Integer.valueOf(fromDate.substring(5, 7));
//        int d = Integer.valueOf(fromDate.substring(8));
//        
//        calendar.set(y, m - 1, d, 0, 0, 0);
//        
//        Calendar calendar2 = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT+8"));
//        System.out.println(toDate.substring(0, 4));
//        System.out.println(toDate.substring(5, 7));
//        System.out.println(toDate.substring(8));
//        int y2 = Integer.valueOf(toDate.substring(0, 4));
//        int m2 = Integer.valueOf(toDate.substring(5, 7));
//        int d2 = Integer.valueOf(toDate.substring(8));
//        
//        calendar2.set(y2, m2 - 1, d2, 23, 59, 59);
//        
//        System.out.println("fromDate:" + calendar.getTimeInMillis());
//        System.out.println("endDate:" + calendar2.getTimeInMillis());

        


        String root ="E:/Richemont-RWF/data_expoted/DATA EXPORT/";
//    	String root ="E:/Richemont-RWF/data_expoted/20170707/Data Export0707/";
        String[] fileName={ "user"};
//        String[] fileName={"wechat_reporting_menu_click","wechat_reporting_user_cumulate","wechat_reporting_user_summary","wechat_reporting_article_summary",
//        "wechat_reporting_article_total","user","wechat_reporting_upstream_message_keyword","wechat_reporting_menu_version"};
        for(int i=0;i<fileName.length;i++){
            File inFile = new File(root + fileName[i] + ".csv"); // 璇诲彇鐨凜SV鏂囦欢
            File outFile = new File(root + fileName[i] + "_reformat.csv");//鍐欏嚭鐨凜SV鏂囦欢
            String inString = "";
            try {
                BufferedReader reader = new BufferedReader(new FileReader(inFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
                while((inString = reader.readLine())!= null){
                    
                    String dateLong = "";
                    String createTimeLong = "";
                    if(inString.indexOf("\"refDate\":")>-1){
                        dateLong =inString.substring(inString.indexOf("\"refDate\":")+10,inString.indexOf("\"refDate\":")+23);
                    }
                    
                    if(inString.indexOf("\"createTime\":")>-1){
                        createTimeLong= inString.substring(inString.indexOf("\"createTime\":")+13,inString.indexOf("\"createTime\":")+26);
                    }
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    
                    Calendar cal = Calendar.getInstance();
                    
                    if(!"".equalsIgnoreCase(dateLong)){
                        cal.setTimeInMillis(Long.parseLong(dateLong));
                    }else if(!"".equalsIgnoreCase(createTimeLong)){
                        cal.setTimeInMillis(Long.parseLong(createTimeLong));
                    }
                    
                    Date date= cal.getTime();
    
    
                    
                    inString = inString.replace("5719da69e4b09a3e53db8d1f", "5719da69e4b09a3e53db8d1f---A.LS");
                    inString = inString.replace("56cffb5ce4b0afa643e83e52", "56cffb5ce4b0afa643e83e52--JLC");
                    inString = inString.replace("58071490e4b075eeca4404f6", "58071490e4b075eeca4404f6---Shanghaitan");
                    inString = inString.replace("56fb480be4b09a3e53ceef39", "56fb480be4b09a3e53ceef39---Duhill");
                    inString = inString.replace("5677abcae4b0f209ca917681", "5677abcae4b0f209ca917681---Vacheron Constantin");
                    inString = inString.replace("568cad09e4b0f209ca943fc7", "568cad09e4b0f209ca943fc7---Montblanc");
                    inString = inString.replace("5488342fe4b089059d836950", "5488342fe4b089059d836950---Piaget");
                    inString = inString.replace("54dd6911e4b02cf68c8ef087", "54dd6911e4b02cf68c8ef087--IWC");
                    inString = inString.replace("551cc66ce4b0dee7b02575d9", "551cc66ce4b0dee7b02575d9---CAR");
                    inString = inString.replace("558bbec1e4b09ab51bc5f022", "558bbec1e4b09ab51bc5f022---VCA");
                    inString = inString.replace("55a63786e4b0aafe3af028c1", "55a63786e4b0aafe3af028c1---Lancel");
                    inString = inString.replace("5677d75de4b0f209ca9179d4", "5677d75de4b0f209ca9179d4---Panerai");
                    inString = inString.replace("568f6d4be4b080241429bccc", "568f6d4be4b080241429bccc---Roger Dubuis");
       
                    
                   
                    
                    //if (inString.indexOf("Shanghaitan")>-1) {
                    	 if(!"".equalsIgnoreCase(dateLong)){
                             
                             if(sdf.format(date).contains("2017-07-05")){   
                                 writer.write(inString + "refDateStr:" + sdf.format(date));
                                 writer.newLine();
                            }
                         }else if(!"".equalsIgnoreCase(createTimeLong)){
            
                            if(sdf.format(date).contains("2017-07-05")){   
                                 writer.write(inString + "createTimeStr:" + sdf.format(date));
                                 writer.newLine();
                            }
                         }
                    //}
                   
                   
                }
                System.out.println(fileName[i] + " is done");
                reader.close();
                writer.close();
            } catch (FileNotFoundException ex) {
                System.out.println("not file");
            } catch (IOException ex) {
                System.out.println("exception");
            }
        }
       
    }

}
