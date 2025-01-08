package model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.dto.EsmReply;
import model.dto.EsmResultOfType;
import model.dto.EsmResultWithDate;

/**
 * @author Seo Ji Woo, Jiwon Lee
 * 
 * 정서 반복 기록 응답 삽입
 * 정서 반복 기록 응답 조회(테스트 로그 아이디로 조회)
 */

public class EsmReplyDAO {/*REPLY!=RESULT*/
	private final static String SQLST_INSERT_ESM_REPLY = "insert esm_reply(esm_test_log_id, esm_emotion_id, esm_reply_content) values(?,?,?)";
	private final static String SQLST_SELECT_ESM_REPLY_LIST_BY_ESM_TEST_LOG_ID = "select * from esm_reply where esm_test_log_id = ? ORDER BY esm_emotion_id";
	private final static String SQLST_SELECT_ESM_REPLY_SUM_POSITIVE_BY_ESM_TEST_LOG_ID = "select SUM(esm_reply_content) from esm_reply where esm_test_log_id = ? and esm_emotion_id>=1 and esm_emotion_id<=5";
	private final static String SQLST_SELECT_ESM_REPLY_SUM_NEGATIVE_BY_ESM_TEST_LOG_ID = "select SUM(esm_reply_content) from esm_reply where esm_test_log_id = ? and esm_emotion_id>=6 and esm_emotion_id<=10";
	private final static String SQLST_SELECT_ESM_REPLY_AVG_POSITIVE_LIST_OF_WEEK = "select stbl.esm_test_date, SUM(stbl.reply_sum)/COUNT(*) 'positive' \r\n" + 
			"	FROM (select ptbl.esm_test_date, ptbl.esm_test_time, SUM(ptbl.esm_reply_content) 'reply_sum'\r\n" + 
			"		FROM (SELECT * FROM esm_test_log \r\n" + 
			"			NATURAL JOIN esm_reply\r\n" + 
			"			WHERE user_id = ? AND esm_emotion_id >=1 AND esm_emotion_id<=5 AND esm_test_date>=? AND esm_test_date<=?) ptbl\r\n" + 
			"		GROUP BY ptbl.esm_test_date, ptbl.esm_test_time) stbl \r\n" + 
			"	GROUP BY stbl.esm_test_date;";
	private final static String SQLST_SELECT_ESM_REPLY_AVG_NEGATIVE_LIST_OF_WEEK = "select stbl.esm_test_date, SUM(stbl.reply_sum)/COUNT(*) 'positive' \r\n" + 
			"	FROM (select ptbl.esm_test_date, ptbl.esm_test_time, SUM(ptbl.esm_reply_content) 'reply_sum'\r\n" + 
			"		FROM (SELECT * FROM esm_test_log \r\n" + 
			"			NATURAL JOIN esm_reply\r\n" + 
			"			WHERE user_id = ? AND esm_emotion_id >=6 AND esm_emotion_id<=10 AND esm_test_date>=? AND esm_test_date<=?) ptbl\r\n" + 
			"		GROUP BY ptbl.esm_test_date, ptbl.esm_test_time) stbl \r\n" + 
			"	GROUP BY stbl.esm_test_date;";
	private final static String SQLST_SELECT_ESM_RESULT_BY_ESM_TEST_LOG_ID = "SELECT esm_type, SUM(esm_reply_content) as 'result' FROM esm_reply \r\n" + 
			"LEFT JOIN esm_emotion on esm_reply.esm_emotion_id = esm_emotion.esm_emotion_id \r\n" + 
			"WHERE esm_test_log_id=? GROUP BY esm_type";
	
