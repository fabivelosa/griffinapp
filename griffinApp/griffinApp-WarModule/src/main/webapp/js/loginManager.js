var rootUrl="http://localhost:8080/callfailures/api";

var startup =function() {
	console.log('Index initiated');
};

var loginSubmit=function(id,password){
	console.log('loginsubmission pressed: '+id + password);
	$.ajax({
		type: 'GET',
		url: rootUrl +'/' +'login' + '/' + id,
		dataType: "json",
		success: function(data){
		//	$('#btnDelete').show();
			console.log('User found success:'+ data);
		//	currentWine = data;
		// render function	renderDetails(currentWine);
		},
		error: function(){
			console.log('error occured: No user Found'+id);
		}
	});
};


$(document).ready(function(){
		startup();
	$('#loginBtn').click(function(){
		//loginSubmit($('#userNameLogin').val);
		loginSubmit(document.getElementById("userNameLogin").value,document.getElementById("passwordLogin").value);
	});
	

}
);