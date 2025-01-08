package model.dto.export.column;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum EsmColumnInfoOfUser implements ExcelColumnInfo {
    ESM_ID("NO.",0),
    ESM_DATETIME("검사시간", 1),
    ESM_ANSWER1("문항1", 2),
    ESM_ANSWER2("문항2", 3),
    ESM_ANSWER3("문항3", 4),
    ESM_ANSWER4("문항4", 5),
    ESM_ANSWER5("문항5", 6),
    ESM_ANSWER6("문항6", 7),
    ESM_ANSWER7("문항7", 8),
    ESM_ANSWER8("문항8", 9),
    ESM_ANSWER9("문항9", 10),
    ESM_ANSWER10("문항10", 11),
    ESM_POSITIVE("긍정정서",12),
    ESM_NEGATIVE("부정정서",13);

    private final String columnText;
    private final int columnIndex;

    EsmColumnInfoOfUser(String columnText, int columnIndex){
        this.columnText = columnText;
        this.columnIndex = columnIndex;
    }

    public static List<EsmColumnInfoOfUser> getAllColumns() {
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

    public static EsmColumnInfoOfUser findColumnByEsmType(String typeName){
        return Arrays.stream(values())
                .filter(type -> type.columnText.equals(typeName.equals("positive")?"긍정정서":"부정정서"))
                .findAny()
                .orElse(ESM_POSITIVE);
    }
}
