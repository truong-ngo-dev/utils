package com.nob.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.Objects;

/**
 * Utility of apache poi lib for manipulate excel file
 * @author Truong Ngo
 * */
public class ExcelUtils {

    public static String getValueAsString(Cell cell) {
        if (Objects.isNull(cell)) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> getNumbericString(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    public static String getNumbericString(double number) {
        return  (number == (long) number) ?
                String.format("%d", (long) number) :
                String.format("%s", number);
    }
    
    public static void cloneRow(Row src, Row dst) {
        if (src == null || dst == null) return;
        for (Cell srcCell : src) {
            Cell dstCell = dst.createCell(srcCell.getColumnIndex());
            cloneCell(srcCell, dstCell);
        }
    }

    public static void cloneCell(Cell srcCell, Cell dstCell) {
        switch (srcCell.getCellType()) {
            case STRING -> dstCell.setCellValue(srcCell.getStringCellValue());
            case NUMERIC -> dstCell.setCellValue(srcCell.getNumericCellValue());
            case BOOLEAN -> dstCell.setCellValue(srcCell.getBooleanCellValue());
            case FORMULA -> dstCell.setCellFormula(srcCell.getCellFormula());
            case ERROR -> dstCell.setCellErrorValue(srcCell.getErrorCellValue());
            case BLANK -> dstCell.setBlank();
        }
        if (srcCell.getCellStyle() != null) {
            dstCell.setCellStyle(srcCell.getCellStyle());
        }
    }
}
