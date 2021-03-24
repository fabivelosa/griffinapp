var rootURL = "http://localhost:8080/callfailures/";

var currentUser;

var newUser=function () {
	
	currentUser = {};
	renderDetails(currentUser); // Display empty form
};

var addUser = function () {
	console.log('addUser');
	$.ajax({
		type: 'POST',
		contentType: 'application/json',
		url: rootURL,
		dataType: "json",
		data: formToJSON(),
		success: function(data, textStatus, jqXHR){
			alert('User created successfully');
			
		},
		error: function(jqXHR, textStatus, errorThrown){
			alert('addUser error: ' + textStatus);
		}
	});
};

var renderDetails=function(user){
	$('#userId').val(user.userId);
	$('#userName').val(user.userName);
	$('#userType').val(user.userType);
	$('#userPassword').val(user.userPassword);
}

var formToJSON=function () {
	
	return JSON.stringify({
		"id": $('#userId').val(),
		"name": $('#userName').val(), 
		"type": $('#userType').val(),
		"password": $('#userPassword').val()
		});
};

$(document).ready(function(){

	newUser();
	
	$('#createUserButton').click(function() {
		if ($('#userPassword').val() == $('#confirmPassword').val())
			addUser();
		else
			alert('Error: Passwords must match');
		return false;
	});
		
	$('#userId').val("");
	$('#userName').val("");
	$('#userType').val("");
	$('#userPassword').val("");
	$('#confirmPassword').val("");
	
});