package org.datavalidator.constants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ApplicationConstants
{
    public static final String REPORT_OUTPUT_FILE_LOCATION = "./report-output.xlsx";
    public static final String DATA_MISMATCH_SHEET_NAME = "Data Mismatch Report";
    public static final String DATA_DUPLICATION_SHEET_NAME = "Data Duplication Report";
    public static final String DATA_DUPLICATION_SHEET_COLUMN_NAME = "Source Row Number With Duplicates";

    static{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.setProperty("current.date", dateFormat.format(new Date()));
    }
}
