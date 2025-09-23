console.log("langGame.js 로드됨");

// ===== 전역 변수 =====
let gameData = [];
let gameId = 0;
let currentIndex = 0;
let audioEndedNextFlag = 0;
let isUserInteracting = false;
let isAudioEnded = false;
let isAudioReplayEnded = false;
let isWritingContent = false;
let audio = null;
let exceptionMeta = {};
let autoProgressTimer = null;

// ===== 초기화 함수 =====
function initializeLangGame(data, gameIdValue, currentIndexValue, audioEndedNextFlagValue, exceptionMetaData = {}) {
    gameData = data;
    gameId = gameIdValue;
    currentIndex = currentIndexValue;
    audioEndedNextFlag = audioEndedNextFlagValue;
    exceptionMeta = exceptionMetaData;
    audio = document.getElementById('voice-audio');
    
    console.log("LangGame 초기화 완료:", { gameId, currentIndex, audioEndedNextFlag });
}

// ===== 이벤트 리스너 & 초기화 =====
document.addEventListener("DOMContentLoaded", function () {
    console.log("DOMContentLoaded - langGame.js 초기화");
});

// ===== 시작 모달 핸들러 =====
function startEvaluation() {
    document.getElementById('start-modal').style.display = 'none';
    // 초기사운드
    playVoice(gameData[currentIndex].voiceUrl);
    
    // 초기에 버튼/모달/네비 상태 일괄 갱신
    updateContentForIndex(currentIndex);
}

// ===== 음성 재생 =====
function playVoice(url) {
    if (!url || url == null || url == "") return;
    isAudioEnded = false;
    isAudioReplayEnded = true;
    audio.pause();
    audio.src = url;
    audio.load();
    
    audio.onended = function () {
        console.log("오디오 종료됨: checkAutoProgress 실행");
        isAudioEnded = true;
        checkAutoProgress(); // 자동 진행 조건 판단
    };
    
    audio.play().catch(err => console.warn('재생 실패:', err));
}

// ===== 다시 듣기 버튼 인터렉션 =====
function audioReplay() {
    if (audio.src) {
        isAudioEnded = true;
        isAudioReplayEnded = false;
        
        // 먼저 기존 재생 중이던 오디오 중지 및 초기화
        audio.pause();
        audio.currentTime = 0; // 재생 위치를 처음으로
        
        // ended 이벤트 재설정 (중복 방지 위해 기존 리스너 제거는 생략 가능)
        audio.onended = function () {
            isAudioReplayEnded = true;
            checkAutoProgress();
        };
        
        // 다시 재생
        audio.play().catch(err => console.warn('다시듣기 실패:', err));
    }
}

// ===== 네비게이션 함수들 =====
function goPrev() {
    if (currentIndex <= 0) return;
    currentIndex -= 1;
    updateContentForIndex(currentIndex);
}

function goNext() {
	if (autoProgressTimer){
		clearTimeout(autoProgressTimer);
		autoProgressTimer = null;
	}
	
    if (currentIndex >= gameData.length - 1) {
        if (confirm(gameId + "번 문항의 게임을 종료하시겠습니까?")) {
            window.location.href = "langTest.jsp";
        }
        return;
    }
    currentIndex += 1;
    updateContentForIndex(currentIndex);
}

