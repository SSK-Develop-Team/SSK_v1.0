<!-- n개 그림 버튼 중 선택 -->
<!-- 2번, 3번, 10번, 26번, 39번, 42번, 47번, 49번 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ page import="model.dto.User" %>
<%@ page import="model.dto.LangGame" %>
<%@ page import="util.process.LangGameProcessor" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Arrays" %>

<!DOCTYPE html>
<html>
<link href="css/langGame.css" rel="stylesheet" type='text/css' >
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	<link rel="stylesheet" type="text/css" href="css/style.css">
	<%
		User currUser = (User)session.getAttribute("currUser");
		int gameID = (int)session.getAttribute("langGameID");

		ArrayList<LangGame> langGameList = (ArrayList<LangGame>)session.getAttribute("currLangGameList");
		int i = (int)session.getAttribute("currLangGameIndex");

		LangGame currLangGameElement = langGameList.get(i);
		int audioEndedNextFlag = 0;
		if(currLangGameElement.getLangGameHint()==null&&currLangGameElement.getLangGameHintVoice()==null&&currLangGameElement.getLangGameAnswer()==null&&currLangGameElement.getLangGameAnswerVoice()==null){
			audioEndedNextFlag = 1;
		}

		String langGameContent = LangGameProcessor.changeNameOfLangGameContent(currLangGameElement.getLangGameContent(), currUser.getUserName()) ;
	%>
	
	<%  // 예외 퀴즈 개별 설정
		Map<String, Object> QuizData = LangGameProcessor.getButtonData(gameID, i);
		
		// 버튼 개별 설정 데이터(left, top, width)
		List<Map<String, String>> buttonData = (List<Map<String, String>>) QuizData.get("buttonData");
		// 퀴즈 정답 리스트
		int[] correctAnswer = (int[]) QuizData.get("correctAnswer");
		// 문항의 퀴즈 번호
		int QuizNum = Arrays.binarySearch((int[]) QuizData.get("pageNum"), i)+1;
		
		// 현재 langGameId가 예외 문항인지 확인
		boolean isExceptionGame = false;
		if((gameID == 2 && (i == 1 || i == 3 || i == 5 || i == 7 || i == 9)) ||
		   (gameID == 3 && (i == 1 || i == 2 || i == 3)) ||
		   (gameID == 10 && (i == 1 || i == 2 || i == 3)) ||
		   (gameID == 26 && (i == 1 || i == 2 || i == 3)) ||
		   (gameID == 39 && (i == 1 || i == 2 || i == 3)) ||
		   (gameID == 47 && (i == 1 || i == 2 || i == 3)) ||
		   (gameID == 42 && (i == 1 || i == 2 || i == 3)) ||
		   (gameID == 49 && (i == 1 || i == 2))) {
			isExceptionGame = true;
		}
		
	%>
	<title><%= gameID %>번 문항 직접 평가</title>
<style>
/* 시작 모달 스타일 지정 */
.start-modal-content {
	position: absolute;
	top:50%;
	left:50%;
	transform: translate(-50%, -50%);
	text-align: center;
	padding: 40px;
	width: 90%;
	max-width: 500px;
	margin: 0 !important;         /* W3CSS 기본 margin:auto 제거 */
  	box-sizing: border-box;       /* padding 포함해서 중앙 정렬 정확히 */
}

@media screen and (max-width: 600px) {
	.start-modal-content {
	    width: 90% !important;    /* 양 옆 공백 제거 */
	    max-width: none !important;  /* 최대폭 제한 해제 */
	    padding: 20px;
	}
}

</style>
</head>
<body>
<%@ include file="sidebar.jsp" %>

