package model.dto.export;

import static model.dto.export.column.EsmColumnInfoOfTest.*;
import static model.dto.export.column.EsmRecordColumnInfoOfTest.*;
import static model.dto.export.column.LangColumnInfoOfTest.*;
import static model.dto.export.column.SdqColumnInfoOfTest.*;


import java.sql.Date;
import java.util.ArrayList;

import model.dto.LangReply;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.dto.EsmResultOfType;
import model.dto.SdqResultOfType;
import model.dto.export.column.EsmColumnInfoOfTest;
import model.dto.export.column.EsmRecordColumnInfoOfTest;
import model.dto.export.column.LangColumnInfoOfTest;
import model.dto.export.column.SdqColumnInfoOfTest;
import model.dto.export.data.EsmExcelDTO;
import model.dto.export.data.EsmRecordExcelDTO;
import model.dto.export.data.LangExcelDTO;
import model.dto.export.data.SdqExcelDTO;

public class SskExcelByTest extends SskExcel{

    /*init*/
    public SskExcelByTest(){
        wb = new XSSFWorkbook();
        sheet = wb.createSheet("검사 결과");

        setDefaultCellStyle();
        setHeaderCellStyle();
        setBodyCellStyle();
    }

    /*Lang Data Export*/
    public void addLangData(ArrayList<LangExcelDTO> langExcelDTOS){
    	this.fileName = "언어_발달_검사_결과_" + new Date(System.currentTimeMillis()) +".xlsx";

        Row titleRow = sheet.createRow(rowIndex);
        titleRow.setHeight((short)1000);
        Cell titleCell = titleRow.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, LangColumnInfoOfTest.getAllColumns().size()-1));
        titleCell.setCellValue("언어 발달 평가");
        titleCell.setCellStyle(defaultCellStyle);
        rowIndex++;
        
        Row headerRow = sheet.createRow(rowIndex++);

        for(LangColumnInfoOfTest x : LangColumnInfoOfTest.getAllColumns()){
            createCellWithStyle(headerRow,x.getColumnIndex(),x.getColumnText(),headerCellStyle);
            setAutoSizeColumnPlus(sheet,x.getColumnIndex());
        }

        for (LangExcelDTO langExcelDTO : langExcelDTOS) {
            Row bodyRow = sheet.createRow(rowIndex++);
            createCellWithStyleInt(bodyRow, LANG_USER_ID.getColumnIndex(), langExcelDTO.getUserId(), bodyCellStyle);
            createCellWithStyle(bodyRow, LANG_USER_NAME.getColumnIndex(), langExcelDTO.getUserName(), bodyCellStyle);
            createCellWithStyle(bodyRow, LANG_DATE.getColumnIndex(), langExcelDTO.getDateStr(), bodyCellStyle);
            createCellWithStyle(bodyRow, LANG_AGE_GROUP.getColumnIndex(), langExcelDTO.getAgeGroupStr(), bodyCellStyle);
            sheet.autoSizeColumn(LANG_DATE.getColumnIndex());
            sheet.autoSizeColumn(LANG_AGE_GROUP.getColumnIndex());
            System.out.println("langExcelDTO.getReplyList() size : " + langExcelDTO.getReplyList().size());
            createCellWithStyle(bodyRow, LANG_ANSWER1.getColumnIndex(), langExcelDTO.getReplyList().get(0), bodyCellStyle);
            createCellWithStyle(bodyRow, LANG_ANSWER2.getColumnIndex(), langExcelDTO.getReplyList().get(1), bodyCellStyle);
            createCellWithStyle(bodyRow, LANG_ANSWER3.getColumnIndex(), langExcelDTO.getReplyList().get(2), bodyCellStyle);
            createCellWithStyle(bodyRow, LANG_ANSWER4.getColumnIndex(), langExcelDTO.getReplyList().get(3), bodyCellStyle);
            createCellWithStyle(bodyRow, LANG_ANSWER5.getColumnIndex(), langExcelDTO.getReplyList().get(4), bodyCellStyle);
            createCellWithStyle(bodyRow, LANG_ANSWER6.getColumnIndex(), langExcelDTO.getReplyList().get(5), bodyCellStyle);
            createCellWithStyle(bodyRow, LANG_ANSWER7.getColumnIndex(), langExcelDTO.getReplyList().get(6), bodyCellStyle);
        }
    }

    /*Sdq Data Export*/
    public void addSdqData(ArrayList<SdqExcelDTO> sdqExcelDTOS){
    	this.fileName = "SDQ_정서_행동_발달_검사_결과_" + new Date(System.currentTimeMillis()) +".xlsx";

        Row titleRow = sheet.createRow(rowIndex);
        titleRow.setHeight((short)1000);
        Cell titleCell = titleRow.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0,SdqColumnInfoOfTest.getAllColumns().size()-1));
        titleCell.setCellValue("정서/행동 발달 평가");
        titleCell.setCellStyle(defaultCellStyle);
        rowIndex++;

        Row headerRow = sheet.createRow(rowIndex++);

        for(SdqColumnInfoOfTest x : SdqColumnInfoOfTest.getAllColumns()){
            createCellWithStyle(headerRow,x.getColumnIndex(),x.getColumnText(),headerCellStyle);
            setAutoSizeColumnPlus(sheet,x.getColumnIndex());
        }

        for (SdqExcelDTO sdqExcelDTO : sdqExcelDTOS) {
            Row bodyRow = sheet.createRow(rowIndex++);
            createCellWithStyleInt(bodyRow, SDQ_USER_ID.getColumnIndex(), sdqExcelDTO.getUserId(), bodyCellStyle);
            createCellWithStyle(bodyRow, SDQ_USER_NAME.getColumnIndex(), sdqExcelDTO.getUserName(), bodyCellStyle);
            createCellWithStyle(bodyRow, SDQ_TARGET.getColumnIndex(), sdqExcelDTO.getTarget(), bodyCellStyle);
            createCellWithStyle(bodyRow, SDQ_DATETIME.getColumnIndex(), sdqExcelDTO.getDateStr(), bodyCellStyle);
            sheet.autoSizeColumn(SDQ_DATETIME.getColumnIndex());
            createCellWithStyleInt(bodyRow, SDQ_ANSWER1.getColumnIndex(), sdqExcelDTO.getReplyList().get(0), bodyCellStyle);
            createCellWithStyleInt(bodyRow, SDQ_ANSWER2.getColumnIndex(), sdqExcelDTO.getReplyList().get(1), bodyCellStyle);
            createCellWithStyleInt(bodyRow, SDQ_ANSWER3.getColumnIndex(), sdqExcelDTO.getReplyList().get(2), bodyCellStyle);
            createCellWithStyleInt(bodyRow, SDQ_ANSWER4.getColumnIndex(), sdqExcelDTO.getReplyList().get(3), bodyCellStyle);
            createCellWithStyleInt(bodyRow, SDQ_ANSWER5.getColumnIndex(), sdqExcelDTO.getReplyList().get(4), bodyCellStyle);
            createCellWithStyleInt(bodyRow, SDQ_ANSWER6.getColumnIndex(), sdqExcelDTO.getReplyList().get(5), bodyCellStyle);
            createCellWithStyleInt(bodyRow, SDQ_ANSWER7.getColumnIndex(), sdqExcelDTO.getReplyList().get(6), bodyCellStyle);
            createCellWithStyleInt(bodyRow, SDQ_ANSWER8.getColumnIndex(), sdqExcelDTO.getReplyList().get(7), bodyCellStyle);
            createCellWithStyleInt(bodyRow, SDQ_ANSWER9.getColumnIndex(), sdqExcelDTO.getReplyList().get(8), bodyCellStyle);
            createCellWithStyleInt(bodyRow, SDQ_ANSWER10.getColumnIndex(), sdqExcelDTO.getReplyList().get(9), bodyCellStyle);

            ArrayList<SdqResultOfType> scoreList = sdqExcelDTO.getScoreList();

            /*match sdq result and type column*/
            for (SdqResultOfType sdqResultOfType : scoreList) {
                String typeName = sdqResultOfType.getSdqType();
                int columnIndex = SdqColumnInfoOfTest.findByColumnText(typeName).getColumnIndex();

                createCellWithStyleInt(bodyRow, columnIndex, sdqResultOfType.getResult(), bodyCellStyle);
                sheet.autoSizeColumn(columnIndex);

            }

        }

    }

    /*Esm Data Export*/
    public void addEsmData(ArrayList<EsmExcelDTO> esmExcelDTOS){
    	this.fileName = "ESM_정서_반복_기록_" + new Date(System.currentTimeMillis()) +".xlsx";
        
        Row titleRow = sheet.createRow(rowIndex);
        titleRow.setHeight((short)1000);
        Cell titleCell = titleRow.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0,EsmColumnInfoOfTest.getAllColumns().size()-1));
        titleCell.setCellValue("정서 반복 기록");
        titleCell.setCellStyle(defaultCellStyle);
        rowIndex++;

        Row headerRow = sheet.createRow(rowIndex++);

        for(EsmColumnInfoOfTest x : EsmColumnInfoOfTest.getAllColumns()){
            createCellWithStyle(headerRow,x.getColumnIndex(),x.getColumnText(),headerCellStyle);
            setAutoSizeColumnPlus(sheet,x.getColumnIndex());
        }

        for (EsmExcelDTO esmExcelDTO : esmExcelDTOS) {
            Row bodyRow = sheet.createRow(rowIndex++);
            createCellWithStyleInt(bodyRow, ESM_USER_ID.getColumnIndex(), esmExcelDTO.getUserId(), bodyCellStyle);
            createCellWithStyle(bodyRow, ESM_USER_NAME.getColumnIndex(), esmExcelDTO.getUserName(), bodyCellStyle);
            createCellWithStyle(bodyRow, ESM_DATETIME.getColumnIndex(), esmExcelDTO.getDateStr(), bodyCellStyle);
            sheet.autoSizeColumn(ESM_DATETIME.getColumnIndex());
            createCellWithStyleInt(bodyRow, ESM_ANSWER1.getColumnIndex(), esmExcelDTO.getReplyList().get(0), bodyCellStyle);
            createCellWithStyleInt(bodyRow, ESM_ANSWER2.getColumnIndex(), esmExcelDTO.getReplyList().get(1), bodyCellStyle);
            createCellWithStyleInt(bodyRow, ESM_ANSWER3.getColumnIndex(), esmExcelDTO.getReplyList().get(2), bodyCellStyle);
            createCellWithStyleInt(bodyRow, ESM_ANSWER4.getColumnIndex(), esmExcelDTO.getReplyList().get(3), bodyCellStyle);
            createCellWithStyleInt(bodyRow, ESM_ANSWER5.getColumnIndex(), esmExcelDTO.getReplyList().get(4), bodyCellStyle);
            createCellWithStyleInt(bodyRow, ESM_ANSWER6.getColumnIndex(), esmExcelDTO.getReplyList().get(5), bodyCellStyle);
            createCellWithStyleInt(bodyRow, ESM_ANSWER7.getColumnIndex(), esmExcelDTO.getReplyList().get(6), bodyCellStyle);
            createCellWithStyleInt(bodyRow, ESM_ANSWER8.getColumnIndex(), esmExcelDTO.getReplyList().get(7), bodyCellStyle);
            createCellWithStyleInt(bodyRow, ESM_ANSWER9.getColumnIndex(), esmExcelDTO.getReplyList().get(8), bodyCellStyle);
            createCellWithStyleInt(bodyRow, ESM_ANSWER10.getColumnIndex(), esmExcelDTO.getReplyList().get(9), bodyCellStyle);

            ArrayList<EsmResultOfType> scoreList = esmExcelDTO.getScoreList();

            /*match sdq result and type column*/
            for (EsmResultOfType esmResultOfType : scoreList) {
                String typeName = esmResultOfType.getEsmType();
                int columnIndex = EsmColumnInfoOfTest.findColumnByEsmType(typeName).getColumnIndex();

                createCellWithStyleInt(bodyRow, columnIndex, esmResultOfType.getResult(), bodyCellStyle);
            }

        }
    }

    /*ESM Record Data Export*/
    public void addEsmRecordData(ArrayList<EsmRecordExcelDTO> esmRecordExcelDTOS){
    	this.fileName = "ESM_정서_다이어리_" + new Date(System.currentTimeMillis()) +".xlsx";
        
        Row titleRow = sheet.createRow(rowIndex);
        titleRow.setHeight((short)1000);
        Cell titleCell = titleRow.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0,EsmRecordColumnInfoOfTest.getAllColumns().size()-1));
        titleCell.setCellValue("정서 다이어리");
        titleCell.setCellStyle(defaultCellStyle);
        rowIndex++;

        Row headerRow = sheet.createRow(rowIndex++);

        for(EsmRecordColumnInfoOfTest x : EsmRecordColumnInfoOfTest.getAllColumns()){
            createCellWithStyle(headerRow,x.getColumnIndex(),x.getColumnText(),headerCellStyle);
            setAutoSizeColumnPlus(sheet,x.getColumnIndex());
        }

        for (EsmRecordExcelDTO esmRecordExcelDTO : esmRecordExcelDTOS) {
            Row bodyRow = sheet.createRow(rowIndex++);
            createCellWithStyleInt(bodyRow, ESM_RECORD_USER_ID.getColumnIndex(), esmRecordExcelDTO.getUserId(), bodyCellStyle);
            createCellWithStyle(bodyRow, ESM_RECORD_USER_NAME.getColumnIndex(), esmRecordExcelDTO.getUserName(), bodyCellStyle);
            createCellWithStyle(bodyRow, ESM_RECORD_DATETIME.getColumnIndex(), esmRecordExcelDTO.getDateStr(), bodyCellStyle);
            sheet.autoSizeColumn(ESM_RECORD_DATETIME.getColumnIndex());
            createCellWithStyle(bodyRow, ESM_RECORD_TEXT.getColumnIndex(), esmRecordExcelDTO.getText(), bodyCellStyle);
        }
    }

    public void setAutoSizeColumnPlus(Sheet sheet, int columnIndex){
        sheet.autoSizeColumn(columnIndex);
        sheet.setColumnWidth(columnIndex,sheet.getColumnWidth(columnIndex)+1000);
    }
    public void createCellWithStyle(Row row, int columnIndex, String value, CellStyle cellStyle){
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }

    public void createCellWithStyleInt(Row row, int columnIndex, int value, CellStyle cellStyle){
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }

    public void createCellWithStyleFloat(Row row, int columnIndex, float value, CellStyle cellStyle){
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }



    
}