// ===== 콘텐츠 업데이트 함수 =====
function updateContentForIndex(index) {
    const data = gameData[index];
    if (!data) return;

    // 1) 이미지
    const img = document.getElementById('main-image');
    if (img) img.src = data.imgUrl + '.jpg';

    // 2) 화자
    const speaker = document.getElementById('speaker-name');
    if (speaker) {
        if (data.speaker && data.speaker !== '-') {
            speaker.textContent = data.speaker;
            speaker.style.display = 'block';
        } else {
            speaker.style.display = 'none';
        }
    }

    // 3) 본문 컨텐츠
    const content = document.getElementById('content-text');
    if (content) {
        if (data.content && data.content !== '-') {
            content.innerHTML = data.content;
            content.style.display = 'block';
        } else {
            content.style.display = 'none';
        }
    }

    // 4) 오디오 (자동 진행 플래그 계산 포함)
    const noMeta =
        ((!data.hint || data.hint === '') &&
        (!data.hintVoice || data.hintVoice === '') &&
        (!data.answer || data.answer === '') &&
        (!data.answerVoice || data.answerVoice === '')) || (currentIndex >= gameData.length - 1);
    audioEndedNextFlag = noMeta ? 1 : 0;

    playVoice(data.voiceUrl);

    // 5) 예외 컨텐츠 처리
    handleExceptionContent(index, data);

    // 6) 모달(팁/정답/평가기준) 내용 갱신
    updateModalContents(data);

    // 7) 네비/부가 버튼 표시 여부
    updateButtonStates(index, data);
}

// ===== 예외 컨텐츠 처리 =====
function handleExceptionContent(index, data) {
    // Except01, Except02: 버튼 선택 기능
    updateButtonsForCurrentData(index);
    
    // Except03: 글자쓰기 버튼
    if (isExceptionGame(gameId, index)) {
        const writeBtn = document.getElementById('write-btn');
        if (writeBtn) {
            writeBtn.style.display = 'block';
            isWritingContent = true;
        }
    } else {
        const writeBtn = document.getElementById('write-btn');
        if (writeBtn) {
            writeBtn.style.display = 'none';
            isWritingContent = false;
        }
    }
    
    // Except04: 인라인 캔버스
    if (isExceptionGame50(gameId, index)) {
        const writeBtn = document.getElementById('inner-canvas');
        if (writeBtn) {
            removePainting();
            writeBtn.style.display = 'block';
            isWritingContent = true;
            if (typeof initCanvas === 'function') {
                initCanvas();
            }
        }
    } else {
        const writeBtn = document.getElementById('inner-canvas');
        if (writeBtn) {
            writeBtn.style.display = 'none';
            isWritingContent = false;
        }
    }
}

// ===== 예외 게임 확인 함수들 =====
function isExceptionGame(gameID, index) {
    return (
        (gameID === 30 && index === 1) ||
        (gameID === 35 && index === 1) ||
        (gameID === 60 && (index === 1 || index === 2 || index === 3)) ||
        (gameID === 63 && (index === 1 || index === 2 || index === 3))
    );
}

function isExceptionGame50(gameID, index) {
    return (gameID === 50 && (index === 1 || index === 2 || index === 3 || index === 4));
}

// ===== 버튼 오버레이 생성/제거 (Except01, Except02) =====
function updateButtonsForCurrentData(index) {
    const cfg = exceptionMeta[index];          // 없으면 일반 문항
    const container = document.getElementById('quiz-container');
    if (!container) return;
    // 이전 버튼 이미지 제거
    [...container.querySelectorAll('img[data-role="answer-btn"]')].forEach(el => el.remove());

    if (!cfg) return; // 일반 문항이면 버튼 없음

    // 새 버튼 생성
    cfg.buttons.forEach((btn, idx) => {
        const el = document.createElement('img');
        el.setAttribute('data-role', 'answer-btn');
        el.className = `selectImg0${idx + 1}`;
        el.src = btn.imageSrc;
        el.alt = `Button${idx + 1}`;
        el.style.position = 'absolute';
        el.style.left = btn.left;
        el.style.top = btn.top;
        el.style.width = btn.width;
        
        // Except02의 경우 모달 열기 기능 추가
        if (gameId === 4) {
            el.onclick = btn.isCorrect ? function() { openModal(); correctA(); } : wrongA;
        } else {
            el.onclick = btn.isCorrect ? correctA : wrongA;
        }
        
        container.appendChild(el);
    });
}

