package org.datavalidator;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.datavalidator.constants.ApplicationConstants;
import org.datavalidator.util.DataCompareUtil;
import org.datavalidator.util.ExcelUtil;
import org.datavalidator.util.ValidationUtil;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandLineApplication {
    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static void main(String[] args) {
        if(args.length ==0 || args.length!=6)
        {
            logger.error("Please pass all 6 input parameters ,received parameter number is :{}",args.length);
            logger.error("The input parameter sequence is : sourcefilelocation sourcefilesheetnumber targetfilelocation targetfilesheetnumber mappingfilelocation mappingfilesheetnumber ");
            logger.error("Terminating the execution !!");
            System.exit(0);
        }
        String sourceInputFilePath = args[0];
        int sourceInputSheetNumber = Integer.parseInt(args[1]);
        String targetInputFilePath = args[2];
        int targetInputSheetNumber = Integer.parseInt(args[3]);
        String mappingInputFilePath = args[4];
        int mappingInputSheetNumber = Integer.parseInt(args[5]);


        String outputFilePath = ApplicationConstants.REPORT_OUTPUT_FILE_LOCATION;


        Pair<Map<String,String>,Map<String,String>> mappingAndKeyColumnMappingPair = ValidationUtil.getMappingAndKeyColumnMappingPair(mappingInputFilePath,mappingInputSheetNumber);
        Map<String,String> mappingData = mappingAndKeyColumnMappingPair.getValue0();
        //logger.info("mappingData:{}",mappingData);

        Map<String,String> keyColumnMappingData = mappingAndKeyColumnMappingPair.getValue1();
        //logger.info("keyColumnMappingData:{}",keyColumnMappingData);

        Map<Integer, Map> sourceDataSet = ValidationUtil.getDataSetFromSheet(sourceInputFilePath,sourceInputSheetNumber);
        Map<Integer, Map> targetDataSet = ValidationUtil.getDataSetFromSheet(targetInputFilePath,targetInputSheetNumber);

        List<String> keyColumnListForSource = new ArrayList<String>();
        keyColumnListForSource.addAll(keyColumnMappingData.keySet());
        Map<Integer, Map> sourceDuplicateDataSet = DataCompareUtil.getDuplicateDataSet(sourceDataSet,keyColumnListForSource);
        //logger.info("sourceDuplicateDataSet:{}",sourceDuplicateDataSet);
        Map<Integer, Map> sourceUniqueDataSet = DataCompareUtil.getUniqueDataSet(sourceDataSet,sourceDuplicateDataSet);
        //logger.info("sourceUniqueDataSet:{}",sourceUniqueDataSet);


        List<String> keyColumnListForTarget = new ArrayList<String>();
        keyColumnListForTarget.addAll(keyColumnMappingData.values());
        Map<Integer, Map> targetDuplicateDataSet = DataCompareUtil.getDuplicateDataSet(targetDataSet,keyColumnListForTarget);
        //logger.info("targetDuplicateDataSet:{}",targetDuplicateDataSet);
        Map<Integer, Map> targetUniqueDataSet = DataCompareUtil.getUniqueDataSet(targetDataSet,targetDuplicateDataSet);
        //logger.info("targetUniqueDataSet:{}",targetUniqueDataSet);

        //ValidationUtil.printDataDuplicationDetails(sourceDuplicateDataSet,targetDuplicateDataSet);
        //ValidationUtil.printDataMisMatchDetails(sourceUniqueDataSet,targetUniqueDataSet,mappingData,keyColumnMappingData);

        //ExcelWriterUtil.writeAllErrorDetails(sourceDuplicateDataSet);
        ValidationUtil.printAllErrorDetails(ApplicationConstants.DATA_MISMATCH_SHEET_NAME,ApplicationConstants.DATA_DUPLICATION_SHEET_NAME,sourceDuplicateDataSet,sourceUniqueDataSet,targetUniqueDataSet,mappingData,keyColumnMappingData,outputFilePath);

    }
}
