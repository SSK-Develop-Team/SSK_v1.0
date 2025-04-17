package util.process;

import model.dto.AgeGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class LangGameProcessor {
	/**
	 * 언어 게임 문장의 이름 변환 함수(현재 사용 X)
	 *  : 00아, 00이는 등 00-> 사용자 이름으로 변환하여 해당 문장을 반환
	 */
	public static String changeNameOfLangGameContent(String langGameContent,String userName) {
		String res = langGameContent;
		char lastName = userName.charAt(userName.length() - 1);
		int index = (lastName - 0xAC00) % 28;
		String addName = userName.substring(1,userName.length());
		
		while(res.contains("00")) {
			if(res.charAt(res.indexOf("00")+2)=='아') {
				res = index>0?res.replace("00아", addName+"아"):res.replace("00아", addName+"야");
			}else if(res.charAt(res.indexOf("00")+2)=='이') {
				res = index>0?res.replace("00이", addName+"이"):res.replace("00이", addName);
			}else {
				res = res.replace("00", addName);
			}
		}
		return res;
	}
	
	/**
	 * 예외 문항 처리
	 * : langGameLocation 지정 함수
	 * : langGameId는 실제 DB에 저장된 langGameId의 -1한 값*/
	public static String getForwardLocationByLangQuestionIdAndLangGameId(int langQuestionId, int langGameId){
		String location = "/langGame.jsp";

		// 이미지 버튼 터치 게임 Except01
		if((langQuestionId == 2 && (langGameId == 1 || langGameId == 3 || langGameId == 5 || langGameId == 7 || langGameId == 9))||
				(langQuestionId == 3 && (langGameId == 1 || langGameId == 2 || langGameId == 3))||
				(langQuestionId == 10 && (langGameId == 1 || langGameId == 2 || langGameId == 3))||
				(langQuestionId == 26 && (langGameId == 1 || langGameId == 2 || langGameId == 3))||
				(langQuestionId == 39 && (langGameId == 1 || langGameId == 2 || langGameId == 3))||
				(langQuestionId == 47 && (langGameId == 1 || langGameId == 2 || langGameId == 3))||
				(langQuestionId == 42 && (langGameId == 1 || langGameId == 2 || langGameId == 3))||
				(langQuestionId == 49 && (langGameId == 1 || langGameId == 2))){
			location = "/langGameExcept01.jsp";
		}
		// 번호 터치 후 화면 전환 게임 Except02
		else if(langQuestionId == 4 && (langGameId == 1 || langGameId == 3 || langGameId == 5 || langGameId == 7)) {
			location = "/langGameExcept02.jsp";
		}
		// 글자쓰기 게임 Except 03
		else if((langQuestionId == 30 && langGameId == 1) || 
				(langQuestionId == 35 && langGameId == 1) || 
				(langQuestionId == 60 && (langGameId == 1 || langGameId == 2 || langGameId == 3)) ||
				(langQuestionId == 63 && (langGameId == 1 || langGameId == 2 || langGameId == 3))){
			location = "/langGameExcept03.jsp";
		}
		// 글자쓰기 게임 Except 04
		else if(langQuestionId == 50 && (langGameId == 1 || langGameId == 2 || langGameId == 3 || langGameId == 4)){
			location = "/langGameExcept04.jsp";
		}
		return location;
	}
	
	/*
	 * 예외 문항 개별 설정 
	 * */
    public static Map<String, Object> getButtonData(int langQuestionId, int langGameId){
    	// 예외 문항 버튼 sytle 리스트
        List<Map<String, String>> buttonData = new ArrayList<>();
        // 문항 별 정답 리스트
        int[] correctAnswer = new int[0]; 
        int[] pageNum = new int[0];
        boolean isImageDuplicated = true;
        
        // 화면 왼쪽 버튼 5개(위에 2개, 아래 3개)
		if(langQuestionId == 2 && (langGameId == 1 || langGameId == 3 || langGameId == 5 || langGameId == 7 || langGameId == 9)) {
			buttonData = Arrays.asList(
	            Map.of("left", "15%", "top", "10%", "width", "15%"),
	            Map.of("left", "40%", "top", "10%", "width", "15%"),
	            Map.of("left", "4%", "top", "55%", "width", "16%"),
	            Map.of("left", "26%", "top", "52%", "width", "17%"),
	            Map.of("left", "50%", "top", "55%", "width", "15%"));
	        correctAnswer = new int[]{1, 5, 3, 2, 4};
	        pageNum = new int[]{1, 3, 5, 7, 9};
	    } 
        // 화면 오른쪽 버튼 2개
		else if(langQuestionId == 3 && (langGameId == 1 || langGameId == 2 || langGameId == 3)) {
			buttonData = Arrays.asList(
	                Map.of("left", "42%", "top", "24%", "width", "25%"),
	                Map.of("left", "70%", "top", "24%", "width", "26%"));
	        correctAnswer = new int[]{2, 1, 2};
	        pageNum = new int[]{1, 2, 3};
	    } 
		// 화면 오른쪽 버튼 3개 & 버튼 이미지 동일
		else if (langQuestionId == 10 && (langGameId == 1 || langGameId == 2 || langGameId == 3)){
	        buttonData = Arrays.asList(
	                Map.of("left", "35%", "top", "33%", "width", "17%"),
	                Map.of("left", "56%", "top", "33%", "width", "17%"),
	                Map.of("left", "77%", "top", "33%", "width", "17%"));
	        correctAnswer = new int[]{2, 1, 3};
	        pageNum = new int[]{1, 2, 3};
	    }    
		// 화면 오른쪽 버튼 3개 & 버튼 이미지 상이
		else if (langQuestionId == 26 && (langGameId == 1 || langGameId == 2 || langGameId == 3)){
	        buttonData = Arrays.asList(
	                Map.of("left", "35%", "top", "33%", "width", "17%"),
	                Map.of("left", "56%", "top", "33%", "width", "17%"),
	                Map.of("left", "77%", "top", "33%", "width", "17%"));
	        correctAnswer = new int[]{3, 3, 2};
	        pageNum = new int[]{1, 2, 3};
	        isImageDuplicated = false;
	    }    
		// 화면 오른쪽 버튼 3개 & 버튼 이미지 상이
		else if (langQuestionId == 39 && (langGameId == 1 || langGameId == 2 || langGameId == 3)){
	        buttonData = Arrays.asList(
	                Map.of("left", "35%", "top", "33%", "width", "17%"),
	                Map.of("left", "56%", "top", "33%", "width", "17%"),
	                Map.of("left", "77%", "top", "33%", "width", "17%"));
	        correctAnswer = new int[]{1, 1, 2};
	        pageNum = new int[]{1, 2, 3};
	        isImageDuplicated = false;
	    }    
		// 화면 가운데 버튼 3개 & 버튼 이미지 동일
		else if (langQuestionId == 47 && (langGameId == 1 || langGameId == 2 || langGameId == 3)){
	        buttonData = Arrays.asList(
	                Map.of("left", "15%", "top", "34%", "width", "15%"),
	                Map.of("left", "43%", "top", "34%", "width", "15%"),
	                Map.of("left", "70%", "top", "34%", "width", "15%"));
	        correctAnswer = new int[]{1, 3, 2};
	        pageNum = new int[]{1, 2, 3};
	    }
		// 화면 오른쪽 버튼 4개 (위에 2개, 아래 2개)
		else if (langQuestionId == 42 && (langGameId == 1 || langGameId == 2 || langGameId == 3)) {
	        buttonData = Arrays.asList(
	                Map.of("left", "51%", "top", "10%", "width", "18%"),
	                Map.of("left", "73%", "top", "10%", "width", "18%"),
	                Map.of("left", "51%", "top", "54%", "width", "18%"),
	                Map.of("left", "73%", "top", "54%", "width", "18%"));
	        correctAnswer = new int[]{2, 1, 4};
	        pageNum = new int[]{1, 2, 3};
	    }
		// 화면 하단 버튼 4개
		else if (langQuestionId == 4 && (langGameId == 1 || langGameId == 3 || langGameId == 5 || langGameId == 7)) {
	        buttonData = Arrays.asList(
	                Map.of("left", "13%", "top", "52%", "width", "10%"),
	                Map.of("left", "30%", "top", "70%", "width", "10%"),
	                Map.of("left", "61%", "top", "52%", "width", "10%"),
	                Map.of("left", "79%", "top", "70%", "width", "10%"));
	        correctAnswer = new int[]{1, 2, 3, 4};
	        pageNum = new int[]{1, 3, 5, 7};
	    }
		// 우측 약간 아래 버튼 2개
		else if (langQuestionId == 49 && (langGameId == 1)) {
	        buttonData = Arrays.asList(
	                Map.of("left", "44%", "top", "46%", "width", "23%"),
	                Map.of("left", "65%", "top", "46%", "width", "25%"));
	        correctAnswer = new int[]{2};
	        pageNum = new int[]{1};
	        isImageDuplicated = false;
	    }
		// 우측 약간 아래 버튼 3개
		else if (langQuestionId == 49 && (langGameId == 2)) {
	        buttonData = Arrays.asList(
	                Map.of("left", "42%", "top", "46%", "width", "11%"),
	                Map.of("left", "56%", "top", "46%", "width", "18%"),
	                Map.of("left", "76%", "top", "46%", "width", "12%"));
	        correctAnswer = new int[]{3};
	        pageNum = new int[]{2};
	        isImageDuplicated = false;
	    };

        // 두 개의 값을 하나의 Map에 저장 후 반환
        Map<String, Object> result = new HashMap<>();
        result.put("buttonData", buttonData);
        result.put("correctAnswer", correctAnswer);
        result.put("pageNum", pageNum);
        result.put("imageDuplicated", isImageDuplicated);

        return result;
    }

}