package model.dto;

public class AgeGroup {
	private int ageGroupId;
	private int startAge;
	private int endAge;
	public int getAgeGroupId() {
		return ageGroupId;
	}
	public void setAgeGroupId(int ageGroupId) {
		this.ageGroupId = ageGroupId;
	}
	public int getStartAge() {
		return startAge;
	}
	public void setStartAge(int startAge) {
		this.startAge = startAge;
	}
	public int getEndAge() {
		return endAge;
	}
	public void setEndAge(int endAge) {
		this.endAge = endAge;
	}

	/*refactoring required*/
	public static String getAgeGroupStrByAgeGroupId(int ageGroupId){
		String answer = "";
		switch (ageGroupId){
			case 0:
				answer = "3세 0개월 ~ 3세 3개월";
				break;
			case 1:
				answer =  "3세 3개월 ~ 3세 5개월";
			case 2:
				answer = "3세 6개월 ~ 3세 8개월";
				break;
			case 3:
				answer = "3세 9개월 ~ 3세 11개월";
				break;
			case 4:
				answer = "4세 0개월 ~ 4세 3개월";
				break;
			case 5:
				answer = "4세 4개월 ~ 4세 7개월";
				break;
			case 6:
				answer = "4세 8개월 ~ 4세 11개월";
				break;
			case 7:
				answer = "5세 0개월 ~ 5세 5개월";
				break;
			case 8:
				answer = "5세 6개월 ~ 5세 11개월";
				break;
			case 9:
				answer = "6세 0개월 ~ 6세 5개월";
				break;
			case 10:
				answer = "6세 6개월 ~ 6세 11개월";
				break;
			case 11:
				answer = "7세";
				break;
			case 12:
				answer = "8세";
				break;
			case 13:
				answer = "9세";
				break;
		}
		return answer;
	}
}
