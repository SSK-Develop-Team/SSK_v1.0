<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="model.dto.User" %>
<%@ page import="java.sql.Date" %>
<%@ page import="java.sql.Time" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.dto.EsmReply" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>정서 반복 기록 시간별 그래프</title>
<%
	User currUser = (User)session.getAttribute("currUser");
	User focusUser = (User)request.getAttribute("focusUser");
	ArrayList<Date> esmTestDateList = (ArrayList<Date>)request.getAttribute("esmTestDateList");
	int selectedIndexOfEsmTestDateList = (int)request.getAttribute("selectedIndexOfEsmTestDateList"); 
	ArrayList<EsmTestLog> selectedDateEsmTestLogList = (ArrayList<EsmTestLog>)request.getAttribute("selectedEsmTestLogList");
	ArrayList<Integer> positiveList = (ArrayList<Integer>)request.getAttribute("positiveList");
	ArrayList<Integer> negativeList = (ArrayList<Integer>)request.getAttribute("negativeList");
%>
<script type="text/javascript" src="http://www.gstatic.com/charts/loader.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
<script type="text/javascript">
	$(window).resize(function(){
		drawChart();
    });
   google.charts.load('current', {'packages':['corechart']});
   google.charts.setOnLoadCallback(drawChart);
   
   function drawChart() {
	   var data = new google.visualization.DataTable();
       data.addColumn('timeofday', 'Time');
       data.addColumn('number', '긍정 감정');
       data.addColumn('number', '부정 감정');
       data.addRows([
    	   <% 
           for(int i = 0; i < selectedDateEsmTestLogList.size(); i++){
        	   LocalTime localTime = selectedDateEsmTestLogList.get(i).getEsmTestTime().toLocalTime();
	        %>
	        [[<%=localTime.getHour()%>,<%=localTime.getMinute()%>,<%=localTime.getSecond()%>],  <%=positiveList.get(i)%>, <%=negativeList.get(i)%>],
	       <%   
	           }
	        %>
       ]);
      
   
    var options = {
		annotations : {
       		alwaysOutside : true
       	},
        vAxis : {
            viewWindow : {
            	max : 25,
                min : 0
            },
        },
        hAxis: {
        	format: 'H시', 
        	ticks: [[7, 0, 0],[9, 0, 0],[12, 0, 0],  [15, 0, 0], [18, 0, 0], [21, 0, 0], [23, 0, 0]],
        	gridlines: {count: 6}
        },
        'legend' : 'none',
         series : {
         	0 : {color : '#66cc66'},
            1 : {color : '#ff6666'},
         },
         lineWidth : 5,
         pointSize : 10
     };
   
     var chart = new google.visualization.LineChart(document.getElementById('esmProfileChart'));
   
     chart.draw(data, options);
   }
