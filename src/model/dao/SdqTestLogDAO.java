package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.dto.SdqTestLog;

public class SdqTestLogDAO {

	public static boolean insertSdqTestLog(Connection con, SdqTestLog sdqTestLog) {
		try {
				PreparedStatement pstmt = con.prepareStatement("insert sdq_test_log(user_id, sdq_test_date, sdq_test_time) values (?,?,?)",Statement.RETURN_GENERATED_KEYS);
				pstmt.setInt(1, sdqTestLog.getUserId());
				pstmt.setDate(2, sdqTestLog.getSdqTestDate());
				pstmt.setTime(3, sdqTestLog.getSdqTestTime());
				
				pstmt.executeUpdate();
				
				ResultSet rs = pstmt.getGeneratedKeys();
				if(rs.next()) {
					sdqTestLog.setSdqTestLogId(rs.getInt(1));
					return true;
				}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static ArrayList<SdqTestLog> getSdqTestLogAllByUserId(Connection con, int userId){
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from sdq_test_log where user_id = ?");
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			ArrayList<SdqTestLog> sdqTestLogList = new ArrayList<SdqTestLog>();
			while(rs.next()) {
				SdqTestLog sdqTestLog = new SdqTestLog();
				sdqTestLog.setSdqTestLogId(rs.getInt(1));
				sdqTestLog.setUserId(rs.getInt(2));
				sdqTestLog.setSdqTestDate(rs.getDate(3));
				sdqTestLog.setSdqTestTime(rs.getTime(4));
				
				sdqTestLogList.add(sdqTestLog);
			}
			return sdqTestLogList;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static SdqTestLog getSdqTestLogById(Connection con, int sdqTestLogId) {
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from sdq_test_log where sdq_test_log_id=?");
			pstmt.setInt(1, sdqTestLogId);
			ResultSet rs = pstmt.executeQuery();
			SdqTestLog sdqTestLog = new SdqTestLog();
			while(rs.next()) {
				sdqTestLog.setSdqTestLogId(rs.getInt(1));
				sdqTestLog.setUserId(rs.getInt(2));
				sdqTestLog.setSdqTestDate(rs.getDate(3));
				sdqTestLog.setSdqTestTime(rs.getTime(4));
			}
			return sdqTestLog;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*정해진 아동리스트에 대한 전체 정서 행동 발달 검사 기록*/
	public static ArrayList<SdqTestLog> getSdqTestLogListOfChildList(Connection con, String[] childIdStrList){
		String SQLST = "select * from sdq_test_log where user_id IN (";
		
		for(int i = 0;i<childIdStrList.length;i++) {
			SQLST+=childIdStrList[i];
			if(i<childIdStrList.length-1) SQLST+=",";
			else SQLST+=")";
		}
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST);

			ResultSet rs = pstmt.executeQuery();
			ArrayList<SdqTestLog> sdqTestLogList = new ArrayList<SdqTestLog>();
			while(rs.next()) {
				SdqTestLog sdqTestLog = new SdqTestLog();
				sdqTestLog.setSdqTestLogId(rs.getInt(1));
				sdqTestLog.setUserId(rs.getInt(2));
				sdqTestLog.setSdqTestDate(rs.getDate(3));
				sdqTestLog.setSdqTestTime(rs.getTime(4));
				
				sdqTestLogList.add(sdqTestLog);
			}
			return sdqTestLogList;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	

}
