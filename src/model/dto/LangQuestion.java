package model.dto;

public class LangQuestion {
	private int langQuestionId;
	private int ageGroupId;
	private String langType;
	private String langQuestionContent;
	private int IsLangGame;
	
	public int getLangQuestionId() {
		return langQuestionId;
	}
	public void setLangQuestionId(int langQuestionId) {
		this.langQuestionId = langQuestionId;
	}
	public int getAgeGroupId() {
		return ageGroupId;
	}
	public void setAgeGroupId(int ageGroupId) {
		this.ageGroupId = ageGroupId;
	}
	public String getLangType() {
		return langType;
	}
	public void setLangType(String langType) {
		this.langType = langType;
	}
	public String getLangQuestionContent() {
		return langQuestionContent;
	}
	public void setLangQuestionContent(String langQuestionContent) {
		this.langQuestionContent = langQuestionContent;
	}
	public int getIsLangGame() {
		return IsLangGame;
	}
	public void setIsLangGame(int IsLangGame) {
		this.IsLangGame = IsLangGame;
	}
	

}
