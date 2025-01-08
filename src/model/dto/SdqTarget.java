package model.dto;

import java.util.Arrays;

public enum SdqTarget {
    PARENT("PARENT", "부모용"),
    CHILD("CHILD", "아동용");

    private final String typeName;
    private final String typeNameKr;

    SdqTarget(String typeName, String typeNameKr){
        this.typeName = typeName;
        this.typeNameKr = typeNameKr;
    }

    public static String getTypeName(SdqTarget type) {
        return type.typeName;
    }
    public static String getTypeNameKr(SdqTarget type) {
        return type.typeNameKr;
    }

    public static SdqTarget findByTypeName(String typeName) {
        return Arrays.stream(values())
                .filter(type -> type.typeName.equals(typeName))
                .findAny()
                .orElse(PARENT);
    }

    public static String getTypeNameKrByTypeName(String typeName){
        return Arrays.stream(values())
                .filter(type -> type.typeName.equals(typeName))
                .findAny()
                .orElse(PARENT).typeNameKr;
    }
}
