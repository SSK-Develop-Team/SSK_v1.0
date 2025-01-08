/**
 * @author Jiwon Lee - Copyright 2023 Hong Lab
 * 회원가입 유효성 검사
 *  - 아이디 중복 검사
 *  - 이메일 형식 검사
 *  - 비밀번호 일치 검사
 *  - 빈칸 검사
 */

let checkedId = false;//아이디 확인
let checkedEmail = false;//이메일 확인
let checkedPW = false;//비밀번호 확인
let checkedEsmTimes= false; //알람 정보 확인

const idInput = document.getElementById("userId");
const checkIdBtn = document.getElementById("checkId");
const pwInput = document.getElementById("userPw");
const pwChkInput = document.getElementById("userPwChk");
const emailInput = document.getElementById("userEmail");
const nameInput = document.getElementById("userName");
const alarmSt = document.getElementsByClassName("alarmStart");
const alarmEd = document.getElementsByClassName("alarmEnd");
const alarmInv = document.getElementsByClassName("alarmInterval");


checkIdBtn.addEventListener('click', checkId);
pwInput.addEventListener('change', checkPW);
pwChkInput.addEventListener('change', checkPW);
emailInput.addEventListener('change', checkEmail);
nameInput.addEventListener('change', checkName);
Array.from(alarmSt).forEach(element => {
    element.addEventListener('change', checkEsmTimes);
});
Array.from(alarmEd).forEach(element => {
    element.addEventListener('change', checkEsmTimes);
});
Array.from(alarmInv).forEach(element => {
    element.addEventListener('change',checkEsmTimes);
});

window.onload = function(){
	const id = document.getElementById("userId").value;
	const originUserLoginId = document.getElementById("originUserLoginId").value;
	if(originUserLoginId!="") {
		checkId();
		checkEmail();
		checkName();	
	}
	checkEsmTimes();
}
/* 아이디 중복 검사 */
function checkId(){
	const id = document.getElementById("userId").value;
	const originUserLoginId = document.getElementById("originUserLoginId").value;
 	const idMsg = document.getElementById('check_id_m');

	idMsg.style.color = 'red';

	//입력 값이 있는지 확인
	if(id === ""){
		idMsg.style.color = 'red';
		idMsg.innerHTML = '아이디를 입력하세요.';
    	return false;
	}

	//계정을 수정하는 경우, 기존의 아이디와 일치하는지 확인
	if(originUserLoginId!="" && id == originUserLoginId){
		checkedId = true;
		checkIdBtn.style.backgroundColor = 'green';
		idMsg.style.color = 'green';
		idMsg.innerHTML = '기존의 아이디와 일치합니다.';
		return false;
	}

	const xhttp = new XMLHttpRequest();

	xhttp.onreadystatechange = function () {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
	    	result = xhttp.responseText;
	    	if (result === 'ok') {
	        	checkedId = true;
	        	checkIdBtn.style.backgroundColor = 'green';
	        	idMsg.style.color = 'green';
				idMsg.innerHTML = '사용 가능한 아이디입니다.';
			} else {
				checkedId = false;
				idInput.value = ''; // input창에 입력된 값 삭제
				idMsg.style.color = 'red';
	        	idMsg.innerHTML = '이미 존재하는 아이디입니다.';
	      	}
		}
	};

	xhttp.open('Post', './CheckId', true);
	xhttp.setRequestHeader('Content-Type', 'text/plain; charset=UTF-8');
	xhttp.send(id);
}


/* 중복확인을 완료하고 아이디를 수정하면 중복확인 취소 */
idInput.addEventListener('change', function (event) {
  if (checkedId === true) {
    checkedId = false;
    checkIdBtn.style.backgroundColor = '#51459E';
    document.getElementById('check_id_m').innerHTML = '';
  }
});