// ===== 모달 내용 업데이트 =====
function updateModalContents(data) {
    // 힌트 모달 내용 업데이트
    const hintModal = document.getElementById('hint-modal');
    if (hintModal) {
        const hintText = hintModal.querySelector('p');
        if (hintText) {
            hintText.innerHTML = data.hint || '';
        }
    }

    // 정답 모달 내용 업데이트
    const answerModal = document.getElementById('answer-modal');
    if (answerModal) {
        const answerText = answerModal.querySelector('p');
        if (answerText) {
            answerText.innerHTML = data.answer || '';
        }
    }

    // 평가기준 모달 내용 업데이트
    const criteriaModal = document.getElementById('criteria-modal');
    if (criteriaModal) {
        const criteriaText = criteriaModal.querySelector('p');
        if (criteriaText) {
            criteriaText.innerHTML = data.criteria || '';
        }
    }

    // Except02: 모달 이미지 업데이트
    const modalImage = document.getElementById('modal-image');
    if (modalImage && exceptionMeta[currentIndex]) {
        const modalImgSrc = data.imgUrl + '_modal.jpg';
        modalImage.src = modalImgSrc;
        
        // 이미지 로드 실패 시 원본 이미지로 대체
        modalImage.onerror = function() {
            console.warn('모달 이미지를 찾을 수 없습니다:', modalImgSrc);
            this.src = data.imgUrl + '.jpg';
        };
    }
}

// ===== 버튼 상태 업데이트 =====
function updateButtonStates(index, data) {
    // 이전 버튼
    const prevBtn = document.getElementById('prev-btn');
    if (prevBtn) prevBtn.style.display = index > 0 ? 'inline-block' : 'none';
    
    // 다음 버튼
    const nextBtn = document.getElementById('next-btn');
    if (nextBtn) nextBtn.style.display = index < gameData.length ? 'inline-block' : 'none';
    
    // 다시듣기 버튼 (상단)
    const replayBtn = document.getElementById('replay-btn');
    if (replayBtn) replayBtn.style.display = data.voiceUrl ? 'inline-block' : 'none';
    
    // 다시듣기 버튼 (하단)
    const audioReplayBtn = document.getElementById('audio-replay-btn');
    if (audioReplayBtn) audioReplayBtn.style.display = data.voiceUrl ? 'inline-block' : 'none';
    
    // 힌트 버튼
    const hintBtn = document.getElementById('hint-btn');
    if (hintBtn) hintBtn.style.display = (data.hint || data.hintVoice) ? 'inline-block' : 'none';
    
    // 정답 버튼
    const answerBtn = document.getElementById('answer-btn');
    if (answerBtn) answerBtn.style.display = (data.answer || data.answerVoice) ? 'inline-block' : 'none';
    
    // 평가기준 버튼
    const criteriaBtn = document.getElementById('criteria-btn');
    if (criteriaBtn) criteriaBtn.style.display = (data.criteria && data.criteria !== '') ? 'inline-block' : 'none';
}

// ===== 모달 열기/닫기 함수들 =====
function openHint() {
    isUserInteracting = true; // 모달이 열렸음을 표시
    document.getElementById('hint-modal').style.display='block';
}

function closeHint() {
    isUserInteracting = false; // 모달이 닫혔음을 표시
    document.getElementById('hint-modal').style.display='none';
    checkAutoProgress()
}

function openAnswer() {
    isUserInteracting = true; // 모달이 열렸음을 표시
    document.getElementById('answer-modal').style.display='block';
}

function closeAnswer() {
    isUserInteracting = false; // 모달이 닫혔음을 표시
    document.getElementById('answer-modal').style.display='none';
    checkAutoProgress()
}

function openCriteria() {
    isUserInteracting = true; // 모달이 열렸음을 표시
    document.getElementById('criteria-modal').style.display='block';
}

function closeCriteria() {
    isUserInteracting = false; // 모달이 닫혔음을 표시
    document.getElementById('criteria-modal').style.display='none';
    checkAutoProgress()
}

