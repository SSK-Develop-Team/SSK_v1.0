package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.dto.SdqReply;
import model.dto.SdqResultOfType;

/**
 * @author Seo Ji Woo
 *
 */

public class SdqReplyDAO {
	
	private final static String SQLST_INSERT_SDQ_REPLY = "insert sdq_reply(sdq_test_log_id, sdq_question_id, sdq_reply_content) values (?,?,?)";
	private final static String SQLST_SELECT_SDQ_REPLY_LIST_BY_SDQ_TEST_LOG_ID = "select * from sdq_reply where sdq_test_log_id = ? ORDER BY sdq_question_id";

	private final static String SQLST_SELECT_SDQ_REPLY_CONTENT_LIST_BY_SDQ_TEST_LOG_ID = "SELECT CASE WHEN sdq_scoring_type=\"forward\" THEN sdq_reply_content-1\n" +
			"\t\t\tWHEN sdq_scoring_type=\"reverse\" THEN 4-sdq_reply_content END as 'sdqReplyContentNum' FROM sdq_reply\n" +
			"            LEFT JOIN sdq_question on sdq_reply.sdq_question_id = sdq_question.sdq_question_id \n" +
			"            WHERE sdq_test_log_id=? ORDER BY sdq_reply.sdq_question_id";
	private final static String SQLST_SELECT_SDQ_REPLY_SUM_BY_TEST_LOG_ID_GROUP_BY_SDQ_TYPE = "SELECT sdq_type, SUM(CASE WHEN sdq_scoring_type=\"forward\" THEN sdq_reply_content-1 \r\n" + 
			"					WHEN sdq_scoring_type=\"reverse\" THEN 4-sdq_reply_content END) as 'resultOfType' FROM sdq_reply \r\n" +
			"LEFT JOIN sdq_question on sdq_reply.sdq_question_id = sdq_question.sdq_question_id \r\n" + 
			"WHERE sdq_test_log_id=? GROUP BY sdq_type";
	private final static String SQLST_SELECT_SDQ_TARGET_BY_SDQ_TEST_LOG_ID = "SELECT sdq_target FROM sdq_reply A LEFT JOIN sdq_question B ON A.sdq_question_id = B.sdq_question_id WHERE A.sdq_test_log_id = ? LIMIT 1;";
	
	public static boolean insertSdqReply(Connection con, SdqReply sdqReply) {
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_INSERT_SDQ_REPLY,Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, sdqReply.getSdqTestLogId());
			pstmt.setInt(2, sdqReply.getSdqQuestionId());
			pstmt.setInt(3, sdqReply.getSdqReplyContent());
			
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			if(rs.next()) {
				sdqReply.setSdqReplyId(rs.getInt(1));
				return true;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static ArrayList<SdqReply> getSdqReplyListBySdqTestLogId(Connection con, int sdqTestLogId){
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_SDQ_REPLY_LIST_BY_SDQ_TEST_LOG_ID);
			pstmt.setInt(1, sdqTestLogId);
			ResultSet rs = pstmt.executeQuery();
			ArrayList<SdqReply> sdqReplyList = new ArrayList<SdqReply>();
			while(rs.next()) {
				SdqReply sdqReply = new SdqReply();
				sdqReply.setSdqReplyId(rs.getInt(1));
				sdqReply.setSdqTestLogId(rs.getInt(2));
				sdqReply.setSdqQuestionId(rs.getInt(3));
				sdqReply.setSdqReplyContent(rs.getInt(4));
				
				sdqReplyList.add(sdqReply);
			}
			return sdqReplyList;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static ArrayList<Integer> getSdqReplyListIntegerBySdqTestLogId(Connection con, int sdqTestLogId){
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_SDQ_REPLY_CONTENT_LIST_BY_SDQ_TEST_LOG_ID);
			pstmt.setInt(1, sdqTestLogId);
			ResultSet rs = pstmt.executeQuery();
			ArrayList<Integer> sdqReplyListInteger = new ArrayList<Integer>();
			while(rs.next()) {
				sdqReplyListInteger.add(rs.getInt(1));
			}
			return sdqReplyListInteger;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayList<SdqResultOfType> getSdqResultOfTypesBySdqTestLogId(Connection con, int sdqTestLogId){
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_SDQ_REPLY_SUM_BY_TEST_LOG_ID_GROUP_BY_SDQ_TYPE);
			pstmt.setInt(1, sdqTestLogId);
			ResultSet rs = pstmt.executeQuery();
			ArrayList<SdqResultOfType> sdqResultOfTypeList = new ArrayList<SdqResultOfType>();
			while(rs.next()) {
				sdqResultOfTypeList.add(new SdqResultOfType(rs.getString("sdq_type"),rs.getInt("resultOfType")));
			}
			return sdqResultOfTypeList;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*검사의 유형(부모용, 아동용) 가져오기*/
	public static String getSdqTargetBySdqTestLogId(Connection con, int sdqTestLogId){
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_SDQ_TARGET_BY_SDQ_TEST_LOG_ID);
			pstmt.setInt(1,sdqTestLogId);

			ResultSet rs = pstmt.executeQuery();

			while(rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
