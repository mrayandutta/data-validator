import org.datavalidator.util.DataCompareUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataCompareUtilTest {
    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @DisplayName("Test DataCompareUtil compare")
    @Test
    public void testCompareRowMap()
    {
        Map<String, String> sourceMap = new LinkedHashMap<>();
        Map<String, String> targetMap = new LinkedHashMap<>();

        sourceMap.put("C1","A");
        sourceMap.put("C2","B");
        sourceMap.put("C3","C");

        targetMap.put("C1","A");
        targetMap.put("C2","B1");
        targetMap.put("C3","C1");

        DataCompareUtil.compareRowMap(sourceMap,targetMap);

    }
}
