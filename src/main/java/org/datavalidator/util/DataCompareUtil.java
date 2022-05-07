package org.datavalidator.util;

import org.datavalidator.model.CellItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataCompareUtil {
    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static void compare(Map<Integer, Map> sourceDataSet,Map<Integer,Map> targetDataSet)
    {
        //sourceDataSet.entrySet().stream()

    }

    public static void compareRowMap(Map<String, CellItem> sourceMap, Map<String, CellItem> targetMap)
    {
        //List output =
        sourceMap.entrySet().stream()
                .filter(item ->
                {
                    boolean valueMismatch = true;
                    String sourceKey = item.getKey();
                    //String sourceValue = item.getValue();
                    CellItem sourceCellItem = item.getValue();
                    if(targetMap.containsKey(sourceKey))
                    {
                        CellItem targetCellItem = targetMap.get(sourceKey);
                        if(sourceCellItem.getData().equalsIgnoreCase(targetCellItem.getData()))
                        {
                            valueMismatch=false;
                        }
                    }
                    return valueMismatch;
                })
                .forEach(item->logger.info("Mismatch for key:{},value:{}",item.getKey(),item.getValue().getData()));
                //.collect(Collectors.toList());
                //logger.info("output:{}",output);

    }
}
