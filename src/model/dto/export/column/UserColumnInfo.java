package model.dto.export.column;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum UserColumnInfo implements ExcelColumnInfo{
    ID("NO.", 0),
    NAME("이름", 1),
    LOGIN_ID("아이디", 2),
    EMAIL("이메일", 3),
    BIRTH("생년월일", 4),
    GENDER("성별", 5);

    private final String columnText;
    private final int columnIndex;

    UserColumnInfo(String columnText, int columnIndex){
        this.columnText = columnText;
        this.columnIndex = columnIndex;
    }

    public static List<UserColumnInfo> getAllColumns() {
        return Arrays.stream(values()).collect(Collectors.toList());
    }

    @Override
    public String getColumnText() {
        return this.columnText;
    }

    @Override
    public int getColumnIndex() {
        return this.columnIndex;
    }
}
