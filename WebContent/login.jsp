<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<style>
input::placeholder {
  font-size: 18px;
}
input {
  font-size: 18px !important;
}
input:-webkit-autofill {
  font-size: 18px !important;
}
</style>


<meta charset="UTF-8">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>PSLE 로그인</title>
</head>
<body>
<div class="w3-row" style="margin-top:15vh;">
  <div class="w3-col s1">&nbsp;</div>
  <div class="w3-col s10 w3-center">
	<div class="w3-padding-large">
		<div class="w3-container w3-center" style="font-size:40px;font-weight:bold;">아동 <span style="color:#51459E;">언어</span> 및 <span style="color:#FF92A4;">정서 행동 발달</span> 평가</div>
		<p class="w3-container" style="margin-top: 10px; font-size:28px;color:#FFFFF;"><span style="font-weight:bold;">P</span>arent <span style="font-weight:bold;">S</span>creening of <span style="font-weight:bold;">L</span>anguage and <span style="font-weight:bold;">E</span>motion</p>
		<p class="w3-container" style="font-size:22px;color:#AEAEB2;">아동의 연령에 맞는 언어 및 정서 행동 발달 수준을 검사하고 평가합니다.</p>
		<div class="w3-row w3-center"  style="margin-top:7vh;">
			<div class="w3-col l2 m2">&nbsp;</div>
			<div class="w3-col l8 m8 w3-light-gray w3-round-large w3-padding">
				<form method="post" action="doLogin" class="w3-container">
					<div class="w3-margin-top w3-row">
						<label class="w3-quarter" style="font-size:22px;" >아이디</label>
						<input type="text" class="w3-input w3-threequarter" name="userid" placeholder="ID" > 
					</div>
					<div class="w3-margin-top w3-row">
						<label class="w3-quarter" style="font-size:22px;">비밀번호</label>
						<input type="password" class="w3-input w3-threequarter" name="userpw" placeholder="Password" ><br/>
					</div>
					<div class="w3-margin-top w3-row">
						<div class="w3-third w3-hide-small">&nbsp;</div>
						<div class="w3-third w3-hide-small">&nbsp;</div>
						<input class="w3-button w3-panel w3-third w3-round-large" type="submit" value="LOGIN"style="min-height:40px;color:white;background-color:#1A2A3A;" />
					</div>
				</form>
			</div>
			<div class="w3-col l2 m2">&nbsp;</div>
		</div>
	</div>
  </div>
  <div class="w3-col s1">&nbsp;</div>
</div>

</body>
</html>