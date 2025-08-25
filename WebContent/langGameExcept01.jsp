<!-- n개 그림 버튼 중 선택 -->
<!-- 2번, 10번, 26번, 39번, 42번, 47번, 49번 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
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
		
	%>
	<title><%= gameID %>번 문항 직접 평가</title>
</head>
<body>
<%@ include file="sidebar.jsp" %>

<div class="w3-row">
	<div class="w3-col w3-hide-small m1 l2">&nbsp;</div>
	<div class="w3-col w3-container s12 m10 l8">
		<!-- 문항으로 & 직접평가 번호 -->
		<div style="font-size:1.3em;font-weight:bold;">
			<button class="w3-button" onclick="FinishGame(<%= gameID %>);" style="border:none; background: none; cursor: pointer;">
			    <img src="./image/left-arrow.png" alt="이전 페이지" style="width: 20px; height: auto;">
			</button>
			직접평가 #<%= gameID %>
		</div>

		<div>			
			<!-- 직접평가 이미지 -->
			<div class="Quiz<%=QuizNum%>" style="height : 100%;width : 100%;position : relative;">
				<img class="gameImg01" src="<%=currLangGameElement.getLangGameImg() %>.jpg" width="100%" height="100%" alt="색깔 맞추기 <%=QuizNum%>"/>
				
				<!-- 버튼 이미지 배치 및 정답 설정 -->
				<% for (int j = 0 ; j < buttonData.size();j++){
						Map<String, String> btn = buttonData.get(j); // 버튼 스타일
						boolean isCorrect = (j + 1 == correctAnswer[QuizNum-1]); // 현재 퀴즈 정답 버튼
						boolean imageDuplicated = (boolean)QuizData.get("imageDuplicated"); // 동일 문항-동일 버튼 이미지 여부
						
						String imageSrc = currLangGameElement.getLangGameImg();
						
						if (imageDuplicated){ // 동일 문항, 동일 버튼 이미지인 경우
							imageSrc = imageSrc.substring(0, imageSrc.lastIndexOf("_")) + "_icon0" + (j+1) + ".png";
						} else {
							imageSrc = imageSrc + "_icon0" + (j+1) + ".png";
						}
						%>
						
											
						<img class="selectImg0<%=j+1%>" id="Button<%=j+1 %>" 
						 	 src="<%=imageSrc%>"
						 	 style="left:<%= btn.get("left") %>; top:<%= btn.get("top") %>;
						 	 width:<%= btn.get("width") %>; position : absolute;" alt="Button<%=j+1 %>" 
						 	 onclick="<%= isCorrect ? "correctA()" : "wrongA()" %>" />
				<%} %>
			</div>	
		</div>
		
		<!-- 직접평가 화자 -->
		<% if(currLangGameElement.getLangGameSpeaker().equals("-")) { %>
		<div class="w3-col m2 l3">&nbsp;</div><%} %>
		<% if(! currLangGameElement.getLangGameSpeaker().equals("-")){%>
		<div class="w3-container w3-round-large" style="background-color:#12192C; color:white; width:100px;text-align:center;font-size:1.1em;padding:2px;">
		<%=currLangGameElement.getLangGameSpeaker() %></div><%} %>

		<!-- 직접평가 컨텐츠 -->
		<% boolean hasLangGameContent = !langGameContent.equals("-"); %>
		
		<div class="w3-container w3-round-large w3-padding lang-game-content" 
		     style="<%= hasLangGameContent ? "border:1px solid #12192C; font-size : 1.3em" : "" %>">
		    <div class="w3-col m2 l3">&nbsp;</div>
		
		    <% if (hasLangGameContent) { %>
		        <div class="w3-container w3-padding-32"><%= langGameContent %></div>
		    <% } %>
		
		    <!-- 이전/다음 버튼 -->
		    <div class="w3-container w3-right">
		        <% if (i > 0) { %>
		            <button class="w3-button" onclick="getPrevContent(<%= i %>);" style="border:none; background-color:#FFFFFF;"> 
		            	&lt; 이전
		            </button>
		        <% } %>

		        <!-- 다시듣기 버튼 -->
		        <% if (langGameList.get(i).getLangGameVoice()!= null & audioEndedNextFlag != 1) { %>
		            <button class="w3-button" onclick="audioReplay()" style="border:none;"> 
		            	<img src="./image/reload.png" alt="다시듣기" style="width: 25px; height: auto;">
		            </button>
		        <% } %>	        
	
		        <% if (i <= langGameList.size() - 1) { %>
		            <button class="w3-button" onclick="getNextContent(<%= i %>, <%= gameID %>, <%= langGameList.size() %>);" style="border:none; background-color:#FFFFFF;">
						다음 &gt;
		            </button>
		        <% } %>
		    </div>
		</div>
			
		<div class="w3-left" style="margin-top:5px;">
			<!-- 팁 버튼 -->
			<%if(currLangGameElement.getLangGameHint()!=null||currLangGameElement.getLangGameHintVoice()!=null){ %>
			<button class="w3-button w3-round-large" onclick="openHint();" style="background-color:#12192C; color:white; text-align:center;font-size:1.1em;margin-right:5px;">팁</button>
			<div id="hint-modal" class="w3-modal">
				<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
					<div class="w3-container w3-center">
						<span onclick="closeHint();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
						<%if(currLangGameElement.getLangGameHint()!=null){ %>
						<p style="font-size: 1.4em;"><%=currLangGameElement.getLangGameHint() %></p>
						<%}%>
					</div>
				</div>
			</div>
			<%} %>
			
			<!-- 정답 버튼 -->
			<%if(currLangGameElement.getLangGameAnswer()!=null||currLangGameElement.getLangGameAnswerVoice()!=null){ %>
			<button class="w3-button w3-round-large" onclick="openAnswer();" style="background-color:#12192C; color:white; text-align:center;font-size:1.1em;margin-right:5px;">정답 </button>
			<div id="answer-modal" class="w3-modal">
				<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
					<div class="w3-container w3-center">
						<span onclick="closeAnswer();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
						<%if(currLangGameElement.getLangGameAnswer()!=null){ %>
						<p style="font-size: 1.4em;"><%=currLangGameElement.getLangGameAnswer() %></p>
						<%} %>
						<%if(currLangGameElement.getLangGameAnswerVoice()!=null){ %>
						<audio id="answer-audio" controls>
							<source src="<%=currLangGameElement.getLangGameAnswerVoice()%>">
						</audio>
						<%} %>
					</div>
				</div>
			</div>
			<%} %>
			
	        <!-- 다시듣기 버튼 -->
	        <% if (langGameList.get(i).getLangGameVoice()!= null & audioEndedNextFlag != 1) { %>
	            <button class="w3-button w3-round-large" onclick="audioReplay()" style="background-color:#12192C; color:white; text-align:center;font-size:1.1em;margin-right:5px;">다시 듣기 </button>
	            </button>
	        <% } %>
	        
			<!-- 평가기준 버튼 -->
			<%if(currLangGameElement.getLangGameCriteria()!=null){ %>
			<button class="w3-button w3-round-large" onclick="openCriteria();" style="background-color:#12192C; color:white; text-align:center;font-size:1.1em;margin-right:5px;">평가기준 </button>
			<div id="criteria-modal" class="w3-modal">
				<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
					<div class="w3-container w3-center">
						<span onclick="closeCriteria();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
						<%if(currLangGameElement.getLangGameCriteria()!=null){ %>
						<p style="font-size: 1.4em;"><%=currLangGameElement.getLangGameCriteria() %></p>
						<%} %>
					</div>
				</div>
			</div>
			<%} %>
	
		</div>
		
	</div>
	<div class="w3-col w3-hide-small m1 l2">&nbsp;</div>
</div>

</body>
<script src="https://code.jquery.com/jquery-3.6.0.slim.js"></script>
<script type="text/javascript">

	var audio = new Audio();
	audio.src="<%=langGameList.get(i).getLangGameVoice()%>";
	
	//음성 재생
	window.onload = function () {
		audio.load();
		audio.play();
	}
	
	function wrongA(){
		var audio = new Audio('./audio/wrong.mp3');
		audio.play();
	}
	
	function correctA(){
		var audio = new Audio('./audio/correct.mp3');
		audio.play();
	}

	function audioReplay() {
		if (audio.paused) {
			audio.play();
		}
	}
	
</script>
<script type="text/javascript" src="js/langGame.js" charset="UTF-8"></script>
</html>