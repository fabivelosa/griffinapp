var createUser = function(){
  console.log('create user called"');
	var formData = new FormData();
	var formData = new FormData($('#createUserForm'));
  var enter = $('.entry');

  if(entry.length > 0){
    console.log('Valid input');
    
  }
}

$(document).ready(function(){
	//Assign button to call function on click
	//$('#uploadBtn').click(function(){submitdata();});

	$('#createUserForm').create(function(user){

 	console.log('Clicked create user');
 	createUser();
	});

});
