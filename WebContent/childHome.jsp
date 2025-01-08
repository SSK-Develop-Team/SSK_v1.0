<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>홈</title>
</head>
<body>
<%@ include file = "sidebar.jsp" %>

<div class="w3-row">
	<div class="w3-col s1 m3 l4">&nbsp;</div>
	<div class="w3-padding w3-col s10 m6 l4">
		<div style="width:100%;text-align:center;color:grey;">━━━━━━언어━━━━━━</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16" style="background-color:#51459E;color:white;font-size:1.3em;" onclick="location.href='GetLangTestMain'">언어 발달 검사</button>
		<div>&nbsp;</div>
		<div>&nbsp;</div>
		<div style="width:100%;text-align:center;color:grey;">━━━━━━정서━━━━━━</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16" style="background-color:#FF92A4;color:white;font-size:1.3em;" onclick="location.href='sdqTestMain.jsp'">정서/행동 발달 검사</button>
		<div>&nbsp;</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16" style="background-color:#FF92A4;color:white;font-size:1.3em;" onclick="location.href='esmTestMain.jsp'">정서 반복 기록</button>
		<div>&nbsp;</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16" style="background-color:#FF92A4;color:white;font-size:1.3em;" onclick="location.href='GetEsmRecordMain'">정서 다이어리</button>
	</div>
	<div class="w3-col s1 m3 l4">&nbsp;</div>
</div>
</body>
</html>