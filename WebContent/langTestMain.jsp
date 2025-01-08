<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="model.dto.User" %>
<%@ page import="java.util.ArrayList" %>

<% 
	User currUser = (User)session.getAttribute("currUser"); 
	String name = currUser.getUserName();	
	AgeGroup currAgeGroup = (AgeGroup)session.getAttribute("currAgeGroup");
	ArrayList<AgeGroup> selectableAgeGroupList = (ArrayList<AgeGroup>)session.getAttribute("selectableAgeGroupList");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">

<title>언어 발달 평가</title>
</head>
<body>
<%@ include file = "sidebar.jsp" %>
<div style="width:100%;background-color:#D9D9D9;">
<div>&nbsp;</div><div>&nbsp;</div><div>&nbsp;</div>
<div class="w3-center" style="font-weight:bold;font-size:1.7em;"> 언어 발달 평가 </div>
<div class="w3-panel" style="width:100%;height:150px;">
<div class="w3-hide-small">&nbsp;</div>
<div class="w3-center" style="text-align:center;font-size:1em;"><%=name%>님, 현재 나이에 적합한 언어 발달 검사가 진행됩니다. <br>
			해당 언어 발달 검사가 아동에게 맞지 않는다면, 다른 나이대의 언어 발달 검사를 선택하여 진행해주세요.<br></div>
</div>
<div>&nbsp;</div>
</div>
<div>&nbsp;</div><div>&nbsp;</div>
<div class="w3-row">
	<div class="w3-col s1 m3 l4">&nbsp;</div>
	<div class="w3-padding w3-col s10 m6 l4">
		<button class="w3-button w3-block w3-round-large w3-padding-16" style="background-color:#51459E;color:white;font-size:1.3em;" onClick="document.getElementById('ageCheck').style.display='block';">검사하기</button>
		<div>&nbsp;</div>
		<button class="w3-button w3-block w3-round-large w3-padding-16" style="background-color:#51459E;color:white;font-size:1.3em;" onclick="location.href='GetLangResultAll'">결과보기</button>
	</div>
	<div class="w3-col s1 m3 l4">&nbsp;</div>
</div>
<div style="position: fixed; right: 2em; bottom: 2em;">
    <button class="w3-button w3-circle" style="background-color:#D9D9D9;width:2.7em;height:2.7em;padding:0;" onclick="location.href='childHome.jsp'">
		<img src="./image/home-icon.png" style="width:1.7em;height:1.7em;">
	</button>
</div>

<%
	
	String[] ageStr = new String[]{"3세 0개월 ~ 3세 3개월", "3세 4개월 ~ 3세 5개월", "3세 6개월 ~ 3세 8개월", "3세 9개월 ~ 3세 11개월", "4세 0개월 ~ 4세 3개월",
			"4세 4개월 ~ 4세 7개월", "4세 8개월 ~ 4세 11개월", "5세 0개월 ~ 5세 5개월", "5세 6개월 ~ 5세 11개월", "6세 0개월 ~ 6세 5개월", "6세 6개월 ~ 6세 11개월", 
			"7세 0개월 ~ 7세 11개월", "8세 0개월 ~ 8세 11개월", "9세 0개월 ~ 9세 11개월", ""};
	
	if(name.length() == 3){
		name = name.substring(1);
	}
	
	else if(name.length() == 4){
		name = name.substring(2);
	}
	
	char lastName = name.charAt(name.length() - 1);
	int index = (lastName - 0xAC00) % 28;
	if(index > 0) name = name + "이";
%>

	<!-- 연령 선택용 모달 -->
	<div id="ageCheck" class="w3-modal">
		<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
			<div class="w3-container w3-center">
				<span onclick="ageCheckClose()" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
				<%if(currAgeGroup.getAgeGroupId()<14){%>
					<p>현재 <%=name%>의 테스트 단계는 <span><%=currAgeGroup.getAgeGroupId()%></span>입니다.</p>
					<p>(<%=ageStr[currAgeGroup.getAgeGroupId()] %>)</p>
					<p>해당 단계를 진행하시겠습니까?</p>
					<button class="w3-button w3-padding-16 w3-margin w3-round-large" style="background-color:#4d4d4d;color:white;font-size:1.3em;"onClick="ageSelect()">다른 단계 진행</button>
					<button class="w3-button w3-padding-16 w3-margin w3-round-large" style="background-color:#51459E;color:white;font-size:1.3em;"onClick="location.href='GetLangTest'">해당 단계 진행</button>
				<%}else{%>
					<p>현재 <%=name%>님은 모든 테스트 단계를 진행할 수 있습니다.</p>
					<p>(<%=ageStr[13] %>)</p>
					<p>해당 단계를 진행하시겠습니까?</p>
					<button class="w3-button w3-padding-16 w3-margin w3-round-large" style="background-color:#4d4d4d;color:white;font-size:1.3em;"onClick="ageSelect()">다른 단계 진행</button>
					<button class="w3-button w3-padding-16 w3-margin w3-round-large" style="background-color:#51459E;color:white;font-size:1.3em;"onClick="location.href='GetLangTest'">해당 단계 진행</button>
				<%}%>
			</div>
		</div>
	</div>
	
	<form method="get" class = "selectModal" action="GetLangTest">
		<div id="ageSelect" class="w3-modal">
			<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
				<div class="w3-container w3-center">
					<span onclick="ageSelectClose()" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
					<p>평가를 진행할 단계를 선택해주세요.</p>

					<select id = "ageGroupSelect" name="ageGroup" style="font-size:1.3em;">
						<%for(AgeGroup a : selectableAgeGroupList){
							if(a.getAgeGroupId()==currAgeGroup.getAgeGroupId()){
						%>
								<option value="<%=currAgeGroup.getAgeGroupId()%>" selected="selected"><%=ageStr[currAgeGroup.getAgeGroupId()] %> (현재 단계)</option>
							<%}else{%>
								<option value="<%=a.getAgeGroupId()%>"><%=ageStr[a.getAgeGroupId()] %></option>
							<%}
						 }%>
					</select>

					<button class="w3-button w3-margin w3-round-large" style="background-color:#51459E;color:white;font-size:1.3em;"type="submit">확인</button>
				</div>
			</div>
		</div>
	</form>
	
</body>

<script src="https://code.jquery.com/jquery-3.6.0.slim.js"></script>
<script type="text/javascript" src="js/selectAge.js" charset="UTF-8"></script>

</html>