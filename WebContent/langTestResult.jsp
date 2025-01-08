<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.dto.User" %>
<%@ page import="model.dto.LangTestLog" %>
<%@ page import="model.dto.LangReply" %>
<%@ page import="model.dto.LangQuestion" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>언어 발달 평가 결과</title>
<script src= "https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.slim.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@2.8.0"></script>
<%
/*
* 전문가가 아동의 결과를 조회하는 경우 currUser == expert , focusUser == child
* 아동이 자신의 결과를 조회하는 경우 currUser == focusUser == child
*/
	User focusUser = (User)request.getAttribute("focusUser");
	User currUser = (User)session.getAttribute("currUser");
	String name = focusUser.getUserName();

	ArrayList<Integer> ageGroupSet = (ArrayList<Integer>)request.getAttribute("ageGroupSet");
	
	boolean isTesting = (boolean)request.getAttribute("isTesting");
	int selectIndex = 0;

	int selectAgeGroupId = (int)request.getAttribute("selectAgeGroupId");
	ArrayList<LangReply> selectLangReplyList = (ArrayList<LangReply>)request.getAttribute("selectLangReplyList");
	ArrayList<LangQuestion> selectLangQuestionList = (ArrayList<LangQuestion>)request.getAttribute("selectLangQuestionList");

	ArrayList<LangTestLog> langLogListByUser = (ArrayList<LangTestLog>)request.getAttribute("langLogListByUser");
	
	if(request.getAttribute("selectIndex") != null) selectIndex = (int)request.getAttribute("selectIndex");
	String[] ageGroupStr = new String[]{"3세 0개월 ~ 3세 3개월", "3세 4개월 ~ 3세 5개월", "3세 6개월 ~ 3세 8개월", "3세 9개월 ~ 3세 11개월", "4세 0개월 ~ 4세 3개월",
			"4세 4개월 ~ 4세 7개월", "4세 8개월 ~ 4세 11개월", "5세 0개월 ~ 5세 5개월", "5세 6개월 ~ 5세 11개월", "6세 0개월 ~ 6세 5개월", "6세 6개월 ~ 6세 11개월",
			"7세 0개월 ~ 7세 11개월", "8세 0개월 ~ 8세 11개월", "9세 0개월 ~ 9세 11개월", ""};
%>
<style>

body{
	/* margin-top: 100px; */
	line-height: 1.6
}
.container{
	width:100%;
}

ul.tabs{
	margin: 0px;
	padding: 0px;
	list-style: none;
}
ul.tabs li{
	background: #ededed;
	color: #222;
	display: inline-block;
	padding: 10px 15px;
	cursor: pointer;
}

ul.tabs li.current{
	background: #1a2a3a;
	color: #fff;
}

.tab-content{
	display: none;
	background: #ededed;
	padding: 15px;
}

.tab-content.current{
	display: inherit;
}


</style>

</head>
<body>
<%@ include file="sidebar.jsp" %>
<div>
	<h4 style="text-align:center;font-weight:bold;"><%=name%>님의 언어 발달 평가 결과</h4>
</div>

  <div class="w3-row">
	<div class="w3-col s1 m2 l4">&nbsp;</div>
	<c:set var="isTesting" scope="page"><%=isTesting%></c:set>
	<c:choose>
	<c:when test="${isTesting ne true}">
		<div class="w3-col s10 m8 l4">
			<div class="w3-dropdown-hover"style="width:100%;">
			    <button class="w3-button"style="width:100%;background-color:#D9D9D9;"><%=ageGroupStr[selectAgeGroupId]%></button>
			    <div class="w3-dropdown-content w3-bar-block w3-border"style="width:100%;">
			      <%for(int i = 0; i < ageGroupSet.size() ; i++){ %>
			      <a href="GetLangLog?ageGroupId=<%=ageGroupSet.get(i)%>&focusUserId=<%=focusUser.getUserId()%>" class="w3-bar-item w3-button"style="width:100%;"><%=ageGroupStr[ageGroupSet.get(i)]%></a>
			      <%} %>
			    </div>
		    </div>
		    <div class="w3-row">&nbsp;</div>
		    <div class="w3-dropdown-hover"style="width:100%;">
			 <%if(langLogListByUser.size() > 0){ %>
			    <button class="w3-button"style="width:100%;background-color:#D9D9D9;"><%=langLogListByUser.get(selectIndex).getLangTestDate().toString()%>&nbsp;<%=langLogListByUser.get(selectIndex).getLangTestTime().toString()%></button>
			    <div class="w3-dropdown-content w3-bar-block w3-border"style="width:100%;">
			      <%for(int i = 0; i < langLogListByUser.size() ; i++){ %>
			      <a href="GetLangLogTime?selectNum=<%=i %>&focusUserId=<%=focusUser.getUserId()%>" class="w3-bar-item w3-button"style="width:100%;"><%=langLogListByUser.get(i).getLangTestDate().toString()%>&nbsp;<%=langLogListByUser.get(i).getLangTestTime().toString()%></a>
			      <%} 
			   } else{ %>
			      <button class="w3-button"style="width:100%;background-color:#D9D9D9;">X</button>
			      <%} %>
			    </div>
		    </div>
		</div>
	</c:when>
	<c:otherwise>
	</c:otherwise>
	</c:choose>

	<c:remove var="selectedIndex" scope="page"/>

  </div>
