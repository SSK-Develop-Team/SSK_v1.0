package model.dto.export.column;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum LangColumnInfoOfUser implements ExcelColumnInfo{
    LANG_ID("NO.", 0),
    LANG_DATE("검사 실시 날짜",1),
    LANG_AGE_GROUP("검사 연령",2),
    LANG_ANSWER1("구문", 3),
    LANG_ANSWER2("문해", 4),
    LANG_ANSWER3("의미", 5),
    LANG_ANSWER4("의미1", 6),
    LANG_ANSWER5("의미2", 7),
    LANG_ANSWER6("조음", 8),

    LANG_ANSWER7("화용",9);

    private final String columnText;
    private final int columnIndex;

    LangColumnInfoOfUser(String columnText, int columnIndex){
        this.columnText = columnText;
        this.columnIndex = columnIndex;
    }
    public static List<LangColumnInfoOfUser> getAllColumns() {
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
