package org.datavalidator.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.datavalidator.model.CellItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility to compare the data sets based on the mapping provided
 */
public class DataCompareUtil {
    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
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

/*
    public static void findDuplicateInStream(   Map<Integer, Map> sourceDataSet,Map<String,String> keyColumnMappingData,
                                                           Stream<T> stream)
    {
        sourceDataSet.entrySet().stream()
                .collect(
                        Collectors.groupingBy(
                                item ->
                                {
                                    StringBuffer value = new StringBuffer();
                                    Map<String,CellItem> rowMap= (Map<String, CellItem>) item;
                                    for(Map.Entry<String,CellItem> me :rowMap.entrySet())
                                    {
                                        if(keyColumnMappingData.containsKey(me.getKey()))
                                        {
                                            CellItem cellItem = me.getValue();
                                            value.append(cellItem.getData());
                                        }
                                        CellItem cellItem = me.getValue();
                                        value.append(cellItem.getData());
                                    }
                                   return value.toString();
                                }
                        ),Collectors.toList()
                );

    }
  */

    public static void findDuplicateInStream(   Map<Integer, Map> sourceDataSet,Map<String,String> keyColumnMappingData)
    {
        Map<Integer, Map> uniqueDataSet = new LinkedHashMap<>();
        sourceDataSet.entrySet()
                .stream()
                .collect(
                        Collectors.groupingBy(item->getConcatenatedFieldValueFromItem(item.getValue(),keyColumnMappingData))
                ).entrySet().stream()
                .filter(item->checkItemForFilter(item,uniqueDataSet))
                .forEach(x->logger.info("x:{}",x));
        logger.info("uniqueDataSet:{}",uniqueDataSet);
        Map<Integer, Map> duplicateDataSet = new LinkedHashMap<>();
        sourceDataSet.entrySet()
                .stream()
                .collect(
                        Collectors.groupingBy(item->getConcatenatedFieldValueFromItem(item.getValue(),keyColumnMappingData))
                ).entrySet().stream()
                .filter(item->!checkItemForFilter(item,duplicateDataSet))
                .forEach(x->logger.info("x:{}",x));
        logger.info("duplicateDataSet:{}",duplicateDataSet);


    }

    public static void getUniqueData(   Map<Integer, Map> sourceDataSet,Map<String,String> keyColumnMappingData)
    {




    }

    private static boolean checkItemForFilter(Object itemObj,Map<Integer, Map> uniqueDataSet)
    {
       boolean validEntry = true;
       Map.Entry me = (Map.Entry) itemObj;
       String key = (String) me.getKey();
       List value = (List) me.getValue();

       logger.info("key:{}",me.getKey());
       if(!value.isEmpty() && value.size()>1)
       {
           logger.info("Record with key :{} is invalid as item count is :{}",key,value.size());
       }
       else
       {
           logger.info("Record with key :{} is valid ",key);
           Map.Entry<Integer,Map> recordEntry = (Map.Entry<Integer,Map>) value.get(0);
           uniqueDataSet.put(recordEntry.getKey(), recordEntry.getValue());
       }
       validEntry = false;
       return validEntry;

    }

    private static String getConcatenatedFieldValueFromItem(Map<String,CellItem> rowMap,Map<String,String> keyColumnMappingData)
    {
        String concatenatedKey = "";
        List<String> keyList = new ArrayList<>();
        for (Map.Entry<String, CellItem> entry : rowMap.entrySet()) {
            logger.info(entry.getKey() + ":" + entry.getValue());
            if(keyColumnMappingData.containsKey(entry.getKey()))
            {
                keyList.add(entry.getValue().getData());
            }
            else
            {
                logger.warn("Ignoring column :{} as it is not part of key columns",entry.getKey());
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
