package model.dto;

import java.sql.Connection;

import model.dao.UserDAO;

/**
 * @author Jiwon Lee
 * 현재 나타나는 pagination을 의미하는 클래스 : 사용자 정보를 pagination할 때 사용
 * listRange - 한 page의 element 수
 * blockRange - 한번에 보여줄 수 있는 page 갯수 (block의 범위)
 * blockStartNum - 한 block의 시작 page 번호
 * blockEndNum - 한 block의 마지막 page 번호
 * lastPageNum - page 총 갯수 (전체의 마지막 page 번호)
 */
public class UserPaging {
	private final static int listRange = 10; 
	private final static int blockRange = 5; 
	private int blockStartNum = 0;
	private int blockEndNum = 0;
	private int lastPageNum = 0;
	private String userRole = "CHILD";
	
	public int getBlockStartNum() {
		return blockStartNum;
	}
	public void setBlockStartNum(int blockStartNum) {
		this.blockStartNum = blockStartNum;
	}
	public int getBlockEndNum() {
		return blockEndNum;
	}
	public void setBlockEndNum(int blockEndNum) {
		this.blockEndNum = blockEndNum;
	}
	public int getLastPageNum() {
		return lastPageNum;
	}
	public void setLastPageNum(int lastPageNum) {
		this.lastPageNum = lastPageNum;
	}
	
	public String getUserRole() {
		return userRole;
	}
	
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	public static int getListRange() {
		return listRange;
	}
	
	public static int getBlockRange() {
		return blockRange;
	}
	
	/*현재 페이지 번호를 기준으로 block의 시작과 끝 번호를 계산*/
	public void makeBlock(int curPage) {
		int blockNum = 0;
		blockNum = (int)Math.floor((curPage-1)/blockRange);
		blockStartNum = (blockRange*blockNum)+1;
		blockEndNum = blockStartNum + (blockRange-1);
	}
	
	/*총 페이지의 마지막 번호 계산*/
	public void makeLastPageNum(Connection con) {
		//전체 element(user) 수 받아오기
		int totalCount = UserDAO.getUserCountByUserRole(con, userRole);
				
		//페이지 총 갯수(마지막 페이지 번호) 계산
		if(totalCount % listRange == 0) {
			lastPageNum = (int)Math.floor(totalCount/listRange);
		}else {
			lastPageNum = (int)Math.floor(totalCount/listRange)+1;
		}
	}
	
	/*검색어가 있는 페이지의 마지막 번호 계산 - default 이름 검색**********************************************************/
	public void makeLastPageNum(Connection con, String keyword) {
		//전체 element(user) 수 받아오기
		int totalCount = UserDAO.getUserCountByUserRoleAndLikeUserName(con, userRole, keyword);
				
		//페이지 총 갯수(마지막 페이지 번호) 계산
		if(totalCount % listRange == 0) {
			lastPageNum = (int)Math.floor(totalCount/listRange);
		}else {
			lastPageNum = (int)Math.floor(totalCount/listRange)+1;
		}
	}
}
