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

const displayTopTenCombinationsChart = function(combinations){
    var ctx = $("#top10ComboChart")[0];
    const top10Chart = new Chart(ctx, {
      type: 'horizontalBar',
      data: {
        labels: combinations.map(combination => {
          return combination.marketOperator.countryDesc + " , " + combination.marketOperator.operatorDesc + " , Cell " + combination.cellId}
          ),
        datasets: [{
          label: "Call Failures",
          backgroundColor: "#4e73df",
          hoverBackgroundColor: "#2e59d9",
          borderColor: "#4e73df",
          barThickness: "flex",
          data: combinations.map(combination => combination.count),
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
          yAxes: [{
            type:"category",
            gridLines: {
              display: false,
              drawBorder: false
            },
            ticks: {
              maxTicksLimit: 10
            }
          }],
          xAxes: [{
            ticks: {
              min: 0,
              max: Math.max(...combinations.map(combination => combination.count)),
              maxTicksLimit: 5,
              padding: 10
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
          display: false
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
              return datasetLabel + ': ' + tooltipItem.xLabel;
            }
          }
        },
      }
    });

    $("#top10ComboChartDate").text(`Data as of ${new Date()}`);
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
        success: function(combinations){
            displayTopTenCombinations(combinations);
            displayTopTenCombinationsChart(combinations);
        },
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