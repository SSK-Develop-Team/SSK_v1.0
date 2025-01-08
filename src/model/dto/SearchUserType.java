package model.dto;

import java.util.Arrays;

public enum SearchUserType {
	LOGIN_ID("loginId"),
	NAME("name");
	
	final private String typeName;
	
	SearchUserType(String searchUserTypeName){
		this.typeName = searchUserTypeName;
	}

	public static String getTypeName(SearchUserType type) {
		return type.typeName;
	}
	
	public static SearchUserType findByTypeName(String typeName) {
		return Arrays.stream(SearchUserType.values())
				.filter(type -> type.typeName.equals(typeName))
				.findAny()
				.orElse(NAME);
	}
}
