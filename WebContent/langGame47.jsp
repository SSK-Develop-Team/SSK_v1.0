<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<link href="css/langGame.css" rel="stylesheet" type='text/css' >

<style>
	.Quiz01 {
		display : block;
		height : 100%;
		width : 100%;
		position : relative;
	}
	
	.Quiz02 {
		display : none;
		height : 100%;
		width : 100%;
		position : relative;
	}
	
	.Quiz03 {
		display : none;
		height : 100%;
		width : 100%;
		position : relative;
	}
	
	#chooseA1, #chooseB1, #chooseC1{
		position : absolute;
		left : 45%;
		top : 10%;
		font-weight : bold;
	}	
	
	#chooseA2, #chooseB2, #chooseC2{
		position : absolute;
		left : 45%;
		top : 40%;
		font-weight : bold;
	}
		
	#chooseA3, #chooseB3, #chooseC3{
		position : absolute;
		left : 45%;
		top : 70%;
		font-weight : bold;
	}
	
	.gametext02 { display : none; }

</style>
	
</head>


<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	<title>47번 문항 직접 평가</title>
</head>
<body>
<%@ include file="sidebar.jsp" %>

<div class="w3-row">
	<div class="w3-col w3-hide-small m1 l3">&nbsp;</div>
	<div class="w3-col w3-container s12 m10 l6">
		<div style="font-size:1em;font-weight:bold;">직접평가 #47</div>
		<div>
			<div class="Quiz01">
				<img class="gameImg01" src="./image/LangGameImg/Age09/age09_2_1.png" width="100%" alt="마녀의 퀴즈 1"/>  
				<button class="w3-button w3-container w3-round-large" id="chooseA1" onclick='wrongA()' style="background-color:#ffffff; text-align:center;padding:2%;">2마리</button>	
				<button class="w3-button w3-container w3-round-large" id="chooseA2" onclick='correctA()' style="background-color:#ffffff; text-align:center;padding:2%;">&nbsp;&nbsp;2개&nbsp;</button>	
				<button class="w3-button w3-container w3-round-large" id="chooseA3" onclick='wrongA()' style="background-color:#ffffff; text-align:center;padding:2%;">2송이</button>	
			</div>
			
			<div class="Quiz02">
				<img class="gameImg02" src="./image/LangGameImg/Age09/age09_2_2.png" width="100%" alt="마녀의 퀴즈 2"/>  
				<button class="w3-button w3-container w3-round-large" id="chooseB1" onclick='wrongA()' style="background-color:#ffffff; text-align:center;padding:2%;">4덩이</button>
				<button class="w3-button w3-container w3-round-large" id="chooseB2" onclick='wrongA()' style="background-color:#ffffff; text-align:center;padding:2%;">4마리</button>
				<button class="w3-button w3-container w3-round-large" id="chooseB3" onclick='correctA()' style="background-color:#ffffff; text-align:center;padding:2%;">4송이</button>
			</div>
			
			<div class="Quiz03">
				<img class="gameImg03" src="./image/LangGameImg/Age09/age09_2_3.png" width="100%" alt="마녀의 퀴즈 3"/>  
				<button class="w3-button w3-container w3-round-large" id="chooseC1" onclick='correctA()' style="background-color:#ffffff; text-align:center;padding:2%;">3마리</button>
				<button class="w3-button w3-container w3-round-large" id="chooseC2" onclick='wrongA()' style="background-color:#ffffff; text-align:center;padding:2%;">&nbsp;&nbsp;3대&nbsp;</button>
				<button class="w3-button w3-container w3-round-large" id="chooseC3" onclick='wrongA()' style="background-color:#ffffff; text-align:center;padding:2%;">3송이</button>
			</div>
		</div>
	
	<div class="w3-container w3-round-large w3-padding" style="border:1px solid #12192C;">

		<div class="w3-container w3-padding-32">
			<div class="gametext">
				<div class="gametext01">내가 보여주는 것을 보고, 숫자를 세는 알맞은 단위가 무엇인지 맞춰보렴!</div>
				<div class="gametext02">그럼 이건?</div>
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
	            <p>개수 + 단위로 말하도록 해주세요.</p>
	          </div>
	        </div>
	      </div>
		
			<button class="w3-button w3-round-large" onclick="openAnswer();;document.getElementById('answer-audio').autoplay();" style="background-color:#12192C; color:white; text-align:center;font-size:0.9em;margin-right:5px;">정답 확인하기</button>
			<div id="answer-modal" class="w3-modal">
				<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
					<div class="w3-container w3-center">
						<span onclick="closeAnswer();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
						<p id="answer-text-1">2개</p>
						<p id="answer-text-2" style="display: none">4송이</p>
						<p id="answer-text-3" style="display: none">3마리</p>
						<audio id="answer-audio" controls>
							<source id="answer-audio-source" src="./audio/Age09/age_09_47_answer_1.wav">
						</audio>
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

