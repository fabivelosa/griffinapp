const rootURL = "http://localhost:8080/callfailures/api";
const authToken = 'Bearer ' + sessionStorage.getItem("auth-token");

const setAuthHeader = function(xhr){
    xhr.setRequestHeader('Authorization', authToken);
}

const displayPhoneEquipmentFailures = function(phoneFailures){
    $("#phoneFailuresTable").show();
    const table = $('#phoneFailuresTable').DataTable();
    table.clear();
    $(phoneFailures).each(function(index, phoneFailure){
        console.log(phoneFailure);
        table.row.add([phoneFailure.userEquipment.model, 
            phoneFailure.eventCause.eventCauseId.eventCauseId, 
            phoneFailure.eventCause.eventCauseId.causeCode,
            phoneFailure.eventCause.description,
            phoneFailure.count
        ]);
    });
    table.draw();
}

const queryPhoneEquipmentFailures = function(tac){
    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL}/userEquipment/query?tac=${tac}`,
        beforeSend: setAuthHeader,
        success: displayPhoneEquipmentFailures,
        error: function(jqXHR, textStatus, errorThrown){
            console.log(jqXHR);
        }
    });
}

const addUserEquipmentOptions = function(userEquipments){
    $("#selectUserEquipmentDropdown").empty();    
    let options = "";
    $(userEquipments).each(function(index, userEquipment){
        options += `<option value=\"${userEquipment.tac}\">${userEquipment.model}</option>`
    });
    $("#selectUserEquipmentDropdown").append(options);
}

const setUserQuipmentDropdownOptions = function(){
    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL}/userEquipment`,
        beforeSend: setAuthHeader,
        success: addUserEquipmentOptions,
        error: function(){
            alert("Failed to fetch user equipment options");
        }
    });
}

const displayIMSISummary = function(imsiSummary, textStatus, jqXHR){
    $("#errorAlertOnSummaryForm").hide();
    $("#imsiSummaryTable").show();
    $("#imsiSummaryNumber").text(imsiSummary.imsi);
    $("#imsiSummaryFromDate").text($('#startDateOnIMSISummaryForm').val());
    $("#imsiSummaryToDate").text($('#endDateOnIMSISummaryForm').val());
    $("#imsiSummaryCallFailureCount").text(imsiSummary.callFailuresCount);
    $("#imsiSummaryTotalDuration").text(imsiSummary.totalDurationMs/1000 + " s");
}

const displayErrorOnIMSISummary = function(jqXHR, textStatus, errorThrown){
    $("#imsiSummaryTable").hide();
    $("#errorAlertOnSummaryForm").show();
    $("#errorAlertOnSummaryForm").text(jqXHR.responseJSON.errorMessage);
}

const queryIMSISUmmary = function(imsi, from, to){
    $.ajax({
        type: "GET",
        dataType: "json",
        url: `${rootURL}/events/query?imsi=${imsi}&from=${from}&to=${to}&summary=true`,
        beforeSend: setAuthHeader,
        success: displayIMSISummary,
        error: displayErrorOnIMSISummary
    })
}

//Combinations Handlers
const displayTopTenCombinations = function(combinations){
    $("#combinationsTable").show();
    const table = $('#combinationsTable').DataTable();
    table.clear();
    $(combinations).each(function(index, combination){
        console.log(combination);
        table.row.add([combination.cellId,
			combination.marketOperator.countryDesc, 
            combination.marketOperator.marketOperatorId.countryCode, 
			combination.marketOperator.operatorDesc, 
            combination.marketOperator.marketOperatorId.operatorCode,
            combination.count
        ]);
    });
    table.draw();
}

const displayTopCombinationsError = function(jqXHR, textStatus, errorThrown){
    $("#combinationsTable").hide();
    $("#errorAlertOnTopCombinationsForm").show();
    $("#errorAlertOnSummaryForm").text(jqXHR.responseJSON.errorMessage);
}


const queryTopCombinations = function(from, to){	
	$.ajax({
        type: "GET",
        dataType: "json",
        url: `${rootURL}/Combinations/query?from=${from}&to=${to}`,
        beforeSend: setAuthHeader,
        success: displayTopTenCombinations,
        error: displayTopCombinationsError
    })
	
}

const autoCompleteIMSI = function(){
    $.ajax({
        type: "GET",
        dataType: "json",
        url: `${rootURL}/IMSIs/query/all`,
        beforeSend: setAuthHeader,
        success: function(data){
            var list = [];
            for(var i=0; i<data.length; i++){
                list.push(data[i].imsi)
            }
            $("#imsiOnIMSISummaryForm").autocomplete({source:list});
        }
    })
}



$(document).ready(function(){		
    setUserQuipmentDropdownOptions();
    autoCompleteIMSI();
    $('#imsiSummaryForm').submit(function(event){
        event.preventDefault();
        const imsi = $('#imsiOnIMSISummaryForm').val();
        const from = new Date($('#startDateOnIMSISummaryForm').val()).valueOf();
        const to = new Date($('#endDateOnIMSISummaryForm').val()).valueOf();
        queryIMSISUmmary(imsi, from, to);
    });
 
    $("#userEquipmentFailuresForm").submit(function(event){
        event.preventDefault();
        const tac = $("#selectUserEquipmentDropdown").val();
        queryPhoneEquipmentFailures(tac);
    });

	$("#userTop10CombinationsForm").submit(function(event){
        event.preventDefault();
  		$("#errorAlertOnTopCombinationsForm").hide();
        const from = new Date($('#startDateOnTop10CombinationsForm').val()).valueOf();
        const to = new Date($('#endDateOnTop10CombinationsForm').val()).valueOf();
        queryTopCombinations(from,to);
    });


    $("#netFirstQuery").click(function(){
        $("#networkEngQueryOne").show();
        $("#networkEngQueryTwo").hide();
		$("#networkEngQueryThree").hide();
    });
    $("#netSecondQuery").click(function(){
        $("#networkEngQueryOne").hide();
        $("#networkEngQueryTwo").show();
		$("#networkEngQueryThree").hide();
    });
 	$("#netThirdQuery").click(function(){
        $("#networkEngQueryOne").hide();
        $("#networkEngQueryTwo").hide();
		$("#networkEngQueryThree").show();
    });

 	 $("#errorAlertOnTopCombinationsForm").hide();
	$("#networkEngQueryThree").hide();
});