package model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

import model.dto.EsmRecord;

public class EsmRecordDAO {
	private final static String SQLST_INSERT_ESM_RECORD ="INSERT esm_record(esm_record_text, esm_record_date, esm_record_time, user_id) values(?,?,?,?) ";
	private final static String SQLST_SELECT_ESM_RECORD_BY_DATE = "SELECT * FROM esm_record WHERE user_id = ? AND esm_record_date = ?";
	private final static String SQLST_SELECT_ESM_RECORD_DATE_GROUP_BY_DATE = "SELECT esm_record_date, COUNT(*) FROM esm_record WHERE user_id=? GROUP BY esm_record_date";
	private final static String SQLST_SELECT_ESM_RECORD_BY_ESM_RECORD_ID = "SELECT * FROM esm_record WHERE esm_record_id = ?";
	private final static String SQLST_SELECT_ESM_RECORD_BY_USER_ID = "SELECT * FROM esm_record WHERE user_id = ?";
	private final static String SQLST_UPDATE_ESM_RECORD = "UPDATE esm_record SET esm_record_text = ? WHERE esm_record_id = ?";
	private final static String SQLST_DELETE_ESM_RECORD = "DELETE FROM esm_record WHERE esm_record_id = ?";
	
	/*텍스트 기록 삽입*/
	public static boolean insertEsmRecord(Connection con, String esmRecordText, Date esmRecordDate, Time esmRecordTime, int userId) {
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_INSERT_ESM_RECORD);
			pstmt.setString(1, esmRecordText);
			pstmt.setDate(2, esmRecordDate);
			pstmt.setTime(3, esmRecordTime);
			pstmt.setInt(4, userId);
			int insertCount = pstmt.executeUpdate();
			System.out.println(insertCount);
			if(insertCount == 1) {
				return true;
			}
			else {
				return false;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/*일별 텍스트 기록 조회*/
	public static ArrayList<EsmRecord> getEsmRecordListByDate(Connection con, int userId, Date esmRecordDate){
		ArrayList<EsmRecord> esmRecordList = new ArrayList<EsmRecord>();
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_ESM_RECORD_BY_DATE);
			pstmt.setInt(1, userId);
			pstmt.setDate(2, esmRecordDate);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				EsmRecord esmRecordElement = new EsmRecord();
				esmRecordElement.setEsmRecordId(rs.getInt("esm_record_id"));
				esmRecordElement.setEsmRecordText(rs.getString("esm_record_text"));
				esmRecordElement.setEsmRecordDate(rs.getDate("esm_record_date"));
				esmRecordElement.setEsmRecordTime(rs.getTime("esm_record_time"));
				esmRecordElement.setUserId(rs.getInt("user_id"));
				esmRecordList.add(esmRecordElement);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return esmRecordList;
	}
	
	/*사용자가 기록한 날짜 리스트 가져오기*/
	public static ArrayList<Date> getEsmRecordDateList(Connection con, int userId){
		ArrayList<Date> esmRecordDateList = new ArrayList<Date>();
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_ESM_RECORD_DATE_GROUP_BY_DATE);
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				esmRecordDateList.add(rs.getDate("esm_record_date"));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return esmRecordDateList;
	}
	
	/*정서 다이어리 조회*/
	public static EsmRecord getEsmRecordById(Connection con, int esmRecordId){
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_ESM_RECORD_BY_ESM_RECORD_ID);
			pstmt.setInt(1, esmRecordId);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				EsmRecord esmRecord = new EsmRecord();
				esmRecord.setEsmRecordId(rs.getInt("esm_record_id"));
				esmRecord.setEsmRecordText(rs.getString("esm_record_text"));
				esmRecord.setEsmRecordDate(rs.getDate("esm_record_date"));
				esmRecord.setEsmRecordTime(rs.getTime("esm_record_time"));
				esmRecord.setUserId(rs.getInt("user_id"));
				return esmRecord;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	public static ArrayList<EsmRecord> getEsmRecordListByUser(Connection con, int userId){
		ArrayList<EsmRecord> esmRecordList = new ArrayList<EsmRecord>();
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_ESM_RECORD_BY_USER_ID);
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();

			while(rs.next()) {
				EsmRecord esmRecordElement = new EsmRecord();
				esmRecordElement.setEsmRecordId(rs.getInt("esm_record_id"));
				esmRecordElement.setEsmRecordText(rs.getString("esm_record_text"));
				esmRecordElement.setEsmRecordDate(rs.getDate("esm_record_date"));
				esmRecordElement.setEsmRecordTime(rs.getTime("esm_record_time"));
				esmRecordElement.setUserId(rs.getInt("user_id"));
				esmRecordList.add(esmRecordElement);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return esmRecordList;
	}
	
	/*정서 다이어리 수정*/
	public static boolean updateEsmRecord(Connection con, int esmRecordId, String esmRecordText) {
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_UPDATE_ESM_RECORD);
			pstmt.setString(1, esmRecordText);
			pstmt.setInt(2, esmRecordId);
			
			int res = pstmt.executeUpdate();
			if(res > 0) {
				return true;
			}else {
				return false;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*정서 다이어리 삭제*/
	public static boolean deleteEsmRecord(Connection con, int esmRecordId) {
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_DELETE_ESM_RECORD);
			pstmt.setInt(1, esmRecordId);
			
			int res = pstmt.executeUpdate();
			if(res > 0) {
				return true;
			}else {
				return false;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*정해진 아동리스트에 대한 전체 정서 다이어리 조회*/
	public static ArrayList<EsmRecord> getEsmRecordListOfChildList(Connection con, String[] childIdStrList){
		String SQLST = "select * from esm_record where user_id IN (";
		
		for(int i = 0;i<childIdStrList.length;i++) {
			SQLST+=childIdStrList[i];
			if(i<childIdStrList.length-1) SQLST+=",";
			else SQLST+=")";
		}
		ArrayList<EsmRecord> esmRecordList = new ArrayList<EsmRecord>();
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST);
			
			ResultSet rs = pstmt.executeQuery();

			while(rs.next()) {
				EsmRecord esmRecordElement = new EsmRecord();
				esmRecordElement.setEsmRecordId(rs.getInt("esm_record_id"));
				esmRecordElement.setEsmRecordText(rs.getString("esm_record_text"));
				esmRecordElement.setEsmRecordDate(rs.getDate("esm_record_date"));
				esmRecordElement.setEsmRecordTime(rs.getTime("esm_record_time"));
				esmRecordElement.setUserId(rs.getInt("user_id"));
				esmRecordList.add(esmRecordElement);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return esmRecordList;
	}
}
