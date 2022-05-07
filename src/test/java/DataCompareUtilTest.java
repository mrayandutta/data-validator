import org.datavalidator.model.CellItem;
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
        Map<String, CellItem> sourceMap = new LinkedHashMap<>();
        Map<String, CellItem> targetMap = new LinkedHashMap<>();

        sourceMap.put("C1",new CellItem("A",1,null));
        sourceMap.put("C2",new CellItem("B",2,null));
        sourceMap.put("C3",new CellItem("C",3,null));

        targetMap.put("C1",new CellItem("A",1,null));
        targetMap.put("C2",new CellItem("B1",2,null));
        targetMap.put("C3",new CellItem("C1",3,null));

        DataCompareUtil.compareRowMap(sourceMap,targetMap);

    }
}
