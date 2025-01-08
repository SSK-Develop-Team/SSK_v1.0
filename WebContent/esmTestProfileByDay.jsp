<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="model.dto.User" %>
<%@ page import="java.sql.Date" %>
<%@ page import="java.sql.Time" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.dto.EsmReply" %>
<%@ page import="model.dto.EsmResultWithDate" %>
<%@ page import="model.dto.EsmDateWeekType" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>정서 반복 기록 일별 그래프</title>
<%
	User currUser = (User)session.getAttribute("currUser");
	User focusUser = (User)request.getAttribute("focusUser");
	EsmDateWeekType selectedDateWeek = (EsmDateWeekType)request.getAttribute("selectedDateWeek");
	ArrayList<EsmDateWeekType> dateWeekList = (ArrayList<EsmDateWeekType>)request.getAttribute("dateWeekList");
	ArrayList<Date> sDateListOfWeek = (ArrayList<Date>)request.getAttribute("sDateListOfWeek");
	ArrayList<EsmResultWithDate> esmReplyOfDayList = (ArrayList<EsmResultWithDate>)request.getAttribute("esmReplyOfDayList");
	String[] startDayOfWeekOptionList = {"일요일부터 보기", "월요일부터 보기", "화요일부터 보기", "수요일부터 보기", "목요일부터 보기", "금요일부터 보기", "토요일부터 보기"};
	int startDayOfWeek = (int)request.getAttribute("startDayOfWeek");
	
	LocalDate sdate = sDateListOfWeek.get(0).toLocalDate();
	LocalDate edate = sDateListOfWeek.get(6).toLocalDate();
	
	int indexOfDate = dateWeekList.indexOf(selectedDateWeek);
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
       data.addColumn('date', 'Date');
       data.addColumn('number', '긍정 감정');
       data.addColumn('number', '부정 감정');
       data.addRows([
    	   <%for(EsmResultWithDate x : esmReplyOfDayList){
        	   LocalDate date = x.getDate().toLocalDate();%>
	        [new Date(<%=date.getYear()%>, <%=date.getMonthValue()-1%>, <%=date.getDayOfMonth()%>),<%=x.getPositiveAvg()%>,<%=x.getNegativeAvg()%>],
	       <%   
	           }
	        %>
       ]);
       
	const minDate = new Date(<%=sdate.getYear()%>, <%=sdate.getMonthValue()-1%>, <%=sdate.getDayOfMonth()%>);
    const maxDate = new Date(<%=edate.getYear()%>, <%=edate.getMonthValue()-1%>, <%=edate.getDayOfMonth()%>);
    minDate.setDate(minDate.getDate()-1);
    maxDate.setDate(maxDate.getDate()+1);
   
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
        	viewWindow: {
               min: minDate,
               max: maxDate
            },
        	format: 'MM/dd',
        	ticks: [
        		<% for(Date x : sDateListOfWeek){
        			LocalDate date = x.toLocalDate();
        		%>
        			new Date(<%=date.getYear()%>, <%=date.getMonthValue()-1%>, <%=date.getDayOfMonth()%>),
        		<%}%>],
        	gridlines:{count:7},
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
   <div style="text-align:center;font-weight:bold;font-size:1.5em;margin-top:0.5em">정서 반복 기록 - 일 별 그래프</div>
   <div style="text-align:center;font-size:1em;margin-top:0.5em"><%= focusUser.getUserName()%>님의 결과입니다.</div>
   <div>&nbsp;</div>
   
   <!-- 날짜 선택 -->
   <div class="w3-row">
		<div class="w3-col s1 m2 l4">&nbsp;</div>
		<c:set var="selectedIndex" scope="page"><%=indexOfDate%></c:set>
		<c:choose>
		<c:when test="${selectedIndex eq 0}">
			<div class="w3-col s1 m1 l1 w3-center"onclick="alert('이전 기록이 없습니다.');"><img src="./image/left-arrow.png" style="width:2.5em; opacity: 0.5;"/></div>
		</c:when>
		<c:otherwise>
			<div class="w3-col s1 m1 l1 w3-center" onclick="location.href='GetEsmTestProfileByDay?date=<%=dateWeekList.get(indexOfDate-1).getDate().toString()%>&childId=<%=focusUser.getUserId()%>&startDayOfWeek=<%=startDayOfWeek%>'"><img src="./image/left-arrow.png" style="width:2.5em;"/></div>
		</c:otherwise>
		</c:choose>
		<div class="w3-col s8 m6 l2" >
			<div class="w3-dropdown-hover"style="width:100%;">
			    <button class="w3-button"style="width:100%;background-color:#D9D9D9;"><%=selectedDateWeek.getWeekStr()%></button>
			    <div class="w3-dropdown-content w3-bar-block w3-border"style="width:100%;">
			      <%for(EsmDateWeekType x : dateWeekList){ %>
			      <a href="GetEsmTestProfileByDay?date=<%=x.getDate().toString()%>&childId=<%=focusUser.getUserId()%>&startDayOfWeek=<%=startDayOfWeek%>" class="w3-bar-item w3-button"style="width:100%;"><%=x.getWeekStr()%></a>
			      <%} %>
			    </div>
		    </div>
		</div>
		<c:choose>
		<c:when test="${selectedIndex eq dateWeekList.size()-1}">
			<div class="w3-col s1 m1 l1 w3-center"onclick="alert('다음 기록이 없습니다.');"><img src="./image/right-arrow.png" style="width:2.5em; opacity: 0.5;"/></div>
		</c:when>
		<c:otherwise>
			<div class="w3-col s1 m1 l1 w3-center"onclick="location.href='GetEsmTestProfileByDay?date=<%=dateWeekList.get(indexOfDate+1).getDate().toString()%>&childId=<%=focusUser.getUserId()%>&startDayOfWeek=<%=startDayOfWeek%>'"><img src="./image/right-arrow.png" style="width:2.5em;"/></div>
		</c:otherwise>
		</c:choose>
		<c:remove var="selectedIndex" scope="page"/>
		<div class="w3-col s1 m2 l4">&nbsp;</div>
   </div>
	<div class="w3-row">&nbsp;</div>
	<div class="w3-row">
		<div class="w3-col s1 m2 l2">&nbsp;</div>
		<div class="w3-col s10 m8 l8">
			<div class="w3-dropdown-hover w3-right" style="border:1px solid #C4C4C4;border-radius:10px;background-color:#EBEBEB;">
				<button class="w3-button w3-hover-white" style="border:1px solid #C4C4C4;border-radius:10px;background-color:#EBEBEB;font-size:0.9em;"><%=startDayOfWeekOptionList[startDayOfWeek-1]%></button>
				<div class="w3-dropdown-content w3-bar-block w3-border" style="right:0;font-size:0.9em;">
					<%for(int i=0;i<startDayOfWeekOptionList.length;i++){%>
					<a href="GetEsmTestProfileByDay?date=<%=dateWeekList.get(indexOfDate).getDate().toString()%>&childId=<%=focusUser.getUserId()%>&startDayOfWeek=<%=i+1%>" class="w3-bar-item w3-button"><%=startDayOfWeekOptionList[i]%></a>
					<%}%>
				</div>
			</div>
		</div>
		<div class="w3-col s1 m2 l2">&nbsp;</div>
	</div>

	<!-- 시간별 그래프 뷰 -->
	<div class="w3-row">
		<div class="w3-col w3-hide-small m1 l1">&nbsp;</div>
		<div class="w3-col s12 m10 l10">
			<div id="esmProfileChart" style="width:100%;height: 60vh;"></div>
		</div>
		<div class="w3-col w3-hide-small m1 l1">&nbsp;</div>
	</div>
	
	<!-- page redirect button -->
	<div class="w3-row">
		<div class="w3-col s2 m3 l3">&nbsp;</div>
		<div class="w3-col w3-row s4 m3 l2">
			<button class="w3-button w3-col" style="border:1px solid #ff6666;border-radius:10px;background-color:#ff6666;margin-bottom:10px;height:50px;color:white;font-size:0.8em;align-items : center;padding:0px;"onclick="location.href='GetEsmTestProfileByTime?date=<%= selectedDateWeek.getDate().toString()%>&childId=<%=focusUser.getUserId()%>';">시간 별 그래프 보기</button>
		</div>
		<div class="w3-col s1 m1 l3">&nbsp;</div>
		<div class="w3-col w3-row s3 m2 l1">
			<%if(currUser.getUserRole().equals("CHILD")){ %>
			<button class="w3-button w3-col"style="border:1px solid #ff6666;border-radius:10px;background-color:#ff6666;margin-bottom:10px;height:50px;color:white;font-size:0.9em;align-items : center;padding:0px;"onclick="location.href='esmTestMain.jsp';">메인으로</button>
		<%}else{ %>
			<button class="w3-button w3-col"style="border:1px solid #ff6666;border-radius:10px;background-color:#ff6666;margin-bottom:10px;height:50px;color:white;font-size:0.9em;align-items : center;padding:0px;"onclick="location.href='GoToChildHome?childId=<%=focusUser.getUserId()%>';">메인으로</button>
		<%} %>
		</div>
		<div class="w3-col s2 m2 l3">&nbsp;</div>
	</div>

</body>
</html>