const rootURL2 = "http://localhost:8080/callfailures/api";
const authToken2 = 'Bearer ' + sessionStorage.getItem("auth-token");

const setAuthHeader2 = function(xhr){
    xhr.setRequestHeader('Authorization', authToken2);
}

const displayCallFailures = function(callFailures){
    $("#imsiListTable").show();
    const table = $('#imsiListTable').DataTable();
    table.clear();
    $(callFailures).each(function(index, callFailure){
        console.log(callFailure);
        table.row.add([callFailure.imsi]);
    });
    table.draw();
}

const queryCallFailures = function(from, to){
    const startTime = new Date().getTime();
    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL2}/IMSIs/query?from=${from}&to=${to}`,
        beforeSend: setAuthHeader2,
        success: function(response){
            const endTime = new Date().getTime();
            displayResponseSummary(response, startTime, endTime);
            displayCallFailures(response);
        },
        error: displayErrorOnIMSIList
    });
}

const displayCallFailureCount = function(imsiSummary, textStatus, jqXHR){
    $("#errorAlertOnCallFailureForm").hide();
    $("#imsiFailureTable").show();
    $("#imsiNumber").text(imsiSummary.model);
    $("#imsiCallFailureCount").text(imsiSummary.callFailuresCount);
}

const displayErrorOnIMSISummary2 = function(jqXHR, textStatus, errorThrown){
    $("#imsiSummaryTable").hide();
    $("#errorAlertOnCallFailureForm").show();
    $("#errorAlertOnCallFailureForm").text(jqXHR.responseJSON.errorMessage);
}

const displayErrorOnIMSIList = function(jqXHR, textStatus, errorThrown){
    $("#imsiListTable").hide();
    $("#errorAlertOnListForm").show();
    $("#errorAlertOnListForm").text(jqXHR.responseJSON.errorMessage);
}

const queryCallFailureCount = function(imsi, from, to){
    $.ajax({
        type: "GET",
        dataType: "json",
        url: `${rootURL2}/events/query/ue?model=${imsi}&from=${from}&to=${to}`,
        beforeSend: setAuthHeader2,
        success: displayCallFailureCount,
        error: displayErrorOnIMSISummary2
    })
}

const autoCompleteIMSI2 = function(){
    $.ajax({
        type: "GET",
        dataType: "json",
        url:`${rootURL2}/userEquipment`,
        beforeSend: setAuthHeader2,
        success: function(data){
            var list = [];
            for(var i=0; i<data.length; i++){
                list.push(data[i].model)
            }
            $("#imsiOnCallFailureForm").autocomplete({source:list});
        }
    })
}

//Display
const displayIMSIAffectedByFailureClass = function(IMSIs){
    $("#imsiFailuresTable").show();
	$("#errorAlertOnFailuresListForm").hide();
	const table = $('#imsiFailuresTable').DataTable();
    table.clear();
    $(IMSIs).each(function(index, imsi){
      table.row.add([imsi.imsi
        ]);
    });
    table.draw();
    
}

const displayErrorOnIFailuresList = function(jqXHR, textStatus, errorThrown){
    $("#imsiFailuresTable").hide();
    $("#errorAlertOnFailuresListForm").show();
    $("#errorAlertOnFailuresListForm").text(jqXHR.responseJSON.errorMessage);
}

//Query
const queryCallIMSIsWithFailure = function(failureClass){
    const startTime = new Date().getTime();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: `${rootURL}/IMSIs/query/failureClass?failureClass=${failureClass}`,
        beforeSend: setAuthHeader,
        success: function(response){
            const endTime = new Date().getTime();
            displayResponseSummary(response, startTime, endTime);
            displayIMSIAffectedByFailureClass(response);
        },
        error: displayErrorOnIFailuresList
    })
}

$(document).ready(function(){	
    autoCompleteIMSI2();	
    $('#imsiCallFaluireForm').submit(function(event){
        event.preventDefault();
        const imsi = $('#imsiOnCallFailureForm').val();
        const from = new Date($('#startDateOnCallFailureForm').val()).valueOf();
        const to = new Date($('#endDateOnCallFailureForm').val()).valueOf();
        queryCallFailureCount(imsi, from, to);
    });
 
    $("#imsiListForm").submit(function(event){
        event.preventDefault();
        const from = new Date($('#startDateOnListForm').val()).valueOf();
        const to = new Date($('#endDateOnListForm').val()).valueOf();
        queryCallFailures(from, to);
    });

  $("#imsiFailuresForm").submit(function(event){
        event.preventDefault();
        const failureClass = $('#failureClassValue').val();
        queryCallIMSIsWithFailure(failureClass);
    });

    $("#supFirstQuery").click(function(){
        $(".responseWidget").hide()
        $("#supportEngQueryOne").show();
        $("#supportEngQueryTwo").hide();
		$("#supportEngQueryThree").hide();
    });
    $("#supSecondQuery").click(function(){
        $(".responseWidget").hide()
        $("#supportEngQueryOne").hide();
        $("#supportEngQueryTwo").show();
		$("#supportEngQueryThree").hide();
    });
	 $("#supThirdQuery").click(function(){
        $(".responseWidget").hide()
        $("#supportEngQueryOne").hide();
        $("#supportEngQueryTwo").hide();
		$("#supportEngQueryThree").show();
		$("#imsiFailuresTable").hide();
		$("#errorAlertOnFailuresListForm").hide();
    });

	$("#supportEngQueryThree").hide();
	
});