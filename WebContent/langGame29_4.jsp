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
  <link href="css/langGame.css" rel="stylesheet" type='text/css' >
  <%
    User currUser = (User)session.getAttribute("currUser");
    int gameID = (int)session.getAttribute("langGameID");
    int i = (int)session.getAttribute("currLangGameIndex");
    ArrayList<LangGame> langGameList = (ArrayList<LangGame>)session.getAttribute("currLangGameList");//size
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
      <div class="w3-left">자신의 이름을 써보세요.</div>
    </div>
    <div id="canvas-container" style="width:100%;height:400px;">
      <canvas id="jsCanvas" class="canvas"></canvas>
    </div>
    <div class="w3-container w3-right">
      <%if(i>0){%><button class="w3-button" onclick="getPrevContent(<%=i%>);" style="border:none; background-color:#FFFFFF;"> &lt; 이전</button><%} %>
      <%if(i<=langGameList.size()-1){ %><button class="w3-button" onclick="getNextContent(<%=i%>, <%= gameID %>,<%=langGameList.size()%>);" style="border:none; background-color:#FFFFFF;">다음 &gt; </button><%}%>
    </div>
    <div class="w3-left" style="margin-top:5px;">
      <button class="w3-button w3-round-large" onclick="openHint();" style="background-color:#12192C; color:white; text-align:center;font-size:0.9em;margin-right:5px;">힌트 확인하기</button>
      <div id="hint-modal" class="w3-modal">
        <div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
          <div class="w3-container w3-center">
            <span onclick="closeHint();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
            <p>아이가 자신의 이름을 쓸 수 있나요?<br>
              한 글자라도 쓸 수 있나요?<br>
              직접 연필을 주고 종이에 써보게 하세요.</p>
          </div>
        </div>
      </div>
      <button class="w3-button w3-round-large" onclick="openAnswer();" style="background-color:#12192C; color:white; text-align:center;font-size:0.9em;margin-right:5px;">정답 기준 보기</button>
      <div id="answer-modal" class="w3-modal">
        <div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
          <div class="w3-container w3-center">
            <span onclick="closeAnswer();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
            <p>자신의 이름 중 한 글자 이상 쓸 수 있다.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="w3-col w3-hide-small m1 l3">&nbsp;</div>
</div>
<script type="text/javascript" src="js/langGame.js" charset="UTF-8"></script>
<script type="text/javascript" src="js/canvas.js" charset="UTF-8"></script>
</body>
</html>