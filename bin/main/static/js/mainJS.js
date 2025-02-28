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
	var userId = $(".user").text();
	var socket
	if (userId != null) {
		socket = io("http://sd-beanmouse.duckdns.org:43218");
		socket.on("connect", function() {
			socket.emit("joinRoom", userId); // 서버로 본인 아이디를 보내 룸에 join하도록 요청
		});
	}
	// 채팅 기록 불러오기
	function loadChatHistory(chatPartner) {
		$.get(`http://sd-beanmouse.duckdns.org:43218/chat/history?userId=${userId}&partner=${chatPartner}`, function(data) {
			console.log("채팅 기록 불러오기:", data);
			$("#chatContent").html("");
			data.forEach(function(msg) {
				var msgClass = msg.sender === userId ? "mine" : "other";
				$("#chatContent").append(`<div class="message ${msgClass}"><p>${msg.message}</p></div>`);
			});
		});
	}
	// 채팅방 목록 업데이트 함수
	function updateChatRooms(roomInfo) {
		let chatPartner, unreadCount;
		if (typeof roomInfo === "string") {
			// 만약 서버가 단순히 문자열 배열을 반환한다면
			chatPartner = roomInfo;
			unreadCount = 1; // 임의로 1 표시
		} else {
			// 서버가 객체를 반환한다고 가정: { partner: "...", unreadCount: ... }
			chatPartner = roomInfo.partner;
			unreadCount = roomInfo.unreadCount;
		}

		// 이미 해당 파트너의 채팅방이 있는지 확인
		  const $existingRoom = $(`.chat-room[data-partner="${chatPartner}"]`);
		  if ($existingRoom.length === 0) {
		    // 새 채팅방 생성
		    // unreadCount > 0인 경우에만 <span class="new-message">를 생성
		    const newMessageHtml = (unreadCount > 0)
		      ? `<span class="new-message">${unreadCount}</span>`
		      : "";

		    $(".chat-box").append(`
		      <div class="chat-room" data-partner="${chatPartner}">
		        <img src="default_profile.png" />
		        <p><strong>${chatPartner}</strong></p>
		        ${newMessageHtml}
		      </div>
		    `);
		  } else {
		    // 기존 채팅방이 있을 경우, unreadCount 갱신
		    if (unreadCount > 0) {
		      // 알림 배지가 없으면 생성, 있으면 수만 갱신
		      if ($existingRoom.find(".new-message").length === 0) {
		        $existingRoom.append(`<span class="new-message">${unreadCount}</span>`);
		      } else {
		        $existingRoom.find(".new-message").text(unreadCount);
		      }
		    } else {
		      // unreadCount = 0이면 알림 배지 제거
		      $existingRoom.find(".new-message").remove();
		    }
		  }
	}
	$(document).on("click", ".chat-btn", function(e) {
		e.preventDefault();
		// .seller-info 안의 .seller-name 요소에서 판매자 이름을 가져옴
		var chatPartner = $(this).closest('.seller-info').find('.seller-name').text().trim();
		$("#chatTitle").text(chatPartner);
		$("#chatModal").show();
		loadChatHistory(chatPartner);
	});
	$(document).on("click", ".buyer-chat-btn", function(e) {
		e.preventDefault();
		// .bidnickname 안의 .buyer 요소에서 판매자 이름을 가져옴
		var chatPartner = $(this).closest('.bidnickname').find('.buyer').text().trim();
		$("#chatTitle").text(chatPartner);
		$("#chatModal").show();
		loadChatHistory(chatPartner);
	});
	$(document).on("click", ".chat-room", function() {
		var chatPartner = $(this).find("strong").text().trim();
		$("#chatTitle").text(chatPartner);
		$("#chatModal").show();
		loadChatHistory(chatPartner);

		// --- 읽음 처리 API 호출 ---
		$.ajax({
			url: "http://sd-beanmouse.duckdns.org:43218/chat/rooms/read",
			method: "POST",
			contentType: "application/json",
			data: JSON.stringify({ userId: userId, partner: chatPartner }),
			success: function(res) {
				console.log("읽음 처리 완료:", res);
				// 이제 서버 DB에 lastRead가 갱신되었으므로, 새로고침해도 안 읽은 메시지 수가 감소
			},
			error: function(err) {
				console.error("읽음 처리 실패:", err);
			}
		});

		$(this).find(".new-message").remove(); // UI에서 알림 제거
	});
	$(".close-btn").click(function() {
		$("#chatModal").hide();
	});
	$("#modalSendBtn").click(sendMessage);
	$("#modalMessageInput").keypress(function(event) {
		if (event.which === 13) sendMessage();
	});
	function sendMessage() {
		var message = $("#modalMessageInput").val().trim();
		var chatPartner = $("#chatTitle").text();
		// 다시 userId를 가져오는 경우에도 .text() 사용
		var userId = $(".user").text();
		if (message !== "") {
			console.log("메시지 전송:", { sender: userId, receiver: chatPartner, message: message });
			socket.emit("sendMessage", {
				sender: userId,
				receiver: chatPartner,
				message: message,
			});
			$("#chatContent").append(`<div class="message mine"><p>${message}</p></div>`);
			$("#modalMessageInput").val("");
			updateChatRooms(chatPartner);
		}
	}
	socket.on("receiveMessage", function(data) {
		console.log("수신된 메시지:", data);
		if (data.receiver === userId) {
			updateChatRooms(data.sender);
			$("#chatContent").append(`<div class="message other"><p>${data.message}</p></div>`);
		}
	});
	$(".toggle-sidebar").click(function() {
		$.get("http://sd-beanmouse.duckdns.org:43218/chat/rooms?userId=" + userId, function(chatRooms) {
			chatRooms.forEach(updateChatRooms);
		}).fail(function(err) {
			console.error("GET /chat/rooms 실패:", err);
		});
	})
}
$(function() {
	connectAddressControlAreaSummonEvent();
	subcontentController();
	chatcontroller();
});