package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.dto.EsmEmotion;

public class EsmEmotionDAO {
	private final static String SQLST_SELECT_ESM_EMOTION = "SELECT * FROM esm_emotion WHERE esm_type= ? ORDER BY esm_emotion_id";
	
	public static ArrayList<EsmEmotion> getEsmEmotionListByEsmType(Connection con, String esmTypeName){
		ArrayList<EsmEmotion> esmEmotionList = new ArrayList<EsmEmotion>();
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_ESM_EMOTION);
			pstmt.setString(1, esmTypeName);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				EsmEmotion esmEmotionElement = new EsmEmotion(
						rs.getInt("esm_emotion_id"), rs.getString("esm_type"), rs.getString("esm_emotion"), rs.getString("esm_emotion_kr"), rs.getString("esm_emotion_icon"));
				esmEmotionList.add(esmEmotionElement);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return esmEmotionList;
	}

}
