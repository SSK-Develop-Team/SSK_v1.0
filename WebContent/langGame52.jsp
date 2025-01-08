<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ page import="model.dto.User" %>
<%@ page import="model.dto.LangGame" %>
<%@ page import="util.process.LangGameProcessor" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>

<link href="css/langGame.css" rel="stylesheet" type='text/css' >
<style>	
	.oceanBack1 {
		display : block;
		height : 100%;
		width : 100%;
		position : relative;
	}
	
	.oceanBack2 {
		display : none;
		height : 100%;
		width : 100%;
		position : relative;
	}
	

	.selectImg01{
		position : absolute;
		left : 15%;
		top : 23%;
	}
	.selectImg02{
		position : absolute;
		left : 60%;
		top : 20%;
	}
	.selectImg03{
		position : absolute;
		left : 23%;
		top : 16%;
	}
	.selectImg04{
		position : absolute;
		left : 72%;
		top : 23%;
	}
	
</style>
	
</head>


<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	<title>52번 문항 직접 평가</title>
</head>
<body>
<%@ include file="sidebar.jsp" %>

<div class="w3-row">
	<div class="w3-col w3-hide-small m1 l3">&nbsp;</div>
	<div class="w3-col w3-container s12 m10 l6">
		<div style="font-size:1em;font-weight:bold;">직접평가 #52</div>
		<div>
			<div class="oceanBack1">
				<img class="gameImg01" src="./image/LangGameImg/Age10/age10_2_1.png" width="100%" height="100%" alt="바다의 틀린그림 찾기, 오른쪽 위에 물고기 두 마리."/>
				<img class="selectImg01" src="./image/LangGameImg/Age10/age10_2_1_icon01.png" width="4%" alt="작은 문어" onclick="openSelect01();audio01.pause();" />
					<div id="select-modal01" class="w3-modal">
						<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
							<div class="w3-container w3-center">
								<span onclick="closeSelect01()" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
								<img src="./image/LangGameImg/Age10/age10_2_1_modal01.png" width="90%" style="margin-top : 5%">
								<audio id="select-audio01">
									<source src="./audio/Age10/age_10_52_1.wav">
								</audio>
							</div>
						</div>
					</div>
				
				<img class="selectImg02" src="./image/LangGameImg/Age10/age10_2_1_icon01.png" width="12%" alt="큰 문어" onClick="openSelect01();audio01.pause();" />
			</div>
		
			<div class="oceanBack2">
				<img class="gameImg02" src="./image/LangGameImg/Age10/age10_2_2.png" width="100%" height="100%" alt="바다의 틀린그림 찾기 2, 왼쪽 아래에 해초."/>
				<img class="selectImg03" src="./image/LangGameImg/Age10/age10_2_1_icon02.png" height="40%" alt="큰 거북" onClick="openSelect02();audio02.pause();" />
				<div id="select-modal02" class="w3-modal">
						<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
							<div class="w3-container w3-center">
								<span onclick="closeSelect02()" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
								<img src="./image/LangGameImg/Age10/age10_2_2_modal02.png" width="90%" style="margin-top : 5%">
								<audio id="select-audio02">
									<source src="./audio/Age10/age_10_52_1.wav">
								</audio>
							</div>
						</div>
					</div>
				
				<img class="selectImg04" src="./image/LangGameImg/Age10/age10_2_1_icon02.png" height="20%" alt="작은 거북" onClick="openSelect02();audio02.pause();" />
			</div>
		</div>
		<div class="w3-container w3-round-large w3-padding" style="border:1px solid #12192C;">

			<div class="w3-container w3-padding-32">
				<div class="gametext">
					<div class="gametext01">두 그림에서 서로 다른 부분을 찾아 눌러보세요.</div>
				</div>
			</div>
			<div class="w3-container w3-right">
				<button class="w3-button" onclick="contentBack();" style="border:none; background-color:#FFFFFF;"> &lt; 이전</button>
				<button class="w3-button" onclick="content();" style="border:none; background-color:#FFFFFF;">다음 &gt; </button>
			</div>
		</div>
		<div class="w3-left" style="margin-top:5px;">
		
	      <button class="w3-button w3-round-large" onclick="openHint();" style="background-color:#12192C; color:white; text-align:center;font-size:0.9em;margin-right:5px;">힌트 확인하기</button>
	      <div id="hint-modal" class="w3-modal">
	        <div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
	          <div class="w3-container w3-center">
	            <span onclick="closeHint();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
	            <p>아이가 'ㅇㅇ보다 커', ㅇㅇ보다 작아'라는 말로 비교 표현을 사용하는지 확인해 주세요.</p>
	          </div>
	        </div>
	      </div>
		
			<button class="w3-button w3-round-large" onclick="openAnswer();" style="background-color:#12192C; color:white; text-align:center;font-size:0.9em;margin-right:5px;">정답 확인하기</button>
			<div id="answer-modal" class="w3-modal">
				<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
					<div class="w3-container w3-center">
						<span onclick="closeAnswer();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
						<p>더 커요 / 더 작아요</p>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="w3-col w3-hide-small m1 l3">&nbsp;</div>
</div>

</body>
<script src="https://code.jquery.com/jquery-3.6.0.slim.js"></script>
<script type="text/javascript">

	function playAudio(){
		var audio = new Audio('./audio/Age10/age_10_52_1.wav');
		audio.play();
	}

	var cnt = 0;
	var audio01 = new Audio('./audio/Age10/age_10_51_1.wav');
	var audio02 = new Audio('./audio/Age10/age_10_51_1.wav');
	
	//음성 재생
	window.onload = function () {
		audio01.play();
	}
	
	if (performance.navigation.type == 1) {
		$("#rightbtn").css('display', 'inline');
	}
	
	function content() {
		if(cnt==2) cnt = 2;
		else cnt = cnt+1;
		  
		  if(cnt == 1){
			audio01.pause();
			$(".oceanBack1").css('display', 'none');		
			$(".oceanBack2").css('display', 'block');		
       	 	$("#leftbtn").css('display', 'inline');
	      	var audio = new Audio('./audio/Age10/age_10_51_1.wav');
       	 	audio02.load();
			audio02.play();
          } 
          else if(cnt == 2){
      			if(!confirm("52번 문항의 게임을 종료하시겠습니까?")){cnt = 1; return;}
        	  	location.href = './langTest.jsp';
          }
	}     
	
	function contentBack() {
		if(cnt==0) cnt = 0;
		else cnt = cnt-1;
		  
		  if(cnt == 0){
			  	audio02.pause();
				$(".oceanBack2").css('display', 'none');		
				$(".oceanBack1").css('display', 'block');			
        		$("#answer").css('display', 'none');
        		$("#leftbtn").css('display', 'none');
    	      	var audio = new Audio('./audio/Age10/age_10_51_1.wav');
           	 	audio01.load();
    			audio01.play();
	          }
		}
	
</script>
<script type="text/javascript" src="js/langGame.js" charset="UTF-8"></script>
<script type="text/javascript" src="js/langGameModal.js" charset="UTF-8"></script>
</html>