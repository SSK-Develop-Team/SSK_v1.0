<!-- 문제 아래 그림판 생성-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ page import="model.dto.User" %>
<%@ page import="model.dto.LangGame" %>
<%@ page import="util.process.LangGameProcessor" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	<link href="css/langGame.css" rel="stylesheet" type='text/css' >
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
	<style>
	  .canvas {
	    width: 100%;
	    height: 400px;
	    background-color: white;
	    border-radius: 15px;
	    box-shadow: 0 4px 6px rgba(50, 50, 93, 0.11), 0 1px 3px rgba(0, 0, 0, 0.08);
	  }
	</style>
	<title><%= gameID %>번 문항 직접 평가</title>
</head>
<body>
<%@ include file="sidebar.jsp" %>

<div class="w3-row">
	<!-- 시작 알림 모달 -->
	<div id="start-modal" class="w3-modal" style="display:block;">
	  <div class="w3-modal-content w3-animate-opacity w3-round-large" style="position:absolute;top:50%;left:50%;transform:translate(-50%, -50%);text-align: center; padding: 40px;width:90%;max-width:500px;">
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
		<div class="w3-container w3-round-large w3-padding" 
		     style="border:1px solid #12192C; font-size : 1.3em">
			<div id="content-text" class="w3-container w3-padding-32"><%=langGameContent%></div>
			
			<!-- 캔버스 영역 -->
			<div id="inner-canvas" class="w3-panel w3-round-large" style="background-color: #f6f9fc;">
			  <div style="margin-top: 10px;">
			    <div class="w3-right">
			      <button class="w3-button w3-circle" style="background-color:#D9D9D9;transform:translateY(-0.5em);width:2.3em;height:2.3em;padding:0;margin-top:0.5em;" onclick="removePainting();">
			        <img src="./image/reload.png" style="width:1.5em;height:1.5em;">
			      </button>
			    </div>
			    <div class="w3-left">사물의 이름을 써보세요.</div>
			  </div>
			  <div id="canvas-container" style="width:100%;">
			    <canvas id="jsCanvas" class="canvas"></canvas>
			  </div>
			</div>
		
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
						<audio id="answer-audio" controls style="display:none;">
							<source src=""/>
						</audio>
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
	    content: "<%= langGameList.get(j).getLangGameContent().replace("\"", "\\\"").replace("\r","").replace("\n","\\n") %>",
	    voiceUrl: "<%= langGameList.get(j).getLangGameVoice() %>",
	    hint: "<%= langGameList.get(j).getLangGameHint() != null ? langGameList.get(j).getLangGameHint().replace("\"","\\\"").replace("\r","").replace("\n","\\n") : "" %>",
	    hintVoice: "<%= langGameList.get(j).getLangGameHintVoice() != null ? langGameList.get(j).getLangGameHintVoice() : "" %>",
	    answer: "<%= langGameList.get(j).getLangGameAnswer() != null ? langGameList.get(j).getLangGameAnswer().replace("\"","\\\"").replace("\r","").replace("\n","\\n") : "" %>",
	    answerVoice: "<%= langGameList.get(j).getLangGameAnswerVoice() != null ? langGameList.get(j).getLangGameAnswerVoice() : "" %>",
	    criteria: "<%= langGameList.get(j).getLangGameCriteria() != null ? langGameList.get(j).getLangGameCriteria().replace("\"","\\\"").replace("\r","").replace("\n","\\n") : "" %>"
	  }<%= j < langGameList.size() - 1 ? "," : "" %>
	  <% } %>
	];

// 변수 초기화
	const game_id = <%= gameID %>;
	let current_index = <%= i %>;
	let audio_ended_next_flag = <%=audioEndedNextFlag%>; // 오디오 자동 재생 여부
	
	// 통합된 langGame.js 초기화
	initializeLangGame(game_data, game_id, current_index, audio_ended_next_flag);
	
	// 캔버스 초기화
	document.addEventListener("DOMContentLoaded", function () {
	    if (typeof initCanvas === 'function') {
	      initCanvas();
	    }
	});
</script>
<script type="text/javascript" src="js/canvas.js" charset="UTF-8"></script>
</body>
</html>