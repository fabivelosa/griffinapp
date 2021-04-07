const rootURL = "http://localhost:8080/callfailures/api";
const authToken = 'Bearer ' + sessionStorage.getItem("auth-token");

const setAuthHeader = function(xhr){
    xhr.setRequestHeader('Authorization', authToken);
}

const displayResponseSummaryCS = function(response, startTime, endTime){
  $(".responseWidget").show();

  let count = response instanceof Array ? response.length : response ? 1 : 0;
  $(".responseRows").text(`The query returned ${count} row${count > 1 ? "s" : ""}`);
  $(".responseTime").text(`Duration ${(endTime - startTime)/1000} s`);
}

const displayIMSICauseCodes = function(causeCodes){
	$("#emptyCauseCode").hide();
    $("#errorAlertOnCauseCodesQuery").hide();
    $("#causeCodesTable_wrapper").show();
	$("#causeCodeDiv").show();
    $("#causeCodesTable").show();
    const table = $('#causeCodesTable').DataTable();
    table.clear();
    $(causeCodes).each(function(index, causeCode){
        table.row.add([causeCode]);
    });
    table.draw();
}

const displayEquipmentFailures = function(IMSIfailures){
    $("#errorAlertOnEquipmentFailuresQuery").hide();
	$("#emptyFailDetail").hide();
	$("#failDetailDiv").show();
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

const displayErrorOnEquipmentFailures = function(jqXHR, textStatus, errorThrown){
	$("#emptyFailDetail").hide();
	$("#failDetailDiv").hide();
	$(".responseWidget").hide();
    $("#phoneFailuresTable").hide();
    $("#errorAlertOnEquipmentFailuresQuery").show();
    $("#errorAlertOnEquipmentFailuresQuery").text(jqXHR.responseJSON.errorMessage);
}

const queryFailuresByUserEquipment = function(userEquipment){
    const startTime = new Date().getTime();
    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL}/failures/${userEquipment}`,
        beforeSend: setAuthHeader,
        success: function(response){
			if(response.length > 0){
            const endTime = new Date().getTime();
            displayResponseSummaryCS(response, startTime, endTime);
            displayEquipmentFailures(response);
			}else{
				$("#errorAlertOnEquipmentFailuresQuery").hide();
				$("#emptyFailDetail").show();
				$("#failDetailDiv").hide();
			    $("#phoneFailuresTable").hide();
				$(".responseWidget").hide();
			}
        },
        error: displayErrorOnEquipmentFailures
    });
}

const displayErrorOnQueryCauseCodesByIMSI = function(jqXHR, textStatus, errorThrown){
	$("#emptyCauseCode").hide();
	$("#causeCodeDiv").hide();
	$(".responseWidget").hide();
    $("#causeCodesTable_wrapper").hide();
    $("#errorAlertOnCauseCodesQuery").show();
    $("#errorAlertOnCauseCodesQuery").text(jqXHR.responseJSON.errorMessage);
}

const queryCauseCodesByIMSI = function(imsi){
    const startTime = new Date().getTime();
    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL}/causecodes/${imsi}`,
        beforeSend: setAuthHeader,
        success: function(response){
			if(response.length > 0){
            const endTime = new Date().getTime();
            displayResponseSummaryCS(response, startTime, endTime);
            displayIMSICauseCodes(response)
			}else{
				$("#emptyCauseCode").show();
			    $("#errorAlertOnCauseCodesQuery").hide();
			    $("#causeCodesTable_wrapper").hide();
				$("#causeCodeDiv").hide();
			    $("#causeCodesTable").hide();
				$(".responseWidget").hide();
			}
        },
        error: displayErrorOnQueryCauseCodesByIMSI
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

const displayIMSISummary = function(imsiSummary, textStatus, jqXHR){
    $("#errorAlertOnSummaryForm").hide();
	$("#emptyImsiSumm").hide();
	$("#imsiSummDiv").show();
    $("#imsiSummaryTable").show();
    $("#imsiSummaryNumber").text(imsiSummary.imsi);
    $("#imsiSummaryFromDate").text($('#startDateOnIMSISummaryForm').val());
    $("#imsiSummaryToDate").text($('#endDateOnIMSISummaryForm').val());
    $("#imsiSummaryCallFailureCount").text(imsiSummary.callFailuresCount);
   
}

const displayErrorOnIMSISummary = function(jqXHR, textStatus, errorThrown){
	$("#emptyImsiSumm").hide();
	$(".responseWidget").hide();
	$("#imsiSummDiv").hide();
    $("#imsiSummaryTable").hide();
    $("#errorAlertOnSummaryForm").show();
    $("#errorAlertOnSummaryForm").text(jqXHR.responseJSON.errorMessage);
}

const queryIMSISUmmary = function(imsi, from, to){
    const startTime = new Date().getTime();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: `${rootURL}/events/query?imsi=${imsi}&from=${from}&to=${to}&summary=true`,
        beforeSend: setAuthHeader,
        success: function(response){
			if(response.callFailuresCount > 0){
            const endTime = new Date().getTime();
            displayResponseSummaryCS(response, startTime, endTime);
            displayIMSISummary(response)
			}else{
				$(".responseWidget").hide();
				$("#errorAlertOnSummaryForm").hide();
				$("#emptyImsiSumm").show();
				$("#imsiSummDiv").hide();
			    $("#imsiSummaryTable").hide();
			}
        },
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
            $("#selectUserEquipmentDropdown").autocomplete({source:imsis});
        }
    })
}

$(document).ready(function(){		
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

});