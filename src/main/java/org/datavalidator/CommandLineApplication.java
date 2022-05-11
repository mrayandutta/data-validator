package org.datavalidator;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.datavalidator.util.DataCompareUtil;
import org.datavalidator.util.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;

public class CommandLineApplication {
    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static void main(String[] args) {
        if(args.length ==0)
        {
            logger.error("Please pass input file location as parameter ,terminating the execution !!");
            System.exit(0);
        }
        String inputFilePath = args[0];
        String outputFilePath = "./output.xlsx";

        Workbook mappingWorkbook = ExcelUtil.getWorkbookFromExcel(inputFilePath);
        Sheet mappingSheet = ExcelUtil.getSheetFromWorkbook(mappingWorkbook,0);
        Map<String,String> mappingData = ExcelUtil.getMappingData(mappingSheet);
        logger.info("mappingData:{}",mappingData);

        Workbook workbook = ExcelUtil.getWorkbookFromExcel(inputFilePath);
        Sheet sourceSheet = ExcelUtil.getSheetFromWorkbook(workbook,1);
        Map<Integer, Map> sourceDataSet = ExcelUtil.getDataFromSheet(sourceSheet);
        //logger.info("sourceDataSet:{}",sourceDataSet);

        Sheet targetSheet = ExcelUtil.getSheetFromWorkbook(workbook,2);
        Map<Integer, Map> targetDataSet = ExcelUtil.getDataFromSheet(targetSheet);
        //logger.info("targetDataSet:{}",targetDataSet);

        DataCompareUtil.compareMappingColumnsWithSourceAndTarget(mappingSheet,sourceSheet,targetSheet);

        DataCompareUtil.compare(sourceDataSet,targetDataSet,mappingData,workbook);
        ExcelUtil.saveWorkBookChanges(workbook,outputFilePath);

    }
}
