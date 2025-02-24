function connectAddressControlAreaSummonEvent() {
	$("#addr1, #addr2").click(function() {
		new daum.Postcode({
			oncomplete: function(data) {
				$("#addr1").val(data.roadAddress);
			}
		}).open();
	});
}

function subcontentController() {
	var isTrue = false;
	$(".toggle-sidebar").click(function() {
		if (isTrue) {
			$(".subcontent").css("right", "-375px");
			$(".toggle-sidebar div").text("<");
		} else {
			$(".subcontent").css("right", "0px");
			$(".toggle-sidebar div").text(">");
		}
		isTrue = !isTrue;
	})
}
function chatcontroller() {
	var socket = io("http://sd-beanmouse.duckdns.org:43218");
	 var userId = "[[${session.loginMember.id}]]"; // Thymeleaf에서 로그인한 사용자 ID 가져오기

	 // 채팅방 목록 업데이트 함수
	 function updateChatRooms(chatPartner) {
	   if ($(`.chat-room[data-partner="${chatPartner}"]`).length === 0) {
	     $(".chat-box").append(`
	       <div class="chat-room" data-partner="${chatPartner}">
	         <img src="default_profile.png" />
	         <p><strong>${chatPartner}</strong></p>
	         <span class="new-message">1</span>
	       </div>
	     `);
	   }
	 }

	 // 채팅방 클릭 시 열기
	 $(document).on("click", ".chat-room", function () {
	   var chatPartner = $(this).data("partner");
	   $("#chatTitle").text(chatPartner);
	   $("#chatModal").show();
	   loadChatHistory(chatPartner);
	   $(this).find(".new-message").remove(); // 새 메시지 알림 제거
	 });

	 $(".close-btn").click(function () {
	   $("#chatModal").hide();
	 });

	 $("#modalSendBtn").click(sendMessage);
	 $("#modalMessageInput").keypress(function (event) {
	   if (event.which === 13) sendMessage();
	 });

	 function sendMessage() {
	   var message = $("#modalMessageInput").val().trim();
	   var chatPartner = $("#chatTitle").text();

	   if (message !== "") {
	     socket.emit("sendMessage", {
	       sender: userId,
	       receiver: chatPartner,
	       message: message,
	     });

	     $("#chatContent").append(`<div class="message mine"><p>${message}</p></div>`);
	     $("#modalMessageInput").val("");
	     updateChatRooms(chatPartner); // 보낸 사람도 채팅방 추가
	   }
	 }

	 // 메시지 수신
	 socket.on("receiveMessage", function (data) {
	   if (data.receiver === userId) {
	     updateChatRooms(data.sender); // 채팅방 자동 생성
	     $("#chatContent").append(`<div class="message other"><p>${data.message}</p></div>`);
	   }
	 });

	 // 채팅 기록 불러오기
	 function loadChatHistory(chatPartner) {
	   $.get(`/chat/history?partner=${chatPartner}`, function (data) {
	     $("#chatContent").html("");
	     data.forEach(function (msg) {
	       var msgClass = msg.sender === userId ? "mine" : "other";
	       $("#chatContent").append(`<div class="message ${msgClass}"><p>${msg.message}</p></div>`);
	     });
	   });
	 }

	 // 서버에서 기존 채팅방 목록 불러오기
	 $.get("/chat/rooms", function (chatRooms) {
	   chatRooms.forEach(updateChatRooms);
	 });
}
$(function() {
	connectAddressControlAreaSummonEvent();
	subcontentController();
	chatcontroller();
});