package org.datavalidator.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.datavalidator.constants.ApplicationConstants;
import org.datavalidator.model.CellItem;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.sql.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelWriterUtil {
    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void writeAllErrorDetails(String dataMismatchSheetName, List<Pair> errorList,String dataDuplicateSheetName, Map<Integer, Map> duplicateDataSet,String outputFilePath)
    {
        XSSFWorkbook workbook = new XSSFWorkbook();
        writeDataDuplicationDetails(dataDuplicateSheetName,duplicateDataSet,workbook);
        writeDataMisMatchDetails(dataMismatchSheetName,errorList,workbook);

        try {
            FileOutputStream outputStream = new FileOutputStream(outputFilePath);
            workbook.write(outputStream);
            workbook.close();

        }catch (Exception exception)
        {
            logger.error("Error occurred while trying to write file:{} ,exception details:{}",outputFilePath,exception);
        }

    }

    public static void writeDataMisMatchDetails(String sheetName, List<Pair> errorList,XSSFWorkbook workbook)  {
        //logger.info("errorList :{}",errorList);
        //XSSFWorkbook workbook = new XSSFWorkbook();
        logger.error("------------------- Printing Data Mismatch Issue Start ------------------\n");
        XSSFSheet sheet = workbook.createSheet(sheetName);
        int rowCount = 0;
        Row headerRow = sheet.createRow(rowCount);
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("Source Row Number ");
        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue("Target Row Number ");
        Cell headerCell3 = headerRow.createCell(2);
        headerCell3.setCellValue("Source Columns");
        Cell headerCell4 = headerRow.createCell(3);
        headerCell4.setCellValue("Target Columns");

        rowCount++;

        for (Pair pair : errorList) {
            Row row = sheet.createRow(rowCount);
            rowCount++;
            int columnCount = 0;
            Pair sourcePair = (Pair) pair.getValue0();
            Pair targetPair = (Pair) pair.getValue1();

            Integer sourceErrorRowNumber = (Integer) sourcePair.getValue0();
            List<String> sourceErrorColumnList = (List<String>) sourcePair.getValue1();
            Integer targetErrorRowNumber = (Integer) targetPair.getValue0();
            List<String> targetErrorColumnList = (List<String>) targetPair.getValue1();
            String sourceErrorColumnListStr= String.join(",",sourceErrorColumnList);
            String targetErrorColumnListStr= String.join(",",targetErrorColumnList);

            logger.error("Source row number:{},Source Columns:{},Target Row Number:{},Target Columns:{}",sourceErrorRowNumber,sourceErrorColumnListStr,targetErrorRowNumber,targetErrorColumnListStr);


            Cell cell1 = row.createCell(columnCount++);
            cell1.setCellValue(sourceErrorRowNumber.toString());
            Cell cell2 = row.createCell(columnCount++);
            cell2.setCellValue(sourceErrorColumnListStr);

            Cell cell3 = row.createCell(columnCount++);
            cell3.setCellValue(targetErrorRowNumber.toString());
            Cell cell4 = row.createCell(columnCount++);
            cell4.setCellValue(targetErrorColumnListStr);
        }
        /*
        try {
            FileOutputStream outputStream = new FileOutputStream(outputFilePath);
            workbook.write(outputStream);
            workbook.close();

        }catch (Exception exception)
        {
            logger.error("Error occurred while trying to write file:{} ,exception details:{}",outputFilePath,exception);
        }

         */
        logger.error("\n \n --------------------- Printing Data Mismatch Issue End ------------\n");
    }

    public static void writeDataDuplicationDetails(String sheetName, Map<Integer, Map> duplicateDataSet,XSSFWorkbook workbook)  {
        //logger.info("duplicateDataSet :{}",duplicateDataSet);
        //XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet(sheetName);
        int rowCount = 0;
        Row headerRow = sheet.createRow(rowCount);
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue(ApplicationConstants.DATA_DUPLICATION_SHEET_COLUMN_NAME);
        rowCount++;
        List<String> rowNumberList = new ArrayList<>();


        for (Map.Entry me : duplicateDataSet.entrySet())
        {
            Row row = sheet.createRow(rowCount);
            rowCount++;
            Cell cell1 = row.createCell(0);
            Integer rowNumber = (Integer) me.getKey();
            cell1.setCellValue(rowNumber.toString());
            rowNumberList.add(rowNumber.toString());
        }
        if(!rowNumberList.isEmpty())
        {
            logger.error("--------------------- Printing Data Duplication Issue Start ------------\n");
            logger.error("Following row numbers in source record have duplicates :{}",String.join(",",rowNumberList));
            logger.error("\n \n --------------------- Printing Data Duplication Issue End ------------\n");
        }

        /*
        try {
            FileOutputStream outputStream = new FileOutputStream(outputFilePath);
            workbook.write(outputStream);
            workbook.close();

        }catch (Exception exception)
        {
            logger.error("Error occurred while trying to write file:{} ,exception details:{}",outputFilePath,exception);
        }

         */
    }

}
