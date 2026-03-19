
// Lang Test
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

// Book Contents
function comingSoonModalClose(){
	document.getElementById('comingSoonModal').style.display='none';
}

/*function infoLoadCheckClose(){
	document.getElementById('infoLoadCheck').style.display='none';
}

function inputInfoClose(){
	document.getElementById('inputInfo').style.display='none';
}

function inputInfo(){
	document.getElementById('infoLoadCheck').style.display='none';
	document.getElementById('inputInfo').style.display='block';
}

function backInfoCheck(){
	document.getElementById('inputInfo').style.display='none';
	document.getElementById('infoLoadCheck').style.display='block';
}

function setGender(g) {
    document.getElementById('genderInput').value = g;

    const male = document.getElementById('btnMale');
    const female = document.getElementById('btnFemale');

    if (g === 'M') {
      male.classList.add('active');
      female.classList.remove('active');
    } else {
      female.classList.add('active');
      male.classList.remove('active');
    }
  }
  */
