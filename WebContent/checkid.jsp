<%@ page contentType="text/html; charset=UTF-8" %>
<%
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("id");
	boolean result = (boolean) request.getAttribute("result");
%>
<html>
<head>
<title>ID 중복체크</title>
<style>
	#closeBtn{
		border : 1px solid #1a2a3a;
		border-radius : 10px;
		background-color:#ff6666;
		padding : 10px;
		margin : 20px;
		color : white;
		height : 50px;
		width : 150px;
	}
</style>
</head>
<body>
	<div align="center">
		<br/><p><b><%=id%></b>
		<%
			if (result) {
				out.println("는 사용 가능 합니다.</p>");
			} else {
				out.println("는 이미 존재하는 ID입니다.</p>");
			}
		%>
		<button id="closeBtn" href="#" onClick="self.close()">닫기</button>
	</div>
</body>
</html>