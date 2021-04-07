const rootURL2 = "http://localhost:8080/callfailures/api";
const authToken2 = 'Bearer ' + sessionStorage.getItem("auth-token");

const setAuthHeader2 = function(xhr){
    xhr.setRequestHeader('Authorization', authToken2);
}

const displayResponseSummarySup = function(response, startTime, endTime){
  $(".responseWidget").show();

  let count = response instanceof Array ? response.length : response ? 1 : 0;
  $(".responseRows").text(`The query returned ${count} row${count > 1 ? "s" : ""}`);
  $(".responseTime").text(`Duration ${(endTime - startTime)/1000} s`);
}

const displayCallFailures = function(callFailures){
	$("#errorAlertOnListForm").hide();
	$("#emptyCallFail").hide();
	$("#imsiListDiv").show();
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
			if(response.length > 0){
            const endTime = new Date().getTime();
            displayResponseSummarySup(response, startTime, endTime);
            displayCallFailures(response);
			}else{
				$("#errorAlertOnListForm").hide();
				$("#imsiListDiv").hide();
			    $("#imsiListTable").hide();
				$(".responseWidget").hide();
				$("#emptyCallFail").show();
			}
        },
        error: displayErrorOnIMSIList
    });
}

const displayCallFailureCount = function(imsiSummary, textStatus, jqXHR){
    $("#errorAlertOnCallFailureForm").hide();
	$("#emptySEquery1").hide();
    $("#imsiFailureTable").show();
    $("#imsiNumber").text(imsiSummary.model);
    $("#imsiCallFailureCount").text(imsiSummary.callFailuresCount);
}

const displayErrorOnIMSISummary2 = function(jqXHR, textStatus, errorThrown){
	$("#imsiCallFailureResultCard").hide();
	$(".responseWidget").hide();
	$("#imsiFailDiv").hide();
    $("#imsiFailureTable").hide();
	$("#emptySEquery1").hide();
    $("#errorAlertOnCallFailureForm").show();
    $("#errorAlertOnCallFailureForm").text(jqXHR.responseJSON.errorMessage);
}

const displayErrorOnIMSIList = function(jqXHR, textStatus, errorThrown){
	$(".responseWidget").hide();
	$("#imsiListDiv").hide();
	$("#emptyCallFail").hide();
    $("#imsiListTable").hide();
    $("#errorAlertOnListForm").show();
    $("#errorAlertOnListForm").text(jqXHR.responseJSON.errorMessage);
}

const queryCallFailureCount = function(imsi, from, to){
    const startTime = new Date().getTime();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: `${rootURL2}/events/query/ue?model=${imsi}&from=${from}&to=${to}`,
        beforeSend: setAuthHeader2,
        success: function(response){
			if(response.callFailuresCount > 0){
            const endTime = new Date().getTime();
            displayResponseSummarySup(response, startTime, endTime)
            displayCallFailureCount(response);
            displayIMSICallFailureChart(response);
			}else {
			  $("#imsiCallFailureResultCard").hide();
			  $(".responseWidget").hide();
			  $("#imsiFailDiv").hide();
			  $("#errorAlertOnCallFailureForm").hide();
              $("#imsiFailureTable").hide();
			  $("#emptySEquery1").show();
			}
        },
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

const displayIMSICallFailureChart = function(imsiSummary){
    $("#imsiCallFailureResultCard").show();
    var ctx = $("#imsiCallFailureResultChart")[0];
    const imsiSummaryChart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: [imsiSummary.model],
        datasets: [{
          label: "Total Count",
          backgroundColor: "#4e73df",
          hoverBackgroundColor: "#4e73df",
          borderColor: "#4e73df",
          barPercentage:0.5,
          categoryPercentage:1.0,
          maxBarThickness:200,
          data: [imsiSummary.callFailuresCount],
        }],
      },
      options: {
        maintainAspectRatio: false,
        layout: {
          padding: {
            left: 10,
            right: 25,
            top: 25,
            bottom: 0
          }
        },
        scales: {
          xAxes: [{
            type:"category",
            gridLines: {
              display: false,
              drawBorder: false
            },
            ticks: {
              maxTicksLimit: 1
            }
          }],
          yAxes: [{
            ticks: {
              min: 0,
              max: Math.ceil((imsiSummary.callFailuresCount)),       
              padding: 10,
            },
            scaleLabel: {
              display: true,
              labelString: 'Call Failures Count'
            },
            gridLines: {
              color: "rgb(234, 236, 244)",
              zeroLineColor: "rgb(234, 236, 244)",
              drawBorder: false,
              borderDash: [2],
              zeroLineBorderDash: [2]
            }
          }],
        },
        legend: {
          display: true,
          position:'bottom'
        },
        tooltips: {
          titleMarginBottom: 10,
          titleFontColor: '#6e707e',
          titleFontSize: 14,
          backgroundColor: "rgb(255,255,255)",
          bodyFontColor: "#858796",
          borderColor: '#dddfeb',
          borderWidth: 1,
          xPadding: 15,
          yPadding: 15,
          displayColors: false, 
          caretPadding: 10,
          callbacks: {
            label: function(tooltipItem, chart) {
              var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
              return datasetLabel + ': ' + tooltipItem.yLabel;
            }
          }
        },
      }
    });
    $("#imsiCallFailureResultDate").text(`Data from ${$('#startDateOnCallFailureForm').val()} to ${$('#endDateOnCallFailureForm').val()}`);
  }

//Display
const displayIMSIAffectedByFailureClass = function(IMSIs){
	$("#errorAlertOnFailuresListForm").hide();
	$("#emptyFailClass").hide();
	$("#failClassDiv").show();
    $("#imsiFailuresTable").show();
	const table = $('#imsiFailuresTable').DataTable();
    table.clear();
    $(IMSIs).each(function(index, imsi){
      table.row.add([imsi.imsi
        ]);
    });
    table.draw();
    
}

const displayErrorOnIFailuresList = function(jqXHR, textStatus, errorThrown){
	$("#emptyFailClass").hide();
	$("#failClassDiv").hide();
	$(".responseWidget").hide();
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
        url: `${rootURL2}/IMSIs/query/failureClass?failureClass=${failureClass}`,
        beforeSend: setAuthHeader2,
        success: function(response){
		if(response.length > 0){	
            const endTime = new Date().getTime();
            displayResponseSummarySup(response, startTime, endTime);
            displayIMSIAffectedByFailureClass(response);
		}else{
			$("#errorAlertOnFailuresListForm").hide();
			$("#emptyFailClass").show();
			$("#failClassDiv").hide();
		    $("#imsiFailuresTable").hide();
			$(".responseWidget").hide();
		}
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

});