// ===== 자동 진행 확인 =====
function checkAutoProgress() {
    const data = gameData[currentIndex];
    if (!data) return;
    
    const isLastPageQuiz = !((!data.hint || data.hint === '') && 
            (!data.answer || data.answer === '')) // true면 현재 퀴즈인 것 -> 자동재생되면 안됨
    if (audioEndedNextFlag == 1 && !isUserInteracting && isAudioEnded && isAudioReplayEnded && !isWritingContent && !isLastPageQuiz) {
        console.log("다음으로 이동");
        
        autoProgressTimer = setTimeout(() => {
            goNext();
            autoProgressTimer = null;
        }, 1000); // 1초 후 자동 진행
    }
}

// ===== Except02 모달 함수들 =====
function openModal(){
    document.getElementById('modal').style.display='block';
}

function closeModal(){
    document.getElementById('modal').style.display='none';
}

// ===== Except03 글자쓰기 모달 함수들 =====
function openCanvasModal(instructionText) {
    isWritingContent = true; // 모달이 열렸음을 표시
    document.getElementById('canvas-modal').style.display = 'block';
    document.getElementById('canvas-instruction').innerText = instructionText;
    
    // 모달이 표시된 후 캔버스 초기화
    setTimeout(() => {
        const canvas = document.getElementById('jsCanvas');
        if (canvas) {
            // 현재 표시된 크기에 맞게 다시 초기화
            canvas.width = canvas.clientWidth;
            canvas.height = canvas.clientHeight;
        }

        if (typeof initCanvas === 'function') {
            initCanvas();
        } else {
            console.error('initCanvas 함수를 찾을 수 없습니다.');
        }
    }, 200);
}

function closeCanvasModal() {
    isWritingContent = false; // 모달이 닫혔음을 표시
    document.getElementById('canvas-modal').style.display = 'none';
    removePainting();
    checkAutoProgress()
}

function getInstructionText(gameId, order) {
    if (gameId === 30 && order === 1) return '이름을 써보세요.';
    if (gameId === 35 && order === 1) return '이름을 써보세요.';
    if (gameId === 60 && order === 1) return "'할머니, 안녕하세요.'를 써보세요.";
    if (gameId === 60 && order === 2) return "'저는 인형을 받고 싶어요.'를 써보세요.";
    if (gameId === 60 && order === 3) return "'감사합니다. 곧 만나요.'를 써보세요.";
    if (gameId === 63 && order === 1) return "'비가 올 때는 우산을 씁니다.'를 써보세요.";
    if (gameId === 63 && order === 2) return "'아이가 장화를 신고 있어요.'를 써보세요.";
    if (gameId === 63 && order === 3) return "'아이 옆으로 자동차가 지나가요.'를 써보세요.";
    return '글자를 입력해보세요.';
}

// ===== 효과음 함수들 =====
function wrongA() { 
    new Audio('./audio/wrong.mp3').play(); 
}

function correctA() { 
    new Audio('./audio/correct.mp3').play(); 
}

// ===== 기존 AJAX 함수들 (하위 호환성 유지) =====
function getPrevContent(i){
    console.log("getPrevContent 호출됨:", i);
    if(i<=0){
        alert("이전 페이지가 없습니다.");
    }else{
        // AJAX로 이전 콘텐츠 가져오기 (현재는 서버 연동 생략)
        if (typeof fetch !== 'undefined') {
            // fetch API 사용
            fetch("GetLangGamePrevContent", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                }
            })
        .then(response => {
            console.log("이전 콘텐츠 응답 상태:", response.status);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("이전 콘텐츠 받은 데이터:", data);
            if (data.error) {
                throw new Error(data.error);
            }
            if (data.done) {
                alert("이전 페이지가 없습니다.");
            } else {
                // 콘텐츠 업데이트
                updateLangGameContent(data);
            }
        })
        .catch(error => {
            console.error("이전 콘텐츠 처리 오류:", error);
            console.error("오류 상세:", error.message);
            alert("오류가 발생했습니다: " + error.message);
        });
        } else {
            // XMLHttpRequest 사용 (구형 브라우저 호환)
            var xhr = new XMLHttpRequest();
            xhr.open("POST", "GetLangGamePrevContent", true);
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        try {
                            var data = JSON.parse(xhr.responseText);
                            console.log("이전 콘텐츠 받은 데이터 (XHR):", data);
                            if (data.error) {
                                throw new Error(data.error);
                            }
                            if (data.done) {
                                alert("이전 페이지가 없습니다.");
                            } else {
                                updateLangGameContent(data);
                            }
                        } catch (e) {
                            console.error("JSON 파싱 오류:", e);
                            alert("오류가 발생했습니다: " + e.message);
                        }
                    } else {
                        console.error("XHR 오류:", xhr.status);
                        alert("서버 오류가 발생했습니다: " + xhr.status);
                    }
                }
            };
            
            xhr.send();
        }
    }
}

