package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import model.dto.SdqResultAnalysis;


public class SdqResultAnalysisDAO {
	private final static String SQLST_SELECT_SDQ_RESULT_ANALYSIS_BY_TYPE_AND_VALUE= "select * from sdq_result_analysis where sdq_type = ? and min_value<=? and max_value>=? and sdq_target=?";
	
	public static List<SdqResultAnalysis> findSdqResultAnalysisByTypeAndValue(Connection con, String sdqType, int value, String sdqTarget) {
		
		List<SdqResultAnalysis> resultList = new ArrayList<>();
		
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_SDQ_RESULT_ANALYSIS_BY_TYPE_AND_VALUE);
						
	        pstmt.setString(1, sdqType);
	        pstmt.setInt(2, value);
	        pstmt.setInt(3, value);
	        pstmt.setString(4,  sdqTarget);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				SdqResultAnalysis sdqResultAnalysis= new SdqResultAnalysis();
				
				sdqResultAnalysis.setSdqType(rs.getString("sdq_type"));
				sdqResultAnalysis.setSdqAnalysisResult(rs.getString("sdq_analysis_result"));
				sdqResultAnalysis.setSdqTarget(rs.getString("sdq_target"));
				sdqResultAnalysis.setMinValue(rs.getInt("min_value"));
				sdqResultAnalysis.setMaxValue(rs.getInt("max_value"));
				sdqResultAnalysis.setColor(rs.getString("color"));
				sdqResultAnalysis.setDescription(rs.getString("description"));
				
				resultList.add(sdqResultAnalysis);				
			}
			return resultList;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
