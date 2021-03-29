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

$(document).ready(function(){		
    setUserQuipmentDropdownOptions();

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

    $("#netFirstQuery").click(function(){
        $("#networkEngQueryOne").show();
        $("#networkEngQueryTwo").hide();
    });
    $("#netSecondQuery").click(function(){
        $("#networkEngQueryOne").hide();
        $("#networkEngQueryTwo").show();
    });
});