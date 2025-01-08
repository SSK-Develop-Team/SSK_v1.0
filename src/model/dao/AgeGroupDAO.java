package model.dao;

import model.dto.AgeGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AgeGroupDAO {
	private final static String SQLST_SELECT_AGE_GROUP = "select * from age_group where start_age <= ? AND ? <= end_age";
	private final static String SQLST_SELECT_AGE_GROUP_LIST = "select * from age_group where start_age >= ? AND ? >= end_age AND age_group_id < 14";
	/*currentAge 찾기*/
	public static AgeGroup getCurrAgeGroup(Connection con, int nowAge) {
		AgeGroup currAgeGroup = new AgeGroup();
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_AGE_GROUP);
			pstmt.setInt(1, nowAge);
			pstmt.setInt(2, nowAge);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				currAgeGroup.setAgeGroupId(rs.getInt("age_group_id"));
				currAgeGroup.setStartAge(rs.getInt("start_age"));
				currAgeGroup.setEndAge(rs.getInt("end_age"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return currAgeGroup;
	}
	
	/*특정 범위의 AgeGroup 불러오기*/
	public static ArrayList<AgeGroup> getAgeGroupListInRange(Connection con, int startAge, int endAge){
		ArrayList<AgeGroup> ageGroupList = new ArrayList<AgeGroup>();
		try {
			PreparedStatement pstmt = con.prepareStatement(SQLST_SELECT_AGE_GROUP_LIST);

			pstmt.setInt(1, startAge);
			pstmt.setInt(2, endAge);

			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				AgeGroup bean = new AgeGroup();
				bean.setAgeGroupId(rs.getInt("age_group_id"));
				bean.setStartAge(rs.getInt("start_age"));
				bean.setEndAge(rs.getInt("end_age"));
				ageGroupList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ageGroupList;
	}
}
