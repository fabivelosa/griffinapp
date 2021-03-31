const setAuthHeader = function(xhr) {
	xhr.setRequestHeader('Authorization', authToken);
}

//Add dataset
var submitdata = function() {
	console.log('submit called"');
	$("#myBar").display = "block";
	var formData = new FormData();
	var formData = new FormData($('#fileUploadForm')[0]);
	$('#uploadFile')[0].files;

	$.ajax({
		type: 'POST',
		url: 'http://localhost:8080/callfailures/api/file/upload',
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
		var id = setInterval(frame, 5000); 
	 	function frame() {
      if (width >= 100) {
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
		url: 'http://localhost:8080/callfailures/api/file/upload/' + id,
		beforeSend: setAuthHeader,
		dataType: "json",
		async: false,
		success: function(data) {
			currentStatus = data.uploadStatus;
			reportfilename = data.reportFile;
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

$(document).ready(function() {
	//Assign button to call function on click
	//$('#uploadBtn').click(function(){submitdata();});

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

});
