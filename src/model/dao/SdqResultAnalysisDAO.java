package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.dto.SdqResultAnalysis;


public class SdqResultAnalysisDAO {
	private final static String SQLST_SELECT_SDQ_RESULT_ANALYSIS_BY_TYPE_AND_VALUE= "select * from sdq_result_analysis where sdq_type = ? and min_value<=? and max_value>=?";
	
	public static SdqResultAnalysis findSdqResultAnalysisByTypeAndValue(Connection con, String sdqType, int value) {
		SdqResultAnalysis sdqResultAnalysis= new SdqResultAnalysis();
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_SDQ_RESULT_ANALYSIS_BY_TYPE_AND_VALUE);

	        pstmt.setString(1, sdqType);
	        pstmt.setInt(2, value);
	        pstmt.setInt(3, value);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				sdqResultAnalysis.setSdqType(rs.getString("sdq_type"));
				sdqResultAnalysis.setSdqAnalysisResult(rs.getString("sdq_analysis_result"));
				sdqResultAnalysis.setMinValue(rs.getInt("min_value"));
				sdqResultAnalysis.setMaxValue(rs.getInt("max_value"));
				sdqResultAnalysis.setColor(rs.getString("color"));
				sdqResultAnalysis.setDescription(rs.getString("description"));
				
				return sdqResultAnalysis;
			}
			else {
				return null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
