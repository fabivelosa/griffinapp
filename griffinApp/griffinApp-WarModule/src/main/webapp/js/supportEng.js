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
    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL2}/IMSIs/query?from=${from}&to=${to}`,
        beforeSend: setAuthHeader2,
        success: displayCallFailures,
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

    $("#supFirstQuery").click(function(){
        $("#supportEngQueryOne").show();
        $("#supportEngQueryTwo").hide();
    });
    $("#supSecondQuery").click(function(){
        $("#supportEngQueryOne").hide();
        $("#supportEngQueryTwo").show();
    });
});