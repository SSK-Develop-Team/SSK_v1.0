package model.dto;

import java.util.Arrays;

public enum SdqType {
	socialBehavior("사회지향행동"),
	hyperactivity("과잉행동"),
	emotionalSymptoms("정서증상"),
	behaviorProblem("품행문제"),
	peerProblem("또래문제");
	
	final private String typeName;
	
	SdqType(String typeName){
		this.typeName = typeName;
	}
	
	public static String getTypeName(SdqType type) {
		return type.typeName;
	}
	
	public static SdqType findByTypeName(String typeName) {
		return Arrays.stream(SdqType.values())
				.filter(type -> type.typeName.equals(typeName))
				.findAny()
				.orElse(socialBehavior);
	}
}
