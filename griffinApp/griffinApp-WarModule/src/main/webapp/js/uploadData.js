//Add dataset
var submitdata = function(){
	console.log('submit called"');
	var formData = new FormData();
	var formData = new FormData($('#fileUploadForm')[0]); 
	var files = $('#formFile')[0].files;

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
			   console.log('func called 23');
			    var validRowCount = EventsUploadResponseDTO.validRowCount;
				var invalidRowCount = EventsUploadResponseDTO.erroneousData.length;

				if(validRowCount > 0){
					$('#successfulList').append('<li class = "storedRow"> Successfully Stored ' + validRowCount + ' Row' + (validRowCount > 1?'s' : '') +' in the ' + EventsUploadResponseDTO.tabName + ' Table.</li>');
				}
				
				if(invalidRowCount > 0){
					$('#successfulList').append('<li class = "ignoredRow"> Ignored ' + invalidRowCount + ' Row' + (invalidRowCount > 1?'s' : '') +' in the ' + EventsUploadResponseDTO.tabName + ' Table.</li>');
				}
			});
			console.log('file found35');
			var errorLog = '';
			$.each(data,function(index,EventsUploadResponseDTO){
				var invalidRowCount = EventsUploadResponseDTO.erroneousData.length;

				if(invalidRowCount > 0){
					$('#errorsList').append(`<li class = "tableWithError"> Table Name : ${EventsUploadResponseDTO.tabName} has ${invalidRowCount} Ignored Rows</li>`);
					errorLog += '\nTable: ' + EventsUploadResponseDTO.tabName + ', has ' +EventsUploadResponseDTO.tabName + 'Ignored Rows,\n' ;
				}
				
				$.each(EventsUploadResponseDTO.erroneousData,function(index,InvalidRow){
						$('#errorsList')
						.append('<li class = "rowWithError"> Error at Row'+InvalidRow.rowNumber + ', Cause of Error: '+ InvalidRow.errorMessage  +'</li>');
						errorLog += 'Row: ' + InvalidRow.rowNumber + ', Cause: ' +InvalidRow.errorMessage + ',\n';
						});
			});		
			$('#errorBtn').data('errorLogs', errorLog );	
		},
		error: function(){
			console.log('error occured, could not upload file');
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

var downloadErrorLog = function(){
	var fileName = "Error Logs";
	
	var blob = new Blob([$('#errorBtn').data('errorLogs')],{
		type: "text/plain;charset=utf-8"
	});
	
	saveAs(blob,fileName);
}

$(document).ready(function(){
		
	//$('#uploadBtn').submit(function(event){ 
 //	event.preventDefault();
 //	console.log('Clicked upload');
// 	submitdata(); 
//	});
	
	$('#uploadBtn').click(function(){submitdata();});
	
	$('#errorBtn').click(function(){downloadErrorLog();});
	
	
});

