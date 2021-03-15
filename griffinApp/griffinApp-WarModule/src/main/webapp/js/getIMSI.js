var rootURL = "http://localhost:8080/callfailures/api/failures";

var begin = function(){
	console.log('beginning getIMSI');
};

var getIMSIFailures = function(){
	
	console.log('Imsi call');
	$.ajax({
		   type:'GET',
		   url: rootURL,
		   dataType:"json",
		   success: function(){
			console.log('success');
		},
		   error: function(){
			console.log('fail');
		}
		   });

};

$(document).ready(function(){
	
	$(document).on("click", '#getIMSIEvents', function(){getIMSIFailures();});	
	
	begin();
});