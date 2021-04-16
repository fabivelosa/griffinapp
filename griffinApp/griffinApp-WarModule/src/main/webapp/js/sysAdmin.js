/**
 * 
 */
const setAuthHeader = function(xhr) {
	xhr.setRequestHeader('Authorization', authToken);
}
const rootURL = "http://localhost:8080/callfailures/api";
const authToken = 'Bearer ' + sessionStorage.getItem("auth-token");
const userType = sessionStorage.getItem("auth-type");


//Add dataset
var submitdata = function() {
	console.log('submit called"');
	$("#myBar").display = "block";
	var formData = new FormData();
	var formData = new FormData($('#fileUploadForm')[0]);
	$('#uploadFile')[0].files;
	$('#validRecords').hide();
	$('#invalidRecords').hide();
	$("#downloadBtn").hide();

	$.ajax({
		type: 'POST',
		url: rootURL + '/file/upload',
		beforeSend: setAuthHeader,
		dataType: "json",
		data: formData,
		contentType: false,
		processData: false,
		success: updateProgress
	});
}


function updateProgress(data) {
	var uuid = data.uploadID;
	var width = getUploadStatus(uuid);
	var id = setInterval(frame, 300);
	function frame() {
		if (width >= 100) {
			$('#validRecords').text("Valid Records: " + validRecords);
			$('#invalidRecords').text("Invalid Records: " + invalidRecords);
			$('#validRecords').show();
			$('#invalidRecords').show();
			$("#downloadBtn").show();
			clearInterval(id);
			width = 0;
		} else {
			width = getUploadStatus(uuid);
			console.log(width);
			console.log("width is <100");
			$("#myBar").width(width + '%');
		}
	}
}


var getUploadStatus = function(id) {
	var currentStatus = 0;
	$.ajax({
		type: 'GET',
		url: rootURL + '/file/upload/' + id,
		beforeSend: setAuthHeader,
		dataType: "json",
		async: false,
		success: function(data) {
			currentStatus = data.uploadStatus;
			reportfilename = data.reportFile;
			validRecords = data.totalValidRecords;
			invalidRecords = data.totalInvalidRecords;
		}
	});
	return currentStatus;
}


function downloadURI(uri, name) {
	var link = document.createElement("a");
	link.setAttribute('download', name);
	link.href = uri;
	document.body.appendChild(link);
	link.click();
	link.remove();
}

//CREATE USER//
var currentUser;

var newUser = function() {

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

var addUser = function() {
	console.log('addUser');
	console.log(formToJSON());
	$.ajax({
		type: 'POST',
		contentType: 'application/json',
		url: rootURL + '/users',
		dataType: 'json',
		data: formToJSON(),
		contentType: 'application/json',
		beforeSend: function(xhr) {
			xhr.setRequestHeader('Authorization', authToken);
		},
		success: function(data, textStatus, jqXHR) {
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

var renderDetails = function(users) {
	$('#userId').val(users.userId);
	$('#userName').val(users.userName);
	$('#userType').val(users.userType);
	$('#userPassword').val(users.userPassword);

}




pass.onkeyup = function() {

	var lowerCaseLetters = /[a-z]/g;
	if (pass.value.match(lowerCaseLetters)) {
		letter.classList.remove("invalid");
		letter.classList.add("valid");
	} else {
		letter.classList.remove("valid");
		letter.classList.add("invalid");
	}

	var upperCaseLetters = /[A-Z]/g;
	if (pass.value.match(upperCaseLetters)) {
		capital.classList.remove("invalid");
		capital.classList.add("valid");
	} else {
		capital.classList.remove("valid");
		capital.classList.add("invalid");
	}

	var numbers = /[0-9]/g;
	if (pass.value.match(numbers)) {
		number.classList.remove("invalid");
		number.classList.add("valid");
	} else {
		number.classList.remove("valid");
		number.classList.add("invalid");
	}

	var length = document.getElementById("length");
	if (pass.value.length >= 8) {
		length.classList.remove("invalid");
		length.classList.add("valid");
	} else {
		length.classList.remove("valid");
		length.classList.add("invalid");
	}
}

var formToJSON = function() {

	return JSON.stringify({
		'userId': $('#userId').val(),
		'userName': $('#userName').val(),
		'userType': $('#userType').val(),
		'userPassword': $('#userPassword').val(),
		'token': ''
	});
};

document.getElementById("successMessage").style.display = "none";
document.getElementById("alertMissingField").style.display = "none";
document.getElementById("alertPasswordMatch").style.display = "none";

var clear = document.getElementById("createUserButton");

clear.onblur = function() {
	document.getElementById("successMessage").style.display = "none";
	document.getElementById("alertMissingField").style.display = "none";
	document.getElementById("alertPasswordMatch").style.display = "none";
}

//CREATE USER//

$(document).ready(function() {

	if (userType == 'SYSADMIN') {
		$("#SASideBar").show();
		$("#userSADropdown").show();
		$("#SAHeading").show();
		$("#sysAdminOne").hide();
		$("#sysAdminTwo").hide();

	}

	$("#SASelectors").on("click", "a", function(event) {
		$(".responseWidget").hide()
		$.each($("#SASelectors").children(), function(index, selector) {
			if (event.target == selector) {
				$(`#${$(selector).data("section")}`).show();
			} else {
				$(`#${$(selector).data("section")}`).hide();
			}
		});
	});

	$('#fileUploadForm').submit(function(event) {
		event.preventDefault();
		console.log('Clicked upload');
		$("#myBar").width(1);
		submitdata();
	});

	$("#downloadBtn").click(function(e) {
		e.preventDefault();
		downloadURI("http://localhost:8080/fileDownloads/", reportfilename);
		window.location.href = "http://localhost:8080/fileDownloads/" + reportfilename;
	});

	$('#createUserButton').click(function() {
		if (($('#userId').val() == "") || ($('#userName').val() == "") || ($('#userType').val() == null) || ($('#userPassword').val() == "")) {
			document.getElementById("alertMissingField").style.display = "block";
		}
		else if ($('#userPassword').val() != $('#confirmPassword').val())
			document.getElementById("alertPasswordMatch").style.display = "block";

		else {
			document.getElementById("successMessage").style.display = "block";
			addUser();
		}
		return false;
	});
});