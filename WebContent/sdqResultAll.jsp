<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.dto.User" %>
<%@ page import="model.dto.SdqQuestion" %>
<%@ page import="model.dto.SdqResultAnalysis" %>
<%@ page import="model.dto.SdqTestLog" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.sql.Date" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>정서 행동 발달 검사 결과</title>

<%
	User focusUser = (User)request.getAttribute("focusUser");
	User currUser = (User)session.getAttribute("currUser");

	ArrayList<SdqTestLog> sdqTestLogList = (ArrayList<SdqTestLog>)request.getAttribute("sdqTestLogList");
	SdqTestLog selectedSdqTestLog = (SdqTestLog)request.getAttribute("selectedSdqTestLog");
	ArrayList<SdqResultOfType> sdqResult = (ArrayList<SdqResultOfType>)request.getAttribute("sdqResult");
	ArrayList<SdqResultAnalysis> sdqResultAnalysisList = (ArrayList<SdqResultAnalysis>)request.getAttribute("sdqResultAnalysisList");

	int selectedIndex = sdqTestLogList.indexOf(selectedSdqTestLog);
%>
<style>
.fullBtn{
	border:1px solid #ff6666;
	border-radius:10px;
	background-color:#ff6666;
	margin-bottom:10px;
	height:50px;
	color:white;
	font-size:0.9em;
	align-items : center;
	padding:0px;
}
@media (max-width: 390px) {
  .fullBtn {
    font-size:0.8em;
  }
}

.print-button {
	cursor: pointer;
	transition: all 0.3s;
}

   /* 버튼 호버 효과 */
 .print-button:hover {
   background-color: #f5f5f5;
   border-color: #999;
 }

 /* 클릭 효과 */
 .print-button:active {
   background-color: #e5e5e5;
   border-color: #666;
   color: #000;
 }
 
@media print {
    body { 
        background-color: white;
        color: black;
        padding: 10px;
    }
}
@page {
    size: A4 portrait; 
    margin: 2cm; 
}


</style>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
<script type="text/javascript">
	$(window).resize(function(){
		drawChart();
	    });
      google.charts.load('current', {packages:['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['정서 / 행동 발달 검사', '결과', {role:"style"},  {type: 'string', role: 'tooltip', p: {'html': true}}],
          <%for(int i=0;i<sdqResult.size();i++){%>
	          ['<%=sdqResultAnalysisList.get(i).getSdqType()%>', <%=sdqResult.get(i).getResult()%>, '<%=sdqResultAnalysisList.get(i).getColor()%>', '<div style="width:100px;margin:0px;padding:5px;"><b><%=sdqResultAnalysisList.get(i).getSdqType()%></b> <br>결과: <%=sdqResultAnalysisList.get(i).getSdqAnalysisResult()%></div>'],
        <%}%>
          ]);
        
        var view = new google.visualization.DataView(data);
        view.setColumns([0, 1,
                         { calc: "stringify",
                           sourceColumn: 1,
                           type: "string",
                           role: "annotation" },
                         2,3]);

        var options = {
        	annotations : {
        		alwaysOutside : true
        	},
          	vAxis : {
          		viewWindow : {
          			max : 10,
          			min : 0
          		}
          	},
          	tooltip: {isHtml: true},
        	'legend' : 'none'
          	
        };
        
        var chart = new google.visualization.ColumnChart(document.getElementById("columnchart_values"));
        chart.draw(view, options);
      }
    </script>

