<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>전문가 HOME - 아동별 결과 조회</title>
</head>
<body>
<%@ include file = "sidebar.jsp" %>
<%
	ArrayList<User> currUserList = (ArrayList<User>)request.getAttribute("currUserList");
	int currPageNum = (int)request.getAttribute("currPageNum");
	int blockRange = UserPaging.getBlockRange();
	String searchType="name";
	String keyword = null;
	if(request.getAttribute("searchType")!=null&&request.getAttribute("keyword")!=null){
		searchType = (String)request.getAttribute("searchType");
		keyword = (String)request.getAttribute("keyword");
	}

%>
<div class="w3-row">
	<div class="w3-col w3-hide-small w3-hide-middle l1">&nbsp;</div>
	<div class="w3-col s12 m12 l10" style="font-weight:bold;font-size:1.2em;padding-left:1em;"><img src="./image/research.png" style="width:35px;">     아동별 결과 조회</div>
	<div class="w3-col w3-hide-small w3-hide-middle l1">&nbsp;</div>
</div>
<form method="post" action="ExportChildListResultExcelByTest"id="exportFrm">
<div class="w3-row">
	<div class="w3-col w3-hide-small m1 l4">&nbsp;</div>
	<div class="select-box w3-col s12 m11 l7 w3-padding"style="margin-bottom:0.5em;">
		<div class="w3-row" style="font-weight:bold;font-size:0.8em;margin-left:0.3em; margin-bottom:0.3em;">카테고리<span class="w3-right" style="font-weight:100;font-size:0.5em;">아동과 카테고리를 선택하고 excel export 버튼을 누르세요. 검사 별로 엑셀 파일(.xlsx)이 생성됩니다.</span></div>
		<div class="w3-row w3-padding" style="background-color:#D9D9D9;font-size:0.8em;">
			<div class="w3-col s8 m10 l10 w3-row">
				<div class="w3-quarter w3-padding"><input type="checkbox" name="category" value="lang" style="transform:translateY(0.1em);">&nbsp;<label>언어 발달 검사</label></div>
				<div class="w3-quarter w3-padding"><input type="checkbox" name="category" value="sdq" style="transform:translateY(0.1em);">&nbsp;<label>정서 행동 발달 검사</label></div>
				<div class="w3-quarter w3-padding"><input type="checkbox" name="category" value="esm" style="transform:translateY(0.1em);">&nbsp;<label>정서 반복 기록</label></div>
				<div class="w3-quarter w3-padding"><input type="checkbox" name="category" value="esmRecord" style="transform:translateY(0.1em);">&nbsp;<label>정서 다이어리</label></div>
			</div>
			<div class="w3-col s4 m2 l2">
				<div class="w3-button w3-padding" style="background-color:#51459E; color:white;" onclick="exportData();"> excel export </div>
			</div>
		</div>
	</div>
	<div class="w3-col w3-hide-small w3-hide-middle l1">&nbsp;</div>
</div>

