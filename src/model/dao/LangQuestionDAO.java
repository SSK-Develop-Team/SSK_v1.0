package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.dto.LangQuestion;

public class LangQuestionDAO {
	
	private static final String SQL_SELECT_LANG_QUESTION_BY_AGE_GROUP_ID = "select * from lang_question where age_group_id = ?";
	private static final String SQL_SELECT_LANG_QUESTION_BY_ID = "select * from lang_question where lang_question_id = ?";
	public static ArrayList<LangQuestion> getLangQuestionListByAgeGroupId(Connection con, int age) {
		ArrayList<LangQuestion> langQuestionList = new ArrayList<LangQuestion>();
		try {
			PreparedStatement pstmt = con.prepareStatement(SQL_SELECT_LANG_QUESTION_BY_AGE_GROUP_ID);
			pstmt.setInt(1, age);
			ResultSet rs = pstmt.executeQuery();
		
			while(rs.next()) {/*bean을 가져올 때는 반드시 모든 정보를 가져올 것*/
				LangQuestion langQuestion = new LangQuestion();
				langQuestion.setLangQuestionId(rs.getInt(1));
				langQuestion.setAgeGroupId(rs.getInt(2));
				langQuestion.setLangType(rs.getString(3));
				langQuestion.setLangQuestionContent(rs.getString(4));
				
				langQuestionList.add(langQuestion);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return langQuestionList;
	}
	
	public static LangQuestion getLangQuestionById(Connection con, int langQuestionId) {
		try {
			LangQuestion langQuestion = new LangQuestion(); 
			PreparedStatement pstmt = con.prepareStatement(SQL_SELECT_LANG_QUESTION_BY_ID);
			pstmt.setInt(1, langQuestionId);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				langQuestion.setLangQuestionId(rs.getInt(1));
				langQuestion.setAgeGroupId(rs.getInt(2));
				langQuestion.setLangType(rs.getString(3));
				langQuestion.setLangQuestionContent(rs.getString(4));
			}
			return langQuestion;
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
