<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.dto.User" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>정서 반복 기록</title>
</head>
<body>
<%@ include file = "sidebar.jsp" %>
<% 
	User currUser = (User)session.getAttribute("currUser"); 
	String name = currUser.getUserName();
%>
<div style="width:100%;background-color:#FFDDDD;">
<div>&nbsp;</div><div>&nbsp;</div><div>&nbsp;</div>
<div class="w3-center" style="font-weight:bold;font-size:1.7em;"> 정서 반복 기록</div>
<div class="w3-panel" style="width:100%;height:150px;">
<div class="w3-hide-small">&nbsp;</div>
<div class="w3-center" style="text-align:center;font-size:1em;"><%=name %>(이)의 정서를 관찰할 시간입니다. <br>
			지금 이 시간, <%=name %>(이)의 정서를 하단의 기록하기 버튼을 눌러 입력해주세요.<br>
			시간에 따른 자녀의 정서 변화를 시각화된 결과 프로파일로 확인할 수 있습니다.<br></div>
</div>
<div>&nbsp;</div>
</div>
<div>&nbsp;</div><div>&nbsp;</div>
<div class="w3-row">
	<div class="w3-col s1 m3 l4">&nbsp;</div>
	<div class="w3-padding w3-col s10 m6 l4">
		<button class="w3-button w3-block w3-round-large w3-padding-16" style="background-color:#FF92A4;color:white;font-size:1.3em;" onclick="location.href='GetEsmTest?currEsmType=none'">기록하기</button>
		<div>&nbsp;</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16" style="background-color:#FF92A4;color:white;font-size:1.3em;" onclick="location.href='GetEsmTestProfileByTime'">결과 프로파일</button>
	</div>
	<div class="w3-col s1 m3 l4">&nbsp;</div>
</div>
<div style="position: fixed;  right: 2em; bottom: 2em;">
    <button class="w3-button w3-circle" style="background-color:#D9D9D9;width:2.7em;height:2.7em;padding:0;" onclick="location.href='childHome.jsp'">
		<img src="./image/home-icon.png" style="width:1.7em;height:1.7em;">
	</button>
</div>
</body>
</html>
</html>