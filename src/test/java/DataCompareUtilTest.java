import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.datavalidator.model.CellItem;
import org.datavalidator.util.DataCompareUtil;
import org.datavalidator.util.ExcelUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

        sourceMap.put("C1",new CellItem("A",1,null));
        sourceMap.put("C2",new CellItem("B",1,null));
        sourceMap.put("C3",new CellItem("C",1,null));

        Map<String, CellItem> targetMap = new LinkedHashMap<>();

        targetMap.put("C1",new CellItem("A",1,null));
        targetMap.put("C2",new CellItem("B1",1,null));
        targetMap.put("C3",new CellItem("C1",1,null));

        Map<String, String> mappingData = new LinkedHashMap<>();
        mappingData.put("C1","C1");
        mappingData.put("C2","C2");
        mappingData.put("C3","C3");

        Workbook mockWorkbook = mock(Workbook.class);
        CellStyle mockCellStyle = mock(CellStyle.class);
        Cell mockCell = mock(Cell.class);
        when(mockWorkbook.createCellStyle()).thenReturn(mockCellStyle);

        DataCompareUtil.compareRowMapAndHighlight(sourceMap,targetMap,mappingData,mockWorkbook);

    }

    @DisplayName("Test DataCompareUtil compare")
    @Test
    public void testCompare() {
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

        //ExcelUtil mockExcelUtil = mock(ExcelUtil.class);
        //when(mockExcelUtil.).thenReturn(mockCellStyle);

        //DataCompareUtil dataCompareUtil = PowerMockito.field(DataCompareUtil.class, "b");
        //Field field = PowerMockito.field(DataCompareUtil.class, "excelUtil");
        //field.set(DataCompareUtil.class, mock(ExcelUtil.class));


        Map<String, String> mappingData = new LinkedHashMap<>();
        mappingData.put("C1","C1");
        mappingData.put("C2","C2");
        mappingData.put("C3","C3");

        DataCompareUtil.compare(sourceDataMap,targetDataMap,mappingData,mockWorkbook);

    }


    @DisplayName("Test DataCompareUtil getUniqueData and duplicate")
    @Test
    public void testGetUniqueData() {
        Map<String, CellItem> sourceRowMap1 = new LinkedHashMap<>();
        sourceRowMap1.put("C1",new CellItem("A",1,null));
        sourceRowMap1.put("C2",new CellItem("B",1,null));
        sourceRowMap1.put("C3",new CellItem("C",1,null));

        Map<String, CellItem> sourceRowMap2 = new LinkedHashMap<>();
        sourceRowMap2.put("C1",new CellItem("P",2,null));
        sourceRowMap2.put("C2",new CellItem("Q",2,null));
        sourceRowMap2.put("C3",new CellItem("R",2,null));

        Map<String, CellItem> sourceRowMap1Duplicate = new LinkedHashMap<>();
        sourceRowMap1Duplicate.put("C1",new CellItem("A",1,null));
        sourceRowMap1Duplicate.put("C2",new CellItem("B",1,null));
        sourceRowMap1Duplicate.put("C3",new CellItem("XYZ",1,null));

        Map<String, CellItem> sourceRowMap2Duplicate = new LinkedHashMap<>();
        sourceRowMap2Duplicate.put("C1",new CellItem("P",2,null));
        sourceRowMap2Duplicate.put("C2",new CellItem("Q",2,null));
        sourceRowMap2Duplicate.put("C3",new CellItem("R123",2,null));

        Map<String, CellItem> sourceRowMap3 = new LinkedHashMap<>();
        sourceRowMap3.put("C1",new CellItem("X",3,null));
        sourceRowMap3.put("C2",new CellItem("Y",3,null));
        sourceRowMap3.put("C3",new CellItem("Z",3,null));

        Map<Integer, Map> sourceDataMap = new LinkedHashMap<>();

        sourceDataMap.put(1,sourceRowMap1);
        sourceDataMap.put(2,sourceRowMap2);
        sourceDataMap.put(3,sourceRowMap1Duplicate);
        sourceDataMap.put(4,sourceRowMap2Duplicate);
        sourceDataMap.put(5,sourceRowMap3);

        Map<String,String> keyColumnMappingData = new LinkedHashMap<>();
        keyColumnMappingData.put("C1","C1");
        keyColumnMappingData.put("C2","C2");

        List<String> keyColumnListForSource = new ArrayList<String>();
        keyColumnListForSource.addAll(keyColumnMappingData.keySet());

        //DataCompareUtil.populateUniqueData(sourceDataMap,keyColumnListForSource);
        //DataCompareUtil.populateDuplicateData(sourceDataMap,keyColumnListForSource);
        Map<Integer, Map> duplicateDataSet = DataCompareUtil.getDuplicateDataSet(sourceDataMap,keyColumnListForSource);
        Map<Integer, Map> uniqueDataSet = DataCompareUtil.getUniqueDataSet(sourceDataMap,duplicateDataSet);



    }

    @DisplayName("Test DataCompareUtil compareNew")
    @Test
    public void testCompareNew() {
        Map<String, CellItem> sourceRowMap1 = new LinkedHashMap<>();
        sourceRowMap1.put("C1",new CellItem("A",1,null));
        sourceRowMap1.put("C2",new CellItem("B",1,null));
        sourceRowMap1.put("C3",new CellItem("C",1,null));

        Map<String, CellItem> targetRowMap1 = new LinkedHashMap<>();
        targetRowMap1.put("C1",new CellItem("A",1,null));
        targetRowMap1.put("C2",new CellItem("B",1,null));
        targetRowMap1.put("C3",new CellItem("C1",1,null));

        Map<String, CellItem> sourceRowMap2 = new LinkedHashMap<>();
        sourceRowMap2.put("C1",new CellItem("P",2,null));
        sourceRowMap2.put("C2",new CellItem("Q",2,null));
        sourceRowMap2.put("C3",new CellItem("R",2,null));

        Map<String, CellItem> sourceRowMap3 = new LinkedHashMap<>();
        sourceRowMap3.put("C1",new CellItem("X",3,null));
        sourceRowMap3.put("C2",new CellItem("Y",3,null));
        sourceRowMap3.put("C3",new CellItem("Z",3,null));

        Map<String, CellItem> targetRowMap2 = new LinkedHashMap<>();
        targetRowMap2.put("C1",new CellItem("P",2,null));
        targetRowMap2.put("C2",new CellItem("Q",2,null));
        targetRowMap2.put("C3",new CellItem("R",2,null));

        Map<String, CellItem> targetRowMap3 = new LinkedHashMap<>();
        targetRowMap3.put("C1",new CellItem("X1",3,null));
        targetRowMap3.put("C2",new CellItem("Y",3,null));
        targetRowMap3.put("C3",new CellItem("Z",3,null));

        Map<Integer, Map> sourceDataMap = new LinkedHashMap<>();
        Map<Integer, Map> targetDataMap = new LinkedHashMap<>();

        sourceDataMap.put(1,sourceRowMap1);
        sourceDataMap.put(2,sourceRowMap2);
        sourceDataMap.put(3,sourceRowMap3);

        targetDataMap.put(1,targetRowMap1);
        targetDataMap.put(2,targetRowMap2);
        targetDataMap.put(3,targetRowMap3);



        Workbook mockWorkbook = mock(Workbook.class);
        CellStyle mockCellStyle = mock(CellStyle.class);
        Cell mockCell = mock(Cell.class);
        when(mockWorkbook.createCellStyle()).thenReturn(mockCellStyle);

        Map<String, String> mappingData = new LinkedHashMap<>();
        mappingData.put("C1","C1");
        mappingData.put("C2","C2");
        mappingData.put("C3","C3");

        Map<String,String> keyColumnMappingData = new LinkedHashMap<>();
        keyColumnMappingData.put("C1","C1");
        keyColumnMappingData.put("C2","C2");

        DataCompareUtil.compareNew(sourceDataMap,targetDataMap,mappingData,keyColumnMappingData,mockWorkbook);

    }


}
