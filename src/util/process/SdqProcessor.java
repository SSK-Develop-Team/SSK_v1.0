package util.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.dto.SdqReply;

public class SdqProcessor {
	public static int[] getSdqReplyListToSdqResult(ArrayList<SdqReply> sdqReplyList) {
		int[] scoreList = new int[5];
		
	    List<Integer> SOCIAL_BEHAVIOR_IDS = Arrays.asList(1, 4, 9, 17, 20, 26, 29, 34, 42, 45);	// 사회성 행동
	    List<Integer> OVERACTIVE_IDS = Arrays.asList(2, 10, 15, 21, 25, 27, 35, 40, 46, 50);  	// 과잉행동
	    List<Integer> EMOTIONAL_STATE_IDS = Arrays.asList(3, 8, 13, 16, 24, 28, 33, 38, 41, 49);// 정서적 상태
	    List<Integer> CONDUCT_PROBLEM_IDS = Arrays.asList(5, 7, 12, 18, 22, 30, 32, 37, 43, 47);// 품행 문제
	    List<Integer> PEER_PROBLEM_IDS = Arrays.asList(6, 11, 14, 19, 23, 31, 36, 39, 44, 48);	// 또래관계문제

		
		for(int i=0;i<sdqReplyList.size();i++) {
			int QNUM = sdqReplyList.get(i).getSdqQuestionId()%10;
			int score = sdqReplyList.get(i).getSdqReplyContent()-1;//enum 값은 1,2,3 이나, 점수는 0,1,2
			
			if(SOCIAL_BEHAVIOR_IDS.contains(QNUM)) {//사회성 행동
				scoreList[0]+=score;
			}else if(OVERACTIVE_IDS.contains(QNUM)) {//과잉행동
				scoreList[1]+=score;
			}else if(EMOTIONAL_STATE_IDS.contains(QNUM)) {//정서적 상태
				scoreList[2]+=score;
			}else if(CONDUCT_PROBLEM_IDS.contains(QNUM)) {//품행문제
				scoreList[3]+=score;
			}else if(PEER_PROBLEM_IDS.contains(QNUM)) {//또래문제
				scoreList[4]+=score;
			}
		}
		
		return scoreList;
	}

}
