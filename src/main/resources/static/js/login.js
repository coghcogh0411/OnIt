$(function() {
	var user = document.getElementById("user");
	const modalOverlay = document.querySelector(".modal-overlay");
	var mypage = document.getElementById("myPage");
	
	if (user == null) {
		mypage.addEventListener("click", (e) => {
			modalOverlay.style.display = "flex"; // 보이게
			
		});
		// 모달 바깥 클릭 시 닫기 (선택사항)
		modalOverlay.addEventListener("click", (e) => {
			if (e.target === modalOverlay) {
				modalOverlay.style.display = "none";
			}
		});

	}

})