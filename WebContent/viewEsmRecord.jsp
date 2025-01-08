<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@page import="org.json.simple.JSONObject" %>
<%@page import="org.json.simple.JSONArray" %>
<%@page import="model.dto.EsmRecord" %>
<!DOCTYPE html>
<html>
<% 
	User currUser = (User)session.getAttribute("currUser");
	User focusUser = (User)request.getAttribute("focusUser");
	String currDateStr = (String)request.getAttribute("currDateStr");
	ArrayList<EsmRecord> currEsmRecordList = (ArrayList<EsmRecord>)request.getAttribute("currEsmRecordList");
	JSONObject eventsJsonObject = (JSONObject)request.getAttribute("eventsJsonObject");
	JSONArray eventsJsonArray = (JSONArray)eventsJsonObject.get("events");
	int childId = focusUser.getUserId();//child인 경우 본인, 다른 role인 경우 선택한 child
%>
	<head>
	<title>정서 다이어리</title>
	<link href='fullcalendar/main.css' rel='stylesheet' />
	<link href="css/esmRecord.css" rel="stylesheet" type='text/css' >
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
	
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
		    	getDayEsmRecord('GetDayEsmRecord',info.dateStr, <%=childId%>);//해당 날짜 문자열 반환 -> 해당하는 텍스트 데이터 받아오기
			},
			events: <%=eventsJsonArray%>
		});
		calendar.render();
		calendar.gotoDate('<%=currDateStr%>');
	});
	 function getDayEsmRecord(url, date, childId){/*해당 날짜의 ESM 기록을 가져오는 함수*/
		var form = document.createElement('form');
	  	form.setAttribute('method','post');
	  	form.setAttribute('action',url);
	  	document.charset = "utf-8";
	  	var dateInput = document.createElement('input');
	  	dateInput.setAttribute('type','hidden');
	  	dateInput.setAttribute('name','selectedDateStr');
	  	dateInput.setAttribute('value',date);
	  	form.appendChild(dateInput);
	  	var childInput = document.createElement('input');
	  	childInput.setAttribute('type','hidden');
	  	childInput.setAttribute('name','childId');
	  	childInput.setAttribute('value',childId);
	  	form.appendChild(childInput);
	  	document.body.appendChild(form);
	  	form.submit();
	  }
	 function getPreDayEsmRecord(url, date, childId){
		 const preDate = new Date(date);
		 preDate.setDate(preDate.getDate()-1);
		 var preDateStr = preDate.toISOString().slice(0, 10);
		 getDayEsmRecord(url, preDateStr, childId);
	 }
	 function getNextDayEsmRecord(url, date, childId){
		 const nextDate = new Date(date);
		 nextDate.setDate(nextDate.getDate()+1);
		 var nextDateStr = nextDate.toISOString().slice(0, 10);
		 getDayEsmRecord(url, nextDateStr, childId);
	 }
	 
	</script>
