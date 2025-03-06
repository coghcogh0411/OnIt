function communityDetail(no){
	location.href="community-detail?pno="+no;
}
function replyreg(no){
	var reply = document.getElementById("reply").value;
	location.href="reply-reg?no="+no+"&reply="+reply;
}
function resaleCategoryChange(no) {
    location.href = "resale-category?no=" + no;
}

function categorychange(no){
	location.href="community?categoryNo="+no;
}
function communityLike(no){
	location.href="community-like?pno="+no
}
function communityLiked(no){
	location.href="community-liked?pno="+no
}
function auctionLike(no){
	location.href="auction-like?pno="+no
}
function auctionLiked(no){
	location.href="auction-liked?pno="+no
}
function resaleLike(no){
	location.href="resale-like?pno="+no
}
function resaleLiked(no){
	location.href="resale-liked?pno="+no
}
function byeDo(){
	if (prompt("탈퇴하려면 \"탈퇴한다\" 입력하세요") == "탈퇴한다") {
			location.href="member-delete";
		}
}