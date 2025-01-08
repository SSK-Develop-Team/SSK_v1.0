package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.dto.LangGame;

/**
 * @author Jiwon Lee
 * 
 * LangGame(언어 게임 문항) DAO
 * 
 * 언어 문항에 해당하는 게임 contents 가져오기 - getLangGameListByLangQuestionId
 *
 */
public class LangGameDAO {
	private final static String SQLST_SELECT_LANG_GAME_LIST = "select * from lang_game where lang_question_id = ? ORDER BY lang_game_order ASC";
	
	public static ArrayList<LangGame> getLangGameListByLangQuestionId(Connection con, int langQuestionId){
		try {
			ArrayList<LangGame> langGameList = new ArrayList<LangGame>();
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_LANG_GAME_LIST);
			pstmt.setInt(1, langQuestionId);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				LangGame langGameElement = new LangGame();
				langGameElement.setLangGameId(rs.getInt(1));
				langGameElement.setLangQuestionId(rs.getInt(2));
				langGameElement.setLangGameOrder(rs.getInt(3));
				langGameElement.setLangGameContent(rs.getString(4));
				langGameElement.setLangGameImg(rs.getString(5));
				langGameElement.setLangGameSpeaker(rs.getString(6));
				langGameElement.setLangGameVoice(rs.getString(7));
				langGameElement.setLangGameHint(rs.getString(8));
				langGameElement.setLangGameHintVoice(rs.getString(9));
				langGameElement.setLangGameAnswer(rs.getString(10));
				langGameElement.setLangGameAnswerVoice(rs.getString(11));
				langGameList.add(langGameElement);
			}
			return langGameList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
