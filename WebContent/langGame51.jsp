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
	<style>
		.roomBack {
			display : block;
			height : 100%;
			width : 100%;
			position : relative;
		}

		.playBack {
			display : none;
			height : 100%;
			width : 100%;
			position : relative;
		}

		.snowBack {
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
			left : 25%;
			top : 16%;
		}
		.selectImg03{
			position : absolute;
			left : 62%;
			top : 23%;
		}
		.selectImg04{
			position : absolute;
			left : 72%;
			top : 16%;
		}


		.selectImg05{
			position : absolute;
			left : 37%;
			top : 13%;
		}
		.selectImg06{
			position : absolute;
			left : 32%;
			top : 67%;
		}
		.selectImg07{
			position : absolute;
			left : 83%;
			top : 13%;
		}
		.selectImg08{
			position : absolute;
			left : 77%;
			top : 67%;
		}


		.selectImg09{
			position : absolute;
			left : 10%;
			top : 48%;
		}
		.selectImg10{
			position : absolute;
			left : 25%;
			top : 55%;
		}
		.selectImg11{
			position : absolute;
			left : 55%;
			top : 48%;
		}
		.selectImg12{
			position : absolute;
			left : 74%;
			top : 55%;
		}
		#hint-text-2 {
			display : none;
		}
		#hint-text-3 {
			display : none;
		}
	</style>
	<title>51번 문항 직접 평가</title>
</head>
<body>
<%@ include file="sidebar.jsp" %>

<div class="w3-row">
	<div class="w3-col w3-hide-small m1 l3">&nbsp;</div>
	<div class="w3-col w3-container s12 m10 l6">
		<div style="font-size:1em;font-weight:bold;">직접평가 #51</div>
		<div>
			<div class="roomBack">
				<img class="gameImg01" src="./image/LangGameImg/Age10/age10_1_1.png" width="100%" height="100%" alt="거실의 틀린그림 찾기, 가운데 쇼파가 있고 회색 벽돌로 된 벽에는 세 개의 액자가 걸려있다."/>
				<img class="selectImg01" src="./image/LangGameImg/Age10/age10_1_1_icon01.png" width="5%" alt="로켓" onclick="openSelect01();audio01.pause();" />
				<img class="selectImg02" src="./image/LangGameImg/Age10/age10_1_1_icon02.png" width="5%" alt="분홍색 리본" onclick="openSelect02();audio01.pause();" />
				<img class="selectImg03" src="./image/LangGameImg/Age10/age10_1_1_icon03.png" width="5%" alt="로봇" onclick="openSelect01();audio01.pause();" />
				<img class="selectImg04" src="./image/LangGameImg/Age10/age10_1_1_icon04.png" width="5%" alt="빨간색 리본" onclick="openSelect02();audio01.pause();" />
			
				<div id="select-modal01" class="w3-modal">
					<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">

						<div class="w3-container w3-center">
							<span onclick="closeSelect01()" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
							<img src="./image/LangGameImg/Age10/age10_1_1_modal01.png" width="90%" style="margin-top : 5%">
							<audio id="select-audio01">
								<source src="./audio/Age10/age_10_51_2.wav">
							</audio>
						</div>
					</div>
				</div>
				
				<div id="select-modal02" class="w3-modal">
					<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
						<div class="w3-container w3-center">
							<span onclick="closeSelect02();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
							<img src="./image/LangGameImg/Age10/age10_1_1_modal02.png" width="90%" style="margin-top : 5%">
							<audio id="select-audio02">
								<source src="./audio/Age10/age_10_51_2.wav">
							</audio>
						</div>
					</div>
				</div>
			
			</div>
			
			<div class="playBack">
				<img class="gameImg02" src="./image/LangGameImg/Age10/age10_1_2.png" width="100%" height="100%" alt="놀이터의 틀린그림 찾기, 가운데 코끼리 모양 미끄럼틀이 있고 여자아이와 남자아이가 놀고 있다."/>
				<img class="selectImg05" src="./image/LangGameImg/Age10/age10_1_2_icon01.png" height="12%" alt="비행기" onClick="openSelect03();audio01.pause();" />
				<img class="selectImg06" src="./image/LangGameImg/Age10/age10_1_2_icon02.png" height="20%" alt="모래놀이" onClick="openSelect04();audio01.pause();" />
				<img class="selectImg07" src="./image/LangGameImg/Age10/age10_1_2_icon03.png" height="12%" alt="파랑새" onclick="openSelect03();audio01.pause();" />
				<img class="selectImg08" src="./image/LangGameImg/Age10/age10_1_2_icon04.png" height="25%" alt="시소" onclick="openSelect04();audio01.pause();" />
				
				<div id="select-modal03" class="w3-modal">
					<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
						<div class="w3-container w3-center">
							<span onclick="closeSelect03();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
							<img src="./image/LangGameImg/Age10/age10_1_2_modal03.png" width="90%" style="margin-top : 5%">
							<audio id="select-audio03">
								<source src="./audio/Age10/age_10_51_2.wav">
							</audio>
						</div>
					</div>
				</div>
				
				<div id="select-modal04" class="w3-modal">
					<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
						<div class="w3-container w3-center">
							<span onclick="closeSelect04();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
							<img src="./image/LangGameImg/Age10/age10_1_2_modal04.png" width="90%" style="margin-top : 5%">
							<audio id="select-audio04">
								<source src="./audio/Age10/age_10_51_2.wav">
							</audio>
						</div>
					</div>
				</div>
			</div>
		

			<div class="snowBack">
				<img class="gameImg03" src="./image/LangGameImg/Age10/age10_1_3.png" width="100%" height="100%" alt="눈 쌓인 언덕의 틀린그림 찾기, 눈이 펑펑 내리고 뒤로는 큰 보름달이 떠있다."/>
				<img class="selectImg09" src="./image/LangGameImg/Age10/age10_1_3_icon01.png" height="23%" alt="눈사람" onclick="openSelect05();audio01.pause();" />
				<img class="selectImg10" src="./image/LangGameImg/Age10/age10_1_3_icon02.png" height="23%" alt="썰매와 산타" onclick="openSelect06();audio01.pause();" />
				<img class="selectImg11" src="./image/LangGameImg/Age10/age10_1_3_icon03.png" height="23%" alt="북극곰" onclick="openSelect05();audio01.pause();" />
				<img class="selectImg12" src="./image/LangGameImg/Age10/age10_1_3_icon04.png" height="23%" alt="썰매와 산타" onclick="openSelect06();audio01.pause();" />
			
				<div id="select-modal05" class="w3-modal">
					<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
						<div class="w3-container w3-center">
							<span onclick="closeSelect05();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
							<img src="./image/LangGameImg/Age10/age10_1_3_modal05.png" width="80%" style="margin-top : 5%">
							<audio id="select-audio05">
								<source src="./audio/Age10/age_10_51_2.wav">
							</audio>
						</div>
					</div>
				</div>
				
				<div id="select-modal06" class="w3-modal">
					<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
						<div class="w3-container w3-center">
							<span onclick="closeSelect06();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
							<img src="./image/LangGameImg/Age10/age10_1_3_modal06.png" width="90%" style="margin-top : 5%">
							<audio id="select-audio06">
								<source src="./audio/Age10/age_10_51_2.wav">
							</audio>
						</div>
					</div>
				</div>
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
	            <p>답은 틀려도 괜찮아요. <br>그림속 단어를 정확한 발음으로 말할 수 있는지 확인해주세요.<br></p>
	          </div>
	        </div>
	      </div>
		
			<button class="w3-button w3-round-large" onclick="openAnswer();" style="background-color:#12192C; color:white; text-align:center;font-size:0.9em;margin-right:5px;">정답 확인하기</button>
			<div id="answer-modal" class="w3-modal">
				<div class="w3-modal-content w3-animate-opacity w3-round-large modal-content">
					<div class="w3-container w3-center">
						<span onclick="closeAnswer();" class="w3-button w3-display-topright w3-round-xxlarge">&times;</span>
						<p id="hint-text-1">로봇, 로켓, 리본, 빨간색, 분홍색</p>
						<p id="hint-text-2">새, 시소, 삽</p>
						<p id="hint-text-3">썰매, 산타(싼타), 눈사람</p>
						<audio id="answer-audio" controls>
							<source id="hint-audio" src="./audio/Age10/age_10_51_answer_1.wav">
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
var audio01 = new Audio('./audio/Age10/age_10_51_1.wav');
	
