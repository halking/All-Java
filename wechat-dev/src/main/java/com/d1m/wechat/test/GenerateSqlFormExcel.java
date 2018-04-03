package com.d1m.wechat.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;




/**
 * 读取excel，生成sql
 * 
 * @author hzh
 */
public class GenerateSqlFormExcel {


	static String PRJ_DIR = "E:\\IT1\\Journey";

	/**
	 * excel文件路径
	 */
	 static String INPUT_FILE = PRJ_DIR + "/sql/Table_Designs.xlsx";

	 // 导出路径
	 static String OUTPUT_FILE = PRJ_DIR + "/sql/zegna_binding.sql";

	
	/**
	 * 读取的sheet集合
	 */
	 static int[] tableSheets = {0};
	
	 
	 static int tableNameCellIdx = 1;
	 static int tableCommentCellIdx = 2;
	 
	 
	 static int FieldNameCellIdx = 1;
	 static int FieldTypeCellIdx = 2;
	 static int FieldLenCellIdx = 3;
	 static int FieldKeyCellIdx = 4;
	 static int FieldDesCellIdx = 5;
	
	public static void main(String[] args) {
		File file=new File(INPUT_FILE);
		try {
			XSSFWorkbook wb=new XSSFWorkbook(new FileInputStream(file));
			

			StringBuffer buffer = new StringBuffer();
			
			for (int i = 0; i < tableSheets.length; i++) {
				int sheet = tableSheets[i];
				XSSFSheet st=wb.getSheetAt(sheet);

				boolean newTable = true;

				String tableName = null;
				String tableComment = null;
				List<Column> columnList = new ArrayList<Column>();
				List<Column> pkList = new ArrayList<Column>();
				List<Column> keyList = new ArrayList<Column>();
				
				for(int rowIndex=0;rowIndex <= st.getLastRowNum();rowIndex++){
					XSSFRow row=st.getRow(rowIndex);
					if(row == null){
						newTable = true;

						makesql(buffer, tableName, tableComment, columnList, pkList, keyList);
						tableName = null;
						tableComment = null;
						columnList.clear();
						pkList.clear();
						keyList.clear();
						
						continue;
					}
					String value = getCellFormatValue(row.getCell(tableNameCellIdx));
					if(!StringUtils.isBlank(value)){
						if(newTable){
							tableName = value;
							System.out.println(tableName);
							tableComment = getCellFormatValue(row.getCell(tableCommentCellIdx));
							rowIndex ++;	// 跳过列名行
							newTable = false;
							continue;
						}
					}else{
						newTable = true;
						
						makesql(buffer, tableName, tableComment, columnList, pkList, keyList);
						
						tableName = null;
						tableComment = null;
						columnList.clear();
						pkList.clear();
						keyList.clear();
						continue;
					}

					Column column = new Column();
					String name = getCellFormatValue(row.getCell(FieldNameCellIdx));
					String type = getCellFormatValue(row.getCell(FieldTypeCellIdx));
					String len = getCellFormatValue(row.getCell(FieldLenCellIdx));
					String key = getCellFormatValue(row.getCell(FieldKeyCellIdx));
					String des = getCellFormatValue(row.getCell(FieldDesCellIdx));
					column.setName(name);
					column.setType(type);
					column.setComment(des);
					if (!type.startsWith("date")) {
						column.setLength(len);
						if ("pk".equalsIgnoreCase(key)) {
							column.setIndexType((byte) 1);
							pkList.add(column);
						} else if (key.startsWith("idx")) {
							column.setIndexType((byte) 2);
							column.setIndexName(key + "_" + name);
							keyList.add(column);
						} 
					}
					columnList.add(column);
				}
				
				makesql(buffer, tableName, tableComment, columnList, pkList, keyList);
				
			}
			
			//System.out.println(buffer);
			
//			String outputFilePath = file.getParent() 
//					+ File.separator + file.getName() + "_table.sql";
			File outputFile = new File(OUTPUT_FILE);
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outputFile));
			writer.append(buffer.toString());
			writer.flush();
			writer.close();
			System.out.println("done!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void makesql(
			StringBuffer buffer,
			String tableName,
			String tableComment,
			List<Column> columnList,
			List<Column> pkList,
			List<Column> keyList){
		if(tableName == null){
			return;
		}
		

		buffer.append(ColumnBuilder.getCreateTableBegin(tableName));
		for (Column column : columnList) {
			ColumnBuilder builder = new ColumnBuilder(column);
			buffer.append(builder.buildColumnSql());
			buffer.append(",\n");
		}
		if (pkList.size() > 0) {
			ColumnBuilder pkBuilder = new ColumnBuilder(pkList.get(0));
			buffer.append(pkBuilder.getPk());
			buffer.append(",\n");
		}
		for (Column column : keyList) {
			ColumnBuilder builder = new ColumnBuilder(column);
			buffer.append(builder.getKey());
			buffer.append(",\n");
		}
		int lastIndex = buffer.lastIndexOf(",\n");
		// 删除最后一个逗号
		buffer.replace(lastIndex, lastIndex + 1, "");
		buffer.append(ColumnBuilder.getCreateTableEnd(tableComment));
		buffer.append(";\n\n");
	}
	
	
	
	/**
	 * 获取cell的值
	 * 
	 * @param cell
	 * @return
	 */
	private static String getCellFormatValue(XSSFCell cell){
		String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
        	//System.err.println(cell.getCellType());
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case XSSFCell.CELL_TYPE_NUMERIC:
            case XSSFCell.CELL_TYPE_FORMULA: {
                // 判断当前的cell是否为Date
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式
                    
                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();
                    
                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
                    cellvalue = sdf.format(date);
                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                    cellvalue = String.valueOf((long)cell.getNumericCellValue());
                }
                break;
            }
            // 如果当前Cell的Type为STRIN
            case XSSFCell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            // 默认的Cell值
            default:
                cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        
        return StringUtils.deleteWhitespace(cellvalue);
	}
	
}
