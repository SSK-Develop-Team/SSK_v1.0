<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<% 
	EsmRecord currEsmRecord = (EsmRecord)request.getAttribute("currEsmRecord");
	String dateStr = currEsmRecord.getEsmRecordDate().toString();
	if(currEsmRecord.getEsmRecordTime()!=null){
		dateStr = dateStr+" "+currEsmRecord.getEsmRecordTime().toString();
	}
%>
	<head>
	<title>정서 다이어리 수정하기</title>
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<%@ include file="sidebar.jsp"%>
	<div class="w3-row">
		<div class="w3-col s1 m3 l4">&nbsp;</div>
		<div class="w3-col s10 m6 l4">
			<div style="font-size:1.3em;"><b>정서 다이어리 수정하기</b></div>
			<div>&nbsp;</div>
			<div style="float:right;font-size:0.8em;"><%=dateStr%></div>
			<div>&nbsp;</div>
			<form action="UpdateEsmRecord" method="post">
				<input type="hidden" name="esmRecordId" value="<%=currEsmRecord.getEsmRecordId()%>">
				<textarea name="esmRecordText" style="border:1px solid #1A2A3A;border-radius:10px;margin-bottom:10px;width:100%;height:30vh;font-size:1em;"><%=currEsmRecord.getEsmRecordText()%></textarea>
				<button class="w3-button w3-col w3-padding"style="border:1px solid #1A2A3A;border-radius:10px;background-color:#1A2A3A;margin-bottom:10px;height:50px;color:white;font-size:1em;align-items : center;">수정하기</button>
			</form>
			<button class="w3-button w3-col w3-padding"style="border:1px solid #1A2A3A;border-radius:10px;background-color:#1A2A3A;margin-bottom:10px;height:50px;color:white;font-size:1em;align-items : center;"onclick="history.go(-1);">돌아가기</button>
		</div>
		<div class="w3-col s1 m3 l4">&nbsp;</div>
	</div>

</body>
</html>