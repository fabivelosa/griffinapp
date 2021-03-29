const rootURL = "http://localhost:8080/callfailures/api";
const authToken = 'Bearer ' + sessionStorage.getItem("auth-token");

const setAuthHeader = function(xhr){
    xhr.setRequestHeader('Authorization', authToken);
}


const displayIMSIByFailures = function(IMSIfailures){
    $("#phoneFailuresTable").show();
    const table = $('#phoneFailuresTable').DataTable();
    table.clear();
    $(IMSIfailures).each(function(index, IMSIfailure){
        table.row.add([IMSIfailure.imsi, 
            IMSIfailure.eventCause.eventCauseId.eventCauseId, 
            IMSIfailure.eventCause.eventCauseId.causeCode,
            IMSIfailure.eventCause.description
        ]);
    });
    table.draw();
}

const displayIMSICauseCodes = function(causeCodes){
    $("#causeCodesTable").show();
    const table = $('#causeCodesTable').DataTable();
    table.clear();
    $(causeCodes).each(function(index, causeCode){
        table.row.add([causeCode]);
    });
    table.draw();
}


const queryFailuresByUserEquipment = function(userEquipment){
    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL}/failures/${userEquipment}`,
        beforeSend: setAuthHeader,
        success: displayIMSIByFailures,
        error: function(jqXHR, textStatus, errorThrown){
            console.log(jqXHR);
        }
    });
}

const queryCauseCodesByIMSI = function(imsi){
    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL}/causecodes/${imsi}`,
        beforeSend: setAuthHeader,
        success: displayIMSICauseCodes,
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

const setIMSIFieldAutoComplete = function(){
    $.ajax({
        type: "GET",
        dataType: "json",
        url: `${rootURL}/IMSIs/query/all`,
        beforeSend: setAuthHeader,
        success: function(data){
            let imsis = [];
            data.forEach(item => imsis.push(item.imsi));    
            $("#imsiOnImsiCauseCodesForm").autocomplete({source:imsis});
            $("#imsiOnIMSISummaryForm").autocomplete({source:imsis});
        }
    })
}

$(document).ready(function(){		
    setUserQuipmentDropdownOptions();
    setIMSIFieldAutoComplete();
    $('#imsiSummaryForm').submit(function(event){
        event.preventDefault();
        const imsi = $('#imsiOnIMSISummaryForm').val();
        const from = new Date($('#startDateOnIMSISummaryForm').val()).valueOf();
        const to = new Date($('#endDateOnIMSISummaryForm').val()).valueOf();
        queryIMSISUmmary(imsi, from, to);
    });
 
    $("#userEquipmentFailuresForm").submit(function(event){
        event.preventDefault();
        const userEquipment = $("#selectUserEquipmentDropdown").val();
        queryFailuresByUserEquipment(userEquipment);
    });

    $("#imsiCauseCodesForm").submit(function(event){
        event.preventDefault();
        const imsi = $("#imsiOnImsiCauseCodesForm").val();
        queryCauseCodesByIMSI(imsi);
    });

    $("#querySelectors").on("click", "a", function(event){
        $.each($("#querySelectors").children(), function(index, selector) {
            if(event.target == selector){
                $(`#${$(selector).data("section")}`).show();
            }else{
                $(`#${$(selector).data("section")}`).hide();
            }
        });
    });
});