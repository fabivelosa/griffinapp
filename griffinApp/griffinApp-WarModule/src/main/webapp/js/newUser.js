var rootURL = 'http://localhost:8080/callfailures/api';
var authToken = 'Bearer '+sessionStorage.getItem("auth-token");

var currentUser;

var newUser=function () {
	
	currentUser = {};
	renderDetails(currentUser); // Display empty form
};

var pass = document.getElementById('userPassword');

pass.onfocus = function() {
  	document.getElementById("message").style.display = "block";
}

// When the user clicks outside of the password field, hide the message box
pass.onblur = function() {
 	document.getElementById("message").style.display = "none";
}

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
		beforeSend: function(xhr){
		xhr.setRequestHeader('Authorization', authToken);
		},
		success: function(data, textStatus, jqXHR){
			document.getElementById("successMessage").style.display = "block";
			
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




pass.onkeyup = function() {
  
  var lowerCaseLetters = /[a-z]/g;
  if(pass.value.match(lowerCaseLetters)) {
    letter.classList.remove("invalid");
    letter.classList.add("valid");
  } else {
    letter.classList.remove("valid");
    letter.classList.add("invalid");
}

  var upperCaseLetters = /[A-Z]/g;
  if(pass.value.match(upperCaseLetters)) {
    capital.classList.remove("invalid");
    capital.classList.add("valid");
  } else {
    capital.classList.remove("valid");
    capital.classList.add("invalid");
  }

  var numbers = /[0-9]/g;
  if(pass.value.match(numbers)) {
    number.classList.remove("invalid");
    number.classList.add("valid");
  } else {
    number.classList.remove("valid");
    number.classList.add("invalid");
  }

  var length = document.getElementById("length");
  if(pass.value.length >= 8) {
    length.classList.remove("invalid");
    length.classList.add("valid");
  } else {
    length.classList.remove("valid");
    length.classList.add("invalid");
  }
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
 
document.getElementById("successMessage").style.display= "none";
document.getElementById("alertMissingField").style.display= "none";
document.getElementById("alertPasswordMatch").style.display= "none";

var clear = document.getElementById("createUserButton");

clear.onblur=function(){
	document.getElementById("successMessage").style.display= "none";
	document.getElementById("alertMissingField").style.display= "none";
	document.getElementById("alertPasswordMatch").style.display= "none";	
}

$(document).ready(function(){
	$('#createUserButton').click(function() {
		if (($('#userId').val() == "")||($('#userName').val() == "")||($('#userType').val() == null)||($('#userPassword').val() == "")){
		document.getElementById("alertMissingField").style.display= "block";
		}
		else if($('#userPassword').val() != $('#confirmPassword').val())
					document.getElementById("alertPasswordMatch").style.display= "block";			
			
		else{
			document.getElementById("successMessage").style.display= "block";	
			addUser();}
		return false;
	});
	
});