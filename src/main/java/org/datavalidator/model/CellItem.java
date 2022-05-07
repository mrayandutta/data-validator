package org.datavalidator.model;

import org.apache.poi.ss.usermodel.Cell;

public class CellItem
{
    private String data;
    private int rowNumber;
    private Cell cell;

    public CellItem(String data, int rowNumber, Cell cell) {
        this.data = data;
        this.rowNumber = rowNumber;
        this.cell = cell;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }
}
