package model.dto;
/**
 * @author leejiwon
 * 한 타입에 대한 sdq 응답 합산 점수
 * sdq 유형(sdqType), 응답 합산 점수(result)로 이루어져 있음.
 */
public class SdqResultOfType {
	private SdqType sdqType;
	private int result;
	
	public SdqResultOfType() {
		super();
	}
	public SdqResultOfType(String sdqTypeName, int result) {
		this.sdqType = SdqType.findByTypeName(sdqTypeName);
		this.result = result;
	}
	public String getSdqType() {
		return SdqType.getTypeName(sdqType);
	}
	public void setSdqType(String sdqTypeName) {
		this.sdqType = SdqType.findByTypeName(sdqTypeName);
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}

}
