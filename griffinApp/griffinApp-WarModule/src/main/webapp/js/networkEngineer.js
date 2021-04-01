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

const displayIMSISummaryChart = function(imsiSummary){
  $("#imsiSummaryFormResultChartCard").show();
  var ctx = $("#imsiSummaryFormResultChart")[0];
  const imsiSummaryChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: [imsiSummary.imsi],
      datasets: [{
        label: "Total Count",
        backgroundColor: "#4e73df",
        hoverBackgroundColor: "#2e59d9",
        borderColor: "#4e73df",
        barPercentage:0.5,
        categoryPercentage:1.0,
        maxBarThickness:200,
        data: [imsiSummary.callFailuresCount],
      },
      {
        label: "Total Duration (seconds) ",
        backgroundColor: "#4e73df",
        hoverBackgroundColor: "#2e59d9",
        borderColor: "#4e73df",
        barPercentage:0.5,
        categoryPercentage:1.0,
        data: [imsiSummary.totalDurationMs/1000],
        type:"line"
      }
      ],
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
            max: Math.ceil((imsiSummary.totalDurationMs/1000)),       
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
            return datasetLabel + ': ' + tooltipItem.yLabel;
          }
        }
      },
    }
  });
  $("#imsiSummaryFormResultDate").text(`Data from ${$('#startDateOnIMSISummaryForm').val()} to ${$('#endDateOnIMSISummaryForm').val()}`);
}

const displayErrorOnIMSISummary = function(jqXHR, textStatus, errorThrown){
    $("#imsiSummaryTable").hide();
    $("#imsiSummaryFormResultChartCard").hide();
    $("#errorAlertOnSummaryForm").show();
    $("#errorAlertOnSummaryForm").text(jqXHR.responseJSON.errorMessage);
}

const queryIMSISUmmary = function(imsi, from, to){
    $.ajax({
        type: "GET",
        dataType: "json",
        url: `${rootURL}/events/query?imsi=${imsi}&from=${from}&to=${to}&summary=true`,
        beforeSend: setAuthHeader,
        success: function(imsiSummary){
          displayIMSISummary(imsiSummary);
          if(imsiSummary.callFailuresCount > 0){
            displayIMSISummaryChart(imsiSummary);
          }else{
            $("#imsiSummaryFormResultChartCard").hide();
          }
        },
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
    $("#top10ComboChartCard").show();
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
          barThickness:'flex',
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
              maxTicksLimit: 10,
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
    $("#combinationsTable_wrapper").hide();
    $("#top10ComboChartCard").hide();
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
            if(combinations.length > 0){
              displayTopTenCombinationsChart(combinations);
            }else{
              $("#top10ComboChartCard").hide();
            }
        },
        error: displayTopCombinationsError
    })
	
}

const displayTop10IMSISummary = function(topTenIMSIFailures){
	$("#imsiTopSummaryTable").show();
	const table = $('#imsiTopSummaryTable').DataTable();
    table.clear();
    $(topTenIMSIFailures).each(function(index, topTenIMSIFailure){
        console.log(topTenIMSIFailure);
        table.row.add([topTenIMSIFailure.imsi, 
            topTenIMSIFailure.callFailuresCount
        ]);
    });
    table.draw();
}

const displayTop10IMSIsError = function(jqXHR, textStatus, errorThrown){
  $("#imsiTopSummaryTable").hide();
  $("#top10IMSIChartCard").hide();
  $("#errorAlertOnTopCombinationsForm").show();
  $("#errorAlertOnSummaryForm").text(jqXHR.responseJSON.errorMessage);
}

const displayTopTenIMSIsChart = function(imsis){
  $("#top10IMSIChartCard").show();
  var ctx = $("#top10IMSIChart")[0];
  const top10Chart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: imsis.map(imsiData => imsiData.imsi),
      datasets: [{
        label: "Call Failures",
        backgroundColor: "#4e73df",
        hoverBackgroundColor: "#2e59d9",
        borderColor: "#4e73df",
        barPercentage:0.5,
        categoryPercentage:1.0,
        data: imsis.map(imsiData => imsiData.callFailuresCount),
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
            maxTicksLimit: 10
          }
        }],
        yAxes: [{
          ticks: {
            min: 0,
            max: Math.max(...imsis.map(imsiData => imsiData.callFailuresCount)),
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
            return datasetLabel + ': ' + tooltipItem.yLabel;
          }
        }
      },
    }
  });

  $("#top10IMSIDate").text(`Data as of ${new Date()}`);
}

const queryTop10IMSISummary = function(from, to){
    $.ajax({
        type: "GET",
        dataType: "json",
        url: `${rootURL}/IMSIs/query/limit?from=${from}&to=${to}&number=10`,
        beforeSend: setAuthHeader,
        success: function(imsis){
          displayTop10IMSISummary(imsis);
          if(imsis.length > 0){
            displayTopTenIMSIsChart(imsis);
          }else{
            $("#top10IMSIChartCard").hide();
          }
        },
        error: displayTop10IMSIsError
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


    $('#imsiTopSummaryForm').submit(function(event){
        event.preventDefault();
        const from = new Date($('#startDateOnIMSITopSummaryForm').val()).valueOf();
        const to = new Date($('#endDateOnIMSITopSummaryForm').val()).valueOf();
        queryTop10IMSISummary(from, to);
    });

    $("#netFirstQuery").click(function(){
        $("#networkEngQueryOne").show();
        $("#networkEngQueryTwo").hide();
	      $("#networkEngQueryThree").hide();
        $("#networkEngQueryFour").hide();
    });

    $("#netSecondQuery").click(function(){
        $("#networkEngQueryOne").hide();
        $("#networkEngQueryTwo").show();
	      $("#networkEngQueryThree").hide();
        $("#networkEngQueryFour").hide();

    });
    $("#netThirdQuery").click(function(){
        $("#networkEngQueryOne").hide();
        $("#networkEngQueryTwo").hide();
	      $("#networkEngQueryThree").show();
        $("#networkEngQueryFour").hide();
    });

     $("#netFourthQuery").click(function(){
        $("#networkEngQueryOne").hide();
        $("#networkEngQueryTwo").hide();
		$("#networkEngQueryThree").hide();
        $("#networkEngQueryFour").show();
    });

});
