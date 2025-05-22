<!-- 이름쓰기 버튼 출력-->
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
		.container {
			position: relative;
		}
		.container .btn{
			position: absolute;
			top: 48%;
			left: 68%;
			transform: translate(-50%, -25%);
			-ms-transform: translate(-50%, -25%);
			background-color: #555;
			color: white;
			font-size: 1.4rem;;
			padding: 0.6rem 1.2rem;
			border: none;
			cursor: pointer;
			border-radius: 5px;
			text-align: center;
		}
	</style>
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
		
		<div class="container">
			<!-- 직접평가 이미지 -->
			<img src="<%=currLangGameElement.getLangGameImg() %>.jpg" style="width:100%"/>
			
			<!-- 글자 쓰기 버튼 -->
			<button class="btn w3-button" onclick="location.href='langGameCanvas.jsp';">글자 쓰기</button>
		</div>
		
		<!-- 직접평가 화자 -->
		<% if(currLangGameElement.getLangGameSpeaker().equals("-")) { %>
			<div class="w3-col m2 l3">&nbsp;</div><%} %>
		<% if(! currLangGameElement.getLangGameSpeaker().equals("-")){%>
			<div class="w3-container w3-round-large" style="background-color:#12192C; color:white; width:100px;text-align:center;font-size:1.1em;padding:2px;">
			<%=currLangGameElement.getLangGameSpeaker() %></div><%} %>
			
		<!-- 직접평가 컨텐츠 -->
		<div class="w3-container w3-round-large w3-padding lang-game-content" style="border:1px solid #12192C; font-size : 1.3em">
			<div class="w3-container w3-padding-32"><%=langGameContent%></div>
			
			<!-- 이전/다음 버튼 -->
			<div class="w3-container w3-right">
				<%if(i>0){%>
					<button class="w3-button" onclick="getPrevContent(<%=i%>);" style="border:none; background-color:#FFFFFF;"> 
						&lt; 이전
					</button>
				<%} %>
				
		        <!-- 다시듣기 버튼 -->
		        <% if (langGameList.get(i).getLangGameVoice()!= null & audioEndedNextFlag != 1) { %>
		            <button class="w3-button" onclick="audioReplay()" style="border:none;"> 
		            	<img src="./image/reload.png" alt="다시듣기" style="width: 25px; height: auto;">
		            </button>
		        <% } %>
				
				<%if(i<=langGameList.size()-1){ %>
					<button class="w3-button" onclick="getNextContent(<%=i%>, <%= gameID %>,<%=langGameList.size()%>);" style="border:none; background-color:#FFFFFF;">
						다음 &gt; 
					</button>
				<%}%>
			</div>
		</div>
		
		<div class="w3-left" style="margin-top:5px;">
			<!-- 팁 버튼 -->
			<%if(currLangGameElement.getLangGameHint()!=null||currLangGameElement.getLangGameHintVoice()!=null){ %>
			<button class="w3-button w3-round-large" onclick="openHint();" style="background-color:#12192C; color:white; text-align:center;font-size:1.1em;margin-right:5px;">
				팁</button>
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
			<button class="w3-button w3-round-large" onclick="openAnswer();" style="background-color:#12192C; color:white; text-align:center;font-size:1.1em;margin-right:5px;">
				정답</button>
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
<script>
	var audio = new Audio();
	audio.src="<%=langGameList.get(i).getLangGameVoice()%>";
	
	window.onload = function () {
		if(<%=audioEndedNextFlag%>==1){
			audio.addEventListener("ended",function(){
				setTimeout(() => getNextContent(<%=i%>,<%=gameID%>,<%=langGameList.size()%>),2000);
			});
		}
		audio.play();
	}
	
	function audioReplay() {
		if (audio.paused) {
			audio.play();
		}
	}
</script>
<script type="text/javascript" src="js/langGame.js" charset="UTF-8"></script>
<script type="text/javascript" src="js/canvas.js" charset="UTF-8"></script>
</html>