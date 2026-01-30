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
<style>
.section-title {
  display: flex;
  align-items: center;
  text-align: center;
  color: grey;
  font-size: 1.4em;
  margin: 20px 0;
}

.section-title::before,
.section-title::after {
  content: "";
  flex: 1;
  border-bottom: 1px solid grey;
  margin: 0 10px;
}
</style>

<body>
<%@ include file = "sidebar.jsp" %>

<div class="w3-row">
	<div class="w3-col s1 m2 l3">&nbsp;</div>
	<div class="w3-padding w3-col s10 m8 l6">
		<div class="section-title">언어</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16 lang-button" onclick="location.href='GetLangTestMain'">언어 발달 검사</button>
		<div>&nbsp;</div>
		<div>&nbsp;</div>
		<div class="section-title">정서</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16 sdq-button" onclick="location.href='sdqTestMain.jsp'">정서/행동 발달 검사</button>
		<div>&nbsp;</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16 sdq-button" onclick="location.href='esmTestMain.jsp'">정서 반복 기록</button>
		<div>&nbsp;</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16 sdq-button" onclick="location.href='GetEsmRecordMain'">정서 다이어리</button>
		<div>&nbsp;</div>
		<div>&nbsp;</div>
		<div class="section-title">You Tube</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16" 
		        style="background-color:#FAE3C6; border:none;" 
		        onclick="window.open('http://www.youtube.com/@말랑마음', '_blank')">
		    <div style="display:flex; align-items:center; justify-content:center; gap:15px;">
		        <img src="image/Youtube_icon.png" alt="말랑마음 YouTube" 
		             style="width:50px; height:50px; border-radius:50%; box-shadow:0 2px 6px rgba(0,0,0,0.15); background-color:white;">
		        <span style="font-size:1.2em; font-weight:bold; color:#E6735C;">말랑마음 YouTube</span>
		    </div>
		</button>
		
		
	</div>
	<div class="w3-col s1 m2 l3">&nbsp;</div>
</div>
</body>
</html>