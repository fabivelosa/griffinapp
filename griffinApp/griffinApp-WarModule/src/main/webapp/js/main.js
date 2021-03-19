var rootURL = "http://localhost:8080/callfailures/api/";


$(function() {
	initLoginForm();
});

function initLoginForm() {
	$('#loginForm').submit(function(event) {
		event.preventDefault();
		authenticateUser();
	});
}

function authenticateUser() {
	console.log("Authenticating the user.");
	var userName = $('#InputUsername').val();
	var passwd = $('#InputPassword').val();
	var formData = { userName: userName, userPassword: passwd };
	$.ajax({
		type: 'POST',
		url: rootURL + "login/auth/",
		data: JSON.stringify(formData),
		Accept: "application/json",
		contentType: "application/json",
		dataType: "json",
		success: function(authInfo) {
			console.log("Successful user authentication." + authInfo.userType);
			// render the subpage for the specific user category
			if (authInfo.userType == 'SYSADMIN') {
				renderSysAdminContent();
			} else if (authInfo.userType == 'SUPPORTENG') {
				renderSuppEngContent();
			} else if (authInfo.userType == 'CUSTSERVREP') {
				renderCustServReoContent();
			} else if (authInfo.userType == 'NETWORKMNG') {
				renderNetwManagContent();
			}
			initLogout();
		},
		error: function() {
			console.log("Unsuccessful user authentication.");
			$("#invalid-login").addClass('show');
		}
	});
}
// subpage for the specific user category
function renderSysAdminContent() {
	console.log('Display SysAdmin content');
	$.get('sysadmin.html', function(response) {
		$('#main-container').html(response);
		initSysAdmin();
	});
}