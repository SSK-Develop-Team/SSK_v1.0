package model.dto;

public class SdqResultAnalysis {

	private String sdqType;
	private String sdqAnalysisResult;
	private int minValue;
	private int maxValue;
	private String color;
	private String description;
	
	public String getSdqType() {
		return sdqType;
	}
	public void setSdqType(String sdqType) {
		this.sdqType = sdqType;
	}
	public String getSdqAnalysisResult() {
		return sdqAnalysisResult;
	}
	public void setSdqAnalysisResult(String sdqAnalysisResult) {
		this.sdqAnalysisResult = sdqAnalysisResult;
	}
	public int getMinValue() {
		return minValue;
	}
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}
	public int getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
