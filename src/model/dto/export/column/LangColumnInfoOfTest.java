package model.dto.export.column;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum LangColumnInfoOfTest implements ExcelColumnInfo{
	LANG_USER_ID("ID", 0),
	LANG_USER_NAME("이름", 1),
    LANG_DATE("검사 실시 날짜",2),
    LANG_AGE_GROUP("검사 연령",3),
    LANG_ANSWER1("구문", 4),
    LANG_ANSWER2("문해", 5),
    LANG_ANSWER3("문해1", 6),
    LANG_ANSWER4("문해2", 7),
    LANG_ANSWER5("문해3", 8),
    LANG_ANSWER6("의미", 9),
    LANG_ANSWER7("의미1", 10),
    LANG_ANSWER8("의미2", 11),
    LANG_ANSWER9("조음", 12),
    LANG_ANSWER10("화용",13),
    LANG_ANSWER11("화용1",14),
    LANG_ANSWER12("화용2",15);

	private final String columnText;
    private final int columnIndex;

    LangColumnInfoOfTest(String columnText, int columnIndex){
        this.columnText = columnText;
        this.columnIndex = columnIndex;
    }
    public static List<LangColumnInfoOfTest> getAllColumns() {
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