//음성 재생
window.onload = function () {
	audio01.load();
	audio01.play();
}

if (performance.navigation.type == 1) {
$("#rightbtn").css('display', 'inline');
}

function gameBtnHide(){
$("#rightbtn").css('display', 'none');
$("#leftbtn").css('display', 'none');
}

function content() {
	if(cnt==4) cnt = 3;
    else cnt = cnt+1;

	if(cnt == 1){
	    audio01.pause();
	    $(".roomBack").css('display', 'none');
	    $(".playBack").css('display', 'block');
	    $("#leftbtn").css('display', 'inline');
	    $("#hint-text-1").css('display', 'none');
	    $("#hint-text-2").css('display', 'block');
	    $("#hint-text-3").css('display', 'none');
		$("#hint-audio").attr("src" , "./audio/Age10/age_10_51_answer_2.wav");
	    audio01.load();
		audio01.play();
	}
	else if(cnt == 2){
		audio01.pause();
	    $(".playBack").css('display', 'none');
	    $(".snowBack").css('display', 'block');
	    $("#leftbtn").css('display', 'inline');
	    $("#answer").css('display', 'inline');
	    $("#hint-text-1").css('display', 'none');
	    $("#hint-text-2").css('display', 'none');
	    $("#hint-text-3").css('display', 'block');
		$("#hint-audio").attr("src" , "./audio/Age10/age_10_51_answer_3.wav");
	    audio01.load();
		audio01.play();
	}
	else if(cnt == 3){
		if(!confirm("51번 문항의 게임을 종료하시겠습니까?")){cnt = 2; return}
		location.href = './langTest.jsp';
	}
}

function contentBack() {
	if(cnt==0) cnt = 0;
	else cnt = cnt-1;

  if(cnt == 0){
	  audio01.pause();
	  $(".roomBack").css('display', 'block');
	  $(".playBack").css('display', 'none');
	  $("#answer").css('display', 'none');
	  $("#leftbtn").css('display', 'none');
	  $("#hint-text-1").css('display', 'block');
	  $("#hint-text-2").css('display', 'none');
	  $("#hint-text-3").css('display', 'none');
	  $("#hint-audio").attr("src" , "./audio/Age10/age_10_51_answer_1.wav");
	  audio01.load();
	  audio01.play();
  }
  else if(cnt == 1){
	  audio01.pause();
	  $(".playBack").css('display', 'block');
	  $(".snowBack").css('display', 'none');
	  $("#answer").css('display', 'none');
	  $("#hint-text-1").css('display', 'none');
	  $("#hint-text-2").css('display', 'block');
	  $("#hint-text-3").css('display', 'none');
	  $("#hint-audio").attr("src" , "./audio/Age10/age_10_51_answer_2.wav");
	  audio01.load();
	  audio01.play();
}

}
	
</script>
<script type="text/javascript" src="js/langGame.js" charset="UTF-8"></script>
<script type="text/javascript" src="js/langGameModal.js" charset="UTF-8"></script>
</html>