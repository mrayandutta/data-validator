package org.datavalidator.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.datavalidator.model.CellItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;

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
            logger.info(" Column {} is not included in mapping hence skipped ",sourceKey);
            valueMismatch = false;
        }
        return valueMismatch;
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
}