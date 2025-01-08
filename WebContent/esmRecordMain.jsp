<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@page import="org.json.simple.JSONObject" %>
<%@page import="org.json.simple.JSONArray" %>
<%@page import="model.dto.EsmRecord" %>
<!DOCTYPE html>
<html>
<% 
	String currDateStr = (String)request.getAttribute("currDateStr");
	JSONObject eventsJsonObject = (JSONObject)request.getAttribute("eventsJsonObject");
	JSONArray eventsJsonArray = (JSONArray)eventsJsonObject.get("events");	
%>
	<head>
	<title>정서 다이어리</title>
	<link href='fullcalendar/main.css' rel='stylesheet' />
	<link href="css/esmRecord.css" rel="stylesheet" type='text/css' >
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
	
	<style>
	.selectedDate{
		background-color:#CAFFDC !important;
	}
	</style>
	
	<script src='fullcalendar/main.js'></script>
	<script src='fullcalendar/locales-all.js'></script>
	<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
	<script>
	document.addEventListener('DOMContentLoaded', function() {
		var calendarEl = document.getElementById('calendar');
		var calendar = new FullCalendar.Calendar(calendarEl, {
			headerToolbar: {
	        	left: 'prev,next',
	        	center: 'title',
	        	right: 'dayGridMonth'
	        
	    	},
	    	height: 500,
	    	locale: "ko",
	    	selectable: true,
	    	displayEventTime:false,
			timeZone: "local",
	  		dateClick: function(info) {//날짜 클릭 시 이벤트
	  			var days = document.querySelectorAll(".selectedDate");
	  		  	days.forEach(function(day) {
	  		   	 day.classList.remove("selectedDate");
	  		  	});
	  		  	info.dayEl.classList.add("selectedDate");
				setSelectedDateStrInput(info.dateStr);
	  		},
			events: <%=eventsJsonArray%>
		});
		calendar.render();
		calendar.gotoDate('<%=currDateStr%>');
	});
	
	function setSelectedDateStrInput(selectedDateStr){
		document.getElementById("selectedDateStrInput").setAttribute('value',selectedDateStr);
	}
	 function getDayEsmRecord(){/*해당 날짜의 ESM 기록을 가져오는 함수*/
		var form = document.createElement('form');
	  	form.setAttribute('method','post');
	  	form.setAttribute('action','GetDayEsmRecord');
	  	document.charset = "utf-8";
	  	var selectedDateStr = document.getElementById("selectedDateStrInput").value;
	  	var dateInput = document.createElement('input');
	  	dateInput.setAttribute('type','hidden');
	  	dateInput.setAttribute('name','selectedDateStr');
	  	dateInput.setAttribute('value',selectedDateStr);
	  	form.appendChild(dateInput);
	  	document.body.appendChild(form);
	  	form.submit();
	  }
	 function getCreateEsmRecordPage(){
		 var selectedDateStr = document.getElementById("selectedDateStrInput").value;
		 location.href="createEsmRecord.jsp?date="+selectedDateStr;
	 }
	 
	 
	</script>
</head>
<body>
<%@ include file="sidebar.jsp"%>
	<div class="w3-row">
		<div class="w3-col s1 m3 l4">&nbsp;</div>
		<div class="w3-col s10 m6 l4">
			<div style="font-size:1.3em;"><b>정서 다이어리</b></div>
			<div style="font-size:0.8em;">  날짜를 선택한 후 기록하기를 클릭하여 입력해주세요.</div>
			<div>&nbsp;</div>
			<div id="calendar-view">
				<div id='calendar'></div>
			</div>
			<div>&nbsp;</div>
			<button class="w3-button w3-col w3-padding"style="border:1px solid #1A2A3A;border-radius:10px;background-color:#1A2A3A;margin-bottom:10px;height:50px;color:white;font-size:1em;align-items : center;" onclick="getCreateEsmRecordPage()">기록하기</button>
			<button class="w3-button w3-col w3-padding"style="border:1px solid #1A2A3A;border-radius:10px;background-color:#1A2A3A;margin-bottom:10px;height:50px;color:white;font-size:1em;align-items : center;" onclick="getDayEsmRecord()">조회하기</button>
		</div>
		<div class="w3-col s1 m3 l4">&nbsp;</div>
	</div>
	
 	<input type="hidden" id="selectedDateStrInput" name="selectedDateStrInput" value="<%=currDateStr%>"/>
</body>
</html>