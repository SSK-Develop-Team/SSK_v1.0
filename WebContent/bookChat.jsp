<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="model.dto.User" %>
    
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
  <link rel="stylesheet" type="text/css" href="css/style.css?v=1">

  <!-- Markdown render + sanitize -->
  <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/dompurify@3.0.6/dist/purify.min.js"></script>

	<%
	  User currUser = (User)session.getAttribute("currUser");
	  String bookUserName = (String)session.getAttribute("bookUserName");
	  if (bookUserName == null || bookUserName.trim().isEmpty()) {
	    bookUserName = (currUser != null) ? currUser.getUserName() : "아이";
	  }
	  
	  String initialAssistant = (String)request.getAttribute("initialAssistant");
	  if (initialAssistant == null || initialAssistant.trim().isEmpty()) {
	    initialAssistant = "안녕하세요! " + bookUserName + " 아동에게 딱 맞는 동화를 만들어 드릴게요. 함께 시작해 볼까요?";
	  }

	%>
	
	<title>동화 챗봇</title>
	
  <style>
    /* typing dots */
    .msg.typing { display: inline-flex; align-items: center; gap: 10px; }
    .typing-dots { display: inline-flex; gap: 4px; }
    .typing-dots span {
      width: 6px; height: 6px; border-radius: 50%;
      background: #999; opacity: 0.35;
      animation: dotPulse 1.2s infinite;
    }
    .typing-dots span:nth-child(2) { animation-delay: 0.15s; }
    .typing-dots span:nth-child(3) { animation-delay: 0.30s; }
    @keyframes dotPulse {
      0%, 80%, 100% { transform: translateY(0); opacity: 0.35; }
      40% { transform: translateY(-4px); opacity: 0.9; }
    }
    .typing-text { color: #777; font-size: 0.98em; }
  </style>
</head>

<body>
<%@ include file="sidebar.jsp" %>

<div class="w3-row">
	<div class="w3-col s1 m1 l2">
		<button class="w3-button" type="button" onclick="history.back()" style="border:none; background: none; cursor: pointer;">
			<img src="./image/bookChat-left-arrow.png" alt="이전 페이지" style="width: 40px; height: auto;"/>
		</button>
	</div>
	
<!-- Chat area (assistant/user bubbles) -->
	<div class="w3-col w3-container s10 m10 l8 chatArea">
		<div class="chat-wrap">
		  <div id="chatList" class="chat-list">
			  <div class="msg assistant"><%= initialAssistant %></div>
		  </div>
		</div>
	</div>
	
	
	<div class="w3-col s1 m1 l2">&nbsp;</div>
</div>

<div class="chat-input-bar">
  <div class="chat-input-inner">
    <textarea id="msgInput" class="chat-input" rows="1"
      placeholder="메시지를 입력하세요" autocomplete="off"></textarea>
    <button id="sendBtn" class="send-btn" type="button" onclick="sendMessage()">➤</button>
  </div>
</div>

<!-- 이미지 생성중 로딩 모달 -->
<div id="loadingModal" class="w3-modal" style="display:none; z-index:9999;">
  <div class="w3-modal-content w3-animate-opacity w3-round-xxlarge" style="width: 80vw; background:transparent; box-shadow:none;">
    <div class="w3-row-padding" style="padding-top:24px; padding-bottom:24px;">

      <div class="w3-col l3 m2 s1">&nbsp;</div>

      <div class="w3-col l6 m8 s10">
        <div class="w3-card w3-white w3-round-large w3-center" style="border-radius:32px; padding: 18px;">
          <img id="loadingImg" src="./image/book_loading_1.gif" style="width:100%" alt="loading" />
        </div>
      </div>

      <div class="w3-col l3 m2 s1">&nbsp;</div>
    </div>
  </div>
</div>

<script>
  // JSP contextPath를 JS로 전달
  window.__CTX__ = "<%= request.getContextPath() %>";
  window.__THREAD_ID__ = "<%= (String)request.getAttribute("threadId") %>";
</script>
<script src="js/bookChat.js?v=3" charset="UTF-8"></script>
</body>
</html>
