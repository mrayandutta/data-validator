package org.datavalidator.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.datavalidator.model.CellItem;
import org.javatuples.Pair;
import org.javatuples.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility to compare the data sets based on the mapping provided
 */
public class DataCompareUtil {
    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static List compareNew(Map<Integer, Map> sourceDataSet,Map<Integer,Map> targetDataSet,Map<String, String> mappingData,Map<String,String> keyColumnMappingData)
    {
        List<String> keyColumnListForSource = new ArrayList<String>();
        keyColumnListForSource.addAll(keyColumnMappingData.keySet());
        List<String> keyColumnListForTarget = new ArrayList<String>();
        keyColumnListForTarget.addAll(keyColumnMappingData.values());

        Stream<Map.Entry<String, List<Map.Entry<Integer, Map>>>> sourceStream = sourceDataSet.entrySet()
                .stream()
                .collect(
                        Collectors.groupingBy(item->getConcatenatedFieldValueFromItem(item.getValue(),keyColumnListForSource))
                ).entrySet().stream();
        Map<String, List<Map.Entry<Integer, Map>>> sourceDataMap = sourceStream.collect(Collectors.toMap(x->x.getKey(),x->x.getValue()));


        Stream<Map.Entry<String, List<Map.Entry<Integer, Map>>>> targetStream =
                targetDataSet.entrySet()
                .stream()
                .collect(
                        Collectors.groupingBy(item->getConcatenatedFieldValueFromItem(item.getValue(),keyColumnListForTarget))
                ).entrySet().stream();
        Map<String, List<Map.Entry<Integer, Map>>> targetDataMap = targetStream.collect(Collectors.toMap(x->x.getKey(),x->x.getValue()));
        //logger.info("sourceDataMap:{}",sourceDataMap);
        //logger.info("targetDataMap:{}",targetDataMap);

        List errorList = sourceDataMap.entrySet().stream().
                filter(item->targetDataMap.containsKey(item.getKey()))
                .map(sourceItem->
                        {
                            List<Map.Entry<Integer, Map>> sourceRecord = sourceItem.getValue();
                            List<Map.Entry<Integer, Map>> targetRecord = targetDataMap.get(sourceItem.getKey());
                            return validateMappedColumns(sourceRecord,targetRecord,mappingData);
                        }
                        ).collect(Collectors.toList());

        //logger.info("errorList:{}",errorList);
        return errorList;
    }

    //public static Pair<Integer,List<String>> validateMappedColumns(List<Map.Entry<Integer, Map>> sourceRecord ,List<Map.Entry<Integer, Map>> targetRecord,Map<String, String> mappingData)
    public static Pair validateMappedColumns(List<Map.Entry<Integer, Map>> sourceRecord ,List<Map.Entry<Integer, Map>> targetRecord,Map<String, String> mappingData)
    {
        Pair<Integer,List<String>> sourceMisMatchRowColumnListPair = null;
        Pair<Integer,List<String>> targetMisMatchRowColumnListPair = null;

        AtomicReference<Integer> sourceRowNumber=new AtomicReference<>();
        AtomicReference<Integer> targetRowNumber=new AtomicReference<>();

        List<String> sourceMisMatchedColumns = new ArrayList<>();
        List<String> targetMisMatchedColumns = new ArrayList<>();

        mappingData.forEach((sourceColumnName, targetColumnName) ->
        {
            Map<String,CellItem> sourceRecordMap = sourceRecord.get(0).getValue();
            Map<String,CellItem> targetRecordMap = targetRecord.get(0).getValue();

            CellItem sourceCellItem = sourceRecordMap.get(sourceColumnName);
            CellItem targetCellItem = targetRecordMap.get(targetColumnName);
            boolean isDataNotNull = sourceCellItem!=null && targetCellItem!=null;
            if(isDataNotNull)
            {
                if(sourceCellItem.getData().equalsIgnoreCase(targetCellItem.getData()))
                {
                    //logger.info("Data Matched for source row :{},column:{}",sourceRecord.get(0).getKey(),sourceColumnName);
                }
                else
                {
                    //logger.error("Data Mismatch for source row :{},column:{}",sourceRecord.get(0).getKey(),sourceColumnName);
                    sourceRowNumber.set(sourceRecord.get(0).getKey());
                    targetRowNumber.set(targetRecord.get(0).getKey());
                    //logger.error("misMatchedColumns :{},sourceColumnName:{}",misMatchedColumns,sourceColumnName);
                    sourceMisMatchedColumns.add(sourceColumnName);
                    targetMisMatchedColumns.add(targetColumnName);
                }
            }

        });
        if(!sourceMisMatchedColumns.isEmpty())
        {
            //logger.info("Problem with Row:{},columns are :{}",rowNumber,misMatchedColumns);
            sourceMisMatchRowColumnListPair = new Pair<>(sourceRowNumber.get(),sourceMisMatchedColumns);
        }
        if(!targetMisMatchedColumns.isEmpty())
        {
            //logger.info("Problem with Row:{},columns are :{}",rowNumber,misMatchedColumns);
            targetMisMatchRowColumnListPair = new Pair<>(targetRowNumber.get(),targetMisMatchedColumns);
        }
        //logger.info("Problem with Row:{},columns are :{}",rowNumber,misMatchedColumns);
        return new Pair<>(sourceMisMatchRowColumnListPair,targetMisMatchRowColumnListPair);

    }

