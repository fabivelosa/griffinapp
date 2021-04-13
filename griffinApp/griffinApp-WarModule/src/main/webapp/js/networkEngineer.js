const rootURL1 = "http://localhost:8080/callfailures/api";
const authToken1 = 'Bearer ' + sessionStorage.getItem("auth-token");
const userType = sessionStorage.getItem("auth-type");

const setAuthHeader1 = function(xhr){
    xhr.setRequestHeader('Authorization', authToken1);
}

const displayResponseSummary = function(response, startTime, endTime){
  $(".responseWidget").show();

  let count = response instanceof Array ? response.length : response ? 1 : 0;
  $(".responseRows").text(`The query returned ${count} row${count > 1 ? "s" : ""}`);
  $(".responseTime").text(`Duration ${(endTime - startTime)/1000} s`);
}

const displayPhoneEquipmentFailures = function(phoneFailures){
    $("#phoneFailuresTableNE").show();
	$("#alertModelNoData").hide();
    const table = $('#phoneFailuresTableNE').DataTable();
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

const displayPhoneEquipmentFailuresChart = function(phoneFailures){
  let phoneModel = phoneFailures[0];
  $("#userEquipmentFailuresChartCardPhoneModel").val(phoneModel.userEquipment.model);
  $("#userEquipmentFailuresChartCardPhoneTAC").val(phoneModel.userEquipment.tac);
  $("#userEquipmentFailuresChartCardPhoneVendor").val(phoneModel.userEquipment.vendorName);
  $("#userEquipmentFailuresChartCardPhoneAccess").val(phoneModel.userEquipment.accessCapability);

  let sortedPhoneFailures = phoneFailures.sort((a,b) => b.count - a.count);

  $("#userEquipmentFailuresChartCard").show();
  $("#userEquipmentFailuresTitle").text(`Event Cause Distribution for ${phoneModel.userEquipment.model}`);
  var ctx = $("#userEquipmentFailuresChart")[0];
  const phoneFailuresChart = new Chart(ctx, {
    type: 'horizontalBar',
    data: {
      labels: sortedPhoneFailures.map(phoneFailure => phoneFailure.eventCause.description),
      datasets: [{
        label: `Call Failures`,
        backgroundColor: "#4e73df",
        hoverBackgroundColor: "#2e59d9",
        borderColor: "#4e73df",
        barThickness:'flex',
        barPercentage:0.5,
        categoryPercentage:1.0,
        data: sortedPhoneFailures.map(phoneFailure => phoneFailure.count),
      }],
    },
    options: {
	  onClick:UEDrillDownEventHandler,
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
          }
        }],
        xAxes: [{
          ticks: {
            min: 0,
            max: Math.max(...phoneFailures.map(phoneFailure => phoneFailure.count)),
            maxTicksLimit: 10,
            padding: 10
          },
          scaleLabel: {
            display: true,
            labelString: `Call Failures Count for ${phoneFailures[0].userEquipment.model}`
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
      plugins: {
        datalabels: {
            display: false,
        },
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
  $("#userEquipmentFailuresDate").text(`Data as of ${new Date()}`);
}

const queryPhoneEquipmentFailures = function(tac){
    const startTime = new Date().getTime();

    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL1}/userEquipment/query?tac=${tac}`,
        beforeSend: setAuthHeader1,
        success: function(phoneFailures){  
          if(phoneFailures.length > 0){
			displayPhoneEquipmentFailures(phoneFailures);
            displayPhoneEquipmentFailuresChart(phoneFailures);
			const endTime = new Date().getTime();
          	displayResponseSummary(phoneFailures, startTime, endTime);
          }else{
            $("#userEquipmentFailuresChartCard").hide();
			$(".responseWidget").hide();
			$("#phoneTableDiv").hide();
			$("#phoneFailuresTableNE").hide();
			$("#alertModelNoData").show();
          }
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log(jqXHR);
        }
    });
}

const addUserEquipment1Options = function(userEquipments){
    $("#selectUserEquipmentDropdownNE").empty();    
    let options = "";
    $(userEquipments).each(function(index, userEquipment){
        options += `<option value=\"${userEquipment.tac}\">${userEquipment.model}</option>`
    });
    $("#selectUserEquipmentDropdownNE").append(options);
}

const setUserQuipmentDropdownOptions1 = function(){
    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL1}/userEquipment`,
        beforeSend: setAuthHeader1,
        success: addUserEquipment1Options,
        error: function(){
            alert("Failed to fetch user equipment options");
        }
    });
}

const displayIMSISummary1 = function(imsiSummary, textStatus, jqXHR){
    $("#errorAlertOnSummaryFormNE").hide();
	$("#alertIMSISNoData").hide();
    $("#imsiSummaryTableOne").show();
    let imsiHtml = `<span class="imsiDrillDownLinks"><a href=\"#\">${imsiSummary.imsi}</a></span>`
    $("#imsiSummaryNENumber").html(imsiHtml);
    $("#imsiSummaryNEFromDate").text($('#startDateOnIMSISummaryFormNE').val());
    $("#imsiSummaryNEToDate").text($('#endDateOnIMSISummaryFormNE').val());
    $("#imsiSummaryNECallFailureCount").text(imsiSummary.callFailuresCount);
    $("#imsiSummaryNETotalDuration").text(imsiSummary.totalDurationMs/1000 + " s");
}

const displayIMSINoData = function(){
    $("#errorAlertOnSummaryFormNE").hide();
    $("#imsiSummaryTableOne").hide();
	$("#alertIMSISNoData").show();
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
        hoverBackgroundColor: "#4e73df",
        borderColor: "#4e73df",
        barPercentage:0.5,
        categoryPercentage:1.0,
        maxBarThickness:200,
        data: [imsiSummary.callFailuresCount],
      },
      {
        label: "Total Duration (seconds) ",
        backgroundColor: "#FFA500",
        hoverBackgroundColor: "#FFA500",
        borderColor: "#FFA500",
        data: [imsiSummary.totalDurationMs/1000],
        type:"line"
      }
      ],
    },
    options: {
      onClick: imsiSummaryDrillDownEventHandler,
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
            labelString: 'Call Failures Count/Duration'
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
      plugins: {
        datalabels: {
            display: false,
        },
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
  $("#imsiSummaryFormResultDate").text(`Data from ${$('#startDateOnIMSISummaryFormNE').val()} to ${$('#endDateOnIMSISummaryFormNE').val()}`);
}

const displayErrorOnIMSISummary1 = function(jqXHR, textStatus, errorThrown){
    $("#imsiSummaryTableOne").hide();
	$("#imsiSummaryFormResultChartCard").hide();
	$("#alertIMSISNoData").hide();
    $("#errorAlertOnSummaryFormNE").show();
    $("#errorAlertOnSummaryFormNE").text(jqXHR.responseJSON.errorMessage);
}

const queryIMSISUmmary1 = function(imsi, from, to){
    $(".responseWidget").hide();
    const startTime = new Date().getTime();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: `${rootURL}/events/query?imsi=${imsi}&from=${from}&to=${to}&summary=true`,
        beforeSend: setAuthHeader1,
        success: function(imsiSummary){
          if(imsiSummary.callFailuresCount > 0){
			displayIMSISummary1(imsiSummary);
            displayIMSISummaryChart(imsiSummary);
			const endTime = new Date().getTime();
          	displayResponseSummary(imsiSummary, startTime, endTime);
          }else{
            $("#imsiSummaryFormResultChartCard").hide();
			displayIMSINoData();
          }
        },
        error: displayErrorOnIMSISummary1
    })
}

