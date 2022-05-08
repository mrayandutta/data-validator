package org.datavalidator;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.datavalidator.util.DataCompareUtil;
import org.datavalidator.util.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;

public class Application {
    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static void main(String[] args) {
        String inputFilePath = "./sample.xlsx";
        String outputFilePath = "./sample_highlighted.xlsx";
        Workbook workbook = ExcelUtil.getWorkbookFromExcel(inputFilePath);
        Sheet sourceSheet = ExcelUtil.getSheetFromWorkbook(workbook,0);
        Map<Integer, Map> sourceDataSet = ExcelUtil.getDataFromSheet(sourceSheet);
        Sheet targetSheet = ExcelUtil.getSheetFromWorkbook(workbook,1);
        Map<Integer, Map> targetDataSet = ExcelUtil.getDataFromSheet(targetSheet);

        logger.info("sourceDataSet:{}",sourceDataSet);
        logger.info("targetDataSet:{}",targetDataSet);

        String mappingFilePath = "./mapping.xlsx";
        Workbook mappingWorkbook = ExcelUtil.getWorkbookFromExcel(mappingFilePath);
        Sheet mappingSheet = ExcelUtil.getSheetFromWorkbook(mappingWorkbook,0);
        Map<String,String> mappingData = ExcelUtil.getMappingData(mappingSheet);

        DataCompareUtil.compare(sourceDataSet,targetDataSet,mappingData,workbook);
        ExcelUtil.saveWorkBookChanges(workbook,outputFilePath);

    }
}
