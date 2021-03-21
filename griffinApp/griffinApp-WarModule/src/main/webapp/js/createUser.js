var rootURL = "http://localhost:8080/callfailures/api/";


$(function() {
	initCreateUserForm();
});

function initCreateUserForm() {
	$('#createUserForm').submit(function(event) {
		event.preventDefault();
		createNewUser();
	});
}

function createNewUser() {
	console.log("Creating the user.");
	var userID = $('#userId').val();
	var userName = $('#userName').val();
	var userType = $('#userType').val();
	var passwd = $('#userPassword').val();
	var confirm = $('#confirmPassword').val();
	var formData = { userName: userName, userPassword: passwd };
	$.ajax({
		type: 'POST',
		url: rootURL + "/create",
		data: JSON.stringify(formData),
		Accept: "application/json",
		contentType: "application/json",
		dataType: "json",
		success: function(createInfo) {
			console.log("Successful user creation." + createInfo.userType);
			
		},
		error: function() {
			console.log("Unsuccessful user creation.");
			$("#creation-failure").addClass('show');
		}
	});

}