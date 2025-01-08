<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="model.dto.User" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
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
	%>
  <title><%= gameID %>번 문항 직접 평가</title>
  <style>
    body {
      background-color: #f6f9fc;
    }

    .canvas {
      width: 100%;
      height: 400px;
      background-color: white;
      border-radius: 15px;
      box-shadow: 0 4px 6px rgba(50, 50, 93, 0.11), 0 1px 3px rgba(0, 0, 0, 0.08);
    }
  </style>
</head>
<body>
<%@ include file="sidebar.jsp" %>

<div class="w3-row">
  <div class="w3-col w3-hide-small m1 l3">&nbsp;</div>
  <div class="w3-col w3-container s12 m10 l6" >
    <div style="font-size:1em;font-weight:bold;">직접평가 #<%= gameID %></div>
    <div class="w3-container">
      <div class="w3-right">
        <button class="w3-button w3-circle" style="background-color:#D9D9D9;transform:translateY(-0.5em);width:2.3em;height:2.3em;padding:0;margin-top:0.5em;" onclick="removePainting();">
          <img src="./image/reload.png" style="width:1.5em;height:1.5em;">
        </button>
      </div>
      <%if(gameID==64 && currLangGameElement.getLangGameOrder()==3) { %>
		<div class="w3-left">'할머니'를 써보세요.</div>
	  <%}else if(gameID==64 && currLangGameElement.getLangGameOrder()==4) { %>
		<div class="w3-left">'미나'를 써보세요.</div>
	  <%}else if(gameID==31 && currLangGameElement.getLangGameOrder()==8) {%>
	  	<div class="w3-left">이름을 써보세요.</div>
	  <%} %>
    </div>
    <div id="canvas-container" style="width:100%;height:400px;">
      <canvas id="jsCanvas" class="canvas"></canvas>
    </div>
    <div class="w3-container w3-right">
      <button class="w3-button w3-round-large" onclick="history.go(-1);" style="border:none; background-color:#12192C; color:white; ">이전으로</button>
      <%if(i<=langGameList.size()-1){ %><button class="w3-button w3-round-large" onclick="getNextContent(<%=i%>, <%= gameID %>,<%=langGameList.size()%>);" style="border:none; background-color:#12192C; color:white; ">완료 </button><%}%>
    </div>
  </div>
  <div class="w3-col w3-hide-small m1 l3">&nbsp;</div>
</div>
<script type="text/javascript" src="js/langGame.js" charset="UTF-8"></script>
<script type="text/javascript" src="js/canvas.js" charset="UTF-8"></script>
</body>
</html>