<div class="w3-row">
	<div class="w3-col w3-hide-small m1 l3">&nbsp;</div>
	<div class="w3-col s12 m10 l6">
		<div class="w3-panel container">
			<ul class="tabs">
				<li class="tab-link current" data-tab="tab-1">막대그래프로 확인하기</li>
				<li class="tab-link" data-tab="tab-2">오각그래프로 확인하기</li>
			</ul>

			<div id="tab-1" class="tab-content current">
				<canvas id="myChart"></canvas>
			</div>
			<div id="tab-2" class="tab-content">
				<canvas id="myChart2"></canvas>
			</div>

			<div class="btnbox">
			<%if(currUser.getUserRole().equals("CHILD")){ %>
				<input type="button" class="w3-button w3-right w3-round-large w3-margin-top" style="background-color: #1a2a3a;color:white;"id="mainBack" value="돌아가기" onClick="javascript:location.href='childHome.jsp'">
			<%}else{ %>
				<input type="button" class="w3-button w3-right w3-round-large w3-margin-top" style="background-color: #1a2a3a;color:white;"id="mainBack" value="돌아가기" onClick="location.href='GoToChildHome?childId=<%=focusUser.getUserId()%>';">
			<%} %>
			</div>
		</div>
	</div>
	<div class="w3-col w3-hide-small m1 l3">&nbsp;</div>
</div>


<script type="text/javascript">
	$(document).ready(function(){
		
		$('ul.tabs li').click(function(){
			var tab_id = $(this).attr('data-tab');
	
			$('ul.tabs li').removeClass('current');
			$('.tab-content').removeClass('current');
	
			$(this).addClass('current');
			$("#"+tab_id).addClass('current');
		})
	
	})
	
	var ctx = document.getElementById('myChart').getContext('2d');
	var ctx2 = document.getElementById('myChart2').getContext('2d');
	
        var chart = new Chart(ctx, {
            type: 'bar', 
            data: {
                labels: ['<%= selectLangQuestionList.get(0).getLangType()%>', '<%= selectLangQuestionList.get(1).getLangType()%>', '<%= selectLangQuestionList.get(2).getLangType()%>', '<%= selectLangQuestionList.get(3).getLangType()%>', '<%= selectLangQuestionList.get(4).getLangType()%>'],
                datasets: [{
                    backgroundColor: '#6d6db0',
                    borderColor: '#6d6db0',
                    data: [<%= selectLangReplyList.get(0).getLangReplyContent()%>, <%= selectLangReplyList.get(1).getLangReplyContent()%>, <%= selectLangReplyList.get(2).getLangReplyContent()%>, <%= selectLangReplyList.get(3).getLangReplyContent()%>, <%= selectLangReplyList.get(4).getLangReplyContent()%>]
                }]
            },
            
            options: {
                indexAxis: 'y',
                
    			scales: {
    				yAxes: [{
    					ticks: {
    						beginAtZero: true,
    						min : 0,
    						max : 4,
    						stepSize : 1,
    					}
    				}]
    			},
    			annotations : {
    				'legend':'none'
    			},
    			legend: {
    		        display: false
    		    },
    		    tooltips: {
    		        callbacks: {
    		           label: function(tooltipItem) {
    		                  return tooltipItem.yLabel;
    		           }
    		        }
    		    }
            }

            
        });
        
        var options = {
        	    //responsive: false,
        	    maintainAspectRatio: true,
        	    scale: {
        	        ticks: {
        	            beginAtZero: true,
        	            min: 0,
        	            stepSize : 1,
        	            max: 4
        	        }
        	    },
   				legend: {
    		        display: false
    		    }
        	};

        	var dataLiteracy = {
        			labels: ['<%= selectLangQuestionList.get(0).getLangType()%>', '<%= selectLangQuestionList.get(1).getLangType()%>', '<%= selectLangQuestionList.get(2).getLangType()%>', '<%= selectLangQuestionList.get(3).getLangType()%>', '<%= selectLangQuestionList.get(4).getLangType()%>'],
        	    datasets: [{
        	        backgroundColor: "transparent",
        	        fill : false,
        	        borderColor: "rgb(109, 109, 176)",
        	        pointBackgroundColor: "rgb(109, 109, 176)",
        	        pointBorderColor: "#fff",
        	        pointHoverBackgroundColor: "#fff",
        	        pointHoverBorderColor: "rgb(109, 109, 176)",
        	        data: [<%= selectLangReplyList.get(0).getLangReplyContent()%>, <%= selectLangReplyList.get(1).getLangReplyContent()%>, <%= selectLangReplyList.get(2).getLangReplyContent()%>, <%= selectLangReplyList.get(3).getLangReplyContent()%>, <%= selectLangReplyList.get(4).getLangReplyContent()%>]
        	    }]
        	};

        	var myRadarChart = new Chart(ctx2, {
        	    type: 'radar',
        	    data: dataLiteracy,
        	    options: options
        	});
        
        
</script>
</body>
</html>