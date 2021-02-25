var rootUrl="http://localhost:8080/callfailures/api";

var startup =function() {
	console.log('Index initiated');
};

var loginSubmit=function(){
	console.log('loginsubmission pressed: ');
	$.ajax({
		type: 'GET',
		url: rootUrl +'/' +'login' + '/20' ,
		dataType: "json",
		success: function(data){
		//	$('#btnDelete').show();
			console.log('User found success:'+ data);
		//	currentWine = data;
		// render function	renderDetails(currentWine);
		},
		error: function(){
			console.log('error occured: No user Found');
		}
	});
};


var loginSubmits=function(){
	console.log('loginsubmission pressed: ');
	$.ajax({
		type: 'GET',
		url: rootUrl + 'login' + '/bob',
		dataType: "json",
		success: function(data){
		//	$('#btnDelete').show();
			console.log('User found success:'+ data);
		//	currentWine = data;
		// render function	renderDetails(currentWine);
		},
		error: function(){
			console.log('error occured: No user Found');
		}
	});
};

$(document).ready(function(){
	//$('#sampleBtn').click(function()){};
	
	startup();
}
);