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
    SDQ_ANSWER11("문항11", 14),
    SDQ_ANSWER12("문항12", 15),
    SDQ_ANSWER13("문항13", 16),
    SDQ_ANSWER14("문항14", 17),
    SDQ_ANSWER15("문항15", 18),
    SDQ_ANSWER16("문항16", 19),
    SDQ_ANSWER17("문항17", 20),
    SDQ_ANSWER18("문항18", 21),
    SDQ_ANSWER19("문항19", 22),
    SDQ_ANSWER20("문항20", 23),
    SDQ_ANSWER21("문항21", 24),
    SDQ_ANSWER22("문항22", 25),
    SDQ_ANSWER23("문항23", 26),
    SDQ_ANSWER24("문항24", 27),
    SDQ_ANSWER25("문항25", 28),

    SDQ_socialBehavior("사회성 행동",29),
    SDQ_hyperactivity("과잉행동",30),
    SDQ_emotionalSymptoms("정서적 상태",31),
    SDQ_behaviorProblem("품행문제",32),
    SDQ_peerProblem("또래관계문제",33);

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