</script>
</head>
<body>
	<%@ include file="sidebar.jsp"%>
   <!-- page title -->
   <div style="text-align:center;font-weight:bold;font-size:1.5em;margin-top:0.5em">정서 반복 기록 - 시간 별 그래프</div>
   <div style="text-align:center;font-size:1em;margin-top:0.5em"><%= focusUser.getUserName()%>님의 결과입니다.</div>
   <div>&nbsp;</div>
   
   <!-- 날짜 선택 -->
   <div class="w3-row">
		<div class="w3-col s1 m2 l4">&nbsp;</div>
		<c:set var="selectedIndex" scope="page" value="${requestScope.selectedIndexOfEsmTestDateList}" />
		<c:choose>
		<c:when test="${selectedIndex eq 0}">
			<div class="w3-col s1 m1 l1 w3-center"onclick="alert('이전 기록이 없습니다.');"><img src="./image/left-arrow.png" style="width:2.5em; opacity: 0.5;"/></div>
		</c:when>
		<c:otherwise>
			<div class="w3-col s1 m1 l1 w3-center" onclick="location.href='GetEsmTestProfileByTime?date=<%=esmTestDateList.get(selectedIndexOfEsmTestDateList-1).toString()%>&childId=<%=focusUser.getUserId()%>'"><img src="./image/left-arrow.png" style="width:2.5em;"/></div>
		</c:otherwise>
		</c:choose>
		<div class="w3-col s8 m6 l2" >
			<div class="w3-dropdown-hover"style="width:100%;">
			    <button class="w3-button"style="width:100%;background-color:#D9D9D9;"><%=selectedDateEsmTestLogList.get(0).getEsmTestDate().toString()%></button>
			    <div class="w3-dropdown-content w3-bar-block w3-border"style="width:100%;">
			      <%for(int i=0;i<esmTestDateList.size();i++){ %>
			      <a href="GetEsmTestProfileByTime?date=<%=esmTestDateList.get(i).toString()%>&childId=<%=focusUser.getUserId()%>" class="w3-bar-item w3-button"style="width:100%;"><%=esmTestDateList.get(i).toString()%></a>
			      <%} %>
			    </div>
		    </div>
		</div>
		<c:choose>
		<c:when test="${selectedIndex eq esmTestDateList.size()-1}">
			<div class="w3-col s1 m1 l1 w3-center"onclick="alert('다음 기록이 없습니다.');"><img src="./image/right-arrow.png" style="width:2.5em; opacity: 0.5;"/></div>
		</c:when>
		<c:otherwise>
			<div class="w3-col s1 m1 l1 w3-center"onclick="location.href='GetEsmTestProfileByTime?date=<%=esmTestDateList.get(selectedIndexOfEsmTestDateList+1).toString()%>&childId=<%=focusUser.getUserId()%>'"><img src="./image/right-arrow.png" style="width:2.5em;"/></div>
		</c:otherwise>
		</c:choose>
		<c:remove var="selectedIndex" scope="page"/>
		<div class="w3-col s1 m2 l4">&nbsp;</div>
   </div>
	
	<!-- 시간별 그래프 뷰 -->
	<div class="w3-row">
		<div class="w3-col m1 l1">&nbsp;</div>
		<div class="w3-col s12 m10 l10">
			<div id="esmProfileChart" style="width:100%;height: 60vh;"></div>
		</div>
		<div class="w3-col m1 l1">&nbsp;</div>
	</div>
	
	<!-- page redirect button -->
	<div class="w3-row">
		<div class="w3-col s2 m3 l3">&nbsp;</div>
		<div class="w3-col w3-row s4 m2 l2">
			<button class="w3-button w3-col w3-padding" style="border:1px solid #ff6666;border-radius:10px;background-color:#ff6666;margin-bottom:10px;height:50px;color:white;font-size:0.8em;align-items : center;padding:0px;"onclick="location.href='GetEsmTestProfileByDay?date=<%=selectedDateEsmTestLogList.get(0).getEsmTestDate().toString()%>&childId=<%=focusUser.getUserId()%>';">일별 그래프 보기</button>
		</div>
		<div class="w3-col s1 m3 l3">&nbsp;</div>
		<div class="w3-col w3-row s3 m1 l1">
		<%if(currUser.getUserRole().equals("CHILD")){ %>
			<button class="w3-button w3-col"style="border:1px solid #ff6666;border-radius:10px;background-color:#ff6666;margin-bottom:10px;height:50px;color:white;font-size:0.9em;align-items : center;padding:0px;"onclick="location.href='esmTestMain.jsp';">메인으로</button>
		<%}else{ %>
			<button class="w3-button w3-col"style="border:1px solid #ff6666;border-radius:10px;background-color:#ff6666;margin-bottom:10px;height:50px;color:white;font-size:0.9em;align-items : center;padding:0px;"onclick="location.href='GoToChildHome?childId=<%=focusUser.getUserId()%>';">메인으로</button>
		<%} %>
		</div>
		<div class="w3-col s2 m3 l3">&nbsp;</div>
	</div>

</body>
</html>