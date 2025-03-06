$(function(){
	var socket = io.connect("http://sd-beanmouse.duckdns.org:43218");

	     socket.on("connect", function() {
	       console.log("✅ 소켓 연결 성공!");
	     });

	     socket.on("connect_error", function(error) {
	       console.error("❌ 소켓 연결 실패!", error);
	     });

	     $(".bid-button").click(function() {
	       var cost = $(".auction-cost").val();
	       var username = $(".user").text(); // 판매자 이름을 사용 (사용자 정보로 수정 가능)
	       var auctionNo = $(".auctionNo").val();
	       
	       console.log("💰 입찰 시도: " + cost + ", 사용자: " + username + " / 경매번호: " + auctionNo);

	       // 서버로 입찰 정보 전송
	       socket.emit("bid", { auctionNo: auctionNo, amount: cost, username: username });
	     });

	     socket.on("updateBids", function(bids) {
	       var pageAuctionNo = $(".auctionNo").val();
	       
	       // 입찰 목록 새로고침: 기존 목록 초기화
	       $(".bidder-list").empty();
	       
	    	// 서버로부터 받은 입찰 목록(data.bids)에서 현재 페이지 경매번호와 일치하는 항목만 표시
	       bids.forEach(function(bid) {
	         console.log(bid);
	         if (bid.auctionNo.no == pageAuctionNo) {
	             // auctionNo의 no, user의 nickname, amount 저장
	             var auctionNo = bid.auctionNo.no;
	             var nickname = bid.user.nickname;
	             var amount = bid.amount;

	             console.log("경매 번호:", auctionNo);
	             console.log("입찰자 닉네임:", nickname);
	             console.log("입찰 금액:", amount);
	             
	             // 여기에 해당 입찰 정보를 표시하는 로직 추가
	             
	             // 입찰자 이름과 입찰 금액 표시
	             var name = $("<span></span>").text(bid.user.nickname);
	             var amount = $("<span></span>").text(bid.amount + "원");
	             var li = $("<li></li>").append(name, " ", amount);
	             $(".bidder-list").append(li);
	           }
	       });
	       
	     });
});