	/*정서 반복 기록 응답 삽입*/
	public static boolean insertEsmReply(Connection con, EsmReply esmReply) {
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_INSERT_ESM_REPLY, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, esmReply.getEsmTestLogId());
			pstmt.setInt(2, esmReply.getEsmEmotionId());
			pstmt.setInt(3, esmReply.getEsmReplyContent());
			
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			if(rs.next()) {
				esmReply.setEsmReplyId(rs.getInt(1));
				return true;
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*정서 반복 기록 응답(reply) 조회*/
	public static ArrayList<EsmReply> getEsmReplyListByEsmTestLogId(Connection con, int esmTestLogId){
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_ESM_REPLY_LIST_BY_ESM_TEST_LOG_ID);
			pstmt.setInt(1, esmTestLogId);
			ResultSet rs = pstmt.executeQuery();
			ArrayList<EsmReply> esmReplyList = new ArrayList<EsmReply>();
			while(rs.next()) {
				EsmReply esmReply = new EsmReply(rs.getInt(2),rs.getInt(3),rs.getInt(4));
				esmReply.setEsmReplyId(rs.getInt(1));
				esmReplyList.add(esmReply);
			}
			return esmReplyList;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/*정서 반복 기록 응답(reply) 조회*/
	public static ArrayList<Integer> getEsmReplyIntegerListByEsmTestLogId(Connection con, int esmTestLogId){
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_ESM_REPLY_LIST_BY_ESM_TEST_LOG_ID);
			pstmt.setInt(1, esmTestLogId);
			ResultSet rs = pstmt.executeQuery();

			ArrayList<Integer> esmReplyList = new ArrayList<Integer>();
			while(rs.next()) {
				esmReplyList.add(rs.getInt("esm_reply_content"));
			}
			return esmReplyList;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*정서 반복 기록 결과(result) 조회*/
	public static ArrayList<EsmResultOfType> getEsmResultByEsmTestLogId(Connection con, int esmTestLogId){
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_ESM_RESULT_BY_ESM_TEST_LOG_ID );
			pstmt.setInt(1, esmTestLogId);
			ResultSet rs = pstmt.executeQuery();
			ArrayList<EsmResultOfType> esmResultOfTypeList = new ArrayList<EsmResultOfType>();
			while(rs.next()) {
				EsmResultOfType esmResultOfType = new EsmResultOfType(rs.getString(1),rs.getInt(2));
				esmResultOfTypeList.add(esmResultOfType);
			}
			return esmResultOfTypeList;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*정서 반복 기록 positive 값*/
	public static int getEsmReplyPositiveValueByEsmTestLogId(Connection con, int esmTestLogId){
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_ESM_REPLY_SUM_POSITIVE_BY_ESM_TEST_LOG_ID);
			pstmt.setInt(1, esmTestLogId);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return 0;
	}
	
	/*정서 반복 기록 negative 값*/
	public static int getEsmReplyNegativeValueByEsmTestLogId(Connection con, int esmTestLogId){
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_ESM_REPLY_SUM_NEGATIVE_BY_ESM_TEST_LOG_ID);
			pstmt.setInt(1, esmTestLogId);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return 0;
	}

	public static ArrayList<EsmResultWithDate> getEsmReplyListByWeek(Connection con, int userId, Date startDate, Date endDate){
		ArrayList<EsmResultWithDate> EsmReplyOfDayList = new ArrayList<EsmResultWithDate>();
		try {
			PreparedStatement pstmtP = con.prepareStatement(SQLST_SELECT_ESM_REPLY_AVG_POSITIVE_LIST_OF_WEEK);
			pstmtP.setInt(1, userId);
			pstmtP.setDate(2, startDate);
			pstmtP.setDate(3, endDate);
			ResultSet rsP = pstmtP.executeQuery();
			
			while(rsP.next()) {
				EsmReplyOfDayList.add(new EsmResultWithDate(rsP.getDate(1),rsP.getFloat(2),0));
			}
			
			PreparedStatement pstmtN = con.prepareStatement(SQLST_SELECT_ESM_REPLY_AVG_NEGATIVE_LIST_OF_WEEK);
			pstmtN.setInt(1, userId);
			pstmtN.setDate(2, startDate);
			pstmtN.setDate(3, endDate);
			ResultSet rsN = pstmtN.executeQuery();
			int i=0;
			while(rsN.next()) {
				EsmReplyOfDayList.get(i).setNegativeAvg(rsN.getFloat(2));
				System.out.println(EsmReplyOfDayList.get(i).getDate().toString()+" : "+EsmReplyOfDayList.get(i).getPositiveAvg()+" "+EsmReplyOfDayList.get(i).getNegativeAvg());
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return EsmReplyOfDayList;
	}
}
