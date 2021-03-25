const rootURL = "http://localhost:8080/callfailures/api";
const authToken = 'Bearer ' + sessionStorage.getItem("auth-token");

const setAuthHeader = function(xhr){
    xhr.setRequestHeader('Authorization', authToken);
}

const displayPhoneEquipmentFailures = function(phoneFailures){
    $("#imsiTable").show();
    const table = $('#imsiTable').DataTable();
    table.clear();
    $(phoneFailures).each(function(index, phoneFailure){
        console.log(phoneFailure);
//        table.row.add([phoneFailure.userEquipment.model, 
//            phoneFailure.eventCause.eventCauseId.eventCauseId, 
//            phoneFailure.eventCause.eventCauseId.causeCode,
//            phoneFailure.eventCause.description,
//            phoneFailure.count
//        ]);
    });
    table.draw();
}

const queryPhoneEquipmentFailures = function(from, to){
    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL}/IMSIs/query?from=${from}&to=${to}`,
        beforeSend: setAuthHeader,
        success: displayPhoneEquipmentFailures,
        error: function(jqXHR, textStatus, errorThrown){
            console.log(jqXHR);
        }
    });
}

const displayIMSISummary = function(imsiSummary, textStatus, jqXHR){
    $("#errorAlertOnSummaryForm").hide();
    $("#imsiFailureTable").show();
    $("#imsiNumber").text(imsiSummary.model);
    $("#imsiCallFailureCount").text(imsiSummary.callFailuresCount);
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
        url: `${rootURL}/events/query/ue?model=${imsi}&from=${from}&to=${to}`,
        beforeSend: setAuthHeader,
        success: displayIMSISummary,
        error: displayErrorOnIMSISummary
    })
}

$(document).ready(function(){		
    $('#imsiCallFaluireForm').submit(function(event){
        event.preventDefault();
        const imsi = $('#imsiOnCallFailureForm').val();
        const from = new Date($('#startDateOnCallFailureForm').val()).valueOf();
        const to = new Date($('#endDateOnCallFailureForm').val()).valueOf();
        queryIMSISUmmary(imsi, from, to);
    });
 
    $("#imsiListForm").submit(function(event){
        event.preventDefault();
        const from = new Date($('#startDateOnListForm').val()).valueOf();
        const to = new Date($('#endDateOnListForm').val()).valueOf();
        queryPhoneEquipmentFailures(from, to);
    });
});