var cnt = 0;
	
	//음성 재생
	window.onload = function () {
		var audio = new Audio('./audio/Age09/age_09_47_1.wav');
		audio.load();
		audio.play();
	}
	
	if (performance.navigation.type == 1) {
		$("#rightbtn").css('display', 'inline');
	}
	
	function wrongA(){
		var audio = new Audio('./audio/wrong.mp3');
		audio.play();
	}
	
	function correctA(){
		var audio = new Audio('./audio/correct.mp3');
		audio.play();
	}
	
	function content() {
		if(cnt==3) cnt = 3;
		else cnt = cnt+1;
		  
		  if(cnt == 1){
			$(".Quiz01").css('display', 'none');
			$(".Quiz02").css('display', 'block');
        	$(".gametext01").css('display', 'none');
    		$(".gametext02").css('display', 'inline');  
     	 	$("#leftbtn").css('display', 'inline');
			  $("#answer-text-1").css('display', 'none');
			  $("#answer-text-2").css('display', 'block');
			  $("#answer-text-3").css('display', 'none');
			  $("#answer-audio-source").attr("src" , "./audio/Age09/age_09_47_answer_2.wav");
    		var audio = new Audio('./audio/Age09/age_09_47_2.wav');
    		audio.play();
        }
		  else if(cnt == 2){
			 	$(".Quiz02").css('display', 'none');
				$(".Quiz03").css('display', 'block');
	      		$("#leftbtn").css('display', 'inline');
	      		$("#answer").css('display', 'inline');
			  $("#answer-text-1").css('display', 'none');
			  $("#answer-text-2").css('display', 'none');
			  $("#answer-text-3").css('display', 'block');
			  $("#answer-audio-source").attr("src" , "./audio/Age09/age_09_47_answer_3.wav");
	      		var audio = new Audio('./audio/Age09/age_09_47_2.wav');
	      		audio.play();
	       } 
        else if(cnt == 3){
    			if(!confirm("47번 문항의 게임을 종료하시겠습니까?")){return;}
      	  	location.href = './langTest.jsp';
        }
	}     
	
	function contentBack() {
		if(cnt==0) cnt = 0;
		else cnt = cnt-1;
		  
		  if(cnt == 0){
			 	$(".Quiz01").css('display', 'block');
				$(".Quiz02").css('display', 'none');
	          	$(".gametext01").css('display', 'inline');
	      		$(".gametext02").css('display', 'none');  
	       	 	$("#leftbtn").css('display', 'none');
			  $("#answer-text-1").css('display', 'block');
			  $("#answer-text-2").css('display', 'none');
			  $("#answer-text-3").css('display', 'none');
			  $("#answer-audio-source").attr("src" , "./audio/Age09/age_09_47_answer_1.wav");
	      		var audio = new Audio('./audio/Age09/age_09_47_1.wav');
	      		audio.play();
	          }
			  else if(cnt == 1){
			  	$(".Quiz02").css('display', 'block');
				$(".Quiz03").css('display', 'none');
	      		$("#leftbtn").css('display', 'none');
	      		$("#answer").css('display', 'none');
			  $("#answer-text-1").css('display', 'none');
			  $("#answer-text-2").css('display', 'block');
			  $("#answer-text-3").css('display', 'none');
			  $("#answer-audio-source").attr("src" , "./audio/Age09/age_09_47_answer_2.wav");
	      		var audio = new Audio('./audio/Age09/age_09_47_2.wav');
	      		audio.play();
	       	} 
	      		
		}
	
</script>
<script type="text/javascript" src="js/langGame.js" charset="UTF-8"></script>
</html>