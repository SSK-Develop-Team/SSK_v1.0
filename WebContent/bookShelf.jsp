<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="model.dto.Book" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
  
  <!-- markdown to HTML rendering -->
  <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/dompurify@3.0.6/dist/purify.min.js"></script>

  <style>
	.pageWrap{padding: 0px 12px 28px;}
	.badgeRow{display:flex; justify-content:flex-start; align-items:flex-end; margin: 6px 5% 24px;}
	.badge{ width: clamp(70px, 18vw, 140px); height:auto; display:block; }
	.tableRow{margin: 0 auto;}
	.cardWrap{background:#fff; border:1px solid #ddd; border-radius:12px; overflow:hidden;}
	
	/* table 스크롤 */
    .tableScroll {width: 100%; overflow-x: auto;  -webkit-overflow-scrolling: touch;}
    table.bookTable {width: 100%; min-width: 980px;}

    .th {background:#f3f6f6; font-weight:800; white-space: nowrap;}
    .cellTitle { max-width: 280px; }
    .cellWorry { max-width: 320px; }
    .cellTitle, .cellWorry {word-break: break-word;white-space: normal;}
    .cellNo, .cellDate { white-space: nowrap; }
    
    /* 버튼 */
    .btn { border-radius:10px; }
    .btn-detail { background:#bdbdbd; color:#fff; }
    .btn-go { background:#4e9aa0; color:#fff; }
    .btn-del { background:#d77a73; color:#fff; }
    .btnRow {display:flex; justify-content:flex-end; gap:10px; flex-wrap:wrap; white-space: nowrap;}

    /* 모달 스크롤 */
	#detailModal .w3-modal-content{max-height: 85vh; overflow: hidden;display: flex;flex-direction: column;}
	#dGuideWrap{max-height: none; overflow-y: auto; overflow-x: hidden;border-left: 0;}
	#detailModal .modal-content {flex: 1;min-height: 0;overflow: hidden;display: flex;flex-direction: column;border: 1px solid #2E6D71;}
    #detailModal .modal-content > div {flex-shrink: 0;border-bottom: 1px solid #2E6D71;}
    #detailModal .modal-content > div:last-child {flex: 1;min-height: 0;border-bottom: none;}

    
   /* 부모가이드 서식 */
	#dGuide {white-space: normal; line-height: 1.45; font-size: 15px;}
	
	#dGuide h1 { font-weight:bold; font-size: 24px; margin: 0 0 10px; }
	#dGuide h2 { font-weight:bold; font-size: 22px; margin: 5px 0; }
	#dGuide h3 { font-weight:bold; font-size: 18px; margin: 18px 0 12px 0; }
	
	#dGuide p { margin: 18px 0 12px 0; }
	#dGuide ul { margin: 6px 0 12px 18px; padding: 0; }
	#dGuide ol { margin: 6px 0 6px 18px; padding: 0; }
	#dGuide li { margin: 5px 0; }
	
	#dGuide hr { margin: 12px 0; }
    
  </style>
</head>

<body>
<%@ include file="sidebar.jsp" %>

<%
  ArrayList<Book> books = (ArrayList<Book>) request.getAttribute("books");
  if (books == null) books = new ArrayList<>();
%>

<div class="pageWrap">

  <!-- 배지  -->
  <div class="w3-content badgeRow" style="max-width:1100px;">
    <img class="badge" src="<%= request.getContextPath() %>/image/bookShelf_badge.png" alt="내 책장" onerror="this.style.display='none'">
  </div>

  <!-- 테이블 -->
  <div class="w3-content tableRow" style="max-width:1100px;">
    <div class="cardWrap">
      <div class="tableScroll">
        <table class="w3-table w3-striped bookTable">
        
          <thead>
            <tr class="th">
              <th style="width:70px;">No.</th>
              <th>동화 제목</th>
              <th>아동의 고민</th>
              <th style="width:150px;">동화 생성 날짜</th>
              <th style="width:360px;"></th>
            </tr>
          </thead>

          <tbody>
          <%
            int no = 1;
            for (Book b : books) {
              String title = (b.getStoryTitle() == null || b.getStoryTitle().trim().isEmpty()) ? "동화책" : b.getStoryTitle();
              String worry = (b.getSelGoalLabel() == null || b.getSelGoalLabel().trim().isEmpty()) ? "-" : b.getSelGoalLabel();
              String created = (b.getCreatedTime() == null) ? "-" : b.getCreatedTime().toString().substring(0, 10);
          %>
            <tr>
              <td class="cellNo"><%= no++ %></td>
              <td class="cellTitle"><%= title %></td>
              <td class="cellWorry"><%= worry.length() > 40 ? worry.substring(0, 40) + "..." : worry %></td>
              <td class="cellDate"><%= created %></td>
              <td>
                <div class="btnRow">
                  <button class="w3-button btn btn-detail" onclick="openDetail(
                      <%= b.getBookId() %>,'<%= title.replace("\\", "\\\\").replace("'", "\\'") %>', '<%= created %>',
                      '<%= (b.getStoryElements()==null? "": b.getStoryElements()).replace("\\", "\\\\").replace("'", "\\'") %>',
                      '<%= (b.getSelGoalLabel()==null? "": b.getSelGoalLabel()).replace("\\", "\\\\").replace("'", "\\'") %>',
                      '<%= (b.getSituationSummary()==null? "": b.getSituationSummary()).replace("\\", "\\\\").replace("'", "\\'") %>'
                    )">세부 사항</button>

                  <!-- <a class="w3-button btn btn-go" href="GetBookView?bookId=<%= b.getBookId() %>&page=1">동화책 보러 가기</a> -->
                  
                  <%
   					 boolean hasVideo = b.getTtsClip() != null && !b.getTtsClip().trim().isEmpty();
    				String viewUrl = hasVideo
        				? "GetBookVideo?bookId=" + b.getBookId()
        				: "GetBookView?bookId=" + b.getBookId() + "&page=1";
    				String viewLabel = hasVideo ? "동화책 보러 가기" : "동화책 읽기";
				%>

				<a class="w3-button btn btn-go" href="<%= viewUrl %>">
    				<%= viewLabel %>
				</a>	
				
                  <button class="w3-button btn btn-del" onclick="deleteBook(<%= b.getBookId() %>)">삭제</button>
                </div>
              </td>
            </tr> 
            
            <%} if (books.isEmpty()) {%>
            <tr>
              <td colspan="5" style="text-align:center; padding:30px;">생성된 동화책이 없습니다.</td>
            </tr>
            <% } %>
          
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>

<!-- 상세 모달 -->
<div id="detailModal" class="w3-modal" style="display:none;">
  <div class="w3-modal-content w3-round-large" style="max-width: 980px; padding: 18px;">
    <div style="display:flex; justify-content:space-between; align-items:center; position:relative;">
      <div style="font-size:22px; font-weight:900; text-align:center; width:100%;">세부 사항</div>
      <button class="w3-button" onclick="closeDetail()" style="position:absolute; right:0;">✕</button>
    </div>

    <div class="modal-content" style="margin-top:14px; border:1px solid #4e9aa0;">
      <div style="display:flex;">
        <div style="width:200px; background:#4e9aa0; color:#fff; font-weight:800; padding:14px;">동화 제목</div>
        <div id="dTitle" style="flex:1; padding:14px;"></div>
      </div>
      <div style="display:flex;">
        <div style="width:200px; background:#4e9aa0; color:#fff; font-weight:800; padding:14px;">동화 생성 날짜</div>
        <div id="dDate" style="flex:1; padding:14px;"></div>
      </div>
      <div style="display:flex;">
        <div style="width:200px; background:#4e9aa0; color:#fff; font-weight:800; padding:14px;">아동 선호 캐릭터</div>
        <div id="dElements" style="flex:1; padding:14px;"></div>
      </div>
      <div style="display:flex;">
        <div style="width:200px; background:#4e9aa0; color:#fff; font-weight:800; padding:14px;">아동의 고민</div>
        <div id="dWorry" style="flex:1; padding:14px; white-space:pre-wrap;"></div>
      </div>
      <div style="display:flex;">
        <div style="width:200px; background:#4e9aa0; color:#fff; font-weight:800; padding:14px;">부모 가이드</div>
  		<div id="dGuideWrap" style="flex:1; padding:14px;"><div id="dGuide"></div></div>
      </div>
    </div>
  </div>
</div>

<script>
  const CTX = "<%= request.getContextPath() %>";

  function openDetail(bookId, title, date, elements, selLabel, situationSummary) {
    document.getElementById("detailModal").style.display = "block";
    document.getElementById("dTitle").textContent = title || "-";
    document.getElementById("dDate").textContent = date || "-";
    document.getElementById("dElements").textContent = elements || "-";
    let worryText = "";

    if (selLabel) {
        worryText += selLabel;
    }

    if (situationSummary) {
        if (worryText) worryText += "\n\n";
        worryText += "고민 상황 요약: " + situationSummary;
    }

    document.getElementById("dWorry").textContent = worryText || "-";

    const guideEl = document.getElementById("dGuide");
    guideEl.textContent = "불러오는 중...";

    fetch(CTX + "/GetParentGuide?bookId=" + bookId)
    .then(r => r.json())
    .then(data => {
        const md = (data && data.guideText) ? data.guideText : "가이드가 없습니다.";
        const html = DOMPurify.sanitize(marked.parse(md));
        guideEl.innerHTML = html;
      })
      .catch(() => {
    	  guideEl.textContent = "가이드를 불러오지 못했어요.";
      });
  }

  function closeDetail() {
    document.getElementById("detailModal").style.display = "none";
  }

  function deleteBook(bookId) {
    if (!confirm("정말 삭제할까요?")) return;
    fetch(CTX + "/bookDelete?bookId=" + bookId, { method: "POST" })
      .then(r => r.json())
      .then(data => {
        if (data && data.ok) location.reload();
        else alert(data.error || "삭제 실패");
      })
      .catch(() => alert("삭제 실패"));
  }
</script>
</body>
</html>