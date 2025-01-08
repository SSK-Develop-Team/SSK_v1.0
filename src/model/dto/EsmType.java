package model.dto;
import java.util.Arrays;

public enum EsmType {
	positive("positive"),
	negative("negative");
	
	final private String typeName;
	
	EsmType(String typeName) {
		this.typeName = typeName;
	}
	
	public static String getTypeName(EsmType type) {
		return type.typeName;
	}
	
	public static EsmType findByTypeName(String typeName) {
		return Arrays.stream(EsmType.values())
				.filter(type -> type.typeName.equals(typeName))
				.findAny()
				.orElse(positive);
	}
	
}
