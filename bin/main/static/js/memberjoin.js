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
$(function() {
	joinImgInsert();
});