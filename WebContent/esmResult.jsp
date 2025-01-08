<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>정서 반복 기록 결과</title>
<%
	int positive = (int)request.getAttribute("positive");
	int negative = (int)request.getAttribute("negative");

%>
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
      ['정서 반복 기록', '정서', {role:"style"}, {type: 'string', role: 'tooltip', p: {'html': true}}],
      ['긍정 정서', <%=positive%>, "color : #66cc66",'<div style="width:100px;margin:0px;padding:8px;"><b>긍정 정서 : </b>  <%=positive%></div>'],
      ['부정 정서', <%=negative%>, "color : #ff6666",'<div style="width:100px;margin:0px;padding:8px;"><b>부정 정서 : </b>  <%=negative%></div>']
    ]);
    
    var view = new google.visualization.DataView(data);
    view.setColumns([0, 1,
                     { calc: "stringify",
                       sourceColumn: 1,
                       type: "string",
                       role: "annotation" },
                     2,3]);

    var options = {
      chart: {
        title: '정서 반복 기록 결과',
      },
      annotations : {
  		alwaysOutside : true
  		},
    	vAxis : {
    		viewWindow : {
    			max : 25,
    			min : 0
    		}
    	},
		tooltip: {isHtml: true},
    	legend: 'none'
    };
    
    var chart = new google.visualization.ColumnChart(document.getElementById("columnchart_values"));
    chart.draw(view, options);
  }
  
</script>
</head>
<body>
	<%@ include file="sidebar.jsp" %>
	<div class="w3-row">
		<div class="w3-col m1 l2">&nbsp;</div>
		<div class="w3-col s12 m10 l8">
			<div id="columnchart_values" style="width:100%;height: 60vh;"></div>
		</div>
		<div class="w3-col m1 l2">&nbsp;</div>
	</div>
	<div class="w3-row">
		<div class="w3-col s1 m3 l4">&nbsp;</div>
		<div class="w3-col s10 m6 l4">
			<button class="w3-button w3-col w3-padding"style="border:1px solid #ff6666;border-radius:10px;background-color:#ff6666;margin-bottom:10px;height:50px;color:white;font-size:1em;align-items : center;"onclick="location.href='esmTestMain.jsp';">메인으로</button>
		</div>
		<div class="w3-col s1 m3 l4">&nbsp;</div>
	</div>
	
</body>
</html>