var rootURL = "http://localhost:8080/callfailures/";


$(function() {
	initCreateUserForm();
});

function initCreateUserForm() {
	$('#createUserButton').submit(function(event) {
		event.preventDefault();
		buildUser();
	});
}

function buildUser(){
	console.log("Building the user")
	var id = $('#userId').val();
	var name = $('#userName').val();
	var type = $('#userType').val();
	var passwd = $('#userPassword').val();
	var confirm = $('#confirmPassword').val();
		
	var formData = { userID: id, userName: name, userType: type, userPassword: passwd };
	
	

	console.log("Creating the user.");
	
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