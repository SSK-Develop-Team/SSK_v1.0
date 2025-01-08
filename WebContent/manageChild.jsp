<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>아동 계정 관리</title>
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
	<div class="w3-col s12 m12 l10" style="font-weight:bold;font-size:1.2em;padding-left:1em;"><img src="./image/profile.png" style="width:20px;">     아동 계정 관리</div>
	<div class="w3-col w3-hide-small w3-hide-middle l1">&nbsp;</div>
</div>
<div class="w3-row">
	<div class="w3-col w3-hide-small w3-hide-middle l1">&nbsp;</div>
	<div class="w3-col s12 m12 l10">
		<div class="w3-row" style="width:100%;margin-bottom:20px;">
			<div class="search w3-right" style="width: 300px;">
				<%if(keyword!=null){ %>
					<input class="w3-border" type="text" id="keyword" value="<%=keyword%>"placeholder="아동 이름 검색" style="width: 85%;border: 1px solid #bbb;border-radius: 8px;padding: 10px 12px;font-size:0.7em;">
				<%}else{ %>
					<input class="w3-border" type="text" id="keyword" placeholder="아동 이름 검색" style="width: 85%;border: 1px solid #bbb;border-radius: 8px;padding: 10px 12px;font-size:0.7em;">
				<%} %>
				<button style="height:42px;background-color:white;border:none;"onclick="searchKeyword();"><img src="https://s3.ap-northeast-2.amazonaws.com/cdn.wecode.co.kr/icon/search.png" style="width:17px;"></button>
			</div>
			<div class="buttons" style="width:100%;transform:translateY(0.3em);">
				<button class="w3-button w3-right" style="background-color:#51459E; color:white; font-size:0.7em;margin-right:0.7em;" onclick="deleteChild()">선택 계정 삭제</button>
				<button class="w3-button w3-right" style="background-color:#51459E; color:white; font-size:0.7em;margin-right:0.7em;" onclick="updateChild()">선택 계정 수정</button>
				<button class="w3-button w3-right" style="background-color:#51459E; color:white; font-size:0.7em;margin-right:0.7em;" onclick="location.href='register.jsp?role=child';">아동 계정 생성</button>
			</div>
		</div>

		<form method="post" id="manageFrm">
			<%if(currUserList.size()!=0){%>
				<input type="hidden" id="latestChildId" name="latestChildId" value="<%=currUserList.get(0).getUserId()%>"/>
			<%} %>
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
					<%if(currUserList.size()!=0){
						for (int i =0;i<currUserList.size();i++){
					%>
						<tr>
							<td><input type="checkbox" name="childId" value="<%= currUserList.get(i).getUserId() %>" id="check<%= i %>"/></td>
							<td onclick="getChild(<%=currUserList.get(i).getUserId()%>)"><%=(currPageNum-1)*UserPaging.getListRange()+i+1%></td>
							<td onclick="getChild(<%=currUserList.get(i).getUserId()%>)"><%=currUserList.get(i).getUserName() %></td>
							<td onclick="getChild(<%=currUserList.get(i).getUserId()%>)"><%=currUserList.get(i).getUserLoginId() %></td>
							<td onclick="getChild(<%=currUserList.get(i).getUserId()%>)"><%=currUserList.get(i).getUserBirth() %></td>
							<td onclick="getChild(<%=currUserList.get(i).getUserId()%>)"><%=currUserList.get(i).getUserGenderKr() %></td>
							<td onclick="getChild(<%=currUserList.get(i).getUserId()%>)"><%=currUserList.get(i).getRegistrationDate() %></td>
							<td onclick="getChild(<%=currUserList.get(i).getUserId()%>)"><%=currUserList.get(i).getUserEmail() %></td>
						</tr>

					<% }
					}%>
				</table>
				<%if(currUserList.size()==0){%>
				<div class="w3-center w3-panel" style="font-size:1em;">해당하는 아동이 존재하지 않습니다. </div>
				<%} %>
			</div>
			<div class="w3-center">
				<div class="w3-bar">
					<c:set var="uPaging" scope="page" value="${requestScope.userPaging}" />
					<c:set var="curPageNum" scope="page" value="${requestScope.currPageNum}" />
					<c:set var="blockRange" scope="page" value="<%=blockRange%>" />
					<c:set var="searchType" scope="page" value="<%=searchType %>" />
					<c:set var="keyword" scope="page" value="<%=keyword%>" />
					<c:if test="${curPageNum > blockRange && !empty keyword}">
						<a href="GetManageChild?curPage=${uPaging.blockStartNum - 1}&searchType=${searchType}&keyword=${keyword}" class="w3-button">&laquo;</a>
					</c:if>
					<c:if test="${curPageNum > blockRange && empty keyword}">
						<a href="GetManageChild?curPage=${uPaging.blockStartNum - 1}" class="w3-button">&laquo;</a>
					</c:if>
					<c:forEach var="i" begin="${uPaging.blockStartNum}" end="${uPaging.blockEndNum}">
						<c:choose>
							<c:when test="${i>uPaging.lastPageNum}"></c:when>
							<c:when test="${i==curPageNum}">
								<a href="#" class="w3-button w3-gray">${i}</a>
							</c:when>
							<c:when test="${!empty keyword}">
								<a href="GetManageChild?curPage=${i}&searchType=${searchType}&keyword=${keyword}" class="w3-button">${i}</a>
							</c:when>
							<c:otherwise>
								<a href="GetManageChild?curPage=${i}" class="w3-button">${i}</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<c:if test="${uPaging.lastPageNum > uPaging.blockEndNum && !empty keyword}">
						<a href="GetManageChild?curPage=${uPaging.blockEndNum + 1}&searchType=${searchType}&keyword=${keyword}" class="w3-button">&raquo;</a>
					</c:if>
					<c:if test="${uPaging.lastPageNum > uPaging.blockEndNum && empty keyword}">
						<a href="GetManageChild?curPage=${uPaging.blockEndNum + 1}" class="w3-button">&raquo;</a>
					</c:if>
					<c:remove var="uPaging" scope="page"/>
					<c:remove var="curPageNum" scope="page"/>
					<c:remove var="blockRange" scope="page"/>
					<c:remove var="searchType" scope="page"/>
					<c:remove var="keyword" scope="page"/>
				</div>
			</div>
		</form>
	</div>
	<div class="w3-col w3-hide-small w3-hide-middle l1">&nbsp;</div>
