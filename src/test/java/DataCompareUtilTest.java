import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.datavalidator.model.CellItem;
import org.datavalidator.util.DataCompareUtil;
import org.datavalidator.util.ExcelUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

public class DataCompareUtilTest {
    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @DisplayName("Test DataCompareUtil compareRowMap")
    @Test
    public void testCompareRowMap()
    {
        Map<String, CellItem> sourceMap = new LinkedHashMap<>();
        Map<String, CellItem> targetMap = new LinkedHashMap<>();

        sourceMap.put("C1",new CellItem("A",1,null));
        sourceMap.put("C2",new CellItem("B",1,null));
        sourceMap.put("C3",new CellItem("C",1,null));

        targetMap.put("C1",new CellItem("A",1,null));
        targetMap.put("C2",new CellItem("B1",1,null));
        targetMap.put("C3",new CellItem("C1",1,null));

        DataCompareUtil.compareRowMap(sourceMap,targetMap,null);

    }

    @DisplayName("Test DataCompareUtil compare")
    @Test
    public void testCompare()
    {
        Map<String, CellItem> sourceRowMap1 = new LinkedHashMap<>();
        sourceRowMap1.put("C1",new CellItem("A",1,null));
        sourceRowMap1.put("C2",new CellItem("B",1,null));
        sourceRowMap1.put("C3",new CellItem("C",1,null));

        Map<String, CellItem> targetRowMap1 = new LinkedHashMap<>();
        targetRowMap1.put("C1",new CellItem("A",1,null));
        targetRowMap1.put("C2",new CellItem("B1",1,null));
        targetRowMap1.put("C3",new CellItem("C1",1,null));

        Map<String, CellItem> sourceRowMap2 = new LinkedHashMap<>();
        sourceRowMap2.put("C1",new CellItem("P",2,null));
        sourceRowMap2.put("C2",new CellItem("Q",2,null));
        sourceRowMap2.put("C3",new CellItem("R",2,null));

        Map<String, CellItem> targetRowMap2 = new LinkedHashMap<>();
        targetRowMap2.put("C1",new CellItem("P1",2,null));
        targetRowMap2.put("C2",new CellItem("Q",2,null));
        targetRowMap2.put("C3",new CellItem("R",2,null));

        Map<Integer, Map> sourceDataMap = new LinkedHashMap<>();
        Map<Integer, Map> targetDataMap = new LinkedHashMap<>();

        sourceDataMap.put(1,sourceRowMap1);
        sourceDataMap.put(2,sourceRowMap2);

        targetDataMap.put(1,targetRowMap1);
        targetDataMap.put(2,targetRowMap2);

        Workbook mockWorkbook = mock(Workbook.class);
        CellStyle mockCellStyle = mock(CellStyle.class);
        Cell mockCell = mock(Cell.class);
        when(mockWorkbook.createCellStyle()).thenReturn(mockCellStyle);

        //mockStatic(ExcelUtil.class);

        //when(ExcelUtil.highLightCell(any(),any())).thenAnswer(i->mockCell);

        DataCompareUtil.compare(sourceDataMap,targetDataMap,mockWorkbook,null);

    }
}