function getNextContent(i,questionNum,gameSize){
    console.log("getNextContent 호출됨:", i, questionNum, gameSize);
    if(i>=gameSize-1){
        if(!confirm(questionNum + "번 문항의 게임을 종료하시겠습니까?")){return;}
    }
    
    // AJAX로 다음 콘텐츠 가져오기 (현재는 서버 연동 생략)
    if (typeof fetch !== 'undefined') {
        // fetch API 사용
        fetch("GetLangGameNextContent", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            }
        })
    .then(response => {
        console.log("응답 상태:", response.status);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log("받은 데이터:", data);
        if (data.error) {
            throw new Error(data.error);
        }
        if (data.done) {
            // 게임 종료 시 테스트 페이지로 이동
            window.location.href = "langTest.jsp";
        } else {
            // 콘텐츠 업데이트
            updateLangGameContent(data);
        }
    })
    .catch(error => {
        console.error("서버 오류:", error);
        console.error("오류 상세:", error.message);
        alert("오류가 발생했습니다: " + error.message);
    });
    } else {
        // XMLHttpRequest 사용 (구형 브라우저 호환)
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "GetLangGameNextContent", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    try {
                        var data = JSON.parse(xhr.responseText);
                        console.log("받은 데이터 (XHR):", data);
                        if (data.error) {
                            throw new Error(data.error);
                        }
                        if (data.done) {
                            window.location.href = "langTest.jsp";
                        } else {
                            updateLangGameContent(data);
                        }
                    } catch (e) {
                        console.error("JSON 파싱 오류:", e);
                        alert("오류가 발생했습니다: " + e.message);
                    }
                } else {
                    console.error("XHR 오류:", xhr.status);
                    alert("서버 오류가 발생했습니다: " + xhr.status);
                }
            }
        };
        
        xhr.send();
    }
}

// 콘텐츠 업데이트 함수
function updateLangGameContent(data) {
    // 1. 문제 이미지 변경
    const imgElement = document.querySelector('img[src*=".jpg"]');
    if (imgElement && data.imgUrl) {
        imgElement.src = data.imgUrl + ".jpg";
    }

    // 2. 화자 이름 변경
    const speakerDiv = document.querySelector('.w3-container.w3-round-large[style*="background-color:#12192C"]');
    if (speakerDiv) {
        if (data.speaker && data.speaker !== "-") {
            speakerDiv.textContent = data.speaker;
            speakerDiv.style.display = "block";
        } else {
            speakerDiv.style.display = "none";
        }
    }

    // 3. 콘텐츠 텍스트 변경
    const contentDiv = document.querySelector('.w3-container.w3-padding-32');
    if (contentDiv) {
        if (data.content && data.content !== "-") {
            contentDiv.innerHTML = data.content;
            contentDiv.style.display = "block";
        } else {
            contentDiv.style.display = "none";
        }
    }

    // 4. 음성 파일 변경 및 자동 재생
    const audio = document.getElementById('main-audio');
    console.log("오디오 실행 전");
    console.log("audio: ", audio, " data.voiceUrl: ", data.voiceUrl);
    if (audio && data.voiceUrl) {
        audio.src = data.voiceUrl;
        audio.load();
        // 브라우저에서 자동 재생 시도
        console.log("오디오 재생 시도");
        audio.play().catch(function (error) {
            console.log("오디오 실패");
            console.warn("브라우저 자동 재생 실패, 사용자 인터랙션 필요:", error);
            // 재생 버튼을 눈에 띄게 표시
            showPlayButtonForUpdate();
        });
    }

    // 5. 힌트/정답 버튼 상태 업데이트
    updateHintAnswerButtons(data);
}

