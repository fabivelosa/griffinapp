
var i = 0;
var reportfilename;

//Add dataset
var submitdata = function() {
	console.log('submit called"');
	var formData = new FormData();
	var formData = new FormData($('#fileUploadForm')[0]);
	var files = $('#uploadFile')[0].files;

	$.ajax({
		type: 'POST',
		url: 'http://localhost:8080/callfailures/api/file/upload',
		dataType: "json",
		data: formData,
		contentType: false,
		processData: false,
		success: updateProgress
	});
}

var width = 0;
function updateProgress(data) {
var uuid = data.uploadID;
  if (width == 0) {
	console.log('if novo"');
    width = 1;
    var elem = document.getElementById("myBar");
    var id = setInterval(frame, 1000); 
    function frame() {
      if (width >= 100) {
        clearInterval(id);
        width = 0;
      } else {
        width = getStatus(uuid);
        elem.style.width = width + "%";
      }
    }
  }
}

var getStatus = function(id) {
	var currentStatus = "";
	$.ajax({
		type: 'GET',
		url: 'http://localhost:8080/callfailures/api/file/upload/' + id,
		dataType: "json",
		async: false,
		success: function(data) {
			currentStatus = data.uploadStatus;
			reportfilename = data.reportFile;
			console.log("file" + reportfilename);
			console.log("status" + currentStatus);
		}
	});
	return currentStatus;
}




function downloadURI(uri, name) 
{
    var link = document.createElement("a");
    // If you don't know the name or want to use
    // the webserver default set name = ''
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
		submitdata();
	});
	
	$('#downloadBtn').click(function(){
		
		
		 downloadURI('http://localhost:8080/fileDownloads/', reportfilename);
		
	});
	
	$("#downloadBtn").click(function (e) {
          e.preventDefault();
          window.location.href = "http://localhost:8080/fileDownloads/"+reportfilename;
      });


 /* this one should download the file, but its getting Forbidden
   $('#downloadBtn').click(function(){	
		 downloadURI('http://localhost:8080/fileDownloads/', reportfilename);
	});
*/

});