<div class="w3-row">
	<!-- 시작 알림 모달 -->
	<div id="start-modal" class="w3-modal" style="display:block;">
	  <div class="w3-modal-content w3-animate-opacity w3-round-large start-modal-content">
	    <h3>직접평가를 시작하시겠습니까?</h3>
	    <button class="w3-button w3-round-large w3-margin-top" style="background-color:#12192C; color:white;" onclick="startEvaluation()">시작하기</button>
	  </div>
	</div>
	
	<div class="w3-col w3-hide-small m1 l2">&nbsp;</div>
	<div class="w3-col w3-container s12 m10 l8">
		<div style="font-size:1.3em; font-weight:bold;">
			<button class="w3-button" onclick="FinishGame(<%= gameID %>);" style="border:none; background: none; cursor: pointer;">
			    <img src="./image/left-arrow.png" alt="이전 페이지" style="width: 20px; height: auto;"/>
			</button>
			직접평가 #<%= gameID %>
		</div>
		
		<!-- 이미지 -->
		<div id="quiz-container" style="position:relative; width:100%;">
		  <img id="main-image" src="<%=currLangGameElement.getLangGameImg() %>.jpg" style="width:100%;" alt="문항 이미지"/>
		</div>
		
		<!-- 직접평가 화자 -->
		<div id="speaker-name"
		     class="w3-container w3-round-large"
		     style="background-color:#12192C; color:white; width:100px; text-align:center; padding:2px; font-size:1.1em; <%= currLangGameElement.getLangGameSpeaker().equals("-") ? "display:none;" : "display:block;" %>">
		  <%= !currLangGameElement.getLangGameSpeaker().equals("-") ? currLangGameElement.getLangGameSpeaker() : "" %>
		</div>

		<!-- 직접평가 컨텐츠 -->
		<% boolean hasLangGameContent = !langGameContent.equals("-"); %>
		
		<div class="w3-container w3-round-large w3-padding" 
		     style="<%= hasLangGameContent ? "border:1px solid #12192C; font-size : 1.3em" : "" %>">
		    <div class="w3-col m2 l3">&nbsp;</div>
		
		    <% if (hasLangGameContent) { %>
		        <div id="content-text" class="w3-container w3-padding-32"><%= langGameContent %></div>
		    <% } %>
					
			<!-- 이전/다음/다시듣기 버튼 -->
			<div class="w3-container w3-right">
			  <button id="prev-btn" class="w3-button" onclick="goPrev()" style="border:none; background-color:#FFFFFF; display:none;">
			    &lt; 이전
			  </button>
			
			  <button id="replay-btn" class="w3-button" onclick="audioReplay()" style="border:none; display:none;">
			    <img src="./image/reload.png" alt="다시듣기" style="width:25px; height:auto;"/>
			  </button>
			
			  <button id="next-btn" class="w3-button" onclick="goNext()" style="border:none; background-color:#FFFFFF; display:none;">
			    다음 &gt;
			  </button>
			</div>
		</div>
			
		<div class="w3-left" style="margin-top:5px;">
			<!-- 팁 버튼 -->
			<button id="hint-btn" class="w3-button w3-round-large"  onclick="openHint();" style="background-color:#12192C; color:white; text-align:center;font-size:1.1em;margin-right:5px;display:<%=currLangGameElement.getLangGameHint()!=null||currLangGameElement.getLangGameHintVoice()!=null ? "inline-block" : "none" %>;">팁</button>
			<div id="hint-modal" class="w3-modal">
				<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
					<div class="w3-container w3-center">
						<span onclick="closeHint();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
						<p style="font-size: 1.4em;"><%=currLangGameElement.getLangGameHint() %></p>
					</div>
				</div>
			</div>
			
			<!-- 정답 버튼 -->
			<button id="answer-btn" class="w3-button w3-round-large" onclick="openAnswer();" style="background-color:#12192C; color:white; text-align:center;font-size:1.1em;margin-right:5px;display:<%=currLangGameElement.getLangGameAnswer()!=null||currLangGameElement.getLangGameAnswerVoice()!=null ? "inline-block" : "none" %>;">정답 </button>
			<div id="answer-modal" class="w3-modal">
				<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
					<div class="w3-container w3-center">
						<span onclick="closeAnswer();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
						<p style="font-size: 1.4em;"><%=currLangGameElement.getLangGameAnswer() %></p>
					</div>
				</div>
			</div>
			
	        <!-- 다시듣기 버튼 -->
	            <button id="audio-replay-btn" class="w3-button w3-round-large" onclick="audioReplay()" style="background-color:#12192C; color:white; text-align:center;font-size:1.1em;margin-right:5px;display:<%=langGameList.get(i).getLangGameVoice()!=null ? "inline-block" : "none" %>;">다시 듣기 </button>
	        
			<!-- 평가기준 버튼 -->
			<button id="criteria-btn" class="w3-button w3-round-large" onclick="openCriteria();" style="background-color:#12192C; color:white; text-align:center;font-size:1.1em;margin-right:5px;display:<%=currLangGameElement.getLangGameCriteria()!=null ? "inline-block" : "none" %>;">평가기준 </button>
			<div id="criteria-modal" class="w3-modal">
				<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
					<div class="w3-container w3-center">
						<span onclick="closeCriteria();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
						<p style="font-size: 1.4em;"><%=currLangGameElement.getLangGameCriteria() %></p>
					</div>
				</div>
			</div>
			
		</div>
		<audio id="voice-audio" controls style="display:none;"></audio>
	</div>
	<div class="w3-col w3-hide-small m1 l2">&nbsp;</div>
