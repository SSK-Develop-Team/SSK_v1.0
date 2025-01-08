package model.dto.export.column;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum EsmColumnInfoOfTest implements ExcelColumnInfo {
	ESM_USER_ID("NO.", 0),
    ESM_USER_NAME("이름", 1),
    ESM_DATETIME("검사시간", 2),
    ESM_ANSWER1("문항1", 3),
    ESM_ANSWER2("문항2", 4),
    ESM_ANSWER3("문항3", 5),
    ESM_ANSWER4("문항4", 6),
    ESM_ANSWER5("문항5", 7),
    ESM_ANSWER6("문항6", 8),
    ESM_ANSWER7("문항7", 9),
    ESM_ANSWER8("문항8", 10),
    ESM_ANSWER9("문항9", 11),
    ESM_ANSWER10("문항10", 12),
    ESM_POSITIVE("긍정정서",13),
    ESM_NEGATIVE("부정정서",14);

    private final String columnText;
    private final int columnIndex;

    EsmColumnInfoOfTest(String columnText, int columnIndex){
        this.columnText = columnText;
        this.columnIndex = columnIndex;
    }

    public static List<EsmColumnInfoOfTest> getAllColumns() {
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

    public static EsmColumnInfoOfTest findColumnByEsmType(String typeName){
        return Arrays.stream(values())
                .filter(type -> type.columnText.equals(typeName.equals("positive")?"긍정정서":"부정정서"))
                .findAny()
                .orElse(ESM_POSITIVE);
    }
}
