package model.dto;

import java.util.Arrays;

public enum EsmTarget {
    PARENT("PARENT", "부모용"),
    CHILD("CHILD", "아동용");

    private final String typeName;
    private final String typeNameKr;

    EsmTarget(String typeName, String typeNameKr){
        this.typeName = typeName;
        this.typeNameKr = typeNameKr;
    }

    public static String getTypeName(EsmTarget type) {
        return type.typeName;
    }
    public static String getTypeNameKr(EsmTarget type) {
        return type.typeNameKr;
    }

    public static EsmTarget findByTypeName(String typeName) {
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
