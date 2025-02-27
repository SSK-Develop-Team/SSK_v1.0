function getPrevContent(i){
    if(i<=0){
        alert("이전 페이지가 없습니다.");
    }else{
        var prevForm = document.createElement("form");
        prevForm.setAttribute("charset", "UTF-8");
        prevForm.setAttribute("method", "post");
        prevForm.setAttribute("action", "GetLangGamePrevContent");
        document.body.appendChild(prevForm);
        prevForm.submit();
    }
}
function getNextContent(i,questionNum,gameSize){
    if(i>=gameSize-1){
        if(!confirm(questionNum + "번 문항의 게임을 종료하시겠습니까?")){return;}
    }
    var nextForm = document.createElement("form");
    nextForm.setAttribute("charset", "UTF-8");
    nextForm.setAttribute("method", "post");
    nextForm.setAttribute("action", "GetLangGameNextContent");
    document.body.appendChild(nextForm);
    nextForm.submit();
}

function FinishGame(questionNum){
    if(!confirm(questionNum + "번 문항의 게임을 종료하시겠습니까?")){return;}
    
    var BackForm = document.createElement("form");
    BackForm.setAttribute("charset", "UTF-8");
    BackForm.setAttribute("method", "post");
    BackForm.setAttribute("action", "GetLangGameBackToTest");
    document.body.appendChild(BackForm);
    BackForm.submit();
}

function openHint(){
	document.getElementById('hint-modal').style.display='block';
}

function closeHint(){
	document.getElementById('hint-modal').style.display='none';
}

function openAnswer(){
	document.getElementById('answer-modal').style.display='block';
	const audio = document.getElementById('answer-audio');
	if (audio) {
		audio.load();
		audio.play();
	}
}

function closeAnswer(){
	document.getElementById('answer-modal').style.display='none';
	const audio = document.getElementById('answer-audio');
	if (audio) {
		audio.pause();
	}
}

function openCriteria(){
	document.getElementById('criteria-modal').style.display='block';
}

function closeCriteria(){
	document.getElementById('criteria-modal').style.display='none'
}

/*document.getElementById('answer-audio').addEventListener("ended", function() {

	setTimeout(() =>document.getElementById('answer-modal').style.display='none',1000);
});*/
