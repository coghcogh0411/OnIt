function joinCheck() {
	var emailField = document.joinForm.email;
	var idField = document.joinForm.username;
	var pwField = document.joinForm.gm_pw;
	var pwChkField = document.joinForm.gm_pwChk;
	var nameField = document.joinForm.gm_name;
	var jumin1Field = document.joinForm.gm_jumin1;
	var jumin2Field = document.joinForm.gm_jumin2;
	var photoField = document.joinForm.gm_photo;

	if (isEmpty(emailField) || containsHS(emailField)) {
		alert("email?");
		idField.value = "";
		idField.focus();
		return false;
	}
	if (isEmpty(idField) || containsHS(idField)) {
		alert("id?");
		idField.value = "";
		idField.focus();
		return false;
	}
	if (isEmpty(pwField) || notEqual(pwField, pwChkField)
			|| notContains(pwField, "1234567890")) {
		alert("pw?");
		pwField.value = "";
		pwChkField.value = "";
		pwField.focus();
		return false;
	}
	if (isEmpty(nameField)) {
		alert("이름?");
		nameField.value = "";
		nameField.focus();
		return false;
	}
	if (isEmpty(jumin1Field) || isNotNum(jumin1Field)
			|| lessThan(jumin1Field, 6) || isEmpty(jumin2Field)
			|| isNotNum(jumin2Field)) {
		alert("주민번호?");
		jumin1Field.value = "";
		jumin2Field.value = "";
		jumin1Field.focus();
		return false;
	}
	if (isEmpty(photoField)
			|| (isNotType(photoField, "jpg") && isNotType(photoField, "png") && isNotType(
					photoField, "gif"))) {
		alert("프사?");
		photoField.value = "";
		return false;
	}
	return true;
}