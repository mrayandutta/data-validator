package org.datavalidator;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.datavalidator.util.DataCompareUtil;
import org.datavalidator.util.ExcelUtil;
import org.datavalidator.util.ValidationUtil;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static void main(String[] args) {

        //String inputFilePath = "./input-sample-100.xlsx";
        String inputFilePath = "./input-sample.xlsx";
        String outputFilePath = "./output.xlsx";

        Pair<Map<String,String>,Map<String,String>> mappingAndKeyColumnMappingPair = ValidationUtil.getMappingAndKeyColumnMappingPair(inputFilePath,0);
        Map<String,String> mappingData = mappingAndKeyColumnMappingPair.getValue0();
        logger.info("mappingData:{}",mappingData);

        Map<String,String> keyColumnMappingData = mappingAndKeyColumnMappingPair.getValue1();
        logger.info("keyColumnMappingData:{}",keyColumnMappingData);

        Map<Integer, Map> sourceDataSet = ValidationUtil.getDataSetFromSheet(inputFilePath,1);
        Map<Integer, Map> targetDataSet = ValidationUtil.getDataSetFromSheet(inputFilePath,2);

        List<String> keyColumnListForSource = new ArrayList<String>();
        keyColumnListForSource.addAll(keyColumnMappingData.keySet());
        Map<Integer, Map> sourceDuplicateDataSet = DataCompareUtil.getDuplicateDataSet(sourceDataSet,keyColumnListForSource);
        logger.info("sourceDuplicateDataSet:{}",sourceDuplicateDataSet);
        Map<Integer, Map> sourceUniqueDataSet = DataCompareUtil.getUniqueDataSet(sourceDataSet,sourceDuplicateDataSet);
        logger.info("sourceUniqueDataSet:{}",sourceUniqueDataSet);


        List<String> keyColumnListForTarget = new ArrayList<String>();
        keyColumnListForTarget.addAll(keyColumnMappingData.values());
        Map<Integer, Map> targetDuplicateDataSet = DataCompareUtil.getDuplicateDataSet(targetDataSet,keyColumnListForTarget);
        logger.info("targetDuplicateDataSet:{}",targetDuplicateDataSet);
        Map<Integer, Map> targetUniqueDataSet = DataCompareUtil.getUniqueDataSet(targetDataSet,targetDuplicateDataSet);
        logger.info("targetUniqueDataSet:{}",targetUniqueDataSet);

        ValidationUtil.printDataDuplicationDetails(sourceDuplicateDataSet,targetDuplicateDataSet);
        ValidationUtil.printDataMisMatchDetails(sourceUniqueDataSet,targetUniqueDataSet,mappingData,keyColumnMappingData);

    }
}
