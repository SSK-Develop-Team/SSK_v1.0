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
    SDQ_ANSWER11("문항11", 13),
    SDQ_ANSWER12("문항12", 14),
    SDQ_ANSWER13("문항13", 15),
    SDQ_ANSWER14("문항14", 16),
    SDQ_ANSWER15("문항15", 17),
    SDQ_ANSWER16("문항16", 18),
    SDQ_ANSWER17("문항17", 19),
    SDQ_ANSWER18("문항18", 20),
    SDQ_ANSWER19("문항19", 21),
    SDQ_ANSWER20("문항20", 22),
    SDQ_ANSWER21("문항21", 23),
    SDQ_ANSWER22("문항22", 24),
    SDQ_ANSWER23("문항23", 25),
    SDQ_ANSWER24("문항24", 26),
    SDQ_ANSWER25("문항25", 27),
    SDQ_socialBehavior("사회성 행동",28),
    SDQ_hyperactivity("과잉행동",29),
    SDQ_emotionalSymptoms("정서적 상태",30),
    SDQ_behaviorProblem("품행문제",31),
    SDQ_peerProblem("또래관계문제",32);

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
