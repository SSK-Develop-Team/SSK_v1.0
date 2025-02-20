<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.dto.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
<style>
.sidebar{
	height:40px;
}
@media only screen and (max-width: 600px)  {
    .logo{display:none;}
}
</style>
</head>
<body>
<c:set var="user" scope="page" value="${sessionScope.currUser }" />
<!-- Sidebar -->
<div class="w3-sidebar w3-bar-block w3-card w3-animate-left" style="display:none;z-index:5;width:300px;background-color:#D9D9D9;font-size:1em;" id="mySidebar">
	<button class="w3-bar-item w3-button" onclick="w3_close()" style="display: flex;justify-content: right; ">&times;</button>
	<c:choose>
		<c:when test="${user.userRole eq 'ADMIN'}">
			<a href="GetAdminHome" class="w3-bar-item w3-button sidebar">&nbsp;전문가 계정 관리</a>
			<a href="GetManageChild" class="w3-bar-item w3-button sidebar">&nbsp;아동 계정 관리</a>
		</c:when>
		<c:when test="${user.userRole eq 'EXPERT'}">
			<a href="GetExpertHome" class="w3-bar-item w3-button sidebar">&nbsp;아동 결과 조회</a>
			<a href="GetManageChild" class="w3-bar-item w3-button sidebar">&nbsp;아동 계정 관리</a>
		</c:when>
		<c:when test="${user.userRole eq 'CHILD'}">
			<a href="childHome.jsp" class="w3-bar-item w3-button sidebar"><b>홈</b></a>
			<a href="GetLangTestMain" class="w3-bar-item w3-button sidebar">언어 발달 검사</a>
			<a href="sdqTestMain.jsp" class="w3-bar-item w3-button sidebar">정서/행동 발달 검사</a>
			<a href="esmTestMain.jsp" class="w3-bar-item w3-button sidebar">정서 반복 기록</a>
			<a href="GetEsmRecordMain" class="w3-bar-item w3-button sidebar">정서 다이어리</a>
		</c:when>
		<c:otherwise>
			<a href="login.jsp" class="w3-bar-item w3-button sidebar">로그인이 필요합니다. </a>
		</c:otherwise>
	</c:choose>
</div>

<!-- Overlay -->
<div class="w3-overlay" onclick="w3_close()" style="cursor:pointer" id="myOverlay"></div>

<!-- Page content -->

<div class="header">
		<div class="w3-bar">
		<button class="w3-button w3-xxlarge w3-left" onclick="w3_open()">&#9776;</button>
	  		<c:choose>
	  			<c:when test="${user eq null }">
					<button class="w3-bar-item w3-button w3-text-white w3-hover-text-black w3-padding-right w3-right w3-round-xlarge w3-margin-top" style="background-color:#1A2A3A;font-size:1rem;margin-right:20px;" onclick="location.href='login.jsp';">로그인</button>				
				</c:when>
				<c:when test="${user.userRole eq 'CHILD'}">
		  			<form id="f" name = "post"></form>
					<button class="w3-bar-item w3-button w3-text-white w3-hover-text-black w3-padding-right w3-right w3-round-xlarge w3-margin-top" style="background-color:#1A2A3A;font-size:1rem;margin-right:30px;" href="javascript:void(0);" onclick="doLogout();">로그아웃</button>
					<a class="w3-bar-item w3-button w3-hover-none w3-text-black w3-hover-text-blue w3-padding-right w3-right w3-margin-right w3-margin-top"style="font-size:1.1rem;" onclick="location.href='childHome.jsp'">아동 ${user.userName}&nbsp;님</a>
				</c:when>
				<c:when test="${user.userRole eq 'EXPERT'}">
		  			<form id="f" name = "post" ></form>
					<button class="w3-bar-item w3-button w3-text-white w3-hover-text-black w3-padding-right w3-right w3-round-xlarge w3-margin-top" style="background-color:#1A2A3A;font-size:1rem;margin-right:30px;" href="javascript:void(0);" onclick="doLogout();">로그아웃</button>
					<a class="w3-bar-item w3-button w3-hover-none w3-text-black w3-hover-text-blue w3-padding-right w3-right w3-margin-right w3-margin-top"style="font-size:1.1rem;" onclick="location.href='GetExpertHome'">${user.userName}&nbsp;님</a>
				</c:when>
	  			<c:otherwise>
		  			<form id="f" name = "post" ></form>
					<button class="w3-bar-item w3-button w3-text-white w3-hover-text-black w3-padding-right w3-right w3-round-xlarge w3-margin-top" style="background-color:#1A2A3A;font-size:1rem;margin-right:30px;" href="javascript:void(0);" onclick="doLogout();">로그아웃</button>
					<a class="w3-bar-item w3-button w3-hover-none w3-text-black w3-hover-text-blue w3-padding-right w3-right w3-margin-right w3-margin-top"style="font-size:1.1rem;" onclick="location.href='GetAdminHome'">${user.userName}&nbsp;님</a>
	  			</c:otherwise>
	  		</c:choose>
	  		<c:remove var="user" scope="page" />
	  		</div>
	  		<div style="margin-left:2em; margin-right:2em; transform:translateY(-1.5em);">
		  		<div class="logo w3-center" style="font-weight:bold; font-size:1.6rem;">
		  			<a href="childHome.jsp" style="text-decoration:none; color:ingerit;">PSLE</a>
		  		</div>
		  		<hr>
	  		</div>
	</div>
	<script type="text/javascript" src="js/logout.js" charset="UTF-8"></script>
<!-- JS to open and close sidebar with overlay effect -->
<script>
function w3_open() {
  document.getElementById("mySidebar").style.display = "block";
  document.getElementById("myOverlay").style.display = "block";
}

function w3_close() {
  document.getElementById("mySidebar").style.display = "none";
  document.getElementById("myOverlay").style.display = "none";
}
</script>
