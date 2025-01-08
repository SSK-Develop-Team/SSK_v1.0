package model.dto.export.column;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SdqColumnInfoOfTest implements ExcelColumnInfo {
    SDQ_USER_ID("NO.", 0),
    SDQ_USER_NAME("이름", 1),
    SDQ_TARGET("검사종류", 2),
    SDQ_DATETIME("검사시간", 3),
    SDQ_ANSWER1("문항1", 4),
    SDQ_ANSWER2("문항2", 5),
    SDQ_ANSWER3("문항3", 6),
    SDQ_ANSWER4("문항4", 7),
    SDQ_ANSWER5("문항5", 8),
    SDQ_ANSWER6("문항6", 9),
    SDQ_ANSWER7("문항7", 10),
    SDQ_ANSWER8("문항8", 11),
    SDQ_ANSWER9("문항9", 12),
    SDQ_ANSWER10("문항10", 13),
    SDQ_socialBehavior("사회지향행동",14),
    SDQ_hyperactivity("과잉행동",15),
    SDQ_emotionalSymptoms("정서증상",16),
    SDQ_behaviorProblem("품행문제",17),
    SDQ_peerProblem("또래문제",18);

    private final String columnText;
    private final int columnIndex;

    SdqColumnInfoOfTest(String columnText, int columnIndex){
        this.columnText = columnText;
        this.columnIndex = columnIndex;
    }

    public static SdqColumnInfoOfTest findByColumnText(String columnText){
        return Arrays.stream(values())
                .filter(type -> type.columnText.equals(columnText))
                .findAny()
                .orElse(SDQ_socialBehavior);
    }

    public static List<SdqColumnInfoOfTest> getAllColumns() {
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