</head>
<body>
<%@ include file="sidebar.jsp"%>
	<div class="w3-row">
		<div class="w3-col s1 m1 l1">&nbsp;</div>
		<div class="w3-col s10 m10 l10">
			<div style="font-size:1.3em;"><b>정서 다이어리 조회하기</b></div>
			<div style="font-size:0.8em;"><%=focusUser.getUserName()%>님의 다이어리입니다.</div>
			<div>&nbsp;</div>
		 	<div class="w3-row">
				<div id='calendar-view' class="w3-hide-small w3-half w3-container ">
					<div id='calendar'></div>
				</div>
				<div id='record-view' class="w3-half">
				<div class="records-date w3-panel w3-row w3-center">
						<div class="w3-col s3 w3-center" >
							<button class="w3-button w3-circle w3-white w3-left" id="pre_btn" onclick="getPreDayEsmRecord('GetDayEsmRecord','<%=currDateStr%>',<%=childId%>);"><img src="./image/previous_w.png" alt="pre_btn" style="width:1em;height:auto;filter: brightness(0.3);"></button>
						</div>
						<div class="w3-col s6 w3-center" style="font: 1.5em sans-serif;font-weight:bold;" ><%=currDateStr%></div>
						<div class="w3-col s3 w3-center">
							<button class="w3-button w3-circle w3-white w3-right" id="next_btn"onclick="getNextDayEsmRecord('GetDayEsmRecord','<%=currDateStr%>',<%=childId%>);"><img src="./image/next_w.png" alt="next_btn" style="width:1em;height:auto;filter: brightness(0.3);"></button>
						</div>
					</div>
					<hr>
					
					<%if(currEsmRecordList.size()>0){%>
					<div class="record-container" style="height:80%;">
						<%
						for(int i=0;i<currEsmRecordList.size();i++){ %>
						<div class="record-box w3-panel w3-border w3-round-large">
							<%if(!currUser.getUserRole().equals("CHILD")){ %>
								<div class="record-time"style="text-align:right;font-size:0.8em;margin-top:0.5em;">&nbsp;</div>
							<%
							}else if(currEsmRecordList.get(i).getEsmRecordTime() == null) {%>
								<div class="record-time"style="text-align:right;font-size:0.8em;margin-top:0.5em;">
								<button class="w3-button" style="background-color:white;font-size:0.9em;padding:0px;width:2em;" onclick="location.href='GetUpdateEsmRecord?esmRecordId=<%=currEsmRecordList.get(i).getEsmRecordId()%>';">수정</button>  <span style="font-size:0.5em;font-weight:100;">|</span>
								<button class="w3-button" style="background-color:white;font-size:0.9em;padding:0px;width:2em;"onclick="deleteEsmRecord(<%=currEsmRecordList.get(i).getEsmRecordId()%>);">삭제</button></div>
							<%}else{ %>
								<div class="record-time"style="text-align:right;font-size:0.8em;margin-top:0.5em;"><%=currEsmRecordList.get(i).getEsmRecordTime()%>&nbsp;
								<button class="w3-button" style="background-color:white;font-size:0.9em;padding:0px;width:2em;" onclick="location.href='GetUpdateEsmRecord?esmRecordId=<%=currEsmRecordList.get(i).getEsmRecordId()%>';">수정</button>  <span style="font-size:0.5em;font-weight:100;">|</span>
								<button class="w3-button" style="background-color:white;font-size:0.9em;padding:0px;width:2em;"onclick="deleteEsmRecord(<%=currEsmRecordList.get(i).getEsmRecordId()%>);">삭제</button></div>
							<%} %>
							<div class="record-text"style="font-size:1em;min-height:7vh;width:100%;word-break:break-all;margin-bottom:1em;"><%=currEsmRecordList.get(i).getEsmRecordText()%></div>
						</div>
						<%}%>
						</div>
					<%}else{ %>
					<div class="w3-center">
						<div>해당 일자의 기록이 없습니다.</div>
					</div>
				<%} %>
				<div>&nbsp;</div>
				<%if(currUser.getUserRole().equals("CHILD")){ %>
					<button class="w3-button w3-col w3-padding w3-center"style="border:1px solid #1A2A3A;width:90%;border-radius:10px;background-color:#1A2A3A;margin-bottom:10px;margin-left:5%;margin-right:5%;height:50px;color:white;font-size:1em;align-items : center;" onclick="location.href='GetEsmRecordMain'">돌아가기</button>
				<%}else{ %>
					<button class="w3-button w3-col w3-padding w3-center"style="border:1px solid #1A2A3A;width:90%;border-radius:10px;background-color:#1A2A3A;margin-bottom:10px;margin-left:5%;margin-right:5%;height:50px;color:white;font-size:1em;align-items : center;" onclick="location.href='GoToChildHome?childId=<%=focusUser.getUserId()%>'">돌아가기</button>
				<%} %>
				</div>
		 	</div>
		 	<div>&nbsp;</div>
		 	
		</div>
		<div class="w3-col s1 m1 l1">&nbsp;</div>
	</div>
 	<script>
 	function deleteEsmRecord(esmRecordId){
 		if (confirm("해당 정서 다이어리를 삭제하시겠습니까?")) {
            location.href="DeleteEsmRecord?esmRecordId="+esmRecordId;
        }
 	}
 	</script>
</body>
</html>
