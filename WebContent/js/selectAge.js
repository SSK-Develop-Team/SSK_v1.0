

function ageCheckClose(){
	document.getElementById('ageCheck').style.display='none';
}

function ageSelect(){
	document.getElementById('ageCheck').style.display='none';
	document.getElementById('ageSelect').style.display='block';
}

function ageSelectClose(){
	document.getElementById('ageSelect').style.display='none';
}

function modalOpen(){
	$(".modal").css('display', 'block');
	$(".modalLayer").css('display', 'block');
}

function selectModalOpen(){
	$(".modal").css('display', 'none');
	$(".selectModal").css('display', 'block');
	$(".modalLayer").css('display', 'block');
}

function modalClose(){
	$(".modal").css('display', 'none');
	$(".modalLayer").css('display', 'none');
}

function selectModalClose(){
	$(".selectModal").css('display', 'none');
	$(".modalLayer").css('display', 'none');
}