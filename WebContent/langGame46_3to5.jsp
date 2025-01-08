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
  <style>

    .canvas {
      width: 100%;
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
  <div class="w3-col w3-container s12 m10 l6">
    <div style="font-size:1em;font-weight:bold;">직접평가 #<%= gameID %></div>
    <div><img src="<%=currLangGameElement.getLangGameImg() %>" style="width:100%"/></div>
    <% if(currLangGameElement.getLangGameSpeaker().equals("-")) { %><div class="w3-col m2 l3">&nbsp;</div><%} %>
    <% if(! currLangGameElement.getLangGameSpeaker().equals("-")){%><div class="w3-container w3-round-large" style="background-color:#12192C; color:white; width:100px;text-align:center;padding:2px;"><%=currLangGameElement.getLangGameSpeaker() %></div><%} %>
    <div class="w3-container w3-round-large w3-padding" style="border:1px solid #12192C;">
      <div class="w3-container w3-padding-32"><%=langGameContent%></div>
      <div class="w3-panel w3-round-large" style="background-color: #f6f9fc;">
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
      <div>
        <div class="w3-container w3-right">
          <%if(i>0){%><button class="w3-button" onclick="getPrevContent(<%=i%>);" style="border:none; background-color:#FFFFFF;"> &lt; 이전</button><%} %>
          <%if(i<=langGameList.size()-1){ %><button class="w3-button" onclick="getNextContent(<%=i%>, <%= gameID %>,<%=langGameList.size()%>);" style="border:none; background-color:#FFFFFF;">다음 &gt; </button><%}%>
        </div>
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
    audio.play();
  }
</script>
<script type="text/javascript" src="js/langGame.js" charset="UTF-8"></script>
<script type="text/javascript" src="js/canvas.js" charset="UTF-8"></script>
</html>