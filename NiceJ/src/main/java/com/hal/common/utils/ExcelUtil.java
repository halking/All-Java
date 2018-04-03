package com.hal.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by hal on 2016/11/26.
 */
public class ExcelUtil {
    public final static String PATH = "D:\\tmp\\demo.xlsx";

    public static void main(String[] args) throws Exception {
    	parseExcel("E:/IT1/Journey/history-bind-list/list_0122.xlsx");
        
    }
    public static void buildExcel(Class clazz,List<List<String>> values,String path){
    		Field[] fields = clazz.getDeclaredFields();
    		Workbook workbook =new HSSFWorkbook();
    		FileOutputStream out =null;
    		boolean flag = true;
    		Sheet sheet = workbook.createSheet(clazz.getSimpleName());
    		for (int i = 0; i < values.size(); i++) {
    			Row row = sheet.createRow(i);
    			for (int j=0 ;j<fields.length;j++) {
    				Cell cell = row.createCell(j);
    				if (flag) {
    					cell.setCellValue(fields[0].toString());
    					flag =false;
					}
    				cell.setCellValue(values.get(i).get(j));
					}
				}
    		try {
    			flushFile(workbook, path);
			} catch (IOException e) {
				throw new RuntimeException("out is invalid!");
			}
    }
    
    public static void parseExcel(String path) {
    	OPCPackage pkg = null;
    	  Workbook wb = null;
    	try {
    		 pkg = OPCPackage.open(new File(path));
             wb = new XSSFWorkbook(pkg);
		} catch (InvalidFormatException ife) {
			// TODO: handle exception
			ife.printStackTrace();
		}catch (IOException ioe) {
			// TODO: handle exception
		}finally {
			
		}
        List<String> head = new ArrayList<String>();
        List<Object> value = new ArrayList<Object>();
        Sheet sheet = wb.getSheetAt(0);
        int firstRow = sheet.getFirstRowNum();
        Row rowf = sheet.getRow(firstRow);
        int lastColc = rowf.getLastCellNum();
        for (int j = 0; j < lastColc; j++) {
            Cell c = rowf.getCell(j);
            head.add(c.getRichStringCellValue().getString());
        }
        int lastRow = sheet.getLastRowNum();
        for (int i = 1; i <= lastRow; i++) {
            Row row = sheet.getRow(i);
            int lastCol = row.getLastCellNum();
            for (int j = 0; j < lastCol; j++) {
                Cell cell = row.getCell(j);
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        value.add(cell.getRichStringCellValue().getString());
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            value.add(cell.getDateCellValue());
                        } else {
                            value.add(cell.getNumericCellValue());
                        }
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        value.add(cell.getBooleanCellValue());
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        value.add(cell.getCellFormula());
                        break;
                    case Cell.CELL_TYPE_BLANK:
                        value.add(null);
                        break;
                    default:
                        break;
                }
            }
        }
        StringBuffer buffer = new StringBuffer();
/*        buffer.append("insert into user(");
        for (String hd : head) {
            buffer.append(hd + ",");
        }
        buffer.append(")value(");
        for (Object o : value) {
            buffer.append("'" + o + "',");
        }*/
        for (Object o : value) {
            buffer.append(o+"\n");
        }
        System.out.print(buffer.toString());
    }

    public static Workbook CreateHSSFWorkBook(String filename) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        flushFile(workbook, filename);
        return workbook;
    }

    public static Sheet CreteSheet(String workname, String sheetName) throws IOException {
        Workbook wb = CreateHSSFWorkBook(workname);
        Sheet sheet = wb.createSheet(sheetName);
        FileOutputStream fos = new FileOutputStream(workname);
        wb.write(fos);
        fos.close();
        return sheet;
    }

    public static void flushFile(Workbook workbook, String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        workbook.write(fos);
        fos.close();
    }

    private static void createCell(Workbook wb, Row row, short column, short halign, short valign) {
        Cell cell = row.createCell(column);
        cell.setCellValue("Align It");
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cell.setCellStyle(cellStyle);
    }

    public static CellStyle setStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.GREEN.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLUE.getIndex());
        style.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }
    
}