</div>
<script>
	function deleteChild(){
		const childCnt = document.querySelectorAll('input[name="childId"]:checked').length;
		if(childCnt==0){
			alert("아동을 선택해주세요.");
		}else{
			if(confirm("선택한 모든 아동을 삭제합니다.")){
				const deleteFrm = document.getElementById('manageFrm');
				deleteFrm.setAttribute("action", "DeleteUser");
				deleteFrm.submit();
			}
		}
	}
	function updateChild(){
		const childCnt = document.querySelectorAll('input[name="childId"]:checked').length;
		if(childCnt==0){
			alert("아동을 선택해주세요.");
		}else if(childCnt==1){
			const updateFrm = document.getElementById('manageFrm');
			updateFrm.setAttribute("action", "GetUpdateUser")
			updateFrm.submit();
		}else{
			if(confirm("마지막으로 선택한 아동의 계정을 수정합니다.")){
				const updateFrm = document.getElementById('manageFrm');
				updateFrm.setAttribute("action", "GetUpdateUser")
				updateFrm.submit();
			}
		}
	}
	function setLatestChildId(childId){
		const latestChildIdInput = document.getElementById("latestChildId");
		latestChildIdInput.value = childId;
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
			frm.setAttribute("action", "GetManageChild");
			
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
	function getChild(childId){

		/*const latestChildIdInput = document.getElementById("latestChildId");
		latestChildIdInput.value = childId;
		
		const updateFrm = document.getElementById('manageFrm');
		updateFrm.setAttribute("action", "GetUserInfo")
		updateFrm.submit();*/
		
		//Get UserInfo로 가는 폼 생성 
		var form= document.createElement("form");
		form.setAttribute("method", "post");
		form.setAttribute("action","GetUserInfo");
		//childId를 이름으로 하는 input(parameter) 태그 생성 후, childId를 value로 넣어준다. 
		var childIdInput  = document.createElement("input");
		childIdInput .setAttribute("type", "hidden");
		childIdInput .setAttribute("name", "childId");
		childIdInput .setAttribute("value", childId);
	    form.appendChild(childIdInput);
		//폼 제출
		document.body.appendChild(form);		
		form.submit();

	}

</script>
</body>
</html>