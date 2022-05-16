package org.datavalidator.util;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

public class ValidationUtil {
    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static Pair<Map<String,String>,Map<String,String>> getMappingAndKeyColumnMappingPair(String inputFilePath,int sheetNumber)
    {
        Workbook mappingWorkbook = ExcelUtil.getWorkbookFromExcel(inputFilePath);
        Sheet mappingSheet = ExcelUtil.getSheetFromWorkbook(mappingWorkbook,sheetNumber);
        Map<String,String> mappingData = ExcelUtil.getMappingData(mappingSheet);
        logger.info("mappingData:{}",mappingData);

        Map<String,String> keyColumnMappingData = ExcelUtil.getKeyColumnMappingData(mappingSheet);
        logger.info("keyColumnMappingData:{}",keyColumnMappingData);
        return new Pair(mappingData,keyColumnMappingData);
    }
    public static Map<Integer, Map> getDataSetFromSheet(String inputFilePath,int sheetNumber)
    {
        Workbook workbook = ExcelUtil.getWorkbookFromExcel(inputFilePath);
        Sheet sourceSheet = ExcelUtil.getSheetFromWorkbook(workbook,sheetNumber);
        Map<Integer, Map> dataSet = ExcelUtil.getDataFromSheet(sourceSheet);
        return dataSet;
    }

    public static void printDataDuplicationDetails(Map<Integer, Map> sourceDuplicateDataSet,Map<Integer, Map> targetDuplicateDataSet)
    {
        logger.error("-------------------------------------- Printing Data Duplication Issue Start ---------------------------------");
        //ExcelWriterUtil.writeDataDuplicationDetails("Source Data Duplicates ",sourceDuplicateDataSet,"duplicate-error-output.xlsx");

        logger.error("-------------------------------------- Printing Data Duplication Issue End ---------------------------------");
    }

    public static void printDataMisMatchDetails(Map<Integer, Map> sourceUniqueDataSet,Map<Integer, Map> targetUniqueDataSet,Map<String,String> mappingData,Map<String,String> keyColumnMappingData)
    {
        logger.error("-------------------------------------- Printing Data Mismatch Issue Start ---------------------------------");
        /*
        List errorList =DataCompareUtil.compareNew(sourceUniqueDataSet,targetUniqueDataSet,mappingData,keyColumnMappingData);
        errorList.stream().forEach
                (
                        item->
                        {
                            Pair pairItem= (Pair) item;
                            logger.error("Source Issue:{},Target Issue:{}",pairItem.getValue0(),pairItem.getValue1());
                            //logger.info("Target Issue:{}",pairItem.getValue1());
                        }
                );

         */
        //ExcelWriterUtil.writeDataMisMatchDetails("Data Mismatch ",errorList,"datamismatch-error-output.xlsx");
        logger.error("-------------------------------------- Printing Data Mismatch Issue End ---------------------------------");
    }

    public static void printAllErrorDetails(String dataMismatchSheetName, String dataDuplicateSheetName, Map<Integer, Map> duplicateDataSet,Map<Integer, Map> sourceUniqueDataSet,Map<Integer, Map> targetUniqueDataSet,Map<String,String> mappingData,Map<String,String> keyColumnMappingData,String outputFilePath)
    {
        List errorList =DataCompareUtil.compareNew(sourceUniqueDataSet,targetUniqueDataSet,mappingData,keyColumnMappingData);
        ExcelWriterUtil.writeAllErrorDetails(dataMismatchSheetName,errorList,dataDuplicateSheetName,duplicateDataSet,outputFilePath);
    }
}
