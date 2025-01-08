package model.dto.export;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

public class SskExcel {
	String fileName="default.xlsx";
    Workbook wb;
    Sheet sheet;
    int rowIndex = 0;

    CellStyle defaultCellStyle, headerCellStyle, bodyCellStyle;
    
    /*Get FileName*/
    public String getFileName(){
        return fileName;
    }

    /*Get Workbook*/
    public Workbook getWorkBook(){
        return wb;
    }
    
    /*Set Standard Cell Style - defualt, header, body*/
    public void setDefaultCellStyle(){
        defaultCellStyle = wb.createCellStyle();

        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        defaultCellStyle.setFont(font);

        // 정렬
        defaultCellStyle.setAlignment(HorizontalAlignment.CENTER);
        defaultCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); 
    }
    public void setHeaderCellStyle(){
        headerCellStyle = wb.createCellStyle();

        // 배경색 지정
        headerCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 정렬
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 테두리
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
    }

    public void setBodyCellStyle(){
        bodyCellStyle = wb.createCellStyle();

        // 정렬
        bodyCellStyle.setAlignment(HorizontalAlignment.CENTER);
        bodyCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 테두리
        bodyCellStyle.setBorderTop(BorderStyle.THIN);
        bodyCellStyle.setBorderBottom(BorderStyle.THIN);
        bodyCellStyle.setBorderLeft(BorderStyle.THIN);
        bodyCellStyle.setBorderRight(BorderStyle.THIN);
    }
}
