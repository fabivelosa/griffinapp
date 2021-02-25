var rootUrl="http://localhost:8080/callfailures/";

var test =function() {
	console.log('test');
};

var loginSubmit=function(){
	console.log('loginsubmission pressed: ' + id);
	$.ajax({
		type: 'GET',
		url: rootUrl + '/' + 'login'+ '/' +,
		dataType: "json",
		success: function(data){
			$('#btnDelete').show();
			console.log('findById success: ' + data.name);
			currentWine = data;
			renderDetails(currentWine);
		},
		error: function(){
			console.log('error occured:');
		}
	});
};



$(document).ready(function(){

	
	test();
	});