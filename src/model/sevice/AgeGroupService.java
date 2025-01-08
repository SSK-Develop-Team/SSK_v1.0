package model.sevice;

import model.dao.AgeGroupDAO;
import model.dto.AgeGroup;

import java.sql.Connection;
import java.util.ArrayList;

public class AgeGroupService {
    /**
     * 언어 발달 검사 - 선택 가능한 연령 그룸 반환 함수
     *  : curAge를 기준으로 12개월 전부터 6개월 후까지
     *  2023.10.05수정 => 모든 연령에 대한 검사가 가능하도록 수정함
     * */
    public static ArrayList<AgeGroup> getSelectableAgeGroupList(Connection con, AgeGroup currAgeGroup){
/*
        int startAge = currAgeGroup.getStartAge() - 12;

        int endAge = currAgeGroup.getEndAge() + 6;

        if (currAgeGroup.getAgeGroupId() == 14){ // 언어 발달 검사를 할 수 있는 연령이 아닌 경우 -> 전체 연령을 선택할 수 있도록
            startAge = 36;
            endAge = 120;
        }
*/
    	int startAge = 36;

        int endAge = 120;
        
        ArrayList<AgeGroup> ageGroupList = AgeGroupDAO.getAgeGroupListInRange(con, startAge, endAge);

        return ageGroupList;
    }
}
