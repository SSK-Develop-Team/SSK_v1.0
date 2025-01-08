package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.dto.SdqQuestion;


public class SdqQuestionDAO {
	
	private final static String SQLST_SELECT_SDQ_QUESTION_LIST_BY_SDQ_TARGET = "select * from sdq_question where sdq_target=?";

	/*sdq target(부모, 아동)에 따라 List 가져오기*/
	public static ArrayList<SdqQuestion> getSdqQuestionList(Connection con, String sdqTarget) {
		ArrayList<SdqQuestion> sdqQuestionList = new ArrayList<SdqQuestion>();
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_SDQ_QUESTION_LIST_BY_SDQ_TARGET);
			pstmt.setString(1,sdqTarget);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				SdqQuestion bean = new SdqQuestion();
				bean.setSdqQuestionId(rs.getInt("sdq_question_id"));
				bean.setSdqType(rs.getString("sdq_type"));
				bean.setSdqQuestionContent(rs.getString("sdq_question_content"));
				bean.setSdqScoringType(rs.getString("sdq_scoring_type"));
				bean.setSdqTarget(rs.getString("sdq_target"));
				sdqQuestionList.add(bean);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return sdqQuestionList;
		
	}

}
