console.log("canvas.js 로드됨");

function initCanvas() {
  console.log("initCanvas 호출됨");

  const canvas = document.getElementById("jsCanvas");
  const container = document.getElementById("canvas-container");

  if (!canvas || !container) {
    console.warn("❌ canvas 요소나 canvas-container가 없습니다.");
    return;
  }

  const ctx = canvas.getContext("2d");
  let painting = false;

  // 캔버스 크기 설정
  canvas.width = canvas.clientWidth;
  canvas.height = canvas.clientHeight;

  // 선 색상 및 두께
  ctx.strokeStyle = "black";
  ctx.lineWidth = 2.5;
  ctx.lineCap = "round"; // 끝부분 둥글게
  ctx.lineJoin = "round"; // 모서리 둥글게

  // ===== 마우스 이벤트 함수 =====
  function startPainting(e) {
    painting = true;
    const rect = canvas.getBoundingClientRect();
    ctx.beginPath();
    ctx.moveTo(e.clientX - rect.left, e.clientY - rect.top);
  }

  function stopPainting() {
    painting = false;
  }

  function onMouseMove(e) {
    if (!painting) return;
    const rect = canvas.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;
    ctx.lineTo(x, y);
    ctx.stroke();
  }

  // ===== 터치 이벤트 변수 & 함수 =====
  const ongoingTouches = [];

  function handleStart(event) {
	  event.preventDefault();
	  const touches = event.changedTouches;

	  for (let i = 0; i < touches.length; i++) {
	    const { x, y } = getCanvasCoordinates(touches[i], canvas);
	    ongoingTouches.push({ identifier: touches[i].identifier, x, y });

	    ctx.beginPath();
	    ctx.moveTo(x, y);
	  }
	}

	function handleMove(event) {
	  event.preventDefault();
	  const touches = event.changedTouches;

	  for (let i = 0; i < touches.length; i++) {
	    const { x, y } = getCanvasCoordinates(touches[i], canvas);
	    const idx = ongoingTouchIndexById(touches[i].identifier);

	    if (idx >= 0) {
	      ctx.lineTo(x, y);
	      ctx.stroke();

	      ongoingTouches.splice(idx, 1, { identifier: touches[i].identifier, x, y });
	    }
	  }
	}

  function handleEnd(event) {
    event.preventDefault();
    const touches = event.changedTouches;
    for (let i = 0; i < touches.length; i++) {
      const idx = ongoingTouchIndexById(touches[i].identifier);
      if (idx >= 0) {
        // 마지막 선분은 handleMove에서 이미 그려졌으므로 여기서는 정리만
        ongoingTouches.splice(idx, 1);
      }
    }
  }

  function handleCancel(event) {
    event.preventDefault();
    const touches = event.changedTouches;
    for (let i = 0; i < touches.length; i++) {
      const idx = ongoingTouchIndexById(touches[i].identifier);
      if (idx >= 0) ongoingTouches.splice(idx, 1);
    }
  }

  function ongoingTouchIndexById(idToFind) {
    for (let i = 0; i < ongoingTouches.length; i++) {
      if (ongoingTouches[i].identifier === idToFind) {
        return i;
      }
    }
    return -1;
  }

  function getCanvasCoordinates(evt, canvas) {
	  const rect = canvas.getBoundingClientRect();
	  const scaleX = canvas.width / rect.width;
	  const scaleY = canvas.height / rect.height;

	  let clientX, clientY;
	  if (evt.touches) { // 터치 이벤트
	    clientX = evt.touches[0].clientX;
	    clientY = evt.touches[0].clientY;
	  } else { // 마우스 이벤트
	    clientX = evt.clientX;
	    clientY = evt.clientY;
	  }

	  return {
	    x: (clientX - rect.left) * scaleX,
	    y: (clientY - rect.top) * scaleY
	  };
	}
  
  
  // ===== 이벤트 바인딩 =====
  canvas.addEventListener("mousedown", startPainting);
  canvas.addEventListener("mouseup", stopPainting);
  canvas.addEventListener("mousemove", onMouseMove);
  canvas.addEventListener("mouseleave", stopPainting);

  canvas.addEventListener("touchstart", handleStart);
  canvas.addEventListener("touchmove", handleMove);
  canvas.addEventListener("touchend", handleEnd);
  canvas.addEventListener("touchcancel", handleCancel);
}

// 외부에서 호출 가능한 리셋 함수
function removePainting() {
  const canvas = document.getElementById("jsCanvas");
  if (canvas) {
    const ctx = canvas.getContext("2d");
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.beginPath();
  }
}

// 전역 함수로 등록
window.initCanvas = initCanvas;
window.removePainting = removePainting;

// DOM이 이미 로드된 경우 즉시 실행
if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", function () {
    console.log("DOMContentLoaded - canvas.js 초기화");
  });
} else {
  console.log("DOM이 이미 로드됨 - canvas.js 즉시 초기화");
}
