package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.dto.EsmEmotion;

public class EsmEmotionDAO {
	private final static String SQLST_SELECT_ESM_EMOTION = "SELECT * FROM esm_emotion WHERE esm_type= ? and esm_target=? ORDER BY esm_emotion_id";
	
	public static ArrayList<EsmEmotion> getEsmEmotionListByEsmType(Connection con, String esmTypeName, String esmTarget){
		ArrayList<EsmEmotion> esmEmotionList = new ArrayList<EsmEmotion>();
				
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_ESM_EMOTION);
			pstmt.setString(1, esmTypeName);
			pstmt.setString(2,  esmTarget);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				/*EsmEmotion esmEmotionElement = new EsmEmotion(
						rs.getInt("esm_emotion_id"), rs.getString("esm_type"), 
						rs.getString("esm_emotion"), rs.getString("esm_emotion_kr"), 
						rs.getString("esm_emotion_icon"), rs.getString("esm_target"));
				esmEmotionList.add(esmEmotionElement);*/
				EsmEmotion esmEmotionElement = new EsmEmotion();
				esmEmotionElement.setEsmEmotionId(rs.getInt("esm_emotion_id"));
				esmEmotionElement.setEsmType(rs.getString("esm_type"));
				esmEmotionElement.setEsmEmotion(rs.getString("esm_emotion"));
				esmEmotionElement.setEsmEmotionKr(rs.getString("esm_emotion_kr"));
				esmEmotionElement.setEsmEmotionIcon(rs.getString("esm_emotion_icon"));
				esmEmotionElement.setEsmTarget(rs.getString("esm_target"));
				esmEmotionList.add(esmEmotionElement);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return esmEmotionList;
	}

}