</head>
<body>
	<%@ include file="sidebar.jsp"%>
	<!-- page title -->
   <div style="text-align:center;font-weight:bold;font-size:1.3em;margin-top:0.5em">정서/행동 발달 검사 결과</div>
   <div>&nbsp;</div>
   <div>&nbsp;</div>
   
   <!-- 날짜 선택 -->
   <div class="w3-row">
		<div class="w3-col s1 m2 l4">&nbsp;</div>
		<c:set var="selectedIndex" scope="page"><%=selectedIndex%></c:set>
		
		<c:choose>
		<c:when test="${selectedIndex eq 0}">
			<div class="w3-col s1 m1 l1 w3-center"onclick="alert('이전 기록이 없습니다.');"><img src="./image/left-arrow.png" style="width:2.5em; opacity: 0.5;"/></div>
		</c:when>
		<c:otherwise>
			<div class="w3-col s1 m1 l1 w3-center" onclick="location.href='GetSdqResultAll?sdqTestLogId=<%=sdqTestLogList.get(selectedIndex-1).getSdqTestLogId()%>&childId=<%=focusUser.getUserId()%>'"><img src="./image/left-arrow.png" style="width:2.5em;"/></div>
		</c:otherwise>
		</c:choose>
		
		<div class="w3-col s8 m6 l2" >
			<div class="w3-dropdown-hover"style="width:100%;">
			    <button class="w3-button"style="width:100%;background-color:#D9D9D9;"><%=selectedSdqTestLog.getSdqTestDate().toString()%>&nbsp;<%=selectedSdqTestLog.getSdqTestTime().toString()%></button>
			    <div class="w3-dropdown-content w3-bar-block w3-border"style="width:100%;">
			      <%for(int i = sdqTestLogList.size()-1 ; i >= 0 ;i--){ %>
			      <a href="GetSdqResultAll?sdqTestLogId=<%=sdqTestLogList.get(i).getSdqTestLogId()%>&childId=<%=focusUser.getUserId()%>" class="w3-bar-item w3-button"style="width:100%;"><%=sdqTestLogList.get(i).getSdqTestDate().toString()%>&nbsp;<%=sdqTestLogList.get(i).getSdqTestTime().toString()%></a>
			      <%} %>
			    </div>
		    </div>
		</div>
		
		<c:choose>
		<c:when test="${selectedIndex eq sdqTestLogList.size()-1}">
			<div class="w3-col s1 m1 l1 w3-center"onclick="alert('다음 기록이 없습니다.');"><img src="./image/right-arrow.png" style="width:2.5em; opacity: 0.5;"/></div>
		</c:when>
		<c:otherwise>
			<div class="w3-col s1 m1 l1 w3-center"onclick="location.href='GetSdqResultAll?sdqTestLogId=<%=sdqTestLogList.get(selectedIndex+1).getSdqTestLogId()%>&childId=<%=focusUser.getUserId()%>'"><img src="./image/right-arrow.png" style="width:2.5em;"/></div>
		</c:otherwise>
		</c:choose>
		
		<c:remove var="selectedIndex" scope="page"/>
		
		<div class="w3-col s1 m2 l4">&nbsp;</div>
   </div>
   
	<!-- 시간별 그래프 뷰 -->
	<div class="w3-row">
		<div class="w3-col w3-hide-small m1 l2">&nbsp;</div>
		<div class="w3-col s12 m10 l8">
			<div id="columnchart_values" style="width:100%;height: 60vh;"></div>
		</div>
		<div class="w3-col w3-hide-small m1 l2">&nbsp;</div>
	</div>
	<div class="w3-row w3-margin-top">
		<div class="w3-col s2 m2 l3">&nbsp;</div>
		
		<div class="w3-col w3-row s4 m3 l2">
			<button class="w3-button w3-col fullBtn" onclick="document.getElementById('modal').style.display='block';">검사 결과 설명 보기</button>
		</div>
		<div class="w3-col s2 m1 l2">&nbsp;</div>
		
		<div class="w3-col w3-row s3 m2 l1">
 		   <!-- <button class="w3-button w3-col print-button"style="border:1px solid #ff6666;border-radius:10px;background-color:#ff6666;margin-bottom:10px;height:50px;color:white;font-size:1em;align-items : center;padding:0px;" onclick="printPage()">출력하기</button>
			    <script>
			        function printPage() {
			            window.print();
			        }
			    </script> -->
		</div>

		<div class="w3-col w3-row s3 m2 l1">
			<%if(currUser.getUserRole().equals("CHILD")){ %>
			<button class="w3-button w3-col"style="border:1px solid #ff6666;border-radius:10px;background-color:#ff6666;margin-bottom:10px;margin-left:10px;height:50px;color:white;font-size:1em;align-items : center;padding:0px;"onclick="location.href='sdqTestMain.jsp';">메인으로</button>
		<%}else{ %>
			<button class="w3-button w3-col"style="border:1px solid #ff6666;border-radius:10px;background-color:#ff6666;margin-bottom:10px;margin-left:10px;height:50px;color:white;font-size:1em;align-items : center;padding:0px;"onclick="location.href='GoToChildHome?childId=<%=focusUser.getUserId()%>';">메인으로</button>
		<%} %>
		</div>
		<div class="w3-col s2 m2 l3">&nbsp;</div>
	</div>
	
	<div id="modal" class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-container">
        <span onclick="document.getElementById('modal').style.display='none'" class="w3-button w3-display-topright">&times;</span>
        <p class="dsc">
			<div id="sdqAnalysis" style="text-align:center;">
				<div style="font-size:1.5em;font-weight:bold;"><%=focusUser.getUserName()%>의 SDQ 검사 결과 설명</div>
				<%for(int i = 0 ; i<sdqResultAnalysisList.size() ; i++){ %>
					<p style="font-weight: bold;">[ <%=sdqResultAnalysisList.get(i).getSdqType()%> ]</p>
					<p>
						<span style="color:<%=sdqResultAnalysisList.get(i).getColor()%>;"><%=sdqResultAnalysisList.get(i).getSdqAnalysisResult()%></span><br>
						<%=sdqResultAnalysisList.get(i).getDescription()%>
					</p>
					<br>
				<%} %>
			</div>
		</p>
      </div>
    </div>
  </div>
</body>
</html>