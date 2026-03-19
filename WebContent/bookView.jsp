<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="model.dto.Book" %>
    <%@ page import="model.dto.User" %>

    
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
  <link rel="stylesheet" type="text/css" href="css/style.css?v=1">
  
  <!-- markdown to HTML rendering -->
  <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/dompurify@3.0.6/dist/purify.min.js"></script>
  
  <%
  Book book = (Book) request.getAttribute("book");
  String pagesJson = (String) request.getAttribute("pagesJson");
  Integer totalPages = (Integer) request.getAttribute("totalPages");
  if (totalPages == null) totalPages = 0;
  Integer startPage = 1;
  %>
 
  <title>동화책 보기</title>
  
  <style>
  	/*상단 뒤로가기, 제목, PDF, 부모가이드*/
	  .topbar{
	    background:#dfeff0;
	    padding: 10px 12px;
	  }
	
	  /* 1행: 뒤로 / 제목 / 액션 */
	  .topbarRow1{
	    display:flex;
	    align-items:center;
	    gap:10px;
	  }
	  .topbarLeft{
	    flex: 0 0 auto;
	    display:flex;
	    align-items:center;
	  }
	  .topbarTitle{
	    flex: 1 1 auto;
	    text-align:center;
	    font-weight:800;
	    font-size: 20px;
	    line-height:1.2;
	    padding: 0 6px;
	    word-break: keep-all;
	  }
	  .topbarRight{
	    flex: 0 0 auto;
	    display:flex;
	    align-items:center;
	    gap:8px;
	    white-space: nowrap;
	  }
	
	  /* 아이콘 버튼 공통 */
	  .iconBtn{
	    border:none;
	    background:none;
	    cursor:pointer;
	    padding: 0;
	    display:flex;
	    align-items:center;
	    justify-content:center;
	  }
	  .iconBtn img{ width:40px; height:auto; display:block; }
	
	  /* 2행: 모바일에서 액션을 아래로 내림 */
	  .topbarRow2{
	    display:none;
	    margin-top: 8px;
	    justify-content:flex-end;
	    gap:8px;
	  }
	
	  /* 기본(PC/태블릿): 1행 오른쪽에 버튼 */
	  .guideBtn{
	    background:#4e9aa0;
	    color:#fff;
	    border:none;
	    border-radius: 999px;
	    padding: 8px 12px;
	    font-weight:700;
	    cursor:pointer;
	    white-space: nowrap;
	  }
	
	  /* 좁은 화면에서는: 오른쪽 액션을 2행으로 내림 + 제목 폰트 살짝 줄임 */
	  @media (max-width: 480px){
	    .topbarTitle{ font-size: 18px; }
	    .topbarRight{ display:none; }     /* 1행 오른쪽 숨김 */
	    .topbarRow2{ display:flex; }      /* 2행 액션 표시 */
	    .guideBtn{ padding: 8px 10px; font-size: 14px; }
	    .iconBtn img{ width:36px; }
	  }
    
    /*페이지 진행바*/
    .progRow { display:flex; align-items:center; gap:14px; padding: 10px 16px; }
    .seg { flex:1; height: 10px; background:#e6e6e6; border-radius: 999px; overflow:hidden; }
    .segFill { height:100%; width:0%; background:#4e9aa0; transition: width 180ms ease; }
    
    /*페이지 컨텐츠(이미지+텍스트)*/
	.pageRow{display:flex; align-items:stretch;}
	.pageCard{ display:flex; flex-direction:column; gap:14px;}
	
	@media (min-width: 992px){ /* W3CSS의 l breakpoint에 맞춤 */
	  .pageCard{flex-direction:row; align-items:stretch; gap:18px;}
	  .pageMedia, .pageTextWrap{flex:1 1 0;}
	}
	
	.imgCard{width:100%; border:8px solid #222; border-radius:12px; overflow:hidden; background:#fff;}
	.imgCard img{width:100%; height:auto; display:block;}
	
	/* 텍스트 박스 */
	.textBox{background:#efefef; padding:16px; border-radius:12px; line-height:1.8; font-size:18px;
			  display:flex; align-items:center; justify-content:flex-start;}
	
	@media (min-width: 992px){
	.pageTextWrap{display:flex;}
	.textBox{width:100%; height:100%; overflow:auto;}
	}
	
    /*페이지 좌우 버튼*/
	.navCol{display:flex; justify-content:center; align-items:center;}
   
   /* 부모가이드 서식 */
	#guideBody {white-space: normal; line-height: 1.45; font-size: 15px;}
	
	#guideBody h1 { font-weight:bold; font-size: 24px; margin: 0 0 10px; }
	#guideBody h2 { font-weight:bold; font-size: 22px; margin: 14px 0; }
	#guideBody h3 { font-weight:bold; font-size: 18px; margin: 24px 0 12px 0; }
	
	#guideBody p { margin: 24px 0 12px 0; }
	#guideBody ul { margin: 6px 0 12px 18px; padding: 0; }
	#guideBody ol { margin: 6px 0 6px 18px; padding: 0; }
	#guideBody li { margin: 3px 0; }
	
	#guideBody hr { margin: 12px 0; }

  /* 모달 높이 제한 + 스크롤 */
  #guideModal #guideBody {max-height: 70vh; overflow: auto;}
  </style>
</head>
<body>
<%@ include file="sidebar.jsp" %>

<!-- 
 상단 바(제목 등) 
<div class="w3-row topbar">
  <div class="w3-col s2 m2 l2">
	<button class="w3-button" type="button" onclick="location.href='childHome.jsp'" style="border:none; background: none; cursor: pointer;">
		<img src="./image/bookChat-left-arrow.png" alt="이전 페이지" style="width: 40px; height: auto;"/>
	</button>
  </div>

  <div class="w3-col s8 m8 l8 title">"<%= (book != null && book.getStoryTitle()!=null) ? book.getStoryTitle() : "동화책" %>"</div>

 PDF, 부모가이드 버튼 
  <div class="w3-col s2 m2 l2" style="text-align:right; display:flex; justify-content:flex-end; gap:10px;">

	<button class="w3-button" type="button" onclick="location.href='BookPdf?bookId=<%= book.getBookId() %>'" style="border:none; background: none; cursor: pointer; padding:0px;">
		<img src="./image/PdfDownload.png" alt="PdfDownload" style="width: 40px; height: auto;"/>
	</button>
   
    <button class="w3-button w3-round-large" style="background:#4e9aa0;color:#fff;" onclick="openGuide()">부모 가이드</button>
  </div>
</div>
 -->
 
<!-- 상단 바(제목 등) -->
<div class="topbar">
  <!-- 1행 -->
  <div class="topbarRow1">
    <div class="topbarLeft">
      <button class="iconBtn" type="button" onclick="location.href='childHome.jsp'">
        <img src="./image/bookChat-left-arrow.png" alt="이전 페이지"/>
      </button>
    </div>

    <div class="topbarTitle">
      "<%= (book != null && book.getStoryTitle()!=null) ? book.getStoryTitle() : "동화책" %>"
    </div>

    <!-- PC/태블릿에서는 여기(1행 오른쪽)에 표시 -->
    <div class="topbarRight">
      <button class="iconBtn" type="button" onclick="location.href='BookPdf?bookId=<%= book.getBookId() %>'">
        <img src="./image/PdfDownload.png" alt="PdfDownload"/>
      </button>
      <button class="guideBtn" type="button" onclick="openGuide()">부모 가이드</button>
    </div>
  </div>

  <!-- 2행: 모바일에서만 보이게 -->
  <div class="topbarRow2">
    <button class="iconBtn" type="button" onclick="location.href='BookPdf?bookId=<%= book.getBookId() %>'">
      <img src="./image/PdfDownload.png" alt="PdfDownload"/>
    </button>
    <button class="guideBtn" type="button" onclick="openGuide()">부모 가이드</button>
  </div>
</div>

<!-- 페이지 진행 바 -->
<div class="w3-row progRow">
	<div class="w3-col s1 m2 l3"></div>
	<div class="w3-col s19 m7 l5">
		<div style="flex:1; display:flex; gap:10px;" id="segments"></div>
	</div>
	<div class="w3-col s1 m1 l1">
		<div style="font-weight:800;" id="pageCounter">0/0</div>
	</div>
	<div class="w3-col s1 m2 l3"></div>
</div>

<!-- 페이지 컨텐츠 -->
<div class="pageWrap">
  <div class="w3-row pageRow" style="align-items:center;">
  
 	<!-- 좌우 버튼 -->
	<div class="w3-col s2 m2 l2 navCol">
		<button class="w3-button" type="button" onclick="prevPage()" style="border:none; background: none; cursor: pointer;">
			<img src="./image/bookView-left-arrow.png" alt="이전 페이지" style="width: 40px; height: auto;"/>
		</button>
	</div>

  	<!-- 페이지 이미지&텍스트 -->
	<div class="w3-col s8 m8 l8">
	  <div class="pageCard" id="pageCard">
	    <div class="pageMedia">
	      <div class="imgCard">
	        <img id="pageImg" src="" alt="page image">
	      </div>
	    </div>
	
	    <div class="pageTextWrap">
	      <div class="textBox" id="pageText"></div>
	    </div>
	  </div>
	</div>

 	<!-- 좌우 버튼 -->
	<div class="w3-col s2 m2 l2 navCol">
		<button class="w3-button" type="button" onclick="nextPage()" style="border:none; background: none; cursor: pointer;">
			<img src="./image/bookView-right-arrow.png" alt="이전 페이지" style="width: 40px; height: auto;"/>
		</button>
	</div>
  </div>
</div>


<div>&nbsp;</div>
<div class="w3-row">
	<div class="w3-col s1 m3 l4">&nbsp;</div>
	<div class="w3-padding w3-col s10 m6 l4">
		<button class="w3-button w3-block w3-round-large w3-padding-16 book-button" onclick="location.href='GetBookShelf'">내 책장 보기</button>		
	</div>
	<div class="w3-col s1 m3 l4">&nbsp;</div>
</div>


<!-- Parent guide modal -->
<div id="guideModal" class="w3-modal" style="display:none;">
  <div class="w3-modal-content w3-round-large" style="padding: 18px;">
    <div style="display:flex; justify-content:space-between; align-items:center;">
      <div style="font-size:20px;font-weight:800;">부모 가이드</div>
      <button class="w3-button w3-round-large" onclick="closeGuide()">X</button>
    </div>
    <hr>
    <div id="guideBody"></div>
  </div>
</div>

<!-- 안전하게 JSON 주입 -->
<script id="pagesData" type="application/json"><%= pagesJson %></script>

<script>
  const CTX = "<%= request.getContextPath() %>";
  const BOOK_ID = <%= book.getBookId() %>;
  const START_PAGE = <%= startPage %>; // 1-based

  const pages = JSON.parse(document.getElementById("pagesData").textContent || "[]");
  let idx = Math.max(0, Math.min((START_PAGE - 1), pages.length - 1));

  function initSegments() {
    const segWrap = document.getElementById("segments");
    segWrap.innerHTML = "";
    for (let i = 0; i < pages.length; i++) {
      const seg = document.createElement("div");
      seg.className = "seg";
      const fill = document.createElement("div");
      fill.className = "segFill";
      seg.appendChild(fill);
      segWrap.appendChild(seg);
    }
  }

  function render() {
    if (!pages.length) {
      document.getElementById("pageCounter").textContent = "0/0";
      document.getElementById("pageImg").src = "";
      document.getElementById("pageText").textContent = "페이지가 없습니다.";
      return;
    }

    const p = pages[idx];
    // document.getElementById("pageImg").src = p.imageUrl || "";
    document.getElementById("pageImg").src = p.imageUrl || "";
    document.getElementById("pageText").textContent = p.pageText || "";
    document.getElementById("pageCounter").textContent = (idx + 1) + "/" + pages.length;

    const fills = document.querySelectorAll(".segFill");
    fills.forEach((f, i) => f.style.width = (i <= idx ? "100%" : "0%"));

    // URL도 같이 갱신 (새로고침/공유 편하게)
    const url = new URL(window.location.href);
    url.searchParams.set("page", String(idx + 1));
    history.replaceState(null, "", url.toString());
  }

  function prevPage() {
    if (idx > 0) { idx--; render(); }
  }
  function nextPage() {
    if (idx < pages.length - 1) { idx++; render(); }
  }

  document.addEventListener("keydown", function(e){
    if (e.key === "ArrowLeft") prevPage();
    if (e.key === "ArrowRight") nextPage();
    if (e.key === "Escape") closeGuide();
  });

  function openGuide() {
    const modal = document.getElementById("guideModal");
    const body = document.getElementById("guideBody");
    modal.style.display = "block";
    body.textContent = "불러오는 중...";

    fetch(CTX + "/GetParentGuide?bookId=" + BOOK_ID)
      .then(r => r.json())
      .then(data => {
          const md = (data && data.guideText) ? data.guideText : "가이드가 없습니다.";
          const html = DOMPurify.sanitize(marked.parse(md));
          body.innerHTML = html;
        })
        .catch(() => {
          body.textContent = "가이드를 불러오지 못했어요.";
        });
    }

  function closeGuide() {
    document.getElementById("guideModal").style.display = "none";
  }

  initSegments();
  render();
</script>
</body>
</html>