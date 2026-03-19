<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="model.dto.User" %>
<%@ page import="java.util.ArrayList" %>

<% 
	// 로그인 유저 확인
	User currUser = (User) session.getAttribute("currUser");
	if (currUser == null) {
	    response.sendRedirect(request.getContextPath() + "/login.jsp");
	    return;
	}
	String name = currUser.getUserName();	
	AgeGroup currAgeGroup = (AgeGroup)session.getAttribute("currAgeGroup");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" type="text/css" href="css/style.css?v=1">

<title>맞춤형 동화책</title>
</head>

<body>
<%@ include file = "sidebar.jsp" %>

<div style="width:100%;background-color:#DDEDED;">
	<div>&nbsp;</div><div>&nbsp;</div><div class="w3-hide-small">&nbsp;</div>
	<div class="w3-center test-main-title"> 맞춤형 동화책 </div>
	<div class="w3-panel" style="width:100%;height:150px;">
		<div class="w3-hide-small">&nbsp;</div>
		<div class="w3-center test-main-description">
			본 서비스는 아동의 취향, 고민과 언어·정서 검사 결과를 바탕으로,<br>
			아이에게 꼭 맞는 동화책을 생성하고, 부모 가이드를 함께 제공합니다.<br>
		</div>
	</div>
</div>

<div>&nbsp;</div><div>&nbsp;</div>
<div class="w3-row">
	<div class="w3-col s1 m3 l4">&nbsp;</div>
	<div class="w3-padding w3-col s10 m6 l4">

		<button class="w3-button w3-block w3-round-large w3-padding-16 book-button"	onClick="location.href='GetChatBot'">시작하기</button> 
		
		<div>&nbsp;</div>

		<button class="w3-button w3-block w3-round-large w3-padding-16 book-button" onclick="location.href='GetBookShelf'">내 책장 보기</button>		
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