    public static void compare(Map<Integer, Map> sourceDataSet,Map<Integer,Map> targetDataSet,Map<String, String> mappingData,Workbook workbook)
    {
        sourceDataSet.entrySet().stream()
                .forEach(
                        sourceRow->
                        {
                            Map targetRowValue = targetDataSet.get(sourceRow.getKey());
                            compareRowMapAndHighlight(sourceRow.getValue(),targetRowValue,mappingData,workbook);
                        }
                );
    }
    public static void compareLists(List<String> mappingColumns,List<String> actualColumns)
    {
        List<String> missingColumnInActual = mappingColumns.stream()
                .filter(element -> !actualColumns.contains(element))
                .collect(Collectors.toList());
        List<String> additionalColumnInActual = actualColumns.stream()
                .filter(element -> !mappingColumns.contains(element))
                .collect(Collectors.toList());
        if(!missingColumnInActual.isEmpty())
        {
            logger.warn("Following columns are present in mapping but missing in actual :{}",missingColumnInActual);
        }
        if(!additionalColumnInActual.isEmpty())
        {
            logger.warn("Following columns are NOT present in mapping but available  in actual :{}",additionalColumnInActual);
        }
    }

    public static void compareMappingColumnsWithSourceAndTarget(Sheet mappingSheet,Sheet sourceSheet,Sheet targetSheet)
    {
        List<String> mappingSourceColumns = ExcelUtil.getColumnListFromSheet(mappingSheet,1);
        List<String> mappingTargetColumns = ExcelUtil.getColumnListFromSheet(mappingSheet,2);
        List<String> actualSourceColumns = ExcelUtil.getColumnListFromSheet(sourceSheet,0);
        List<String> actualTargetColumns = ExcelUtil.getColumnListFromSheet(targetSheet,0);

        compareLists(mappingSourceColumns,actualSourceColumns);
        compareLists(mappingTargetColumns,actualTargetColumns);
    }

    public static boolean filterMappedColumns(Map.Entry<String,CellItem> item,Map<String, CellItem> targetMap,Map<String, String> mappingData)
    {
        boolean valueMismatch = true;
        String sourceKey = item.getKey();
        if(mappingData.containsKey(item.getKey()))
        {
            String mappedTargetKey = mappingData.get(sourceKey);
            CellItem sourceCellItem = item.getValue();
            if(targetMap.containsKey(mappedTargetKey))
            {
                CellItem targetCellItem = targetMap.get(mappedTargetKey);
                if(sourceCellItem.getData().equalsIgnoreCase(targetCellItem.getData()))
                {
                    valueMismatch=false;
                }
            }
        }
        else
        {
            //logger.info(" Column {} is not included in mapping hence skipped ",sourceKey);
            valueMismatch = false;
        }
        return valueMismatch;
    }


