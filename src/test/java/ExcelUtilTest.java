import org.apache.poi.ss.usermodel.*;
import org.datavalidator.model.CellItem;
import org.datavalidator.util.DataCompareUtil;
import org.datavalidator.util.ExcelUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtilTest {
    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @DisplayName("Test ExcelUtil getMappingData")
    @Test
    public void testGetMappingData()
    {
        String filePath1 = "./mapping.xlsx";
        Workbook workbook = ExcelUtil.getWorkbookFromExcel(filePath1);
        Sheet sheet = ExcelUtil.getSheetFromWorkbook(workbook,0);
        Map<String, String> mappingData =ExcelUtil.getMappingData(sheet);
        logger.info("mappingData:{}",mappingData);
    }

    @DisplayName("Test ExcelUtil getWorkbookFromExcel and getSheetFromWorkbook")
    @Test
    public void testFlow()
    {
        String filePath1 = "./sample.xlsx";
        Workbook workbook = ExcelUtil.getWorkbookFromExcel(filePath1);
        Sheet sheet = ExcelUtil.getSheetFromWorkbook(workbook,0);
        Map<Integer, Map> dataset1 = ExcelUtil.getDataFromSheet(sheet);
    }

}
