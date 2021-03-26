const rootURL = "http://localhost:8080/callfailures/api";


const displayIMSIByFailures = function(IMSIfailures){
    $("#phoneFailuresTable").show();
    const table = $('#phoneFailuresTable').DataTable();
    table.clear();
    $(IMSIfailures).each(function(index, IMSIfailure){
        console.log(IMSIfailure);
        table.row.add([IMSIfailure.imsi, 
            IMSIfailure.eventCause.eventCauseId.eventCauseId, 
            IMSIfailure.eventCause.eventCauseId.causeCode,
            IMSIfailure.eventCause.description
        ]);
    });
    table.draw();
}


const queryFailuresByIMSI = function(imsi){
    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL}/failures/${imsi}`,
        success: displayIMSIByFailures,
        error: function(jqXHR, textStatus, errorThrown){
            console.log(jqXHR);
        }
    });
}


const addUserEquipmentOptions = function(IMSIs){
    $("#selectUserEquipmentDropdown").empty();    
    let options = "";
    $(IMSIs).each(function(index, IMSI){
        options += `<option value=\"${IMSI.imsi}\">${IMSI.imsi}</option>`
    });
    $("#selectUserEquipmentDropdown").append(options);
}

const setUserQuipmentDropdownOptions = function(){
    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL}/IMSIs/query/all`,
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
        const imsi = $("#selectUserEquipmentDropdown").val();
        queryFailuresByIMSI(imsi);
    });


});