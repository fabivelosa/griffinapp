var rootURL = 'http://localhost:8080/callfailures/api';

var currentUser;

var newUser=function () {
	
	currentUser = {};
	renderDetails(currentUser); // Display empty form
};

var addUser = function () {
	console.log('addUser');
	console.log(formToJSON());
	$.ajax({
		type: 'POST',
		contentType: 'application/json',
		url: rootURL + '/users',
		dataType: 'json',
		data: formToJSON(),
		contentType: 'application/json',
		success: function(data, textStatus, jqXHR){
			alert('User created successfully');
			
			$('#userId').val("");
			$('#userName').val("");
			$('#userType').val("");
			$('#userPassword').val("");
			$('#confirmPassword').val("");
			
		}
	//	error: function(jqXHR, textStatus, errorThrown){
	//		alert('addUser error: ' + textStatus);
	//	} 
	});
};

var renderDetails=function(users){
	$('#userId').val(users.userId);
	$('#userName').val(users.userName);
	$('#userType').val(users.userType);
	$('#userPassword').val(users.userPassword);
	
}

var formToJSON=function () {
	
	return JSON.stringify({
		'userId': $('#userId').val(),
		'userName': $('#userName').val(), 
		'userType': $('#userType').val(),
		'userPassword': $('#userPassword').val(),
		'token': ''
		});
};

$(document).ready(function(){

	
	
	$('#createUserButton').click(function() {
		if ($('#userPassword').val() == $('#confirmPassword').val())
			addUser();
		else
			alert('Error: Passwords must match');
		return false;
	});
	
});