const displayTopTenCombinations = function(combinations){
    $("#combinationsTable").show();
	$("#alertNoCombinations").hide();
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
        onClick: cellDrillDownEventHandler,
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
        plugins: {
          datalabels: {
              display: false,
          },
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
	$("#alertNoCombinations").hide();
    $("#top10ComboChartCard").hide();
    $("#errorAlertOnTopCombinationsForm").show();
    $("#errorAlertOnSummaryForm").text(jqXHR.responseJSON.errorMessage);
}

const queryTopCombinations = function(from, to){	
  const startTime = new Date().getTime();
  $.ajax({
        type: "GET",
        dataType: "json",
        url: `${rootURL1}/Combinations/query?from=${from}&to=${to}`,
        beforeSend: setAuthHeader1,
        success: function(combinations){
            if(combinations.length > 0){
			  displayTopTenCombinations(combinations);
              displayTopTenCombinationsChart(combinations);
			  const endTime = new Date().getTime();
              displayResponseSummary(combinations, startTime, endTime);
            }else{
              $("#top10ComboChartCard").hide();
			  $(".responseWidget").hide();
			  $("#combiTableDiv").hide();
			  $("#combinationsTable").hide();
			  $("#alertNoCombinations").show();
            }
            
        },
        error: displayTopCombinationsError
    })
	
}

const autoCompleteIMSI1 = function(){
    $.ajax({
        type: "GET",
        dataType: "json",
        url: `${rootURL1}/IMSIs/query/all`,
        beforeSend: setAuthHeader1,
        success: function(data){
            var list = [];
            for(var i=0; i<data.length; i++){
                list.push(data[i].imsi)
            }
            $("#imsiOnIMSISummaryFormNE").autocomplete({source:list});
        }
    })
}

const displayTop10IMSISummary = function(topTenIMSIFailures){
	$("#imsiTopSummaryTable").show();
	$("#alertNoTopModels").hide();
	const table = $('#imsiTopSummaryTable').DataTable();
    table.clear();
    $(topTenIMSIFailures).each(function(index, topTenIMSIFailure){
      let imsiHtml = `<span class="imsiDrillDownLinks"><a href=\"#\">${topTenIMSIFailure.imsi}</a></span>`
        table.row.add([imsiHtml, 
            topTenIMSIFailure.callFailuresCount
        ]);
    });
    table.draw();
}

const displayTop10IMSIsError = function(jqXHR, textStatus, errorThrown){
  $("#imsiTopSummaryTable").hide();
  $("#top10IMSIChartCard").hide();
  $("#alertNoTopModels").hide();
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
      onClick: topTenIMSIDrillDownEventHandler,
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
      plugins: {
        datalabels: {
            display: false,
        },
      },
      tooltips: {
        intersect: false,
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
    const startTime = new Date().getTime();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: `${rootURL}/IMSIs/query/limit?from=${from}&to=${to}&number=10`,
        beforeSend: setAuthHeader1,
        success: function(imsis){
          if(imsis.length > 0){
			displayTop10IMSISummary(imsis);
            displayTopTenIMSIsChart(imsis);
			const endTime = new Date().getTime();
            displayResponseSummary(imsis, startTime, endTime);
          }else{
            $("#top10IMSIChartCard").hide();
			$(".responseWidget").hide();
			$("#imsiTopDiv").hide();
			$("#imsiTopSummaryTable").hide();
			$("#alertNoTopModels").show();
          }
        },
        error: displayTop10IMSIsError
    })
}

