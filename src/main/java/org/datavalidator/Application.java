package org.datavalidator;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.datavalidator.util.DataCompareUtil;
import org.datavalidator.util.ExcelUtil;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static void main(String[] args) {

        //String inputFilePath = "./input-sample-100.xlsx";
        String inputFilePath = "./input-sample.xlsx";
        String outputFilePath = "./output.xlsx";

        Workbook mappingWorkbook = ExcelUtil.getWorkbookFromExcel(inputFilePath);
        Sheet mappingSheet = ExcelUtil.getSheetFromWorkbook(mappingWorkbook,0);
        Map<String,String> mappingData = ExcelUtil.getMappingData(mappingSheet);
        logger.info("mappingData:{}",mappingData);

        Map<String,String> keyColumnMappingData = ExcelUtil.getKeyColumnMappingData(mappingSheet);
        logger.info("keyColumnMappingData:{}",keyColumnMappingData);

        Workbook workbook = ExcelUtil.getWorkbookFromExcel(inputFilePath);
        Sheet sourceSheet = ExcelUtil.getSheetFromWorkbook(workbook,1);
        Map<Integer, Map> sourceDataSet = ExcelUtil.getDataFromSheet(sourceSheet);
        logger.info("sourceDataSet:{}",sourceDataSet);

        List<String> keyColumnListForSource = new ArrayList<String>();
        keyColumnListForSource.addAll(keyColumnMappingData.keySet());
        Map<Integer, Map> sourceDuplicateDataSet = DataCompareUtil.getDuplicateDataSet(sourceDataSet,keyColumnListForSource);
        logger.info("sourceDuplicateDataSet:{}",sourceDuplicateDataSet);
        Map<Integer, Map> sourceUniqueDataSet = DataCompareUtil.getUniqueDataSet(sourceDataSet,sourceDuplicateDataSet);
        logger.info("sourceUniqueDataSet:{}",sourceUniqueDataSet);

        Sheet targetSheet = ExcelUtil.getSheetFromWorkbook(workbook,2);
        Map<Integer, Map> targetDataSet = ExcelUtil.getDataFromSheet(targetSheet);
        logger.info("targetDataSet:{}",targetDataSet);

        List<String> keyColumnListForTarget = new ArrayList<String>();
        keyColumnListForTarget.addAll(keyColumnMappingData.values());
        Map<Integer, Map> targetDuplicateDataSet = DataCompareUtil.getDuplicateDataSet(targetDataSet,keyColumnListForTarget);
        logger.info("targetDuplicateDataSet:{}",targetDuplicateDataSet);
        Map<Integer, Map> targetUniqueDataSet = DataCompareUtil.getUniqueDataSet(targetDataSet,targetDuplicateDataSet);
        logger.info("targetUniqueDataSet:{}",targetUniqueDataSet);

        //This is for header check
        DataCompareUtil.compareMappingColumnsWithSourceAndTarget(mappingSheet,sourceSheet,targetSheet);

        //DataCompareUtil.compare(sourceDataSet,targetDataSet,mappingData,workbook);
        //DataCompareUtil.compare(sourceUniqueDataSet,targetUniqueDataSet,mappingData,workbook);
        List errorList =DataCompareUtil.compareNew(sourceUniqueDataSet,targetDataSet,mappingData,keyColumnMappingData,workbook);
        errorList.stream().forEach
                (
                   item->
                   {
                       Pair pairItem= (Pair) item;
                       logger.info("Source Issue:{},Target Issue:{}",pairItem.getValue0(),pairItem.getValue1());
                       //logger.info("Target Issue:{}",pairItem.getValue1());
                   }
                );
        //ExcelUtil.saveWorkBookChanges(workbook,outputFilePath);

    }
}
