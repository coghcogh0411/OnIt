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

$(function() {
	connectAddressControlAreaSummonEvent();
	subcontentController();
});