/* 이메일 형식 검사  */
function checkEmailForm(str) {
  const emailForm = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;

  if (!emailForm.test(str)) {
    return false;
  } else {
    return true;
  }
}
function checkEmail(){
	const email = document.getElementById('userEmail').value;
	const emailMsg = document.getElementById('check_email_m');
	emailMsg.style.color = 'red';

	// 입력값이 있는 지 확인
	if (email === '') {
		emailMsg.innerHTML = '이메일을 입력하세요.';
		checkedEmail = false;
	    return false;
	}else if (!checkEmailForm(email)) {
		emailMsg.innerHTML = '유효한 이메일주소가 아닙니다.';
		checkedEmail = false;
		return false;
	}else{
		emailMsg.innerHTML = '';
		checkedEmail = true;
		return true;
	}
}

/* 비밀번호 확인 */
function checkPW() {
  const pw1 = document.getElementById('userPw').value; // 비밀번호
  const pw2 = document.getElementById('userPwChk').value; // 비밀번호 확인
  const checkMsg = document.getElementById('check_pw_m'); // 비밀번호 확인 결과 메세지
  checkMsg.style.color = 'red';

  // 비밀번호가 입력되지 않은 경우
  if (pw1 == '') {
	  checkMsg.innerHTML = '비밀번호를 입력하세요.';
	  checkedPW = false;
  } else if (pw2 == '') {
	  checkMsg.innerHTML = '비밀번호 확인을 입력하세요.';
	  checkedPW = false;
  } else if (pw1 == pw2) {
	  // 비밀번호가 서로 같은 경우
	  checkMsg.style.color = 'green';
	  checkMsg.innerHTML = '비밀번호가 확인되었습니다. ';
	  checkedPW = true;
  } else {
	  checkMsg.innerHTML = '비밀번호가 서로 다릅니다. 다시 확인해 주세요.';
	  checkedPW = false;
  }
}

function checkName(){
	const name = document.getElementById("userName").value;
 	const nameMsg = document.getElementById('check_name_m');
	nameMsg.style.color = 'red';

	//입력 값이 있는지 확인
	if(name === ""){
		nameMsg.innerHTML = '이름을 입력하세요.';
    	return false;
	}else{
		nameMsg.innerHTML = '';
	}
	return true;
}

/*알람시간 검사*/
function checkEsmTimes() {
	const starts = document.querySelectorAll('input[name="alarmStart"]');
	const ends = document.querySelectorAll('input[name="alarmEnd"]');
	const intervals = document.querySelectorAll('input[name="alarmInterval"]');
	  
	const alarmMsg = document.getElementsByClassName('check_alarm_m')[0];
	alarmMsg.style.color = 'red';
	var arr=[];
	
	//유효성 검사
	for (let i = 0; i < starts.length; i++) {
        var startHours = parseInt(starts[i].value.split(':')[0], 10);
        var endHours = parseInt(ends[i].value.split(':')[0], 10);
		  
	    if (starts[i].value.trim() === "" || ends[i].value.trim() === "" || intervals[i].value.trim()=== "") {
			alarmMsg.innerHTML = "알람 설정 시간을 모두 입력하세요."; 
			checkedEsmTimes=false;
			return false;
	    }else if(endHours-startHours < intervals[i].value){
			//데이터마다  종료 시간 - 시작 시간 > 간격 -> false일 경우 notify 
			alarmMsg.innerHTML = "알람 간격이 설정 시간을 넘어가지 않도록 입력하세요."; 
			checkedEsmTimes=false;
	      	return false;
		}else{
			checkedEsmTimes=true;
			alarmMsg.innerHTML = '';
		}
		//1.배열 시작시간,종료시간 넣기
		arr.push([startHours,endHours]); 
	  } 
	// 알람 설정 시간 겹치지 않게 
	//1.[시작시간, 종료 시간][시작 시간, ...]] -> 2.시작 시간 기준으로 정렬 -> 3.이전 종료 시간이 curr 시작 시간 이전인가? false라면 notify    
	
	

	//2.시작 시간을 기준으로 정렬
	arr.sort(function(a, b) {
		return a[0] - b[0];
	});
	//3.이전 종료 시간이 curr 시작 시간 이전인가? false라면 notify    
	for (let i = 0; i < starts.length; i++) {
		if(i>0){
			if(arr[i-1][1]>arr[i][0]){
				alarmMsg.innerHTML = "알람 설정 시간이 겹치치 않도록 입력하세요."; 
				checkedEsmTimes=false;
	      		return false;
			}	
		}
	}
	checkedEsmTimes=true;
	return true;
}