<div class="w3-row">
	<div class="w3-col w3-hide-small w3-hide-middle l1">&nbsp;</div>
	<div class="w3-col s12 m12 l10">
		<div style="width:100%;margin-bottom:50px;">
		<div class="search w3-right" style="width: 300px;">
		 <%if(keyword!=null){ %>
					<input class="w3-border" type="text" id="keyword" value="<%=keyword%>"placeholder="아동 이름 검색" style="width: 85%;border: 1px solid #bbb;border-radius: 8px;padding: 10px 12px;font-size:0.7em;">
				<%}else{ %>
					<input class="w3-border" type="text" id="keyword" placeholder="아동 이름 검색" style="width: 85%;border: 1px solid #bbb;border-radius: 8px;padding: 10px 12px;font-size:0.7em;">
				<%} %>
				<div class="w3-button" style="display:inline-block;width:20px;padding-left:0;height:42px;background-color:white;border:none;"onclick="searchKeyword();"><img src="https://s3.ap-northeast-2.amazonaws.com/cdn.wecode.co.kr/icon/search.png" style="width:20px;"></div>
		</div>
		</div>
		<div class="w3-container" style="font-size:0.5em;">* 정렬 기준 : 등록일 순</div>
		<div class="w3-container">
		  <table class="w3-table-all w3-hoverable" style="font-size:0.8em;">
		    <thead>
		      <tr class="w3-light-grey">
				  <th><input type="checkbox" id="checkChildIdAll" value="0" onclick="selectChildAll(this)"/></th>
				  <th>NO.</th>
				  <th>이름</th>
				  <th>아이디</th>
				  <th>생년월일</th>
				  <th>성별</th>
				  <th>등록일</th>
				  <th>이메일</th>
		      </tr>
		    </thead>
		    <%for (int i =0;i<currUserList.size();i++){
			%>
			<tr>
				<td><input type="checkbox" name="childId" value="<%=currUserList.get(i).getUserId()%>"/></td>
		      	<td onclick = "location.href='GoToChildHome?childId=<%=currUserList.get(i).getUserId()%>';"><%=(currPageNum-1)*UserPaging.getListRange()+i+1%></td>
		      	<td onclick = "location.href='GoToChildHome?childId=<%=currUserList.get(i).getUserId()%>';"><%=currUserList.get(i).getUserName() %></td>
		      	<td onclick = "location.href='GoToChildHome?childId=<%=currUserList.get(i).getUserId()%>';"><%=currUserList.get(i).getUserLoginId() %></td>
		      	<td onclick = "location.href='GoToChildHome?childId=<%=currUserList.get(i).getUserId()%>';"><%=currUserList.get(i).getUserBirth() %></td>
				<td onclick = "location.href='GoToChildHome?childId=<%=currUserList.get(i).getUserId()%>';"><%=currUserList.get(i).getUserGenderKr() %></td>
				<td onclick = "location.href='GoToChildHome?childId=<%=currUserList.get(i).getUserId()%>';"><%=currUserList.get(i).getRegistrationDate() %></td>
		      	<td onclick = "location.href='GoToChildHome?childId=<%=currUserList.get(i).getUserId()%>';"><%=currUserList.get(i).getUserEmail() %></td>
		    </tr>
			<% }%>
		  </table>
		</div>
		<div class="w3-center">
		<div class="w3-bar">
			<c:set var="uPaging" scope="page" value="${requestScope.userPaging}" />
					<c:set var="curPageNum" scope="page" value="${requestScope.currPageNum}" />
					<c:set var="blockRange" scope="page" value="<%=blockRange%>" />
					<c:set var="searchType" scope="page" value="<%=searchType %>" />
					<c:set var="keyword" scope="page" value="<%=keyword%>" />
					<c:if test="${curPageNum > blockRange && !empty keyword}">
						<a href="GetExpertHome?curPage=${uPaging.blockStartNum - 1}&searchType=${searchType}&keyword=${keyword}" class="w3-button">&laquo;</a>
					</c:if>
					<c:if test="${curPageNum > blockRange && empty keyword}">
						<a href="GetExpertHome?curPage=${uPaging.blockStartNum - 1}" class="w3-button">&laquo;</a>
					</c:if>
					<c:forEach var="i" begin="${uPaging.blockStartNum}" end="${uPaging.blockEndNum}">
						<c:choose>
							<c:when test="${i>uPaging.lastPageNum}"></c:when>
							<c:when test="${i==curPageNum}">
								<a href="#" class="w3-button w3-gray">${i}</a>
							</c:when>
							<c:when test="${!empty keyword}">
								<a href="GetExpertHome?curPage=${i}&searchType=${searchType}&keyword=${keyword}" class="w3-button">${i}</a>
							</c:when>
							<c:otherwise>
								<a href="GetExpertHome?curPage=${i}" class="w3-button">${i}</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<c:if test="${uPaging.lastPageNum > uPaging.blockEndNum && !empty keyword}">
						<a href="GetExpertHome?curPage=${uPaging.blockEndNum + 1}&searchType=${searchType}&keyword=${keyword}" class="w3-button">&raquo;</a>
					</c:if>
					<c:if test="${uPaging.lastPageNum > uPaging.blockEndNum && empty keyword}">
						<a href="GetExpertHome?curPage=${uPaging.blockEndNum + 1}" class="w3-button">&raquo;</a>
					</c:if>
					<c:remove var="uPaging" scope="page"/>
					<c:remove var="curPageNum" scope="page"/>
					<c:remove var="blockRange" scope="page"/>
					<c:remove var="searchType" scope="page"/>
					<c:remove var="keyword" scope="page"/>
		</div>
		</div>
	</div>
	<div class="w3-col w3-hide-small w3-hide-middle l1">&nbsp;</div>	
</div>
</form>
<script>
function exportData(){
	const categoryCnt = document.querySelectorAll("input[name='category']:checked").length;
	const childCnt = document.querySelectorAll("input[name='childId']:checked").length;
	if(categoryCnt==0){//미선택 예외 처리
		alert("카테고리를 선택해주세요.");
	}else if(childCnt==0){
		alert("아동을 선택해주세요.");
	}else{
		document.getElementById('exportFrm').submit();
	}
}

function selectChildAll(selectChildAll){
	const checkboxes = document.getElementsByName('childId');
	checkboxes.forEach((checkbox) => {
		  checkbox.checked = selectChildAll.checked; 
		})
}
function searchKeyword(){
	const keyword = document.getElementById("keyword").value;
	const searchType="name";
	
	if(keyword==null){
		alert("검색어를 입력해주세요.");
		
	}else{
		var frm = document.createElement("form");
		var element1 = document.createElement("input");
		var element2 = document.createElement("input");
		
		frm.setAttribute("method", "post")
		frm.setAttribute("action", "GetExpertHome");
		
		element1.type="hidden";
		element1.name = "searchType";
		element1.value = searchType;
		frm.appendChild(element1);
		
		element2.type="hidden";
		element2.name = "keyword";
		element2.value = keyword;
		frm.appendChild(element2);
		
		document.body.appendChild(frm);
		
		frm.submit();
	}
}
</script>
</body>
</html>