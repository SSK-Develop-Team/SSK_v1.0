<%@ page import="model.dto.User" %>	
<%@ page import="model.dto.EsmAlarm" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<!--user (O):계정 수정, (X):계정 생성-->
	<c:set var="user" scope="page" value="${requestScope.user}"/>
	<c:set var="esmTime" scope="page" value="${requestScope.esmTime}"/>
	<c:choose>
		<c:when test="${user ne null}">
			<title>계정 수정</title>
		</c:when>
		<c:when test="${user eq null}">
			<title>계정 생성</title>
		</c:when>
	</c:choose>

	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	<script src="https://kit.fontawesome.com/b2739fdad0.js" crossorigin="anonymous"></script>
	<style>
		.field-icon {
			float: right;
			padding-right:1.75em;
			margin-top: -25px;
			position: relative;
			z-index: 2;
		}
	</style>
</head>

<c:set var="role" scope="page" value="${param.role}"/>
<body onLoad="regFrm.userId.focus()">
 	<div class="header w3-padding-16 w3-center w3-container w3-margin">
 	<c:choose>
	    <c:when test="${role eq 'expert'&& user eq null}">
			<h2><b>전문가 계정 생성</b></h2>
	    </c:when>
	    <c:when test="${role eq 'child' && user eq null}">
			<h2><b>아동 계정 생성</b></h2>
	    </c:when>
		<c:when test="${role eq 'expert'&& user ne null}">
			<h2><b>전문가 계정 수정</b></h2>
		</c:when>
		<c:when test="${role eq 'child' && user ne null}">
			<h2><b>아동 계정 수정</b></h2>
		</c:when>
	</c:choose>
 	</div>
 	<hr>
 	<br>
	<div class="w3-row" style="height:100%;, width:100%;">
		<div class="w3-col s1 m2 l4">&nbsp;</div>
		<div class="w3-col s10 m8 l4 w3-padding-large w3-light-gray ">
			<c:choose>
				<c:when test="${role eq 'expert'&& user eq null}">
					<form name="regFrm" method="post" id="frm" action="DoRegister" class="w3-container" onsubmit="return checkValueforExpert();">
			    </c:when>
			    <c:when test="${role eq 'child' && user eq null}">
					<form name="regFrm" method="post" id="frm" action="DoRegister" class="w3-container" onsubmit="return checkValueforChild();">
			    </c:when>
				<c:when test="${role eq 'expert'&& user ne null}">
					<form name="regFrm" method="post" id="frm" action="UpdateUser" class="w3-container" onsubmit="return checkValueforExpert();">
				</c:when>
				<c:when test="${role eq 'child' && user ne null}">
					<form name="regFrm" method="post" id="frm" action="UpdateUser" class="w3-container" onsubmit="return checkValueforChild();">
				</c:when>
			</c:choose>

			<div class="w3-margin-top">
				<label><span style="color:red; margin-left:-5px;">*</span>아이디</label>
				<div class="w3-row">
					<c:choose>
						<c:when test="${user eq null}">
							<input type="text" class="w3-input w3-threequarter" id="userId" name="userId" placeholder="ID" required>
							<input type="hidden" id="originUserLoginId" name="originUserLoginId" value="${user.userLoginId}">
							<input type="button" class="w3-quarter w3-input w3-button" id="checkId" style="color:white;background-color:#51459E;" value="중복확인">
						</c:when>
						<c:when test="${user ne null}">
							<input type="text" class="w3-input w3-threequarter" id="userId" name="userId" placeholder="ID" value="${user.userLoginId}" required>
							<input type="hidden" id="originUserId" name="originUserId" value="${user.userId}">
							<input type="hidden" id="originUserLoginId" name="originUserLoginId" value="${user.userLoginId}">
							<input type="button" class="w3-quarter w3-input w3-button" id="checkId" style="color:white;background-color:#51459E;" value="중복확인">
						</c:when>
					</c:choose>
				</div>
				<span id="check_id_m" class="msg"></span>
			</div>
			<div class="w3-margin-top">
				<label><span style="color:red; margin-left:-5px;">*</span>패스워드</label>
				<c:choose>
					<c:when test="${user eq null}">
						<input type="password" class="w3-input" id="userPw" name="userPw" placeholder="Password" required>
					</c:when>
					<c:when test="${user ne null}">
						<input type="password" class="w3-input" id="userPw" name="userPw" value="${user.userPassword}" placeholder="Password" required>
					</c:when>
				</c:choose>
				<span class="fa fa-fw fa-eye field-icon toggle-password" id="pwToggleIcon" onclick="togglePasswordType()"></span>
			</div>
			<div class="w3-margin-top">
				<label><span style="color:red; margin-left:-5px;">*</span>패스워드 확인</label>
				<input type="password" class="w3-input" id="userPwChk" name="userPwChk" placeholder="Password Check" required>
				<span id="check_pw_m" class="msg"></span>
			</div>
			<div class="w3-margin-top">
				<c:choose>
				    <c:when test="${role eq 'expert'}">
						<label><span style="color:red; margin-left:-5px;">*</span>전문가 이름</label>
						<input type="hidden" id="userRole" name="userRole" value="EXPERT" required>
				    </c:when>
				    <c:when test="${role eq 'child'}">
						<label><span style="color:red; margin-left:-5px;">*</span>아동 이름</label>
						<input type="hidden" id="userRole" name="userRole" value="CHILD" required>
				    </c:when>
				</c:choose>
				<c:choose>
					<c:when test="${user eq null}">
						<input type="text" class="w3-input" id="userName" name="userName" placeholder="Name" required>
					</c:when>
					<c:when test="${user ne null}">
						<input type="text" class="w3-input" id="userName" name="userName" value="${user.userName}" placeholder="Name" required>
					</c:when>
				</c:choose>
				<span id="check_name_m" class="msg"></span>
			</div>
			<c:choose>
			    <c:when test="${role eq 'child'}">
					<div class="w3-margin-top">
						<div><span style="color:red; margin-left:-5px;">*</span>성별</div>
						<c:choose>
							<c:when test="${user eq null}">
								<label>남</label>
								<input type="radio" class="w3-radio" name="userGender" value="male" required>
								<label>여</label>
								<input type="radio" class="w3-radio" name="userGender" value="female" required>
							</c:when>
							<c:when test="${user.userGender eq 'male'}">
								<label>남</label>
								<input type="radio" class="w3-radio" name="userGender" value="male" checked required>
								<label>여</label>
								<input type="radio" class="w3-radio" name="userGender" value="female" required>
							</c:when>
							<c:when test="${user.userGender eq 'female'}">
								<label>남</label>
								<input type="radio" class="w3-radio" name="userGender" value="male" required>
								<label>여</label>
								<input type="radio" class="w3-radio" name="userGender" value="female" checked required>
							</c:when>
						</c:choose>

					</div>
					<div class="w3-margin-top">
						<label><span style="color:red; margin-left:-5px;">*</span>생년월일</label>
						<c:choose>
							<c:when test="${user eq null}">
								<input type="date" class="w3-input" name="userBirth" required>
							</c:when>
							<c:when test="${user ne null}">
								<input type="date" class="w3-input" name="userBirth" value="${user.userBirth}" required>
							</c:when>
						</c:choose>
					</div>
					<div class="w3-margin-top">
						<div><span style="color:red; margin-left:-5px;">*</span>정서 반복 기록 설정</div>
				 		<!-- esmTime이라는 이름으로 전달된 ArrayList를 받음 -->
						 <% ArrayList<EsmAlarm> esmTime = (ArrayList<EsmAlarm>) request.getAttribute("esmTime"); %>
						<c:choose>
					    	<c:when test="${empty esmTime}">
					        	<!-- esmTime이 비어있을 때의 처리 -->
					        	<div class="w3-container" style="padding:0;">
					        		<table class="w3-table" style="font-size:0.8em;">
										<tbody class="table_body">
											<tr>
										        <th style="padding-left:0;">시작</th>
												<th>종료</th>
												<th>간격</th>
								        	</tr>
											<tr><td style="padding-left:0;padding-top:0;"><input type="time" class="w3-input alarmStart" name="alarmStart" value="09:00" placeholder="Start Time"></td>
												<td style="padding-top:0;"><input type="time" class="w3-input alarmEnd" name="alarmEnd" value="21:00" placeholder="End Time"></td>
												<td style="padding-top:0;"><input type="text" class="w3-input alarmInterval" style="min-width: 25px;" name="alarmInterval" value="3" placeholder="Interval"></td>
												<td style="padding-top:0; width: 12%; padding-right:0;"><input type='button' class="w3-bar w3-gray" style="height: 34px;" value='-' onclick='deleteRow(this)' /></td>
											</tr>
										</tbody>
					        	
										</table>
									</div>
					        
					    	</c:when>
					     	<c:otherwise>
						     	<!-- esmTime이 값이 있을 때의 처리 -->
						        <div class="w3-container" style="padding:0;">
							  		<div class="w3-responsive">
								        <table class="w3-table" style="font-size:0.8em; max-width: 100%;">
									        <tbody id="table_body">
										        <tr>
											        <th style="padding-left:0;">시작</th>
													<th>종료</th>
													<th>간격</th>
									        	</tr>
										        <%if(esmTime.size()!=0){
													for (int i =0;i<esmTime.size();i++){%>
													<tr>
														<td style="padding-left:0;padding-top:0;"><input type="time" class="w3-input alarmStart" name="alarmStart" value="<%=esmTime.get(i).getAlarmStart() %>" placeholder="Start Time"></td>
														<td style="padding-top:0;"><input type="time" class="w3-input alarmEnd" name="alarmEnd" value="<%=esmTime.get(i).getAlarmEnd() %>" placeholder="End Time"></td>
														<td style="padding-top:0;"><input type="text" class="w3-input alarmInterval" style="min-width: 25px;" name="alarmInterval" value="<%=esmTime.get(i).getAlarmInterval() %>" placeholder="Interval"></td>
														<td style="padding-top:0; width: 12%; padding-right:0;"><input type='button' class="w3-bar w3-gray" style="height: 34px;" value='-' onclick='deleteRow(this)' /></td>
													</tr>
												<% }
												}%>
									        </tbody>
										</table>
									</div>
						       </div>
					 		</c:otherwise>
						</c:choose>
						<div class="check_alarm_m""></div>
						<div class="w3-margin-top">
							<input type='button' class="w3-bar w3-gray" style="height:40px;" value='행추가' onclick="add_tr('table_body')" />
						</div>	
					</div>
			    </c:when>
			    <c:otherwise>
				</c:otherwise>
			</c:choose>
			<div class="w3-margin-top">
				<label><span style="color:red; margin-left:-5px;">*</span>이메일</label>
				<input type="text" class="w3-input" id="userEmail" name="userEmail" value="${user.userEmail}" placeholder="Email">
				<span id="check_email_m" class="msg"></span>
			</div>
			
			

			<div class="w3-margin-top w3-left">
				<div class="w3-button" style="color:white;background-color:#51459E;" onclick="history.go(-1);" > 뒤로가기 </div>
			</div>
			
			<div class="w3-margin-top w3-right">
				<c:choose>
					<c:when test="${user ne null}">
						<input type="submit" class="w3-button" style="color:white;background-color:#51459E;" value="수정하기"/>
					</c:when>
					<c:when test="${user eq null}">
						<input type="submit" class="w3-button" style="color:white;background-color:#51459E;" value="회원가입"/>
					</c:when>
				</c:choose>
			</div>
			</form>
		</div>
		<div class="w3-col s1 m2 l4">&nbsp;</div>
	</div>
	<script type="text/javascript" src="./js/checkregister.js"></script>

</body>
</html>