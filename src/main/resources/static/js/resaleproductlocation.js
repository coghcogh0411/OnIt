$(function() {
	$(".location-button").on("click", function() {
		$(".modal-overlay").fadeIn(200);
	});

	$("#location-btn").on("click", function() {
		navigator.geolocation.getCurrentPosition(function(loc) {
			var lat = loc.coords.latitude;
			var lng = loc.coords.longitude;
			console.log("x" + lng + "y" + lat);
			$.ajax({
				url: "https://dapi.kakao.com/v2/local/geo/coord2address.json",
				data: { "x": lng, "y": lat },
				beforeSend: function(req) {
					req.setRequestHeader("Authorization", "KakaoAK 3a68a92db5091200e91fdb8d1750a15b");
				},
				success: function(locData) {
					var region = locData.documents[0].address.region_2depth_name;
					
					$.getJSON("region.get?region=" + region, function(regionData) {
						$(".region-list").empty();
						$.each(regionData.region, function(index, item) {
							$(".region-list").append("<li><a href='/region-get'>" + item.firstName + ", " + item.secondName + "</a></li>");
						})
					}).fail(function() {
						$(".region-list").empty();
						$(".region-list").append("<li>지역 정보를 불러오는 데 실패했습니다.</li>");
					});
				}
			});
		});
	})

	// 닫기 버튼 클릭 시 모달 닫기
	$(".close-modal").on("click", function() {
		$(".modal-overlay").fadeOut(200);
	});

	$("#locationInput").keyup(function() {
		var region = $(this).val();
		$.getJSON("region.get?region=" + region, function(regionData) {
			$(".region-list").empty();
			$.each(regionData.region, function(index, item) {
				$(".region-list").append("<li><a href='/region-get'>" + item.firstName + ", " + item.secondName + "</a></li>");
			})
		}).fail(function() {
			$(".region-list").empty();
			$(".region-list").append("<li>지역 정보를 불러오는 데 실패했습니다.</li>");
		});
	});
	$(document).on("click", ".region-list li", function(e) {
		e.preventDefault();
		e.stopPropagation();

		// 예: <li>광주광역시, 서구</li> -> text() = "광주광역시, 서구"
		var name = $(this).text();
		// 화면에는 전체 문자열("광주광역시, 서구")를 표시
		$(".location-button").text(name);

		// 모달 닫은 뒤 서버로 파라미터 전송
		$(".modal-overlay").fadeOut(200, function() {
			var fromValue = "home";

			// DB 검색용: 쉼표 제거
			// 예: "광주광역시, 서구" -> split -> ["광주광역시", "서구"]
			var splitted = name.split(", ");
			if (splitted.length >= 2) {
				// region=광주광역시 & region2=서구
				// 최종적으로 region + region2 = "광주광역시서구" 로 DB 검색
				location.href = "/region-get?region="
					+ encodeURIComponent(splitted[0])
					+ "&region2=" + encodeURIComponent(splitted[1])
					+ "&from=" + encodeURIComponent(fromValue);
			} else {
				// 혹시 split이 안 된 경우(지역명에 쉼표가 없는 경우 등)
				// 그냥 regionParam1만 보내거나, 예외처리
				location.href = "/region-get?region="
					+ encodeURIComponent(name)
					+ "&region2="
					+ "&from=" + encodeURIComponent(fromValue);
			}
		});
	});
});