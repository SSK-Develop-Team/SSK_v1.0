package model.dto.export.column;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SdqColumnInfoOfUser implements ExcelColumnInfo {
    SDQ_ID("NO.", 0),
    SDQ_TARGET("검사종류", 1),
    SDQ_DATETIME("검사시간", 2),
    SDQ_ANSWER1("문항1", 3),
    SDQ_ANSWER2("문항2", 4),
    SDQ_ANSWER3("문항3", 5),
    SDQ_ANSWER4("문항4", 6),
    SDQ_ANSWER5("문항5", 7),
    SDQ_ANSWER6("문항6", 8),
    SDQ_ANSWER7("문항7", 9),
    SDQ_ANSWER8("문항8", 10),
    SDQ_ANSWER9("문항9", 11),
    SDQ_ANSWER10("문항10", 12),
    SDQ_socialBehavior("사회지향행동",13),
    SDQ_hyperactivity("과잉행동",14),
    SDQ_emotionalSymptoms("정서증상",15),
    SDQ_behaviorProblem("품행문제",16),
    SDQ_peerProblem("또래문제",17);

    private final String columnText;
    private final int columnIndex;

    SdqColumnInfoOfUser(String columnText, int columnIndex){
        this.columnText = columnText;
        this.columnIndex = columnIndex;
    }

    public static SdqColumnInfoOfUser findByColumnText(String columnText){
        return Arrays.stream(values())
                .filter(type -> type.columnText.equals(columnText))
                .findAny()
                .orElse(SDQ_socialBehavior);
    }

    public static List<SdqColumnInfoOfUser> getAllColumns() {
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
