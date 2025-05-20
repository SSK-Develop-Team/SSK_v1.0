<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" type="text/css" href="css/style.css">

<title>홈</title>
</head>
<body>
<%@ include file = "sidebar.jsp" %>

<div class="w3-row">
	<div class="w3-col s1 m2 l3">&nbsp;</div>
	<div class="w3-padding w3-col s10 m8 l6">
		<div style="width:100%;text-align:center;color:grey;font-size:1.4em;margin-bottom: 10px;">━━━━━━언어━━━━━━</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16 lang-button" onclick="location.href='GetLangTestMain'">언어 발달 검사</button>
		<div>&nbsp;</div>
		<div>&nbsp;</div>
		<div style="width:100%;text-align:center;color:grey;font-size:1.4em;margin-bottom: 10px;">━━━━━━정서━━━━━━</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16 sdq-button" onclick="location.href='sdqTestMain.jsp'">정서/행동 발달 검사</button>
		<div>&nbsp;</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16 sdq-button" onclick="location.href='esmTestMain.jsp'">정서 반복 기록</button>
		<div>&nbsp;</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16 sdq-button" onclick="location.href='GetEsmRecordMain'">정서 다이어리</button>
	</div>
	<div class="w3-col s1 m2 l3">&nbsp;</div>
</div>
</body>
</html>