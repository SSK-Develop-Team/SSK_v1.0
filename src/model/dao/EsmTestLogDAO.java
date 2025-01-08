package model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.dto.EsmTestLog;
/**
 * @author Jiwon Lee
 * 
 * 정서 발달 기록(esm_test_log) DAO
 * 테스트 기록 삽입
 * 테스트 기록 조회(사용자 별)
 * 테스트 기록 조회(아이디로)
 *
 */
public class EsmTestLogDAO {
	
	/*정서 발달 기록 삽입*/
	public static boolean insertEsmTestLog(Connection con, EsmTestLog esmTestLog) {
		try {
			PreparedStatement pstmt = con.prepareStatement("insert esm_test_log(user_id, esm_test_date, esm_test_time) values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1,esmTestLog.getUserId());
			pstmt.setDate(2, esmTestLog.getEsmTestDate());
			pstmt.setTime(3, esmTestLog.getEsmTestTime());
			
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			if(rs.next()) {
				esmTestLog.setEsmTestLogId(rs.getInt(1));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*사용자별 정서 발달 기록 조회*/
	public static ArrayList<EsmTestLog> getEsmTestLogListByUserId(Connection con, int userId){
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from esm_test_log where user_id = ?");
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			ArrayList<EsmTestLog> esmTestLogList = new ArrayList<EsmTestLog>();
			while(rs.next()) {
				EsmTestLog esmTestLog = new EsmTestLog(rs.getInt(2),rs.getDate(3),rs.getTime(4));
				esmTestLog.setEsmTestLogId(rs.getInt(1));
				
				esmTestLogList.add(esmTestLog);
			}
			return esmTestLogList;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*사용자별 정서 발달 기록 일자별 조회*/
	public static ArrayList<EsmTestLog> getEsmTestLogListByUserIdAndDate(Connection con, int userId, Date esmTestDate){
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from esm_test_log where user_id = ? and esm_test_date=?");
			pstmt.setInt(1, userId);
			pstmt.setDate(2, esmTestDate);
			ResultSet rs = pstmt.executeQuery();
			ArrayList<EsmTestLog> esmTestLogList = new ArrayList<EsmTestLog>();
			while(rs.next()) {
				EsmTestLog esmTestLog = new EsmTestLog(rs.getInt(2),rs.getDate(3),rs.getTime(4));
				esmTestLog.setEsmTestLogId(rs.getInt(1));
				
				esmTestLogList.add(esmTestLog);
			}
			return esmTestLogList;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*사용자별 정서 발달 기록 일자 조회*/
	public static ArrayList<Date> getEsmTestLogDateListByUserIdGroupByDate(Connection con, int userId){
		ArrayList<Date> esmTestDateList = new ArrayList<Date>();
		try {
			PreparedStatement pstmt = con.prepareStatement("SELECT esm_test_date, COUNT(*) FROM esm_test_log WHERE user_id=? GROUP BY esm_test_date");
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				esmTestDateList.add(rs.getDate("esm_test_date"));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return esmTestDateList;
	}
	
	/*정서 발달 기록 조회*/
	public static EsmTestLog getEsmTestLogById(Connection con, int esmTestLogId) {
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from esm_test_log where esm_test_log_id = ?");
			pstmt.setInt(1, esmTestLogId);
			
			ResultSet rs = pstmt.executeQuery();
			
			EsmTestLog esmTestLog = null;
			while(rs.next()) {
				esmTestLog = new EsmTestLog(rs.getInt(2),rs.getDate(3),rs.getTime(4));
				esmTestLog.setEsmTestLogId(rs.getInt(1));
			}
			return esmTestLog;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/*정해진 아동리스트에 대한 전체 정서 반복 기록*/
	public static ArrayList<EsmTestLog> getEsmTestLogListOfChildList(Connection con, String[] childIdStrList){
		String SQLST = "select * from esm_test_log where user_id IN (";
		
		for(int i = 0;i<childIdStrList.length;i++) {
			SQLST+=childIdStrList[i];
			if(i<childIdStrList.length-1) SQLST+=",";
			else SQLST+=")";
		}
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST);
			ResultSet rs = pstmt.executeQuery();
			ArrayList<EsmTestLog> esmTestLogList = new ArrayList<EsmTestLog>();
			while(rs.next()) {
				EsmTestLog esmTestLog = new EsmTestLog(rs.getInt(2),rs.getDate(3),rs.getTime(4));
				esmTestLog.setEsmTestLogId(rs.getInt(1));
				
				esmTestLogList.add(esmTestLog);
			}
			return esmTestLogList;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*사용자 최근 정서 발달 기록 조회*/
	public static EsmTestLog getRecentEsmTestLogListByUserId(Connection con, int userId){
		EsmTestLog esmTestLog = null;
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from esm_test_log where user_id = ? ORDER BY esm_test_date DESC, esm_test_time DESC LIMIT 0, 1;");
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				esmTestLog = new EsmTestLog(rs.getInt(2),rs.getDate(3),rs.getTime(4));
				esmTestLog.setEsmTestLogId(rs.getInt(1));
			}
			return esmTestLog;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
