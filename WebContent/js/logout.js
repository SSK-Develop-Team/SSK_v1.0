/* 로그아웃 */
function  doLogout() {
  var confirmResult = confirm('로그아웃 하시겠습니까?');
  if (confirmResult) {
	f.action = "DoLogout"; 
	f.method = "post";
	f.submit();
  }
}
function  alertLogin() {
  alert("로그인 후 이용해주세요.");
}
