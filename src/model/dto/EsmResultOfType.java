package model.dto;
/**
 * @author Jiwon Lee
 * 한 타입에 대한 esm 응답 합산 점수
 * esm 유형(esmType), 응답 합산 점수(result)로 이루어져 있음.
 */
public class EsmResultOfType {
	private EsmType esmType;
	private int result;
	
	public EsmResultOfType() {
		super();
	}

	public EsmResultOfType(String esmTypeName, int result) {
		super();
		this.esmType = EsmType.findByTypeName(esmTypeName);
		this.result = result;
	}

	public String getEsmType() {
		return EsmType.getTypeName(esmType);
	}

	public void setEsmType(String esmTypeName) {
		this.esmType = EsmType.findByTypeName(esmTypeName);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
	
	
	
}
