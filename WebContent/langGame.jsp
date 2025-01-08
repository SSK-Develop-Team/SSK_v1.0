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
	<title><%= gameID %>번 문항 직접 평가</title>
</head>
<body>
<%@ include file="sidebar.jsp" %>

<div class="w3-row">
	<div class="w3-col w3-hide-small m1 l3">&nbsp;</div>
	<div class="w3-col w3-container s12 m10 l6">
		<div style="font-size:1em;font-weight:bold;">직접평가 #<%= gameID %></div>
		<div><img src="<%=currLangGameElement.getLangGameImg() %>" style="width:100%"/></div>
		<% if(currLangGameElement.getLangGameSpeaker().equals("-")) { %><div class="w3-col m2 l3">&nbsp;</div><%} %>
		<% if(! currLangGameElement.getLangGameSpeaker().equals("-")){%><div class="w3-container w3-round-large" style="background-color:#12192C; color:white; width:100px;text-align:center;padding:2px;"><%=currLangGameElement.getLangGameSpeaker() %></div><%} %>
		

			<% if(langGameContent.equals("-")) { %>
			<div class="w3-container w3-round-large w3-padding">
				<div class="w3-col m2 l3">&nbsp;</div><%} %>
			<% if(! langGameContent.equals("-")) { %>
			<div class="w3-container w3-round-large w3-padding" style="border:1px solid #12192C;">
				<div class="w3-container w3-padding-32"><%=langGameContent%></div><%} %>
			<div class="w3-container w3-right">
				<%if(i>0){%><button class="w3-button" onclick="getPrevContent(<%=i%>);" style="border:none; background-color:#FFFFFF;"> &lt; 이전</button><%} %>
				<%if(i<=langGameList.size()-1){ %><button class="w3-button" onclick="getNextContent(<%=i%>, <%= gameID %>,<%=langGameList.size()%>);" style="border:none; background-color:#FFFFFF;">다음 &gt; </button><%}%>
			</div>
		</div>
		<div class="w3-left" style="margin-top:5px;">
			<%if(currLangGameElement.getLangGameHint()!=null||currLangGameElement.getLangGameHintVoice()!=null){ %>
			<button class="w3-button w3-round-large" onclick="openHint();" style="background-color:#12192C; color:white; text-align:center;font-size:0.9em;margin-right:5px;">힌트 확인하기</button>
			<div id="hint-modal" class="w3-modal">
				<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
					<div class="w3-container w3-center">
						<span onclick="closeHint();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
						<%if(currLangGameElement.getLangGameHint()!=null){ %>
						<p><%=currLangGameElement.getLangGameHint() %></p>
						<%}%>
					</div>
				</div>
			</div>
			<%} %>
			<%if(currLangGameElement.getLangGameAnswer()!=null||currLangGameElement.getLangGameAnswerVoice()!=null){ %>
			<button class="w3-button w3-round-large" onclick="openAnswer();" style="background-color:#12192C; color:white; text-align:center;font-size:0.9em;margin-right:5px;">정답 확인하기</button>
			<div id="answer-modal" class="w3-modal">
				<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
					<div class="w3-container w3-center">
						<span onclick="closeAnswer();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
						<%if(currLangGameElement.getLangGameAnswer()!=null){ %>
						<p><%=currLangGameElement.getLangGameAnswer() %></p>
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
		</div>
	</div>
	<div class="w3-col w3-hide-small m1 l3">&nbsp;</div>
</div>
</div>
</body>
<script>
	window.onload = function () {
		var audio = new Audio();
		audio.src="<%=langGameList.get(i).getLangGameVoice()%>";
		if(<%=audioEndedNextFlag%>==1){
			audio.addEventListener("ended",function(){
				setTimeout(() => getNextContent(<%=i%>,<%=gameID%>,<%=langGameList.size()%>),2000);
			});
		}
		audio.load();
		audio.play();
	}
</script>
<script type="text/javascript" src="js/langGame.js" charset="UTF-8"></script>
</html>