/* 빈칸 검사 */
function checkValueforChild(){
	/*
	 - 아이디 빈칸 & 중복 검사 : checkedId
	 - 이메일 : checkedEmail
	 - 비밀번호 : checkedPw
	 - 이름 : checkedName
	*/

	if(!checkedId) {
		alert('아이디를 중복 확인해주세요!');
	    return false;
	}else if (!checkedPW) {
	    alert('비밀번호 확인을 해주세요!');
	    return false;
	}else if(!checkedEmail){
		alert('이메일을 확인해주세요!');
		return false;
	}else if(!checkedEsmTimes){
		alert('알람을 확인해주세요!');
		return false;
	}
	if(!checkName()) {
    	return false;
  	}
	return true;
}
/* 빈칸 검사 */
function checkValueforExpert(){
	/*
	 - 아이디 빈칸 & 중복 검사 : checkedId
	 - 이메일 : checkedEmail
	 - 비밀번호 : checkedPw
	 - 이름 : checkedName
	*/

	if(!checkedId) {
		alert('아이디를 중복 확인해주세요!');
	    return false;
	}else if (!checkedPW) {
	    alert('비밀번호 확인을 해주세요!');
	    return false;
	}else if(!checkedEmail){
		alert('이메일을 확인해주세요!');
		return false;
	}
	if(!checkName()) {
    	return false;
  	}
	return true;
}

function togglePasswordType(){
	const pwInput = document.getElementById("userPw");
	const pwToggleIcon = document.getElementById("pwToggleIcon");

	if(pwInput.getAttribute("type") == "password"){
		pwInput.setAttribute("type","text");
		pwToggleIcon.classList.remove("fa-eye");
		pwToggleIcon.classList.add("fa-eye-slash");
	}else{
		pwInput.setAttribute("type","password");
		pwToggleIcon.classList.remove("fa-eye-slash");
		pwToggleIcon.classList.add("fa-eye");
	}
}





 function add_tr(tbodyId) {
    var tbody = document.getElementById(tbodyId); // tbody 요소 가져오기
    var newRow = document.createElement("tr"); // 새로운 행 생성
    // 새로운 행의 HTML 내용
    newRow.innerHTML = `
        <td style="padding-left:0;padding-top:0;"><input type="time" class="w3-input alarmStart" name="alarmStart" placeholder="Start Time"></td>
							<td style="padding-top:0;"><input type="time" class="w3-input alarmEnd" name="alarmEnd" placeholder="End Time"></td>
							<td style="padding-top:0;"><input type="text" class="w3-input alarmInterval" style="min-width: 25px;" name="alarmInterval" placeholder="Interval"></td>
							<td style="padding-top:0; width: 12%; padding-right:0;"><input type='button' class="w3-bar w3-gray" style="height: 34px;" value='-' onclick='deleteRow(this)' /></td>
    `;

    tbody.appendChild(newRow); // tbody에 새로운 행 추가
    
    newRow.querySelector('.alarmStart').addEventListener('change', checkEsmTimes);
    newRow.querySelector('.alarmEnd').addEventListener('change', checkEsmTimes);
    newRow.querySelector('.alarmInterval').addEventListener('change', checkEsmTimes);
    checkEsmTimes();
    }
    

  
function deleteRow(This){
	var tbody = This.closest('tbody'); // 현재 행이 속한 tbody 찾기

    if (tbody.childElementCount == 2) {
        alert("삭제할 수 없습니다.");
    } else {
        This.closest('tr').remove(); // 삭제
        checkEsmTimes();
    }
   
	}
