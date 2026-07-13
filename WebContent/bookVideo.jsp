<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="model.dto.Book" %>
    <%@ page import="model.dto.User" %>
    
 <%
	 Book book = (Book) request.getAttribute("book");
	 if (book == null) {
	     response.sendRedirect(request.getContextPath() + "/GetBookShelf");
	     return;
	 }
	
	 String storyTitle = book.getStoryTitle() != null ? book.getStoryTitle() : "동화책";
	 String displayTitle = "\"" + storyTitle + "\"";
	 String videoUrl = book.getTtsClip();
 %>

    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" type="text/css" href="css/style.css?v=1">

<script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/dompurify@3.0.6/dist/purify.min.js"></script>

<title>동화책 보기</title>

  <style>
  	/*상단 뒤로가기, 제목, 부모가이드*/
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
	  
	  /* 동화 영상 스타일 */
	.videoWrap {
	    width: 100%;
	    max-width: 1536px;
	    aspect-ratio: 2 / 1;
	    margin: 0 auto;
	    background: #fff;
	}
	
	.videoPlayer {
	    width: 100%;
	    height: 120%;
	    display: block;
	    object-fit: contain;
	    background: #fff;
	}
	
	.videoEmpty {
	    width: min(1024px, 90vw);
	    aspect-ratio: 1 / 1;
	    border: 8px solid #000;
	    display: flex;
	    align-items: center;
	    justify-content: center;
	    font-size: 56px;
	    font-weight: 800;
	    color: #111;
	    background: #eef5f5;
	}
	
	  /* 좁은 화면에서는: 오른쪽 액션을 2행으로 내림 + 제목 폰트 살짝 줄임 */
	  @media (max-width: 480px){
	    .topbarTitle{ font-size: 18px; }
	    .topbarRight{ display:none; }     /* 1행 오른쪽 숨김 */
	    .topbarRow2{ display:flex; }      /* 2행 액션 표시 */
	    .guideBtn{ padding: 8px 10px; font-size: 14px; }
	    .iconBtn img{ width:36px; }
	  }
    
   
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

<div class="topbar">
    <div class="topbarRow1">
        <div class="topbarLeft">
            <button class="iconBtn" type="button" onclick="location.href='GetBookShelf'">
                <img src="./image/bookChat-left-arrow.png" alt="이전 페이지">
            </button>
        </div>

        <div class="topbarTitle">
      		"<%= (book != null && book.getStoryTitle()!=null) ? book.getStoryTitle() : "동화책" %>"
    	</div>

        <div class="topbarRight">
            <button class="guideBtn" type="button" onclick="openGuide()">
                부모 가이드
            </button>
        </div>
    </div>
</div>

<!-- 동화 영상 -->
<div class="videoWrap">
    <% if (videoUrl != null && !videoUrl.trim().isEmpty()) { %>
        <video class="videoPlayer" controls>
            <source src="<%= videoUrl %>" type="video/mp4">
        </video>
    <% } else { %>
        <div class="videoEmpty">
        </div>
    <% } %>
</div>

<!-- 부모 가이드 버튼 -->
<div id="guideModal" class="w3-modal" style="display:none;">
    <div class="w3-modal-content w3-round-large" style="padding:18px;">
        <div style="display:flex; justify-content:space-between; align-items:center;">
            <div style="font-size:20px; font-weight:800;">부모 가이드</div>
            <button class="w3-button w3-round-large" onclick="closeGuide()">X</button>
        </div>
        <hr>
        <div id="guideBody"></div>
    </div>
</div>

<!-- 내 책장 보기 버튼 -->
<div>&nbsp;</div>
<div class="w3-row">
	<div class="w3-col s1 m3 l4">&nbsp;</div>
	<div class="w3-padding w3-col s10 m6 l4">
		<button class="w3-button w3-block w3-round-large w3-padding-16 book-button" onclick="location.href='GetBookShelf'">내 책장 보기</button>		
	</div>
	<div class="w3-col s1 m3 l4">&nbsp;</div>
</div>

<script>
    const CTX = "<%= request.getContextPath() %>";
    const BOOK_ID = <%= book.getBookId() %>;

    function openGuide() {
        const modal = document.getElementById("guideModal");
        const body = document.getElementById("guideBody");

        modal.style.display = "block";
        body.textContent = "불러오는 중...";

        fetch(CTX + "/GetParentGuide?bookId=" + BOOK_ID)
            .then(r => r.json())
            .then(data => {
                const md = data && data.guideText ? data.guideText : "가이드가 없습니다.";
                const html = DOMPurify.sanitize(marked.parse(md));
                body.innerHTML = html;
            })
            .catch(() => {
                body.textContent = "가이드를 불러오지 못했습니다.";
            });
    }

    function closeGuide() {
        document.getElementById("guideModal").style.display = "none";
    }

    document.addEventListener("keydown", function(e) {
        if (e.key === "Escape") closeGuide();
    });
</script>

</body>
</html>