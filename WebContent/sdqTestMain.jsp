<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.dto.User" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" type="text/css" href="css/style.css">

<title>정서/행동 발달 검사</title>
</head>

<body>
<%@ include file = "sidebar.jsp" %>

<div style="width:100%;background-color:#FFDDDD;">
	<div>&nbsp;</div><div>&nbsp;</div><div class="w3-hide-small">&nbsp;</div>
	<div class="w3-center test-main-title"> 정서/행동 발달 검사 </div>
	<div class="w3-panel" style="width:100%;height:150px;">
		<div class="w3-hide-small">&nbsp;</div>
		<div class="w3-center test-main-description">본 검사는 자녀의 정서/행동 특성을 평가하는 검사입니다. <br>
			총 25문항에 응답해 주시면 자녀의 정서/행동 상태를 확인할 수 있습니다.<br></div>
	</div>
</div>

<div>&nbsp;</div><div>&nbsp;</div>
<div class="w3-row">
	<div class="w3-col s1 m3 l4">&nbsp;</div>
	<div class="w3-padding w3-col s10 m6 l4">
		<button class="w3-button w3-block w3-round-large w3-padding-16 sdq-button" 
				onclick="location.href='sdqTargetChoice.jsp'">검사하기</button>
		<div>&nbsp;</div>
		
		<button class="w3-button w3-block w3-round-large w3-padding-16 sdq-button" 
				onclick="location.href='GetSdqResultAll'">결과보기</button>
	</div>
	<div class="w3-col s1 m3 l4">&nbsp;</div>
</div>

<div style="position: fixed; right: 2em; bottom: 2em;">
    <button class="w3-button w3-circle" style="background-color:#D9D9D9;width:2.7em;height:2.7em;padding:0;" onclick="location.href='childHome.jsp'">
		<img src="./image/home-icon.png" style="width:1.7em;height:1.7em;">
	</button>
</div>
</body>
</html>
</html>