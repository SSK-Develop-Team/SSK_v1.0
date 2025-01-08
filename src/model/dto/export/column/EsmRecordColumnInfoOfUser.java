package model.dto.export.column;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum EsmRecordColumnInfoOfUser implements ExcelColumnInfo{
    ESM_RECORD_ID("NO.", 0),
    ESM_RECORD_DATETIME("입력 시간", 1),
    ESM_RECORD_TEXT("입력 내용",2);

    private final String columnText;
    private final int columnIndex;

    EsmRecordColumnInfoOfUser(String columnText, int columnIndex){
        this.columnText = columnText;
        this.columnIndex = columnIndex;
    }

    public static List<EsmRecordColumnInfoOfUser> getAllColumns() {
        return Arrays.stream(values()).collect(Collectors.toList());
    }
    @Override
    public String getColumnText() {
        return columnText;
    }

    @Override
    public int getColumnIndex() {
        return columnIndex;
    }
}
