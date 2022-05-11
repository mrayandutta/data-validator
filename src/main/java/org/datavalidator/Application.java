package org.datavalidator;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.datavalidator.util.DataCompareUtil;
import org.datavalidator.util.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static void main(String[] args) {
        String inputFilePath = "./input-sample.xlsx";
        String outputFilePath = "./output.xlsx";
        //String inputFilePath = "C://data-validator-test/input-sample.xlsx";
        //String outputFilePath = "C://data-validator-test//output.xlsx";
        //String mappingFilePath = "C://data-validator-test/mapping.xlsx";
        //String outputFilePath = "C://data-validator-test/sample_highlighted.xlsx";
        //String inputFilePath = "./sample.xlsx";
        //String mappingFilePath = "./mapping.xlsx";
        //String outputFilePath = "./sample_highlighted.xlsx";
        Workbook mappingWorkbook = ExcelUtil.getWorkbookFromExcel(inputFilePath);
        Sheet mappingSheet = ExcelUtil.getSheetFromWorkbook(mappingWorkbook,0);
        Map<String,String> mappingData = ExcelUtil.getMappingData(mappingSheet);
        logger.info("mappingData:{}",mappingData);

        Workbook workbook = ExcelUtil.getWorkbookFromExcel(inputFilePath);
        Sheet sourceSheet = ExcelUtil.getSheetFromWorkbook(workbook,1);
        Map<Integer, Map> sourceDataSet = ExcelUtil.getDataFromSheet(sourceSheet);
        logger.info("sourceDataSet:{}",sourceDataSet);

        Sheet targetSheet = ExcelUtil.getSheetFromWorkbook(workbook,2);
        Map<Integer, Map> targetDataSet = ExcelUtil.getDataFromSheet(targetSheet);
        logger.info("targetDataSet:{}",targetDataSet);

        DataCompareUtil.compareMappingColumnsWithSourceAndTarget(mappingSheet,sourceSheet,targetSheet);

        DataCompareUtil.compare(sourceDataSet,targetDataSet,mappingData,workbook);
        ExcelUtil.saveWorkBookChanges(workbook,outputFilePath);

    }
}
