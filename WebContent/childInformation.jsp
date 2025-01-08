<%@ page import="model.dto.User" %>	
<%@ page import="model.dto.EsmAlarm" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.sql.Time" %>

<!DOCTYPE html>
<html>
<head>
	<!--user (O):계정 수정, (X):계정 생성-->
	<c:set var="child" scope="page" value="${requestScope.child}"/>
	<c:set var="esmTime" scope="page" value="${requestScope.esmTime}"/>

	<title>계정 조회</title>

	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	<script src="https://kit.fontawesome.com/b2739fdad0.js" crossorigin="anonymous"></script>
	<style>
		 .info-row {
		    border: 1px solid #e0e0e0;
		    display: flex;
		  }
		.label{
			font-weight: bold;
			background-color: #f1f1f1;
		    padding: 1em;
		    flex: 0 0 30%;
		    text-align: right;
		    margin-right: 1em;
		}
		 .value {
		    padding: 1em;
		    overflow: hidden; /* 내용이 넘칠 경우 숨김 */
		  }
	</style>
</head>
<%
	String childId = request.getParameter("childId");
%>
<body onLoad="regFrm.userId.focus()">
 	<div class="header w3-padding-16 w3-center w3-container w3-margin">
		<h2><b>아동 계정 정보</b></h2>
 	</div>
 	<hr>
 	<br>
	<div class="w3-row" style="height:100%;, width:100%;">
		<div class="w3-col s1 m2 l4">&nbsp;</div>
		<div class="w3-col s10 m8 l4 w3-padding-large">
			<div class="info-row">
			    <label class="label">아이디</label>
			    <span class="value">${child.userLoginId}</span>
		  	</div>
		  	<div class="info-row">
			    <label class="label">아동 이름</label>
			    <span class="value">${child.userName}</span>
		  	</div>
		  	<div class="info-row">
			    <label class="label">생년월일</label>
			    <span class="value">${child.userBirth}</span>
		  	</div>
		  	<div class="info-row">
			    <label class="label">이메일</label>
			    <span class="value">${child.userEmail}</span>
		  	</div>
			<c:choose>
				<c:when test="${child.userGender eq 'male'}">
					<div class="info-row">
						<label class="label">성별</label>
			    		<span class="value">남자</span>
					</div>	
				</c:when>
				<c:when test="${child.userGender eq 'female'}">
					<div class="info-row">
						<label class="label">성별</label>
			    		<span class="value">여자</span>
					</div>
				</c:when>
			</c:choose>

			<div style="margin-bottom:2.5em;">
			<div class="info-row">
				<label class="label">정서 반복 <span style="white-space: nowrap;">기록</span><br>설정 시간</label>
				 <!-- esmTime이라는 이름으로 전달된 ArrayList를 받음 -->

				 <% ArrayList<EsmAlarm> esmTime = (ArrayList<EsmAlarm>) request.getAttribute("esmTime"); %>
				<c:choose>
				    <c:when test="${empty esmTime}">
				        <!-- esmTime이 비어있을 때의 처리 -->
				        <span class="value">
				        <table class="w3-table" style="font-size:0.8em;">
									<tbody id="table_body">
									<tr>
						        		<th style="padding-left:0">NO.</th>
								        <th>시작</th>
										<th>종료</th>
										<th>간격</th>
						        	</tr>
										<tr>
										<!-- Add a hidden input field for alarmId --> 
				            				<input type="hidden" name="alarmId" value="0"/>
				            				<td style="padding-left:0">[1]</td>	
											<td>9시</td>
											<td>21시</td>
											<td>3시간</td>
										</tr>
									</tbody>
				        	
									</table>
								</span> 
				        
				    </c:when>
				     <c:otherwise>
				     <!-- esmTime이 값이 있을 때의 처리 -->
				      

				        <span class="value">         
				        <table class="w3-table" style="font-size:0.8em;">
					        <tbody id="table_body">
					        <%if(esmTime.size()!=0){%>
					        	<tr>
					        		<th style="padding-left:0">NO.</th>
							        <th>시작</th>
									<th>종료</th>
									<th>간격</th>
					        	</tr>
									<%for (int i =0;i<esmTime.size();i++){
									%>
								
										<tr>
										<!-- Add a hidden input field for alarmId -->
										<input type="hidden" name="alarmId" value="<%=esmTime.get(i).getAlarmId() %>" />
										 <td style="padding-left:0">[<%=(i+1)%>]</td>
											<td><%=esmTime.get(i).getAlarmStart().getHours()%>시</td>
											<td><%=esmTime.get(i).getAlarmEnd().getHours() %>시</td>
											<td><%=esmTime.get(i).getAlarmInterval() %>시간</td>
										</tr>
											
										<% }
										}%>
					        	</tbody>
							</table>
						</span>      
				 </c:otherwise>
				</c:choose>
			</div>
			</div>
			
			
			<div class="w3-margin-top w3-left">
				<div class="w3-button" style="color:white;background-color:#51459E;" onclick="history.go(-1);" > 뒤로가기 </div>
			</div>
			
			<div class="w3-margin-top w3-right">
				<button class="w3-button w3-right" style="background-color:#51459E; color:white;" onclick="deleteChild(<%=childId%>)">삭제하기</button>
				<button class="w3-button w3-right" style="background-color:#51459E; color:white;margin-right:1.5em;" onclick="updateChild(<%=childId%>)">수정하기</button>
			</div>
	</div>
	<div class="w3-col s1 m2 l4">&nbsp;</div>
</div>
<script>
	function deleteChild(childId){
		// Delete user form 만들기 
		// 해당 Controller에 맞는 응답 형식으로 보내주기 (childId로 설정. text 형식으로 보내주어야함. );
		//폼 제출
		var deleteFrm = document.createElement("form");
		deleteFrm.setAttribute("method","post");
		deleteFrm.setAttribute("action","DeleteUser");
		var childIdInput= document.createElement("input");
		childIdInput.setAttribute("type", "text");
		childIdInput.setAttribute("name", "childId");
		childIdInput.setAttribute("value", childId);
		deleteFrm.appendChild(childIdInput);

		document.body.appendChild(deleteFrm);		
		deleteFrm.submit();
	}
	function updateChild(childId){
		// GetUpdateUser form 만들기 
		var updatefrm = document.createElement("form");
		updatefrm.setAttribute("method","post");
		updatefrm.setAttribute("action","GetUpdateUser");
		// 해당 Controller에 맞는 응답 형식으로 보내주기 (latestChildId)
		var childIdInput= document.createElement("input");
		childIdInput.setAttribute("type", "hidden");
		childIdInput.setAttribute("name", "latestChildId");
		childIdInput.setAttribute("value", childId);
		updatefrm.appendChild(childIdInput);
		//폼 제출
		document.body.appendChild(updatefrm);		
		updatefrm.submit();
	}
</script>

</body>
</html>