package org.datavalidator.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.datavalidator.model.CellItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;

public class ExcelUtil {
    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static Workbook getWorkbookFromExcel(String filePath)
    {
        Workbook workbook  = null;
        try {
            workbook = WorkbookFactory.create(new File(filePath));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return workbook;
    }

    public static Sheet getSheetFromWorkbook(Workbook workbook,int sheetNumber)
    {
       return workbook.getSheetAt(sheetNumber);
    }

    public static Map<Integer,Map> populateDataFromSheet(Sheet sheet)
    {   Map<Integer,Map> rowMap = new LinkedHashMap<>();
        try
        {
            int numberOfRows = sheet.getPhysicalNumberOfRows();
            Map<String,String> headerMap = new LinkedHashMap<>();
            // Create a DataFormatter to format and get each cell's value as String
            DataFormatter dataFormatter = new DataFormatter();
            for (int i = 0; i < numberOfRows; i++)
            {
                Map<String,CellItem> dataMap = new LinkedHashMap<>();
                Row row = sheet.getRow(i);
                int numberOfColumns = row.getPhysicalNumberOfCells();
                for (int j = 0; j < numberOfColumns; j++)
                {
                    Cell cell = row.getCell(j);
                    if(cell!=null)
                    {
                        String cellValue = dataFormatter.formatCellValue(cell);
                        //System.out.print(cellValue + "\t");
                        if(i==0)
                        {
                            headerMap.put(String.valueOf(j),cellValue);
                            //highLightCell(cell,workbook);
                        }
                        else
                        {
                            String columnName= headerMap.get(String.valueOf(j));
                            CellItem cellItem = new CellItem(cellValue,i,cell);
                            //dataMap.put(columnName,cellValue);
                            dataMap.put(columnName,cellItem);

                        }
                        //logger.info("j:{},headerMap:{},dataMap:{}",j,headerMap,dataMap);
                    }
                    else
                    {
                        //logger.info("cell is null");
                    }
                }
                if(i!=0)
                {
                    rowMap.put(i,dataMap);
                }

            }
            // Write the output to a file
            //FileOutputStream fileOut = new FileOutputStream("./color_test_new.xlsx");
            //workbook.write(fileOut);
            //fileOut.close();

            //workbook.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
           //
        }
        //logger.info("Read data from sheet '"+sheetName+"'");
        logger.info("rowMap:{}",rowMap);
        return rowMap;

    }

    public static void highLightCell(Cell cell,Workbook workbook) {
        // Create a Font for styling header cells
        //Font headerFont = workbook.createFont();
        //headerFont.setBold(true);
        //headerFont.setFontHeightInPoints((short) 14);
        //headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        //headerCellStyle.setFont(headerFont);

        // fill foreground color ...
        headerCellStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        // and solid fill pattern produces solid grey cell fill
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(headerCellStyle);

    }

    public static void saveWorkBookChanges(Workbook workbook,String filePath)
    {
        try {
            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void main(String[] args)
    {
        String sheetName="Employee";
        int startRowNumber = 0;
        String filePath1 = "./sample.xlsx";
        Workbook workbook = getWorkbookFromExcel(filePath1);
        Sheet sheet = getSheetFromWorkbook(workbook,0);
        Map<Integer, Map> dataset1 = populateDataFromSheet(sheet);
        //Map<Integer, Map> dataset2 = readExcelSheetByName(filePath1,0,startRowNumber);



    }
}
