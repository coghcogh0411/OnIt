function communityDetail(no){
	location.href="community-detail?pno="+no;
}
function replyreg(no){
	var reply = document.getElementById("reply").value;
	location.href="reply-reg?no="+no+"&reply="+reply;
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