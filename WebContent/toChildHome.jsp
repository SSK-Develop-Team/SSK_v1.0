<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>아동 결과 조회</title>
</head>
<body>
<%@ include file = "sidebar.jsp" %>
<%
	User selectedChild = (User)request.getAttribute("selectedChild");
%>

<div>&nbsp;</div>
<div class="w3-row">
	<div class="w3-col s1 m3 l4">&nbsp;</div>
	<div class="w3-padding w3-col s10 m6 l4">
		<div class="w3-margin w3-center">
			<span style="font-weight:bold;font-size:1.8em;">아동 <%=selectedChild.getUserName()%></span>&nbsp;
			<span><%=selectedChild.getUserBirth()%></span>&nbsp;&nbsp;
			<button class="w3-button w3-circle" style="background-color:#D9D9D9;transform:translateY(-0.5em);width:2.3em;height:2.3em;padding:0;margin-top:0.5em;" onclick="location.href='ExportChildResultExcel?childId=<%=selectedChild.getUserId()%>'">
				<img src="./image/printing.png" style="width:1.5em;height:1.5em;">
			</button>
			<button class="w3-button w3-circle" style="background-color:#D9D9D9;transform:translateY(-0.5em);width:2.3em;height:2.3em;padding:0;margin-top:0.5em;" onclick="location.href='GetExpertHome'">
				<img src="./image/turn-back.png" style="width:1.5em;height:1.5em;">
			</button>
		</div>
		<div>&nbsp;</div>
		<div style="width:100%;text-align:center;color:grey;">━━━━━━━━언어━━━━━━━━</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16" style="background-color:#51459E;color:white;font-size:1.3em;" onclick="location.href='GetLangResultAll?childId=<%=selectedChild.getUserId()%>'">언어 발달 검사 결과</button>
		<div>&nbsp;</div>
		<div>&nbsp;</div>
		<div style="width:100%;text-align:center;color:grey;">━━━━━━━━정서━━━━━━━━</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16" style="background-color:#FF92A4;color:white;font-size:1.3em;" onclick="location.href='GetSdqResultAll?childId=<%=selectedChild.getUserId()%>'">정서/행동 발달 검사 결과</button>
		<div>&nbsp;</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16" style="background-color:#FF92A4;color:white;font-size:1.3em;" onclick="location.href='GetEsmTestProfileByTime?childId=<%=selectedChild.getUserId()%>'">정서 반복 기록</button>
		<div>&nbsp;</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16" style="background-color:#FF92A4;color:white;font-size:1.3em;" onclick="location.href='GetDayEsmRecord?childId=<%=selectedChild.getUserId()%>'">정서 다이어리</button>
	</div>
	<div class="w3-col s1 m3 l4">&nbsp;</div>
</div>
</body>
</html>