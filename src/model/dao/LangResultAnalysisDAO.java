package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import model.dto.LangResultAnalysis;

public class LangResultAnalysisDAO {
	private final static String SQLST_SELECT_LANG_RESULT_ANALYSIS_BY_AGE= "SELECT lang_result_analysis.* FROM ssk.lang_result_analysis \r\n" + 
			"LEFT JOIN lang_report_age_group \r\n" + 
			"ON lang_result_analysis.lang_report_age_group_id = lang_report_age_group.lang_report_age_group_id\r\n" + 
			"WHERE lang_report_age_group.min_age_group_id <= ? AND lang_report_age_group.max_age_group_id >= ?;";
	private final static String SQLST_SELECT_LANG_RESULT_ANALYSIS_BY_REPORT_AGE= "SELECT * FROM lang_result_analysis\r\n" + 
			"WHERE lang_report_age_group_id = ?;";
	// 맞춤형 동화책 - 언어점수 결과 연동을 위한  DB
	private final static String SQLST_SELECT_LATEST_LANG_TEST_LOG_ID = 
			"SELECT lang_test_log_id \r\n" +
		    "FROM lang_test_log \r\n" +
		    "WHERE user_id = ? \r\n" +
		    "ORDER BY lang_test_date DESC, lang_test_time DESC \r\n" +
		    "LIMIT 1;";
	private final static String SQLST_SELECT_TOTAL_LANG_SCORE =
		    "SELECT COALESCE(SUM(lang_reply_content), 0) AS total_score \r\n" +
		    "FROM lang_reply \r\n" +
		    "WHERE lang_test_log_id = ?;";
	
	
	public static List<LangResultAnalysis> findLangResultAnalysisByAge(Connection con, int age) {
		
		List<LangResultAnalysis> resultList = new ArrayList<>();
		
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_LANG_RESULT_ANALYSIS_BY_AGE);
						
	        pstmt.setInt(1, age);
	        pstmt.setInt(2, age);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				LangResultAnalysis langResultAnalysis= new LangResultAnalysis();
				
				langResultAnalysis.setLangType(rs.getString("lang_type"));
				langResultAnalysis.setLangReportAgeGroupId(rs.getInt("lang_report_age_group_id"));
				langResultAnalysis.setDescription(rs.getString("description"));
				
				resultList.add(langResultAnalysis);				
			}
			return resultList;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<LangResultAnalysis> findLangResultAnalysisByReportAge (Connection con, int reportAge) {
		List<LangResultAnalysis> resultList = new ArrayList<>();
		
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_LANG_RESULT_ANALYSIS_BY_REPORT_AGE);
						
	        pstmt.setInt(1, reportAge);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				LangResultAnalysis langResultAnalysis= new LangResultAnalysis();
				
				langResultAnalysis.setLangType(rs.getString("lang_type"));
				langResultAnalysis.setLangReportAgeGroupId(rs.getInt("lang_report_age_group_id"));
				langResultAnalysis.setDescription(rs.getString("description"));
				
				resultList.add(langResultAnalysis);				
			}
			return resultList;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static int findLatestLangTestLogId(Connection con, int userId) {
	    try {
	        PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_LATEST_LANG_TEST_LOG_ID);
	        pstmt.setInt(1, userId);

	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            return rs.getInt("lang_test_log_id");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return -1;
	}
	
	public static int findTotalLangScoreByLogId(Connection con, int langTestLogId) {
	    try {
	        PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_TOTAL_LANG_SCORE);
	        pstmt.setInt(1, langTestLogId);

	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            return rs.getInt("total_score");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return 0;
	}
}