</div>
<script type="text/javascript" src="js/langGame.js" charset="UTF-8"></script>
<script>
//===== 데이터 =====
	var game_data = [
	  <% for (int j = 0; j < langGameList.size(); j++) { %>
	  {
	    index: <%= j %>,
	    imgUrl: "<%= langGameList.get(j).getLangGameImg() %>",
	    speaker: "<%= langGameList.get(j).getLangGameSpeaker() %>",
	    content: "<%= langGameList.get(j).getLangGameContent().replace("\"","\\\"").replace("\r","").replace("\n","\\n") %>",
	    voiceUrl: "<%= langGameList.get(j).getLangGameVoice() %>",
	    hint: "<%= langGameList.get(j).getLangGameHint() != null ? langGameList.get(j).getLangGameHint().replace("\"","\\\"").replace("\r","").replace("\n","\\n") : "" %>",
	    hintVoice: "<%= langGameList.get(j).getLangGameHintVoice() != null ? langGameList.get(j).getLangGameHintVoice() : "" %>",
	    answer: "<%= langGameList.get(j).getLangGameAnswer() != null ? langGameList.get(j).getLangGameAnswer().replace("\"","\\\"").replace("\r","").replace("\n","\\n") : "" %>",
	    answerVoice: "<%= langGameList.get(j).getLangGameAnswerVoice() != null ? langGameList.get(j).getLangGameAnswerVoice() : "" %>",
	    criteria: "<%= langGameList.get(j).getLangGameCriteria() != null ? langGameList.get(j).getLangGameCriteria().replace("\"","\\\"").replace("\r","").replace("\n","\\n") : "" %>"
	  }<%= j < langGameList.size() - 1 ? "," : "" %>
	  <% } %>
	];
	
	// ===== 예외 버튼 메타(있으면 index 키로 저장) =====
	//  - 서버에서 각 인덱스에 대해 LangGameProcessor.getButtonData(gameID, idx) 호출
	//  - buttonData가 존재하고 길이가 > 0인 경우만 등록
	var exception_meta = {};
	<% for (int idx = 0; idx < langGameList.size(); idx++) {
	     Map<String, Object> Q = LangGameProcessor.getButtonData(gameID, idx);
	     if (Q != null) {
	       List<Map<String, String>> btns = (List<Map<String, String>>) Q.get("buttonData");
	       if (btns != null && !btns.isEmpty()) {
	         int[] correctArr = (int[]) Q.get("correctAnswer");
	         int quizNum = Arrays.binarySearch((int[]) Q.get("pageNum"), idx) + 1;
	         boolean imageDuplicated = (boolean) Q.get("imageDuplicated");
	         LangGame lg = langGameList.get(idx);
	%>
	exception_meta[<%= idx %>] = {
	  quizNum: <%= quizNum %>,
	  imageDuplicated: <%= imageDuplicated %>,
	  correctIndex: <%= (correctArr != null && quizNum > 0) ? correctArr[quizNum - 1] : -1 %>,
	  buttons: [
	    <% for (int b = 0; b < btns.size(); b++) {
	         Map<String,String> btn = btns.get(b);
	         boolean isCorrect = (correctArr != null && quizNum > 0 && (b+1 == correctArr[quizNum - 1]));
	         String base = lg.getLangGameImg();
	         String src = imageDuplicated
	                      ? base.substring(0, base.lastIndexOf("_")) + "_icon0" + (b+1) + ".png"
	                      : base + "_icon0" + (b+1) + ".png";
	    %>
	    {
	      left: "<%= btn.get("left") %>",
	      top: "<%= btn.get("top") %>",
	      width: "<%= btn.get("width") %>",
	      imageSrc: "<%= src %>",
	      isCorrect: <%= isCorrect %>
	    }<%= (b < btns.size() - 1) ? "," : "" %>
	    <% } %>
	  ]
	};
	<%   }
	     }
	   } %>
	
// 변수 초기화
	const game_id = <%= gameID %>;
	let current_index = <%= i %>;
	let audio_ended_next_flag = <%=audioEndedNextFlag%>; // 오디오 자동 재생 여부
	
	// 통합된 langGame.js 초기화
	initializeLangGame(game_data, game_id, current_index, audio_ended_next_flag, exception_meta);
</script>
</body>
</html>