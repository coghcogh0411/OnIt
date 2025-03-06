function joinImgInsert() {
	const box = document.querySelector(".profile-picture");
	const previewImage = document.getElementById("preview-image");
	const fileInput = document.getElementById("profile-image");

	// 드래그 앤드롭 이벤트 처리
	box.addEventListener("dragover", function(e) {
		e.preventDefault();
		box.style.backgroundColor = "#e0e0e0";
	});

	box.addEventListener("dragleave", function() {
		box.style.backgroundColor = "#f0f0f0";
	});

	box.addEventListener("drop", function(e) {
		e.preventDefault();
		box.style.backgroundColor = "#f0f0f0";

		const file = e.dataTransfer.files[0];
		if (file && file.type.startsWith("image")) {
			displayImage(file);
			updateInputFile(file); // 드래그앤드롭으로 업로드 시 input 값 설정
		}
	});

	// Input 파일 선택 이벤트 처리
	fileInput.addEventListener("change", function(e) {
		const file = e.target.files[0];
		if (file && file.type.startsWith("image")) {
			displayImage(file);
		}
	});

	// 이미지 미리보기 표시 함수
	function displayImage(file) {
		const reader = new FileReader();
		reader.onload = function(e) {
			previewImage.src = e.target.result;
			previewImage.style.display = "block";
		};
		reader.readAsDataURL(file);
	}

	// 드래그앤드롭 파일을 input에 설정하는 함수
	function updateInputFile(file) {
		const dataTransfer = new DataTransfer();
		dataTransfer.items.add(file);
		fileInput.files = dataTransfer.files; // input 요소에 파일 설정
	}
}
function JoinValidCheck() {
	const form = document.forms["joinForm"];
	const email = document.getElementById("email");
	const username = document.getElementById("username");
	const password = document.getElementById("password");
	const passwordConfirm = document.getElementById("password-confirm");
	const nickname = document.getElementById("nickname");
	const nameInput = document.getElementById("name");

	const year = form.year;
	const month = form.month;
	const day = form.day;

	const phone1 = form.phone1;
	const phone2 = form.phone2;
	const phone3 = form.phone3;

	//const profileImg = document.getElementById("profile-image");

	form.addEventListener("submit", function(e) {
		// 1) 이메일 체크
		if (isEmpty(email)) {
			alert("이메일을 입력하세요");
			email.focus();
			e.preventDefault();
			return;
		}
		// 이메일 형식은 브라우저 기본 type="email"으로 어느정도 검사

		// 2) 아이디(=username)
		if (isEmpty(username)) {
			alert("아이디를 입력하세요");
			username.focus();
			e.preventDefault();
			return;
		}
		// 아이디 최소 4글자
		if (lessThan(username, 4)) {
			alert("아이디는 최소 4글자 이상이어야 합니다");
			username.focus();
			e.preventDefault();
			return;
		}
		// 아이디에 한글/특수문자 들어있는지 검사
		if (containsHS(username)) {
			alert("아이디는 영어,숫자,@, -, _, . 만 가능합니다");
			username.focus();
			e.preventDefault();
			return;
		}

		// 3) 비밀번호
		if (isEmpty(password)) {
			alert("비밀번호를 입력하세요");
			password.focus();
			e.preventDefault();
			return;
		}
		// 비밀번호 확인
		if (notEqual(password, passwordConfirm)) {
			alert("비밀번호가 일치하지 않습니다");
			passwordConfirm.focus();
			e.preventDefault();
			return;
		}

		// 4) 닉네임
		if (isEmpty(nickname)) {
			alert("닉네임을 입력하세요");
			nickname.focus();
			e.preventDefault();
			return;
		}
		// 닉네임도 한글/특수문자 금지라면 containsHS로 검사할 수 있음(필요시)

		// 5) 이름
		if (isEmpty(nameInput)) {
			alert("이름을 입력하세요");
			nameInput.focus();
			e.preventDefault();
			return;
		}
		// 필요시 이름도 영문만인지 한글 허용인지 정책 결정

		// 6) 생년월일
		if (isEmpty(year) || isNotNum(year) || year.value.length != 4) {
			alert("올바른 연도(YYYY)를 입력하세요");
			year.focus();
			e.preventDefault();
			return;
		}
		if (isEmpty(month) || isNotNum(month) || month.value.length < 1 || month.value.length > 2) {
			alert("올바른 월(MM)을 입력하세요");
			month.focus();
			e.preventDefault();
			return;
		}
		if (isEmpty(day) || isNotNum(day) || day.value.length < 1 || day.value.length > 2) {
			alert("올바른 일(DD)을 입력하세요");
			day.focus();
			e.preventDefault();
			return;
		}
		// 실제로는 월1~12, 일1~31 범위도 체크해야 함

		// 7) 전화번호
		if (isEmpty(phone1) || isEmpty(phone2) || isEmpty(phone3)) {
			alert("휴대전화 번호를 모두 입력하세요");
			e.preventDefault();
			return;
		}
		if (isNotNum(phone1) || isNotNum(phone2) || isNotNum(phone3)) {
			alert("전화번호는 숫자만 입력하세요");
			e.preventDefault();
			return;
		}

		// 8) 프로필 사진(필요시)
		// if (!isEmpty(profileImg)) {
		//   // 특정 확장자만 허용
		//   if ( isNotType(profileImg, "png") && isNotType(profileImg, "jpg") ) {
		//     alert("이미지는 png 또는 jpg만 가능합니다");
		//     e.preventDefault();
		//     return;
		//   }
		// }

		// 모든 검사 통과 → submit 진행
	});
}

function JoinIdInputEvent() {
	$("#username").keyup(function() {
		var userId = $(this).val();
		$.getJSON(`member.get?m=${userId}`, function(memberData) {
			if (memberData.member[0] == null) {
				$("#id").css("display", "none");
			} else {
				$("#id").css("display", "flex");
			}
		})
	})
	$("#nickname").keyup(function() {
		var nickname = $(this).val();
		$.get(`membernickname.get?m=${nickname}`, function(nicknameData) {
			console.log(nicknameData);
			if (nicknameData == null || !nicknameData) {
				$("#nick").css("display", "none");
			} else {
				$("#nick").css("display", "flex");
			}
		})
	})
}

function JoinPwInputEvent() {
	$("#password").keyup(function() {
		var pw = $("#password").val();
		var pwconfirm = $("#password-confirm").val();
		if (pw == pwconfirm) {
			$("#pwconfirm").css("display", "none");
		} else {
			$("#pwconfirm").css("display", "flex");
		}
	})
	$("#password-confirm").keyup(function() {
		var pw = $("#password").val();
		var pwconfirm = $("#password-confirm").val();
		if (pw == pwconfirm) {
			$("#pwconfirm").css("display", "none");
		} else {
			$("#pwconfirm").css("display", "flex");
		}
	})
}
$(function() {
	joinImgInsert();
	JoinIdInputEvent();
	JoinPwInputEvent()
	JoinValidCheck()
});