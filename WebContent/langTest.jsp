<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="model.dto.LangQuestion"%>
<%@ page import="model.dto.User"%>

<%
	String currNumStr="";

	ArrayList<LangQuestion> currQuestionList = (ArrayList<LangQuestion>)session.getAttribute("currQuestionList");
	
	ArrayList<Integer> langProgList = new ArrayList<Integer>();
	if(session.getAttribute("langProgList") != null) langProgList = (ArrayList)session.getAttribute("langProgList");
	else {
		for(int i=0; i<5; i++){
			langProgList.add(0);
		}
	}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>언어 발달 평가</title>
<style>
.select {
	font-size: 1rem;
	text-align: center;
}

.btn{
      border : 1px solid #1a2a3a;
      border-radius : 10px;
      background-color:#1a2a3a;
      padding : 0;
      margin : 8px;
      color : white;
      height : 50px;
      width : 150px;
   }
   
.textBox{
	font-size : 1.2em;
	text-align: center;
}
#reply{
	margin:0.75em;
}
.reply-content{
	font-size: 0.9rem;
}

</style>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>
<body>
	<%@ include file="sidebar.jsp" %>
	<div class="container">
		<div class="w3-row">
			<div class="w3-col w3-hide-small m1 l1">&nbsp;</div>
			<div class="w3-col s12 m10 l10">
				<div class="w3-panel">
					<div style="font-weight:bold; font-size : 1.8em;">언어 발달 평가</div>
					<h6>질문에 답하기 어려울 경우, 직접 평가(게임)를 하고 다시 돌아와 결정해주세요.</h6>
				</div>
				<form method="post" action="DoLangTest">

					<%
						for(int i=0; i < currQuestionList.size(); i++){

							int gameID = currQuestionList.get(i).getLangQuestionId();
							if(gameID == 50) continue;
							if(gameID < 10) currNumStr = "Q0" + gameID;
							else currNumStr = "Q" + gameID;
					%>

					<div class="w3-container w3-margin-top w3-margin-bottom">
						<div>
							<div class="testNum"><b><%=currNumStr%></b></div>
							<div class="w3-row w3-margin">
								<div class="w3-col s12 m9 l10" style="background: #F4F4F4;">
									<div class="w3-margin w3-center question-content"><%="우리 아이는 "  + currQuestionList.get(i).getLangQuestionContent() %></div>
								</div>
								<div class="w3-col s5 m3 l2 w3-right" style="padding-top:16px;padding-bottom:16px;padding-left:8px;">
									<div class="w3-button w3-round-large w3-padding" style="background-color:#51459E;color:white;padding:0px;" onclick="selectGame(<%=gameID%>);">게임하고 오기</div>
								</div>
							</div>
						</div>
						<div class="w3-row w3-margin">
							<div class="w3-col m3 l3">
								<%if(langProgList.get(i) == 1){ %> <input type="radio" class="reply w3-radio" name="reply<%=i%>" value="1" checked="checked">
								<%} else {%><input type="radio" class="reply w3-radio" name="reply<%=i%>" value="1"> <%} %>
								<label class="reply-content">전혀 못한다 (0%)</label>
							</div>
							<div class="w3-col m3 l3">
								<%if(langProgList.get(i) == 2){ %> <input type="radio" class="reply w3-radio" name="reply<%=i%>" value="2" checked="checked">
								<%} else {%><input type="radio" class="reply w3-radio" name="reply<%=i%>" value="2"> <%} %>
								<label class="reply-content">조금 할 수 있다 (30%)</label>
							</div>
							<div class="w3-col m2 l2">
								<%if(langProgList.get(i) == 3){ %> <input type="radio" class="reply w3-radio" class="reply" name="reply<%=i%>" value="3" checked="checked">
								<%} else {%><input type="radio" class="reply w3-radio" name="reply<%=i%>" value="3"> <%} %>
								<label class="reply-content">잘한다 (60%)</label>
							</div>
							<div class="w3-col m3 l3">
								<%if(langProgList.get(i) == 4){ %> <input type="radio" class="reply w3-radio" name="reply<%=i%>" value="4" checked="checked">
								<%} else {%><input type="radio" class="reply w3-radio" name="reply<%=i%>" value="4"> <%} %>
								<label class="reply-content">매우 잘한다 (100%)</label>
							</div>
							<div class="w3-col w3-hide-small m2 l2"></div>
						</div>
					</div>
					<%} %>
					<p>
					<div class="btnBox" style="float : right; margin-top : 3%;">
						<input type="button" class="btn" id="cancelLang" value="취소" onClick="location.href='langTestMain.jsp'">
						<input type="submit" class="btn" id="submitLang" value="결과 제출">
					</div>
				</form>
			</div>
			<div class="w3-col w3-hide-small m1 l1">&nbsp;</div>
		</div>
	</div>
