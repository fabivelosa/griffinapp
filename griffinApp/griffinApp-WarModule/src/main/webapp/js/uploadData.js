var rootUrl="http://localhost:8080/callfailures/api/";

var verifyLoaded=function(){
	console.log('initiated');		
};

var displayErrors=function(data){
	console.log('display Called');
	$.each(data,function(index,DataErrorMessage){
	$('#errorsList')
	.append('<li>'+DataErrorMessage.rowNumber +'  '+DataErrorMessage.message+'</li>');
			});
};

//Add dataset
var submitdata = function(){
	console.log('submit called"');
	var formData = new FormData();
	var formData = new FormData($('#fileUploadForm')[0]); 
	var files = $('#uploadFile')[0].files;

	
	if(files.length >0){
		console.log('file found');
	$.ajax({
		type: 'POST',
		url: 'http://localhost:8080/callfailures/api/file/upload',
		dataType: "json",
		data:  formData,
		contentType:false,
		processData: false,
		success: function(data){
			var errorCount = 0;
			//Display Erronous Data
			$.each(data,function(index,EventsUploadResponseDTO){
				$.each(EventsUploadResponseDTO.erroneousData,function(index,InvalidRow){
						$('#errorsList')
						.append('<li> Error at Row'+InvalidRow.rowNumber + ', Cause of Error: '+ InvalidRow.errorMessage  +'</li>');
						errorCount++;
						});
			});
			
			$('#displayCount').val('Errors Found: '+ errorCount);
			
		},
		error: function(){
			console.log('error');
			$('#displayCount').val('fail');
			//display error message here
		}		
		
	});
	}else{
		console.log('Please select file');
	}
};


var renderErrors = function(data){
				$.each(data,function(){
					$('#errorsList').append('')
				});
}

$(document).ready(function(){
	//Assign button to call function on click
	//$('#uploadBtn').click(function(){submitdata();});
		
	$('#fileUploadForm').submit(function(event){ 
 	event.preventDefault();
 	console.log('Clicked upload');
 	submitdata(); 
	});
	
	verifyLoaded();
});