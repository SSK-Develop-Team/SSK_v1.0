// /WebContent/js/bookChat.js
(function () {
  var ctx = window.__CTX__;
  var chatList = document.getElementById("chatList");
  var msgInput = document.getElementById("msgInput");
  var sendBtn = document.getElementById("sendBtn");

  var THREAD_KEY = "chat.thread_id";
  var BOOT_KEY = "chat.booted";
  var HISTORY_KEY = "chat.history";
  
  // 새로고침 시 대화 유지 / 재진입 시 thread_id 초기화, 새로운 대화
    function navType() {
    var nav = performance.getEntriesByType && performance.getEntriesByType("navigation");
    if (nav && nav[0] && nav[0].type) return nav[0].type; // 'navigate' | 'reload' | 'back_forward'
    // fallback (구형 브라우저)
    var t = performance.navigation && performance.navigation.type;
    return t === 1 ? "reload" : "navigate";
  }

  function newThreadId() {
    if (window.crypto && crypto.randomUUID) return crypto.randomUUID();
    return "t_" + Date.now() + "_" + Math.random().toString(16).slice(2);
  }

  // 로드 시: reload면 유지, 그 외면 리셋
  (function initThread() {
    var t = navType();
    var keep = t === "reload";

    if (!keep) {
      sessionStorage.removeItem(THREAD_KEY);
      sessionStorage.removeItem(BOOT_KEY);
      sessionStorage.removeItem(HISTORY_KEY);
    }

    if (!sessionStorage.getItem(THREAD_KEY)) {
      sessionStorage.setItem(THREAD_KEY, newThreadId());
      sessionStorage.setItem(BOOT_KEY, "false");
    }
  })();

  function getThreadId() {
    return sessionStorage.getItem(THREAD_KEY);
  }
  function getBooted() {
    return sessionStorage.getItem(BOOT_KEY) === "true";
  }
  function setBooted(v) {
    sessionStorage.setItem(BOOT_KEY, v ? "true" : "false");
  }
  
  // 사용자 입력창 관련
  function autosizeTextarea(el) {
    el.style.height = "auto";
    el.style.height = Math.min(el.scrollHeight, 140) + "px";
  }

  msgInput.addEventListener("input", function () {
    autosizeTextarea(msgInput);
  });

  msgInput.addEventListener("keydown", function (e) {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      sendMessage();
    }
  });

  function scrollToBottom() {
    var last = chatList.lastElementChild;
    if (last) last.scrollIntoView({ behavior: "smooth", block: "end" });
  }

  function stripStepPrefix(text) {
    var s = (text == null ? "" : String(text)).replace(/^\s+|\s+$/g, "");
    return s.replace(/^STEP:\s*\d+\s*\n?/, "").replace(/^\s+|\s+$/g, "");
  }

  function renderAssistantMarkdownInto(el, text) {
    var cleaned = stripStepPrefix(text);
    var dirty = marked.parse(cleaned);
    el.innerHTML = DOMPurify.sanitize(dirty);
  }

  function appendMessage(role, text) {
    var div = document.createElement("div");
    div.className = "msg " + (role === "user" ? "user" : "assistant");

    if (role === "assistant") {
      renderAssistantMarkdownInto(div, text);
    } else {
      div.textContent = text == null ? "" : String(text);
    }

    chatList.appendChild(div);
    scrollToBottom();
    return div;
  }
 
  
  function loadHistory() {
    try {
      var raw = sessionStorage.getItem(HISTORY_KEY);
      if (!raw) return;
      var arr = JSON.parse(raw);
      if (!Array.isArray(arr)) return;

      arr.forEach(function (m) {
          rawAppend(m.role, m.text);
      });
    } catch (e) {
      console.warn("history restore failed", e);
    }
  }

  function saveHistory(role, text) {
    try {
      var raw = sessionStorage.getItem(HISTORY_KEY);
      var arr = raw ? JSON.parse(raw) : [];
      if (!Array.isArray(arr)) arr = [];
      arr.push({ role: role, text: text == null ? "" : String(text) });
      sessionStorage.setItem(HISTORY_KEY, JSON.stringify(arr));
    } catch (e) {
    }
  }

  var rawAppend = appendMessage;

  appendMessage = function (role, text) {
    var el = rawAppend(role, text);
    saveHistory(role, text);
    return el;
  };
  
  loadHistory();  
  
  function lockUI(locked) {
    msgInput.disabled = locked;
    sendBtn.disabled = locked;
  }

  function makeTypingBubble() {
    var div = document.createElement("div");
    div.className = "msg assistant typing";
    div.innerHTML =
      '<div class="typing-dots" aria-label="typing">' +
      "<span></span><span></span><span></span>" +
      "</div>" +
      '<div class="typing-text">생각하는 중...</div>';

    chatList.appendChild(div);
    scrollToBottom();
    return div;
  }

  function setTypingText(typingEl, text) {
    var t = typingEl.querySelector(".typing-text");
    if (t) t.textContent = text == null ? "" : String(text);
  }

  // 로딩 모달
  function showLoadingModal(show) {
		var modal = document.getElementById("loadingModal");
		if (!modal) return;
		
			
			showLoadingModal._timers = showLoadingModal._timers || [];
			showLoadingModal._timers.forEach(clearTimeout);
			showLoadingModal._timers = [];
			
			modal.style.display = show ? "block" : "none";
		if (!show) return;
		
		var img = document.getElementById("loadingImg");
		
		if (img) img.src = "./image/book_loading_1.gif";
		
		showLoadingModal._timers.push(setTimeout(function () {
		  if (img) img.src = "./image/book_loading_2.gif";
		}, 30000));
		
		showLoadingModal._timers.push(setTimeout(function () {
		  if (img) img.src = "./image/book_loading_3.gif";
		}, 60000));
	}

  function sendMessage() {
	  console.log("sendMessage called");
	  
	  var msg = (msgInput.value || "").trim();
	  if (!msg) return;

	  appendMessage("user", msg);
	  msgInput.value = "";
	  autosizeTextarea(msgInput);

	  lockUI(true);
	  var typing = makeTypingBubble();

	  fetch(ctx + "/ChatApi", {
	    method: "POST",
	    headers: { "Content-Type": "application/json" },
	    body: JSON.stringify({ thread_id: getThreadId(), message: msg })
	  })
	    .then(function (res) {
	      return res.json().catch(function () { return {}; }).then(function (data) {
	        if (!res.ok) throw new Error(data.error || "chat failed");
	        return data;
	      });
	    })
	    .then(function (data) {
	      setBooted(true);
	      
	      if (data.done_collect) {
	        setTypingText(typing, "좋아요! 이제 동화책을 만들고 있어요. 잠시만 기다려 주세요.");
	        if (typeof showLoadingModal === "function") showLoadingModal(true);

	        return fetch(ctx + "/BookFinalize", { method: "POST" })
	          .then(function (fin) {
	            return fin.json().catch(function () { return {}; }).then(function (finData) {
	              if (!fin.ok) throw new Error(finData.error || "finalize failed");
	              window.location.href = finData.redirectUrl;
	            });
	          });
	      }

	      typing.classList.remove("typing");
	      renderAssistantMarkdownInto(typing, data.reply || "");

	      saveHistory("assistant", data.reply || "");
	    })
	    .catch(function (e) {
	      console.error(e);
	      setTypingText(typing, "잠시 오류가 났어. 다시 한 번 말해줄래?");
	      if (typeof showLoadingModal === "function") showLoadingModal(false);
	    })
	    .finally(function () {
	      lockUI(false);
	      msgInput.focus();
	    });
	}
  
  
  // onclick에서 쓰기 위해 전역 등록
  sendBtn.addEventListener("click", sendMessage);
  window.sendMessage = sendMessage;
})();