const canvas = document.getElementById("jsCanvas");
const ctx = canvas.getContext("2d");

var clientWidth = document.getElementById('canvas-container').clientWidth;//실제 너비 받아오기
var clientHeight = document.getElementById('canvas-container').clientHeight;

canvas.width = clientWidth;
canvas.height = clientHeight;

ctx.strokeStyle = "black";
ctx.lineWidth = 2.5;

let painting = false;


function startPainting() {
    painting=true;
}
function stopPainting() {
    painting=false;
}

function onMouseMove(event) {
    const x = event.pageX - canvas.offsetLeft;
    const y = event.pageY - canvas.offsetTop;
    if(!painting) {
        ctx.beginPath();
        ctx.moveTo(x, y);
    }
    else {
        ctx.lineTo(x, y);
        ctx.stroke();
    }
}
/* Touch */
const ongoingTouches = []; //모바일 터치용

function handleStart(event) {
	event.preventDefault();
	const touches = event.changedTouches;

	for (let i = 0; i < touches.length; i++) {
	    ongoingTouches.push(copyTouch(touches[i]));
	    ctx.beginPath();
	    ctx.arc(touches[i].pageX - canvas.offsetLeft, touches[i].pageY - canvas.offsetTop, 1, 0, 2 * Math.PI, false); // a circle at the start
	    ctx.fill();
	}
}

function handleMove(event) {
	event.preventDefault();
	const touches = event.changedTouches;

	for (let i = 0; i < touches.length; i++) {
	    const idx = ongoingTouchIndexById(touches[i].identifier);
	
	    if (idx >= 0) {
	      ctx.beginPath();
	      ctx.moveTo(ongoingTouches[idx].pageX - canvas.offsetLeft, ongoingTouches[idx].pageY - canvas.offsetTop);
	      ctx.lineTo(touches[i].pageX - canvas.offsetLeft, touches[i].pageY - canvas.offsetTop);
	      ctx.lineWidth = 4;
	      ctx.stroke();
	
	      ongoingTouches.splice(idx, 1, copyTouch(touches[i])); // swap in the new touch record
	    } else {
	      console.log("can't figure out which touch to continue");
	 	}
	}
}

function handleEnd(event) {
  	event.preventDefault();
  	const touches = event.changedTouches;

  	for (let i = 0; i < touches.length; i++) {
    	let idx = ongoingTouchIndexById(touches[i].identifier);

    	if (idx >= 0) {
	      ctx.lineWidth = 4;
	      ctx.beginPath();
	      ctx.moveTo(ongoingTouches[idx].pageX - canvas.offsetLeft, ongoingTouches[idx].pageY - canvas.offsetTop);
	      ctx.lineTo(touches[i].pageX - canvas.offsetLeft, touches[i].pageY - canvas.offsetTop);
	      ctx.fillRect(touches[i].pageX - canvas.offsetLeft - 2, touches[i].pageY  - canvas.offsetTop - 2, 4, 4); // and a square at the end
	      ongoingTouches.splice(idx, 1); // remove it; we're done
	    } else {
	      console.log("can't figure out which touch to end");
	    }
  	}
}

function handleCancel(event) {
	event.preventDefault();
	console.log("touchcancel.");
  	const touches = event.changedTouches;

  	for (let i = 0; i < touches.length; i++) {
    	let idx = ongoingTouchIndexById(touches[i].identifier);
    	ongoingTouches.splice(idx, 1); // remove it; we're done
  	}
}

function ongoingTouchIndexById(idToFind) {
  for (let i = 0; i < ongoingTouches.length; i++) {
    const id = ongoingTouches[i].identifier;

    if (id === idToFind) {
      return i;
    }
  }
  return -1; // not found
}

function copyTouch({ identifier, pageX, pageY }) {
  return { identifier, pageX, pageY };
}
/****/


if (canvas) {
    canvas.addEventListener("mousemove", onMouseMove);
    canvas.addEventListener("mousedown", startPainting);
    canvas.addEventListener("mouseup", stopPainting);
    canvas.addEventListener("mouseleave", stopPainting);

    canvas.addEventListener("touchstart", handleStart);
    canvas.addEventListener("touchmove", handleMove);
    canvas.addEventListener("touchend", handleEnd);
	canvas.addEventListener("touchcancel", handleCancel);
}

function removePainting(){
    // 픽셀 정리
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    // 컨텍스트 리셋
    ctx.beginPath();
}
