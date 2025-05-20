package model.dto;

public class LangResultAnalysis {
	private String langType;
	private int lang_report_age_group_id;
	private String description;
	
	public String getLangType() {
		return langType;
	}
	public void setLangType(String langType) {
		this.langType = langType;
	}
	public int getLangReportAgeGroupId() {
		return lang_report_age_group_id;
	}
	public void setLangReportAgeGroupId(int lang_report_age_group_id) {
		this.lang_report_age_group_id = lang_report_age_group_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
