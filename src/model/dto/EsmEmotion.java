package model.dto;

public class EsmEmotion {
	
	private int esmEmotionId;
	private EsmType esmType;
	private String esmEmotion;
	private String esmEmotionKr;
	private String esmEmotionIcon;
	private EsmTarget esmTarget;
	
	/*public EsmEmotion(int esmEmotionId, String esmType, String esmEmotion, String esmEmotionKr,
			String esmEmotionIcon, EsmTarget esmTarget) {
		super();
		this.esmEmotionId = esmEmotionId;
		this.esmType = EsmType.findByTypeName(esmType);
		this.esmEmotion = esmEmotion;
		this.esmEmotionKr = esmEmotionKr;
		this.esmEmotionIcon = esmEmotionIcon;
		this.esmTarget = esmTarget;
	}*/
	
	public int getEsmEmotionId() {
		return esmEmotionId;
	}
	public void setEsmEmotionId(int esmEmotionId) {
		this.esmEmotionId = esmEmotionId;
	}
	public String getEsmType() {
		return esmType.toString();
	}
	public void setEsmType(String esmType) {
		this.esmType = EsmType.findByTypeName(esmType);
	}
	public String getEsmEmotion() {
		return esmEmotion;
	}
	public void setEsmEmotion(String esmEmotion) {
		this.esmEmotion = esmEmotion;
	}
	public String getEsmEmotionKr() {
		return esmEmotionKr;
	}
	public void setEsmEmotionKr(String esmEmotionKr) {
		this.esmEmotionKr = esmEmotionKr;
	}
	public String getEsmEmotionIcon() {
		return esmEmotionIcon;
	}
	public void setEsmEmotionIcon(String esmEmotionIcon) {
		this.esmEmotionIcon = esmEmotionIcon;
	}
	public String getEsmTarget() {
		return EsmTarget.getTypeName(esmTarget);
	}
	public void setEsmTarget(String esmTarget) {
		this.esmTarget = EsmTarget.findByTypeName(esmTarget);
	}
	
}
