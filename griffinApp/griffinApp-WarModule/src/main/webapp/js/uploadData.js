var rootUrl="http://localhost:8080/callfailures/api/";

var verifyLoaded=function(){
	console.log('initiated');		
};

//Display all issues
var getErrors=function(){
	$.ajax({
		   type:'GET',
		   url: 'http://localhost:8080/callfailures/api/events/sampleJSON2',
		   dataType:"json",
		   success: displayErrors,
		   error: function(){
			console.log('Error occured');
			}
		   });
};

var displayErrors=function(data){
	console.log('display Called');
	$.each(data,function(index,DataErrorMessage){
	$('#errorsList').append('<li>'+DataErrorMessage.rowNumber +'  '+DataErrorMessage.message+'</li>');
				
			});
}


$(document).ready(function(){
	//Assign button to call function on click
	$('#displayData').click(function(){
		getErrors();
	});
		
	verifyLoaded();
});