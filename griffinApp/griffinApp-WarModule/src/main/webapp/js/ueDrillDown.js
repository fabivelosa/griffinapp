
const displayListOfFailureEventForDrillDown = function(EventsList){
    hideAllSections();
    $("#networkEngQueryTwoDrillDown").show();
    $("#networkEngQueryTwoDrillDownTitle").text(`Drilldown : ${EventsList[0].eventCause.description}`)
    displayNetworkEngQueryTwoDrillDownTable(EventsList);
    displayNetworkEngQueryTwoDrillDownCharts(EventsList);
    displayCauseDetails(EventsList[0]);
}

const displayNetworkEngQueryTwoDrillDownTable = function(EventsList){
    const table = $('#networkEngQueryTwoDrillDownTable').DataTable();
    table.clear();
    $(EventsList).each(function(index, event){
        table.row.add([event.dateTime, 
            event.imsi,
            event.duration,
            event.cellId,
            event.failureClass.failureClass,
            event.failureClass.failureDesc,
            event.eventCause.eventCauseId.eventCauseId,
            event.eventCause.eventCauseId.causeCode,
            event.eventCause.description
        ]);
    });
    table.draw();
}

//Organise Graphs for drilldown
const displayNetworkEngQueryTwoDrillDownCharts = function(EventsList){    
    generateBarLineChartUE(EventsList);
    generateCellIDBarChartUE(EventsList);
}

//Fail Events Chart
let networkEngQueryTwoDrillDownBarChart = new Chart($("#networkEngQueryTwoDrillDownChart")[0], imsiLineChartConfig);

const generateBarLineChartUE = function(EventsList) {
    const cumulativeDurations = generateCumulativeTimeSeriesData(EventsList)["durations"];
    const incrementalDurations = generateIncrementalTimeSeriesData(EventsList)["durations"];
    const cumulativeCounts = generateCumulativeTimeSeriesData(EventsList)["counts"];
    const incrementalCounts = generateIncrementalTimeSeriesData(EventsList)["counts"];
    networkEngQueryTwoDrillDownBarChart.data.datasets[0].data = incrementalDurations;
    networkEngQueryTwoDrillDownBarChart.data.datasets[1].data = incrementalCounts;
    imsiLineChartConfig.options.scales.yAxes[0].ticks.max = getRoundedUpYAxisMaxValue(incrementalDurations, incrementalCounts);
    networkEngQueryTwoDrillDownBarChart.update();
    $("#networkEngQueryTwoDrillDownChartTitleType").text("Incremental");
    $("#networkEngQueryTwoDrillDownChartTitleDescription").text(`Failures of type: ${EventsList[0].eventCause.description}`);
    $("#networkEngQueryTwoIncrementalBtn").show();
    $("#networkEngQueryTwoCumulativeBtn").hide();
    initDrillDownButtons(networkEngQueryTwoDrillDownBarChart, cumulativeDurations, incrementalDurations, cumulativeCounts, incrementalCounts);
};

//Cell ID Chart
let networkEngQueryTwoDrillDownCellIDChart = new Chart($("#networkEngQueryTwoDrillDownCellIDChart")[0], cellIDChartConfig);

const generateCellIDBarChartUE = function(EventsList){
    const dataset = generateMarketOperatorCell(EventsList);
    const labels =  Object.keys(dataset);
    const data = [];
    for(let i = 0; i < labels.length; i++){
        data[i] = dataset[labels[i]];
    }
    networkEngQueryTwoDrillDownCellIDChart.destroy();
    cellIDChartConfig.options.scales.yAxes[0].ticks.max = Math.max(...data);
    networkEngQueryTwoDrillDownCellIDChart = new Chart($("#networkEngQueryTwoDrillDownCellIDChart")[0], cellIDChartConfig);
    networkEngQueryTwoDrillDownCellIDChart.data.datasets[0].data = data;
    networkEngQueryTwoDrillDownCellIDChart.data.labels = labels;
    networkEngQueryTwoDrillDownCellIDChart.data.datasets[0].backgroundColor = colorPalette.slice(0, data.length);
    networkEngQueryTwoDrillDownCellIDChart.update();
}

//Show the graphs
const initDrillDownButtonsUE = function(chart, cumulativeDurations, incrementalDurations, cumulativeCounts, incrementalCounts){
    $("#networkEngQueryTwoIncrementalBtn").click(function (event) {
        $("#networkEngQueryTwoIncrementalBtn").hide();
        $("#networkEngQueryTwoCumulativeBtn").show();
        $("#networkEngQueryTwoDrillDownChartTitleType").text("Incremental ")
        chart.data.datasets[0].data = incrementalDurations;
        chart.data.datasets[1].data = incrementalCounts;
        imsiLineChartConfig.options.scales.yAxes[0].ticks.max = getRoundedUpYAxisMaxValue(incrementalDurations, incrementalCounts);
        chart.update();
    });

    $("#networkEngQueryTwoCumulativeBtn").click(function (event) {
        $("#networkEngQueryTwoIncrementalBtn").show();
        $("#networkEngQueryTwoCumulativeBtn").hide();
        $("#networkEngQueryTwoDrillDownChartTitleType").text("Cumulative ")
        chart.data.datasets[0].data = cumulativeDurations;
        chart.data.datasets[1].data = cumulativeCounts;
        imsiLineChartConfig.options.scales.yAxes[0].ticks.max = getRoundedUpYAxisMaxValue(cumulativeDurations, cumulativeCounts);
        chart.update();
    });
};

const queryListOfUEEventForDrillDown = function(description){
    const startTime = new Date();
    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL2}/userEquipment/description/query?description=${description}`, // query for events here
        beforeSend: setAuthHeader1,
        success: function(EventsList){
            displayListOfFailureEventForDrillDown(EventsList);       
            displayResponseSummary(EventsList);
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("There is an error in queryListOfUEEventForDrillDown")
        }
    });
}

const displayCauseDetails = function(event){
    $("#networkEngQueryTwoDrillDownChartCardDetailsEventID").val(event.eventId);
    $("#networkEngQueryTwoDrillDownChartCardDetailsCauseCode").val(event.eventCause.eventCauseId.causeCode);
    $("#networkEngQueryTwoDrillDownChartCardDetailsDescription").val(event.eventCause.description);
}


const UEDrillDownEventHandler = function(event, array){
    $("#ueDrillDownBackIcon").data("target", "networkEngQueryTwo"); // add in html to drilldown section for querytwo
    imsiLineChartConfig.options.scales.xAxes[0].ticks.minRotation = 0;
    let activeBar = this.getElementAtEvent(event);
    if(activeBar[0]){
        let index = activeBar[0]["_index"];
        let description = this.data.labels[index];    
        queryListOfUEEventForDrillDown(description);
    }
}