const hideSEQueries = function(){
  $(".responseWidget").hide()
  $.each($("#querySESelectors").children(), function(index, selector) {
          $(`#${$(selector).data("section")}`).hide();
  });
}

const hideCSQueries = function(){
  $(".responseWidget").hide()
  $.each($("#queryCSSelectors").children(), function(index, selector) {
          $(`#${$(selector).data("section")}`).hide();
  });
}

const hideNEQueries = function(){
  $(".responseWidget").hide()
  $.each($("#queryNESelectors").children(), function(index, selector) {
          $(`#${$(selector).data("section")}`).hide();
  });
}

const autoCompleteIMSI = function(){
  $.ajax({
      type: "GET",
      dataType: "json",
      url: `${rootURL}/IMSIs/query/all`,
      beforeSend: setAuthHeader1,
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
	if (userType == 'SUPPORTENG') {
			$("#SupportSideBar").show();
			$("#CSRepSideBar").show();
			$("#userSEDropdown").show();
			$("#SEHeading").show();
			} 
	else if (userType == 'CUSTSERVREP') {
				$("#CSRepSideBar").show();
				$("#userCSDropdown").show();
				$("#CSHeading").show();
			} 
	else if (userType == 'NETWORKMNG') {
				$("#SupportSideBar").show();
				$("#CSRepSideBar").show();
				$("#NESideBar").show();
				$("#userNEDropdown").show();
				$("#NEHeading").show();
			}
		
    setUserQuipmentDropdownOptions1();
    autoCompleteIMSI1();

    $('#imsiSummaryFormNE').submit(function(event){
        event.preventDefault();
        const imsi = $('#imsiOnIMSISummaryFormNE').val();
        const from = new Date($('#startDateOnIMSISummaryFormNE').val()).valueOf();
        const to = new Date($('#endDateOnIMSISummaryFormNE').val()).valueOf();
        queryIMSISUmmary1(imsi, from, to);
    });
 
    $("#userEquipmentFailuresFormNE").submit(function(event){
        event.preventDefault();
        const tac = $("#selectUserEquipmentDropdownNE").val();
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

	$("#queryNESelectors").on("click", "a", function(event){
    $(".responseWidget").hide();
    $(".drillDownSections").hide();
		hideSEQueries();
		hideCSQueries();
        $.each($("#queryNESelectors").children(), function(index, selector) {
            if(event.target == selector){
                $(`#${$(selector).data("section")}`).show();
            }else{
                $(`#${$(selector).data("section")}`).hide();
            }
        });
    });

	$("#queryCSSelectors").on("click", "a", function(event){
    $(".responseWidget").hide();
    $(".drillDownSections").hide();
		hideSEQueries();
		hideNEQueries();
        $.each($("#queryCSSelectors").children(), function(index, selector) {
            if(event.target == selector){
                $(`#${$(selector).data("section")}`).show();
            }else{
                $(`#${$(selector).data("section")}`).hide();
            }
        });
    });

    $("#querySESelectors").on("click", "a", function(event){
    $(".responseWidget").hide();
    $(".drillDownSections").hide();
		hideCSQueries();
		hideNEQueries();
        $.each($("#querySESelectors").children(), function(index, selector) {
            if(event.target == selector){
                $(`#${$(selector).data("section")}`).show();
            }else{
                $(`#${$(selector).data("section")}`).hide();
            }
        });
    });

    // Drill Down Back Event Handler
    $(".drillDownBackIcon").click(function(event){
      hideAllSections();
      $(".drillDownSections").hide();

      console.log(`#${$(this).data("target")}`);
      $(`#${$(this).data("target")}`).show();
    });

    $("body").on("click", ".imsiDrillDownLinks a", function(event){
      const parentContainer = $(this).closest(".queryContainer").attr("id");
      imsiClickFromTableEventHandler($(this).text(), parentContainer);
    });
	$("body").on("click", ".failureDescriptionLinks a", function(event){
      const parentContainer = $(this).closest(".queryContainer").attr("id");
      descriptionClickFromTableEventHandler($(this).text(), parentContainer);
    });

});
