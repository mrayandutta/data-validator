package org.datavalidator.util;

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

    public static void compareRowMap(Map<String, String> sourceMap,Map<String, String> targetMap)
    {
        //List output =
        sourceMap.entrySet().stream()
                .filter(item ->
                {
                    boolean valueMismatch = true;
                    String sourceKey = item.getKey();
                    String sourceValue = item.getValue();
                    if(targetMap.containsKey(sourceKey))
                    {
                        String targetValue = targetMap.get(sourceKey);
                        if(sourceValue.equalsIgnoreCase(targetValue))
                        {
                            valueMismatch=false;
                        }
                    }
                    return valueMismatch;
                })
                .forEach(item->logger.info("Mismatch for key:{},value:{}",item.getKey(),item.getValue()));
                //.collect(Collectors.toList());
                //logger.info("output:{}",output);

    }
}