</body>

<script src="https://code.jquery.com/jquery-3.6.0.slim.js"></script>
<script>

		
		var reply1 = "<%=langProgList.get(0)%>";
		var reply2 = "<%=langProgList.get(1)%>";
		var reply3 = "<%=langProgList.get(2)%>";
		var reply4 = "<%=langProgList.get(3)%>";
		var reply5 = "<%=langProgList.get(4)%>";

		if(50 == <%=currQuestionList.get(4).getLangQuestionId()%>){
			reply5 = "0";
		}
		
		$("input[name='reply0']").change(function(){
			if($("input[name='reply0']:checked")){
				reply1 = $("input[name='reply0']:checked").val();
			}
		});
		$("input[name='reply1']").change(function(){
			if($("input[name='reply1']:checked")){
				reply2 = $("input[name='reply1']:checked").val();
			}
		});
		$("input[name='reply2']").change(function(){
			if($("input[name='reply2']:checked")){
				reply3 = $("input[name='reply2']:checked").val();
			}
		});
		$("input[name='reply3']").change(function(){
			if($("input[name='reply3']:checked")){
				reply4 = $("input[name='reply3']:checked").val();
			}
		});
		$("input[name='reply4']").change(function(){
			if($("input[name='reply4']:checked")){
				reply5 = $("input[name='reply4']:checked").val();
			}
		});


	function selectGame(param){
		var form = document.createElement('form');
		form.setAttribute('method','post');
		form.setAttribute('action', 'GetLangGame');
		document.charset = "utf-8";

		var input = document.createElement('input');
		input.setAttribute('type', 'hidden');
		input.setAttribute('name', 'langGameID');
		input.setAttribute('value', param);
		
		var inputReply1 = document.createElement('input');
		inputReply1.setAttribute('type', 'hidden');
		inputReply1.setAttribute('name', 'langTestProgress1');
		inputReply1.setAttribute('value', reply1);
		
		var inputReply2 = document.createElement('input');
		inputReply2.setAttribute('type', 'hidden');
		inputReply2.setAttribute('name', 'langTestProgress2');
		inputReply2.setAttribute('value', reply2);
		
		var inputReply3 = document.createElement('input');
		inputReply3.setAttribute('type', 'hidden');
		inputReply3.setAttribute('name', 'langTestProgress3');
		inputReply3.setAttribute('value', reply3);
		
		var inputReply4 = document.createElement('input');
		inputReply4.setAttribute('type', 'hidden');
		inputReply4.setAttribute('name', 'langTestProgress4');
		inputReply4.setAttribute('value', reply4);
		
		var inputReply5 = document.createElement('input');
		inputReply5.setAttribute('type', 'hidden');
		inputReply5.setAttribute('name', 'langTestProgress5');

		inputReply5.setAttribute('value', reply5);

		form.appendChild(input);
		form.appendChild(inputReply1);
		form.appendChild(inputReply2);
		form.appendChild(inputReply3);
		form.appendChild(inputReply4);
		form.appendChild(inputReply5);

		document.body.appendChild(form);
		form.submit(); 		
		
	}	
	

</script>

</html>
