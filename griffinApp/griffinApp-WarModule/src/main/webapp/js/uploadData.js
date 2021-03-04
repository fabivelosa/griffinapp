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
			$('#sampleData').css("visibility", "visible");
			$('#successfulList').empty();
			$('#errorsList').empty();
			
			$.each(data,function(index,EventsUploadResponseDTO){
			    console.log(EventsUploadResponseDTO.validRowCount);
			    var validRowCount = EventsUploadResponseDTO.validRowCount;
				var invalidRowCount = EventsUploadResponseDTO.erroneousData.length;

				if(validRowCount > 0){
					$('#successfulList').append('<li class = "storedRow"> Successfully Stored ' + validRowCount + ' Row' + (validRowCount > 1?'s' : '') +' in the ' + EventsUploadResponseDTO.tabName + ' Table.</li>');
				}
				
				if(invalidRowCount > 0){
					$('#successfulList').append('<li class = "ignoredRow"> Ignored ' + invalidRowCount + ' Row' + (invalidRowCount > 1?'s' : '') +' in the ' + EventsUploadResponseDTO.tabName + ' Table.</li>');
				}
			});
			
			$.each(data,function(index,EventsUploadResponseDTO){
				var invalidRowCount = EventsUploadResponseDTO.erroneousData.length;

				if(invalidRowCount > 0){
					$('#errorsList').append(`<li class = "tableWithError"> Table Name : ${EventsUploadResponseDTO.tabName} has ${invalidRowCount} Ignored Rows</li>`);
				}
				
				$.each(EventsUploadResponseDTO.erroneousData,function(index,InvalidRow){
						$('#errorsList')
						.append('<li class = "rowWithError"> Error at Row'+InvalidRow.rowNumber + ', Cause of Error: '+ InvalidRow.errorMessage  +'</li>');
						});
			});			
		},
		error: function(){
			console.log('error');
			$('#displayCount').css("visibility", "visible");
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

