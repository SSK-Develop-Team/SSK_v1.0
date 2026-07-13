<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="model.dto.User" %>

<%
    User currUser = (User) session.getAttribute("currUser");

    if (currUser == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String ctx = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" type="text/css" href="css/style.css?v=3">

<title>맞춤형 동화책</title>
</head>

<body>
<%@ include file = "sidebar.jsp" %>

<div class="w3-row">
	<div class="w3-col s1 m1 l2">
		<div class="w3-hide-small">&nbsp;</div>
		<button class="w3-button" type="button" onclick="history.back()" style="border:none; background: none; cursor: pointer;">
			<img src="./image/bookChat-left-arrow.png" alt="이전 페이지" style="width: 40px; height: auto;"/>
		</button>
	</div>

<div style="width:100%;background-color:#DDEDED;">
	<div>&nbsp;</div><div>&nbsp;</div><div class="w3-hide-small">&nbsp;</div><div class="w3-hide-small">&nbsp;</div>
	<div class="w3-center book-sub-title"> 동화책을 어떤 방식으로 볼지 선택해주세요. </div>
	<div class="w3-panel" style="width:100%;height:150px;">
		<div class="w3-hide-small">&nbsp;</div>
		<div class="w3-center test-main-description">
			"동화책 읽어주기"를 선택하면, 완성된 동화책을 음성이 담긴 영상으로 볼 수 있습니다.<br>
			"동화책 보기"를 선택하면, 완성된 동화책을 직접 읽을 수 있습니다.<br>
		</div>
	</div>
</div>

<div>&nbsp;</div><div>&nbsp;</div>
<div class="w3-row">
	<div class="w3-col s1 m3 l4">&nbsp;</div>
	<div class="w3-padding w3-col s10 m6 l4">

		<button class="w3-button w3-block w3-round-large w3-padding-16 book-button"	onClick="location.href='GetChatBot?readMode=listen'">동화책 읽어주기</button> 
		
		<div>&nbsp;</div>

		<button class="w3-button w3-block w3-round-large w3-padding-16 book-button" onclick="location.href='GetChatBot?readMode=read'">동화책 보기</button>		
	</div>
	<div class="w3-col s1 m3 l4">&nbsp;</div>
</div>

<div style="position: fixed; right: 2em; bottom: 2em;">
    <button class="w3-button w3-circle" style="background-color:#D9D9D9;width:2.7em;height:2.7em;padding:0;" onclick="location.href='childHome.jsp'">
		<img src="./image/home-icon.png" style="width:1.7em;height:1.7em;">
	</button>
</div>
</body>

<script type="text/javascript" src="js/selectAge.js" charset="UTF-8"></script>

</html>
</html>