<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>정서 반복 기록</title>

<style>
@media (max-width: 500px){
	.target-btn{
		height:100px;
	}
}
@media (min-width: 501px){
	.target-btn{
		height:200px;
	}
}
</style>

</head>
<body>
<%@ include file = "sidebar.jsp" %>

<div class="w3-row w3-panel w3-center" style="padding:4vw;"> 
	<div class="w3-center" style="font-size:1.8em; font-weight:bold;">정서 반복 기록</div>
</div>

<div class="w3-row" style="height:250px;">
	<div class="w3-panel w3-half w3-row">
		<div class="w3-col s1 m4 l7">&nbsp;</div>
		<button class="w3-button w3-round-large w3-col s10 m7 l4 target-btn" 
				style="background-color:#FF92A4; color:white; font-size: 1.3em; " 
				onclick="getEsmTest('PARENT')">부모용 검사 <br>(3~10세)
		</button>
		<div class="w3-col s1 m1 l1">&nbsp;</div>
	</div>
	
	<div class="w3-panel w3-half w3-row">
		<div class="w3-col s1 m1 l1">&nbsp;</div>
		<button class="w3-button w3-round-large w3-col s10 m7 l4 target-btn"
				style="background-color:#FF92A4; color:white; font-size: 1.3em;"
				onclick="getEsmTest('CHILD')">아동용 검사<br>(11세 이상)
		</button>
		<div class="w3-col s1 m4 l7">&nbsp;</div>
	</div>
</div>
</body>

<script>
function getEsmTest(target){
	var form = document.createElement("form");
	form.method = "POST";
	form.action = "GetEsmTest";	
	
	var targetElement = document.createElement("input")
	targetElement.type = "hidden";
	targetElement.name = "target";
	targetElement.value = target;
	
	var typeElement = document.createElement("input")
	typeElement.type = "hidden";
	typeElement.name = "currEsmType";
	typeElement.value = 'none';
	
	form.appendChild(targetElement);
	form.appendChild(typeElement);
	
	document.body.appendChild(form);
	form.submit();
}

</script>

</html>