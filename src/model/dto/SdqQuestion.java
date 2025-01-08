package model.dto;

public class SdqQuestion {
	
	private int sdqQuestionId;
	private SdqType sdqType;
	private String sdqQuestionContent;
	private String sdqScoringType;
	private SdqTarget sdqTarget;
	
	public int getSdqQuestionId() {
		return sdqQuestionId;
	}
	public void setSdqQuestionId(int sdqQuestionId) {
		this.sdqQuestionId = sdqQuestionId;
	}
	public String getSdqType() {
		return SdqType.getTypeName(sdqType);
	}
	public void setSdqType(String sdqType) {
		this.sdqType = SdqType.findByTypeName(sdqType);
	}
	public String getSdqQuestionContent() {
		return sdqQuestionContent;
	}
	public void setSdqQuestionContent(String sdqQuestionContent) {
		this.sdqQuestionContent = sdqQuestionContent;
	}
	public String getSdqScoringType() {
		return sdqScoringType;
	}
	public void setSdqScoringType(String sdqScoringType) {
		this.sdqScoringType = sdqScoringType;
	}
	public String getSdqTarget() {
		return SdqTarget.getTypeName(sdqTarget);
	}
	public void setSdqTarget(String sdqTarget) {
		this.sdqTarget = SdqTarget.findByTypeName(sdqTarget);
	}
}
