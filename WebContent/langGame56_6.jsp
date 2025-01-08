<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ page import="model.dto.User" %>
<%@ page import="model.dto.LangGame" %>
<%@ page import="util.process.LangGameProcessor" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	<link href="css/langGame.css" rel="stylesheet" type='text/css' >
	<style type = "text/css">
		.townBack {
			display : block;
			height : 100%;
			width : 100%;
			position : relative;
		}
		
		#select01{
			position : absolute;
			left : 2%;
			top : 13%;
		}
		#select02{
			position : absolute;
			left : 35%;
			top : 13%;
		}
		#select03{
			position : absolute;
			left : 2%;
			top : 45%;
		}
		#select04{
			position : absolute;
			left : 21%;
			top : 45%;
		}
		#select05{
			position : absolute;
			left : 2%;
			top : 75%;
		}
		
		#select01, #select02, #select03, #select04, #select05{
			background-color:#4472c4; 
			color : #ffffff; 
			text-align:center;
			padding:10px; 
			width:10%; 
			font-size:4%;
		}
		
		@media (max-width:1280px) {
			#select01, #select02, #select03, #select04, #select05{
				background-color:#4472c4; 
				color : #ffffff; 
				text-align:center; 
				padding:0px;
				width:15%; 
				height : 10%;
				font-size:4%;
			}
		}
		
		@media (min-width:851px) and (max-width:920px) {
			#select01, #select02, #select03, #select04, #select05{
				background-color:#4472c4; 
				color : #ffffff; 
				padding : 0px;
				text-align:center;
				width:17%; 
				font-size:4%;
			}
			
			#select02 { left : 33% }
		}
		
		@media (max-width:850px) {
			#select01, #select02, #select03, #select04, #select05{
				background-color:#4472c4; 
				color : #ffffff; 
				text-align:center;
				width:18%; 
				padding: 0px;
				height : 10%;
				font-size:4%;
			}
			
			#select02{ left : 32%; }
		}
		
		@media (max-width:300px) {
			#select01, #select03, #select05{
				background-color:#4472c4; 
				color : #ffffff; 
				text-align:center;
				width:20%; 
				padding: 0px;
				height : 15%;
				font-size:4%;
			}
			
			#select02{ left : 25%; width : 25%; height : 15%; }
			#select04{ left : 25%; width : 25%; height : 15%; }
		}
		
	</style>
	
	
   	<%
	    User currUser = (User)session.getAttribute("currUser");
	    int gameID = (int)session.getAttribute("langGameID");
	    int i = (int)session.getAttribute("currLangGameIndex");
	    ArrayList<LangGame> langGameList = (ArrayList<LangGame>)session.getAttribute("currLangGameList");//size
  	%>
  	
	<title><%=gameID%>번 문항 직접 평가</title>
</head>
<body>
<%@ include file="sidebar.jsp" %>

<div class="w3-row">
	<div class="w3-col w3-hide-small m1 l3">&nbsp;</div>
	<div class="w3-col w3-container s12 m10 l6">
		<div style="font-size:1em;font-weight:bold;">직접평가 #<%=gameID%></div>
		<div>
			<div class="townBack">
				<img class="gameImg01" src="./image/LangGameImg/Age11/LangGame11_1_6.png" width="100%" height="100%" alt="마을의 전경, 교차로 네 개가 있고 건물이 아홉개가 있는 구조."/>
				<button class="w3-button" id="select01" onclick='wrongA()'>커피하우스</button>	
				<button class="w3-button" id="select02" onclick='wrongA()'>멋쟁이이발소</button>	
				<button class="w3-button" id="select03" onclick='correctA()'>쌍둥이빵집</button>	
				<button class="w3-button" id="select04" onclick='wrongA()'>리라과일가게</button>	
				<button class="w3-button" id="select05" onclick='wrongA()'>똑똑서점</button>	
			</div>
		</div>
		<div class="w3-container w3-round-large w3-padding" style="border:1px solid #12192C;">

			<div class="w3-container w3-padding-32">
				<div class="gametext">
					<div class="gametext01">쌍둥이빵집을 찾아서 눌러보아요.</div>
				</div>
			</div>
			
			<div class="w3-container w3-right">
 		      <%if(i>0){%><button class="w3-button" onclick="getPrevContent(<%=i%>);" style="border:none; background-color:#FFFFFF;"> &lt; 이전</button><%} %>
		      <%if(i<=langGameList.size()-1){ %><button class="w3-button" onclick="getNextContent(<%=i%>, <%= gameID %>,<%=langGameList.size()%>);" style="border:none; background-color:#FFFFFF;">다음 &gt; </button><%}%>
		    </div>

		</div>
		<div class="w3-left" style="margin-top:5px;">
		
	     <button class="w3-button w3-round-large" onclick="openHint();" style="background-color:#12192C; color:white; text-align:center;font-size:0.9em;margin-right:5px;">힌트 확인하기</button>
	      <div id="hint-modal" class="w3-modal">
	        <div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
	          <div class="w3-container w3-center">
	            <span onclick="closeHint();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
	            <%if(gameID == 56) {%> <p>화면의 글자를 읽고 글자에 맞는 그림을 찾는지 확인해주세요.</p><%} %>
	            <%if(gameID == 57) {%> <p>아이가 사과잼, 쌍둥이, 리라과일가게, 오렌지를 정확한 발음으로 말할 수 있는지 확인해주세요.</p><%} %>
	          </div>
	        </div>
	      </div>
		
			<%if(gameID == 56) {%>
			<button class="w3-button w3-round-large" onclick="openAnswer();;document.getElementById('answer-audio').autoplay();" style="background-color:#12192C; color:white; text-align:center;font-size:0.9em;margin-right:5px;">정답 확인하기</button>
			<div id="answer-modal" class="w3-modal">
				<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
					<div class="w3-container w3-center">
						<span onclick="closeAnswer();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
						<p>쌍둥이빵집을 누르면 정답.</p>
					</div>
				</div>
			</div>
			<%} %>
		</div>
	</div>
	<div class="w3-col w3-hide-small m1 l3">&nbsp;</div>
</div>

</body>
<script src="https://code.jquery.com/jquery-3.6.0.slim.js"></script>
<script type="text/javascript">
	function wrongA(){
		var audio = new Audio('./audio/wrong.mp3');
		audio.play();
	}
	
	function correctA(){
		var audio = new Audio('./audio/correct.mp3');
		audio.play();
	}

	window.onload = function () {
		var audio = new Audio('./audio/Age11/age_11_56_7.wav');
		audio.play();
	}
</script>
<script type="text/javascript" src="js/langGame.js" charset="UTF-8"></script>
</html>