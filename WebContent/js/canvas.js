console.log("canvas.js 로드됨");

function initCanvas() {
  console.log("initCanvas 호출됨");
  
  const canvas = document.getElementById('jsCanvas');
  const container = document.getElementById('canvas-container');

  if (!canvas || !container) {
    console.warn("❌ canvas 요소나 canvas-container가 없습니다.");
    return;
  }

  const ctx = canvas.getContext('2d');
  let painting = false;

  // 캔버스 크기 설정
  canvas.width = canvas.clientWidth;
  canvas.height = canvas.clientHeight;

  // 선 색상 및 두께
  ctx.strokeStyle = "black";
  ctx.lineWidth = 2.5;

  // 마우스 이벤트 함수들
  function startPainting() { painting = true; }
  function stopPainting() { painting = false; }

  function onMouseMove(event) {
	    const rect = canvas.getBoundingClientRect();
	    const x = event.clientX - rect.left;
	    const y = event.clientY - rect.top;

	    if (!painting) {
	        ctx.beginPath();
	        ctx.moveTo(x, y);
	    } else {
	        ctx.lineTo(x, y);
	        ctx.stroke();
	    }
	}

  // 터치용 변수 및 함수
  const ongoingTouches = [];

  function handleStart(event) {
    event.preventDefault();
    const touches = event.changedTouches;
    for (let i = 0; i < touches.length; i++) {
      ongoingTouches.push(copyTouch(touches[i]));
      ctx.beginPath();
      ctx.arc(touches[i].pageX - canvas.offsetLeft, touches[i].pageY - canvas.offsetTop, 1, 0, 2 * Math.PI, false);
      ctx.fill();
    }
  }

  function handleMove(event) {
	    event.preventDefault();
	    const touches = event.changedTouches;

	    for (let i = 0; i < touches.length; i++) {
	        const rect = canvas.getBoundingClientRect();
	        const x = touches[i].clientX - rect.left;
	        const y = touches[i].clientY - rect.top;

	        const idx = ongoingTouchIndexById(touches[i].identifier);

	        if (idx >= 0) {
	            ctx.beginPath();
	            ctx.moveTo(ongoingTouches[idx].x, ongoingTouches[idx].y);
	            ctx.lineTo(x, y);
	            ctx.stroke();

	            // 업데이트된 좌표로 바꾸기
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
        ctx.lineWidth = 4;
        ctx.beginPath();
        ctx.moveTo(ongoingTouches[idx].pageX - canvas.offsetLeft, ongoingTouches[idx].pageY - canvas.offsetTop);
        ctx.lineTo(touches[i].pageX - canvas.offsetLeft, touches[i].pageY - canvas.offsetTop);
        ctx.fillRect(touches[i].pageX - canvas.offsetLeft - 2, touches[i].pageY - canvas.offsetTop - 2, 4, 4);
        ongoingTouches.splice(idx, 1);
      }
    }
  }

  function handleCancel(event) {
    event.preventDefault();
    const touches = event.changedTouches;
    for (let i = 0; i < touches.length; i++) {
      const idx = ongoingTouchIndexById(touches[i].identifier);
      ongoingTouches.splice(idx, 1);
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

  function copyTouch(touch) {
	    const rect = canvas.getBoundingClientRect();
	    return {
	        identifier: touch.identifier,
	        x: touch.clientX - rect.left,
	        y: touch.clientY - rect.top
	    };
	}

  // 이벤트 바인딩
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
  const canvas = document.getElementById('jsCanvas');
  if (canvas) {
    const ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.beginPath();
  }
}

// 전역 함수로 등록
window.initCanvas = initCanvas;
window.removePainting = removePainting;

// DOM이 이미 로드된 경우 즉시 실행
if (document.readyState === 'loading') {
  document.addEventListener("DOMContentLoaded", function () {
    console.log("DOMContentLoaded - canvas.js 초기화");
  });
} else {
  console.log("DOM이 이미 로드됨 - canvas.js 즉시 초기화");
}