package org.datavalidator.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.datavalidator.model.CellItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.invoke.MethodHandles;
import java.util.*;

public class ExcelUtil {
    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Method to return an Excel workbook book when location is supplied
     * @param filePath
     * @return
     */
    public static Workbook getWorkbookFromExcel(String filePath)
    {
        Workbook workbook  = null;
        try {
            workbook = WorkbookFactory.create(new File(filePath));
        }
        catch (Exception e)
        {
            logger.error("Error while trying to get workbook from filePath*:{}",filePath);
            logger.error("Exception details :{}",e);
            //e.printStackTrace();
        }
        return workbook;
    }

    public static Sheet getSheetFromWorkbook(Workbook workbook,int sheetNumber)
    {
        return workbook.getSheetAt(sheetNumber);
    }

    public static Map<Integer,Map> getDataFromSheet(Sheet sheet)
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
                        if(i==0)
                        {
                            headerMap.put(String.valueOf(j),cellValue);
                        }
                        else
                        {
                            String columnName= headerMap.get(String.valueOf(j));
                            CellItem cellItem = new CellItem(cellValue,i,cell);
                            dataMap.put(columnName,cellItem);
                        }
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
        //logger.info("rowMap:{}",rowMap);
        return rowMap;
    }

    public static List<String> getColumnListFromSheet(Sheet sheet,int rowNumber)
    {   List<String> columnNameList = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();
        //Row firstRow = sheet.rowIterator().next();
        Row row = sheet.getRow(rowNumber);
        int numberOfColumns = row.getPhysicalNumberOfCells();
        for (int j = 0; j < numberOfColumns; j++)
        {
            Cell cell = row.getCell(j);
            String cellValue = dataFormatter.formatCellValue(cell);
            columnNameList.add(cellValue);
        }


        //logger.info("columnNameList:{}",columnNameList);
        return columnNameList;

    }

    public static Map<String,String> getKeyColumnMappingData(Sheet sheet)
    {
        Map<String,String> keyColumnMap = new LinkedHashMap<>();
        List<Integer> keyColumnIndexList = new ArrayList<>();
        Row firstRow = sheet.getRow(0);
        Row sourceMappingRow = sheet.getRow(1);
        Row targetMappingRow = sheet.getRow(2);
        DataFormatter dataFormatter = new DataFormatter();
        //int numberOfColumns = firstRow.getPhysicalNumberOfCells();
        int numberOfColumns = firstRow.getLastCellNum();
        for (int i = 0; i < numberOfColumns; i++) {
            Cell cell = firstRow.getCell(i);
            String cellValue = dataFormatter.formatCellValue(cell);
            if("Y".equalsIgnoreCase(cellValue))
            {
                keyColumnIndexList.add(i) ;
                Cell sourceCell = sourceMappingRow.getCell(i);
                Cell targetCell = targetMappingRow.getCell(i);
                keyColumnMap.put(dataFormatter.formatCellValue(sourceCell),dataFormatter.formatCellValue(targetCell));

            }

        }
        return keyColumnMap;
    }

    public static Map<String,String> getMappingData(Sheet sheet)
    {
        Map<String,String> sourceTargetMap = new LinkedHashMap<>();
        try
        {
            int numberOfRows = sheet.getPhysicalNumberOfRows();
            List<Integer> keyColumnIndexList = new ArrayList<>();
            Map<Integer,String> sourceColumnMap = new LinkedHashMap<>();
            Map<Integer,String> targetColumnMap = new LinkedHashMap<>();
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
                        if(i==0)
                        {
                            if( j >0 && cellValue!=null && cellValue.equalsIgnoreCase("Y"))
                            {
                                keyColumnIndexList.add(j);
                            }
                        }
                        else
                        {
                            if(i==1 & j>0)
                            {
                                sourceColumnMap.put(j,cellValue);
                            }
                            if(i==2 & j>0)
                            {
                                targetColumnMap.put(j,cellValue);
                                sourceTargetMap.put(sourceColumnMap.get(j),cellValue);
                            }
                        }
                    }
                }
            }

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
        logger.info("sourceTargetMap:{}",sourceTargetMap);
        return sourceTargetMap;

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

    public static void addCommentToCell(Workbook workbook,int sheetNumber,Cell cell,String commentText)
    {
        Sheet sheet = workbook.getSheetAt(sheetNumber);
        Drawing<Shape> drawing = (Drawing<Shape>) sheet.createDrawingPatriarch();
        ClientAnchor clientAnchor = drawing.createAnchor(0, 0, 0, 0, 0, 2, 7, 12);

        Comment comment = (Comment) drawing.createCellComment(clientAnchor);
        CreationHelper creationHelper = (XSSFCreationHelper) workbook.getCreationHelper();
        RichTextString richTextString = creationHelper.createRichTextString(commentText);

        comment.setString(richTextString);
        comment.setAuthor("DataValidator");

        cell.setCellComment(comment);
    }

    public static void saveWorkBookChanges(Workbook workbook,String filePath)
    {
        try {
            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();
            //workbook.close();
            logger.info("File written at :{}",filePath);
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
        Map<Integer, Map> dataset1 = getDataFromSheet(sheet);
        //Map<Integer, Map> dataset2 = readExcelSheetByName(filePath1,0,startRowNumber);



    }
}