// 힌트/정답 버튼 상태 업데이트
function updateHintAnswerButtons(data) {
    // 힌트 버튼
    const hintButton = document.querySelector('button[onclick="openHint()"]');
    if (hintButton) {
        if (data.hint) {
            hintButton.style.display = "inline-block";
            // 힌트 모달 내용 업데이트
            const hintModal = document.getElementById('hint-modal');
            if (hintModal) {
                const hintText = hintModal.querySelector('p');
                if (hintText) {
                    hintText.textContent = data.hint;
                }
            }
        } else {
            hintButton.style.display = "none";
        }
    }

    // 정답 버튼
    const answerButton = document.querySelector('button[onclick="openAnswer()"]');
    if (answerButton) {
        if (data.answer) {
            answerButton.style.display = "inline-block";
            // 정답 모달 내용 업데이트
            const answerModal = document.getElementById('answer-modal');
            if (answerModal) {
                const answerText = answerModal.querySelector('p');
                if (answerText) {
                    answerText.textContent = data.answer;
                }
                // 정답 오디오 업데이트
                const answerAudio = answerModal.querySelector('audio');
                if (answerAudio && data.answerVoice) {
                    answerAudio.src = data.answerVoice;
                }
            }
        } else {
            answerButton.style.display = "none";
        }
    }

    // 평가기준 버튼
    const criteriaButton = document.querySelector('button[onclick="openCriteria()"]');
    if (criteriaButton) {
        if (data.criteria) {
            criteriaButton.style.display = "inline-block";
            // 평가기준 모달 내용 업데이트
            const criteriaModal = document.getElementById('criteria-modal');
            if (criteriaModal) {
                const criteriaText = criteriaModal.querySelector('p');
                if (criteriaText) {
                    criteriaText.textContent = data.criteria;
                }
            }
        } else {
            criteriaButton.style.display = "none";
        }
    }
}

// 업데이트된 콘텐츠에서 재생 버튼 강조
function showPlayButtonForUpdate() {
    const playButtons = document.querySelectorAll('button[onclick="audioReplay()"]');
    playButtons.forEach(button => {
        button.style.backgroundColor = '#ff6b6b';
        button.style.animation = 'pulse 2s infinite';
        button.style.border = '2px solid #ff4757';
    });
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

// ===== 전역 함수로 노출 =====
window.initializeLangGame = initializeLangGame;
window.startEvaluation = startEvaluation;
window.playVoice = playVoice;
window.audioReplay = audioReplay;
window.goPrev = goPrev;
window.goNext = goNext;
window.updateContentForIndex = updateContentForIndex;
window.openHint = openHint;
window.closeHint = closeHint;
window.openAnswer = openAnswer;
window.closeAnswer = closeAnswer;
window.openCriteria = openCriteria;
window.closeCriteria = closeCriteria;
window.openModal = openModal;
window.closeModal = closeModal;
window.openCanvasModal = openCanvasModal;
window.closeCanvasModal = closeCanvasModal;
window.getInstructionText = getInstructionText;
window.wrongA = wrongA;
window.correctA = correctA;
window.getPrevContent = getPrevContent;
window.getNextContent = getNextContent;
window.updateLangGameContent = updateLangGameContent;
window.updateHintAnswerButtons = updateHintAnswerButtons;
window.showPlayButtonForUpdate = showPlayButtonForUpdate;
window.FinishGame = FinishGame;
window.isExceptionGame50 = isExceptionGame50;
window.isExceptionGame = isExceptionGame;
window.updateButtonsForCurrentData = updateButtonsForCurrentData;
window.updateModalContents = updateModalContents;
window.updateButtonStates = updateButtonStates;
window.checkAutoProgress = checkAutoProgress;
window.handleExceptionContent = handleExceptionContent;
window.playVoice = playVoice;