package model.dto;

public class EsmReply {
	
	private int esmReplyId;
	private int esmTestLogId;
	private int esmEmotionId;
	private int esmReplyContent;
	
	public EsmReply(int esmTestLogId, int esmEmotionId, int esmReplyContent) {
		super();
		this.esmTestLogId = esmTestLogId;
		this.esmEmotionId = esmEmotionId;
		this.esmReplyContent = esmReplyContent;
	}
	
	public int getEsmReplyId() {
		return esmReplyId;
	}
	public void setEsmReplyId(int esmReplyId) {
		this.esmReplyId = esmReplyId;
	}
	public int getEsmTestLogId() {
		return esmTestLogId;
	}
	public void setEsmTestLogId(int esmTestLogId) {
		this.esmTestLogId = esmTestLogId;
	}
	public int getEsmEmotionId() {
		return esmEmotionId;
	}
	public void setEsmEmotionId(int esmEmotionId) {
		this.esmEmotionId = esmEmotionId;
	}
	public int getEsmReplyContent() {
		return esmReplyContent;
	}
	public void setEsmReplyContent(int esmReplyContent) {
		this.esmReplyContent = esmReplyContent;
	}

}
