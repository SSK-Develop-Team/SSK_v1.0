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

    /*for (SdqResultAnalysis item : sdqResultAnalysisList) {
        out.println("1: " + item.getSdqType() + "<br>");
        out.println("2: " + item.getSdqAnalysisResult() + "<br>");
        out.println("3: " + item.getSdqTarget() + "<br>");
        out.println("4: " + item.getDescription() + "<br>");
        out.println("---------------------------<br>");
    }*/
    
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
        font-size: 16px !important;
        line-height: 1.6;
        zoom: 2.5; /* 전체 확대 */
        margin: 0;
        padding: 0;
    }

    #printArea {
        width: 100%;
        padding: 20px;
    }

    .report-result {
        font-size: 16px !important;
        margin-bottom: 10px;
    }

    .report-intro {
        font-size: 16px !important;
        margin-bottom: 25px;
    }

    .w3-button, .non-print, nav, footer, button {
        display: none !important;
    }

    html, body {
        -webkit-print-color-adjust: exact !important; /* 색 보존 */
        print-color-adjust: exact !important;
    }
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
	
	<!-- 검사 결과 보기 모달 -->
	<div id="modal" class="w3-modal">
    <div class="w3-modal-content">
      <div class="w3-container">
        <span onclick="document.getElementById('modal').style.display='none'" class="w3-button w3-display-topright">&times;</span>
        <p class="dsc">
			<div id="sdqAnalysis" style="text-align:center;">
			
				<!-- 토글 버튼 -->
				<div style="text-align: center; margin: 20px;">
				    <button onclick="showTarget('CHILD')" class="w3-button w3-round-large w3-light-grey">자녀용 보고서</button>
				    <button onclick="showTarget('PARENT')" class="w3-button w3-round-large w3-light-grey">부모용 보고서</button>
				</div>
				
				<div id="printArea">
					<div class="report-title" style="font-size:1.5em;font-weight:bold;"><정서⋅행동 발달 평가 결과 보고서></div>
					<div>&nbsp;</div>
					<!-- 안내문: 아동용 -->
					<div class="report-intro" data-target="CHILD" style="display: block;text-align: left;">
					    안녕하세요. 이화여자대학교 SSK 연구팀입니다.<br>
					    본 검사는 아동·청소년의 정신 건강 상태를 평가하기 위해 전 세계적으로 널리 사용되는 표준화된 강점·난점 검사(SDQ)를 한국 실정에 적합하도록 수정한 것입니다.
					    총 25개 문항을 통해 여러분의 심리적 상태를 다섯 가지 주요 영역(사회성 행동, 과잉행동, 정서적 상태, 품행문제, 또래관계문제)에서 종합적으로 평가합니다.
					    SDQ는 단순히 어려움을 확인하는 데 그치지 않고, 여러분의 강점과 긍정적인 모습을 함께 살펴보는 데 초점을 맞추고 있습니다.
					    이를 통해 자신을 더 잘 이해하고, 앞으로 어떻게 하면 더 나아질 수 있을지 생각해보는 기회가 되길 바랍니다.
					</div>
					
					<!-- 안내문: 부모용 -->
					<div class="report-intro" data-target="PARENT" style="display: none;text-align: left;">
					    안녕하세요. 이화여자대학교 SSK 연구팀입니다.<br>
					    본 검사는 아동·청소년의 정신 건강 상태를 평가하기 위해 전 세계적으로 널리 사용되는 표준화된 강점·난점 검사(SDQ)를 한국 실정에 적합하도록 수정한 것입니다.
					    총 25개 문항을 통해 자녀의 심리적 상태를 다섯 가지 주요 영역(사회성 행동, 과잉행동, 정서적 상태, 품행문제, 또래관계문제)에서 종합적으로 평가합니다.
					    SDQ는 단순히 자녀의 어려움을 확인하는 데 그치지 않고, 자녀의 긍정적인 면을 함께 살펴보는 데 초점을 맞추고 있습니다.
					    이에 SDQ 결과를 해석할 때는 현재 자녀가 겪고 있는 어려움뿐만 아니라 강점을 확인함으로써 자녀의 심리적 특성에 대한 폭넓은 시각을 가지는 것이 중요합니다.
					    아래의 결과를 통해 자녀의 현재 상태를 이해하고, 필요시 적절한 지원 방안을 고민하는 데 도움이 되길 바랍니다.
					</div>
					
					<!-- 결과 출력 -->
					<%
					for (SdqResultAnalysis result : sdqResultAnalysisList) {
						String type = result.getSdqType();						// 또래문제, 또래관계문제,품행문제, 정서증상, 정서적 상태, 과잉행동, 사회성 행동
						String analysisResult = result.getSdqAnalysisResult(); 	// 정상, 경계선, 개입필요
					    String target = result.getSdqTarget(); 					// CHILD, PARENT
					    String color = result.getColor();
					    String description = result.getDescription();
					%>
					
						<div class="report-result" data-target="<%= target %>" style="display: <%= "CHILD".equals(target) ? "block" : "none" %>;">
							<div>&nbsp;</div>
							<p style="font-weight: bold; text-align: left;"><%= type %></p>
							<p style="text-align: left;">
								-
						        <% if ("PARENT".equalsIgnoreCase(target)) { %>
						            	자녀의 <%= type %> 행동은 
						        <% } else { %>
						            <%= focusUser.getUserName() %>의 <%= type %> 행동은 
						        <% } %>
						        
						   <span style="text-decoration: underline;"><%= analysisResult %></span>
						        범위에 있습니다. <%= description %>
						        
							</p>
						</div>
					<% } %>
				</div>
				
				<div style="text-align: right; margin: 20px;">
				    <button onclick="printResult()" class="w3-button w3-round-large w3-dark-grey">
				        	인쇄
				    </button>
				</div>
				
				<!-- 기존 결과 출력 -->
				<!--  
				<div style="font-size:1.5em;font-weight:bold;"><%=focusUser.getUserName()%>의 SDQ 검사 결과 설명</div>
					<%for(int i = 0 ; i<sdqResultAnalysisList.size() ; i++){ %>
						<p style="font-weight: bold;">[ <%=sdqResultAnalysisList.get(i).getSdqType()%> ]</p>
						<p>
							<span style="color:<%=sdqResultAnalysisList.get(i).getColor()%>;"><%=sdqResultAnalysisList.get(i).getSdqAnalysisResult()%></span><br>
							<%=sdqResultAnalysisList.get(i).getDescription()%>
						</p>
						<br>
					<%} %>
				-->
			</div>
		</p>
      </div>
    </div>
	</div>
<script>
function showTarget(targetType) {
    const allBlocks = document.querySelectorAll('.result-block');

    // 안내문 토글
    document.querySelectorAll(".report-intro").forEach(el => {
        el.style.display = (el.getAttribute("data-target") === targetType) ? "block" : "none";
    });

    // 결과 블록 토글
    document.querySelectorAll(".report-result").forEach(el => {
        el.style.display = (el.getAttribute("data-target") === targetType) ? "block" : "none";
    });
}

function printResult() {
    const printContents = document.getElementById("printArea").innerHTML;
    const originalContents = document.body.innerHTML;

    document.body.innerHTML = printContents;
    window.print();
    document.body.innerHTML = originalContents;

    location.reload(); 
}

</script>
</body>
</html>