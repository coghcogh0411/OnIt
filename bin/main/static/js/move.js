function communityDetail(no){
	location.href="community-detail?no="+no;
}
function replyreg(no){
	var reply = document.getElementById("reply").value;
	location.href="reply-reg?no="+no+"&reply="+reply;
	
}