    public static void populateDuplicateData(   Map<Integer, Map> dataSet,List<String> keyColumnList)
    {
        Map<Integer, Map> duplicateDataSet = new LinkedHashMap<>();
        dataSet.entrySet()
                .stream()
                .collect(
                        Collectors.groupingBy(item->getConcatenatedFieldValueFromItem(item.getValue(),keyColumnList))
                ).entrySet().stream()
                .filter(item->addFilteredItemToDataSet(item,duplicateDataSet))
                .count(); //Count is added only to invoke the operation;
                //.forEach(x->logger.info("x:{}",x));
        logger.info("duplicateDataSet:{}",duplicateDataSet);


    }

    public static Map<Integer, Map> getUniqueDataSet(   Map<Integer, Map> dataSet,Map<Integer, Map> duplicateDataSet)
    {
        Map<Integer, Map> uniqueDataSet =
        dataSet.entrySet().stream()
                .filter(item -> !duplicateDataSet.containsKey(item.getKey()))
                .collect(Collectors.toMap(item->item.getKey(),item->item.getValue()));
        //logger.info("uniqueDataSet:{}",uniqueDataSet);
        return uniqueDataSet;
    }

    public static Map<Integer, Map> getDuplicateDataSet(   Map<Integer, Map> dataSet,List<String> keyColumnList)
    {
        Stream<Map.Entry<String, List<Map.Entry<Integer, Map>>>> stream1 = dataSet.entrySet()
                .stream()
                .collect(
                        Collectors.groupingBy(item->getConcatenatedFieldValueFromItem(item.getValue(),keyColumnList))
                )
                .entrySet().stream();
        Map<Integer, Map> duplicateDataSet =
        stream1.filter(me->
                {
                    boolean valid = false;
                    List<?> value = me.getValue();
                    if(value.size() > 1)
                    {
                        valid=true;
                    }
                    return valid;
                }

        )

                .map(me-> me.getValue())
                .flatMap(List::stream)
                .collect(Collectors.toMap(me->me.getKey(),me->me.getValue()));
        //logger.info("duplicateDataSet:{}",duplicateDataSet);
        return duplicateDataSet;
    }

    private static boolean addFilteredItemToDataSet(Object itemObj,Map<Integer, Map> dataSet)
    {
       Map.Entry me = (Map.Entry) itemObj;
       String key = (String) me.getKey();
       List value = (List) me.getValue();
       boolean validEntry = false;
       if(!value.isEmpty() && value.size()>1 )
       {
           logger.info("Record with key :{} is valid duplicate as item count is :{}",key,value.size());
           validEntry =true;
           Map.Entry<Integer,Map> recordEntry = (Map.Entry<Integer,Map>) value.get(0);
           dataSet.put(recordEntry.getKey(), recordEntry.getValue());
       }
       else
       {
           logger.info("Record with key :{} is invalid as item count is :{}",key,value.size());
       }
       return validEntry;

    }

    private static String getConcatenatedFieldValueFromItem(Map<String,CellItem> rowMap,List<String> keyColumnList)
    {
        String concatenatedKey = "";
        List<String> keyList = new ArrayList<>();
        for (Map.Entry<String, CellItem> entry : rowMap.entrySet()) {
            //logger.info(entry.getKey() + ":" + entry.getValue());
            if(keyColumnList.contains(entry.getKey()))
            {
                keyList.add(entry.getValue().getData());
            }
            else
            {
               // logger.warn("Ignoring column :{} as it is not part of key columns",entry.getKey());
            }
        }
        concatenatedKey = String.join("_",keyList);
        return concatenatedKey;

    }

    public static void compareRowMapAndHighlight(Map<String, CellItem> sourceMap, Map<String, CellItem> targetMap,Map<String, String> mappingData,Workbook workbook)
    {
        sourceMap.entrySet().stream()
                .filter(item -> filterMappedColumns(item,targetMap,mappingData))
                .forEach(item->
                {
                    Cell sourceCell= item.getValue().getCell();
                    String targetKey = mappingData.get(item.getKey());
                    Cell targetCell= targetMap.get(targetKey).getCell();
                    ExcelUtil.highLightCell(sourceCell,workbook);
                    ExcelUtil.highLightCell(targetCell,workbook);

                });

    }

    public static void main(String[] args) {

    }
}
