var i = 0;

//Add dataset
var submitdata = function(){
	console.log('submit called"');
	var formData = new FormData();
	var formData = new FormData($('#fileUploadForm')[0]); 
	var files = $('#uploadFile')[0].files;


	$.ajax({
		type: 'POST',
		url: 'http://localhost:8080/callfailures/api/file/upload',
		dataType: "json",
		data:  formData,
		contentType:false,
		processData: false,
		success: updateProgress
	});
		
}

var updateProgress = function(data){
	var uuid = data.uploadID;
	var status = "";
	var width = 0;
	var elem = document.getElementById("myBar");
	elem.style.width = 1 + "%";
	while (status != "completed"){
		status = getStatus(uuid);
		if (status == "eventCause inprogress") {
			width = 15;
		}
		if (status == "failureClass inprogress") {
			width = 30;
		}
		if (status == "userEquipment inprogress") {
			width = 45;
		}
		if (status == "marketOperator inprogress") {
			width = 60;
		}
		if (status == "event inprogress") {
			width = 75;
		}
		if (status == "completed") {
			width = 100;
		}
	    var id = setInterval(frame, 10);
	    function frame() {
		  console.log(width);
	      if (width >= 100) {
			elem.style.width = width + "%";
	        elem.innerHTML = width  + "%";
	        clearInterval(id);
	      } else {
	        elem.style.width = width + "%";
	        elem.innerHTML = width  + "%";
	      }
	    }
		console.log(status);
	}
}

var getStatus = function(id) {
	var currentStatus = "";
	$.ajax({
		type: 'GET',
		url: 'http://localhost:8080/callfailures/api/file/upload/' + id,
		dataType: "json",
		async:false,
		success: function(data) {
			currentStatus = data.uploadStatus;
		}
	});
	console.log(currentStatus);
	return currentStatus;
}
		


$(document).ready(function(){
	//Assign button to call function on click
	//$('#uploadBtn').click(function(){submitdata();});
		
	$('#fileUploadForm').submit(function(event){ 
 	event.preventDefault();
 	console.log('Clicked upload');
 	submitdata(); 
	});
	
});

