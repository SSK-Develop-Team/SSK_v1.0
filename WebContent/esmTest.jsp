<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
%>
<%@ page import="model.dto.User" %>
<%@ page import="model.dto.SdqTestLog" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.ArrayList" %>      
<%
	ArrayList<EsmEmotion> esmEmotionList = (ArrayList<EsmEmotion>)request.getAttribute("esmEmotionList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>정서 반복 기록</title>
<style>
[type="radio"]:checked {
	border: 0.4em solid #ff6666;
}

[type="radio"]:hover {
	box-shadow: 0 0 0 max(4px, 0.2em) #ff9e9b;
	cursor: pointer;
}

[type="radio"] {
	vertical-align: middle;
	appearance: none;
	border: max(2px, 0.1em) solid gray;
	border-radius: 50%;
	width: 1.25em;
	height: 1.25em;
	transition: border 0.5s ease-in-out;
}
.esmBtn{
	margin-right:0.2em;
	border:1px solid #ff6666;
	border-radius : 10px;
	background-color:#ff6666;
	color : white;
	height:50px;
	font-size:1em;
	align-items:center;
	text-align:center;
}
</style>

</head>
<body>
	<%@ include file="sidebar.jsp" %>
	<div class="w3-container w3-center"><h4><b>정서 반복 기록</b></h4></div>
	<div class="w3-row" >
		<div class="w3-col m3 l4">&nbsp;</div>
		<div class="w3-col s12 m6 l4" id="sdqChat" style="padding-bottom:6px;padding-top:10px;">
			<form id="esmForm" method="post" action="GetEsmTest">
			<input type="hidden" id="currEsmType" name="currEsmType" value="<%=esmEmotionList.get(0).getEsmType()%>"/>
			<%for(int i=0;i<esmEmotionList.size();i++){ %>
				<div class="w3-center">
					<div class=""><%=esmEmotionList.get(i).getEsmEmotionKr() %></div>
					<div class=""><%=esmEmotionList.get(i).getEsmEmotion() %></div>
					<div class="radio">
						<input type="radio" class="w3-radio" style="width:1.25em; height:1.25em; margin-right:0.25em; margin-left:0.25em;" name="<%=esmEmotionList.get(i).getEsmEmotion() %>" value="1" checked>
						<input type="radio" class="w3-radio" style="width:1.75em; height:1.75em; margin-right:0.25em; margin-left:0.25em;" name="<%=esmEmotionList.get(i).getEsmEmotion() %>" value="2">
						<input type="radio" class="w3-radio" style="width:2.25em; height:2.25em; margin-right:0.25em; margin-left:0.25em;" name="<%=esmEmotionList.get(i).getEsmEmotion() %>" value="3">
						<input type="radio" class="w3-radio" style="width:2.75em; height:2.75em; margin-right:0.25em; margin-left:0.25em;" name="<%=esmEmotionList.get(i).getEsmEmotion() %>" value="4">
						<input type="radio" class="w3-radio" style="width:3.25em; height:3.25em; margin-right:0.25em; margin-left:0.25em;" name="<%=esmEmotionList.get(i).getEsmEmotion() %>" value="5">
					</div>
				</div>
				<div>&nbsp;</div>
				<div>&nbsp;</div>
			<%} %>
			</form>
			<div class="w3-row">
				<div class="w3-col s1 m1 l3">&nbsp;</div>
				<%if(esmEmotionList.get(0).getEsmType().equals("positive")){ %>
				<button class="w3-col s5 m5 l3 esmBtn" onclick="location.href='esmTestMain.jsp';">메인으로</button>
				<button class="w3-col s5 m5 l3 esmBtn" onclick="document.getElementById('esmForm').submit();">다음으로</button>
				<%}else{ %>
				<button class="w3-col s5 m5 l3 esmBtn" onclick="getPreviousEsmEmotion();">이전으로</button>
				<button class="w3-col s5 m5 l3 esmBtn" onclick="document.getElementById('esmForm').submit();">제출하기</button>
				<%} %>
			</div>
		</div>
		<div class="w3-col s1 m1 l4">&nbsp;</div>
	</div>
	<div>&nbsp;</div>
	<div>&nbsp;</div>
<script>
function getPreviousEsmEmotion(){
	document.getElementById("currEsmType").value="none"; 
	document.getElementById("esmForm").submit();
}
</script>
</body>
</html>