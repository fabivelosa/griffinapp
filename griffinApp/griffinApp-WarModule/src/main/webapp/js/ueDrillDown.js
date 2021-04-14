
const displayListOfFailureEventForDrillDown = function(EventsList){
    hideAllSections();
    $("#networkEngQueryTwoDrillDown").show();
    $("#networkEngQueryTwoDrillDownTitle").text(`Drilldown : ${EventsList[0].eventCause.description}`)
    displayNetworkEngQueryTwoDrillDownTable(EventsList);
    displayNetworkEngQueryTwoDrillDownCharts(EventsList);
    displayCauseDetails(EventsList);
}

const displayNetworkEngQueryTwoDrillDownTable = function(EventsList){
    const table = $('#networkEngQueryTwoDrillDownTable').DataTable();
    table.clear();
    $(EventsList).each(function(index, event){
        table.row.add([event.dateTime, 
            event.imsi,
            event.duration,
            event.cellId,
            event.marketOperator.countryDesc,
            event.marketOperator.operatorDesc,
            event.ueType.model,
            event.ueType.vendorName,
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
    networkEngQueryTwoDrillDownBarChart.destroy();

    // add the event handler to the chart
    imsiLineChartConfig.options.onClick = ueDrillDownLineChartEventHandler;

    networkEngQueryTwoDrillDownBarChart = new Chart($("#networkEngQueryTwoDrillDownChart")[0], imsiLineChartConfig);
    const incrementalDurations = generateIncrementalTimeSeriesData(EventsList)["durations"];
    const incrementalCounts = generateIncrementalTimeSeriesData(EventsList)["counts"];
    networkEngQueryTwoDrillDownBarChart.data.datasets[0].data = incrementalDurations;
    networkEngQueryTwoDrillDownBarChart.data.datasets[1].data = incrementalCounts;
    imsiLineChartConfig.options.scales.yAxes[0].ticks.max = getRoundedUpYAxisMaxValue(incrementalDurations, incrementalCounts);
    networkEngQueryTwoDrillDownBarChart.update();
    $("#networkEngQueryTwoDrillDownChartTitleType").text("Incremental");
    $("#networkEngQueryTwoDrillDownChartTitleDescription").text(`Failures of type: ${EventsList[0].eventCause.description}`);
    $("#networkEngQueryTwoIncrementalBtn").show();
    $("#networkEngQueryTwoCumulativeBtn").hide();
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

    // add the event handler to the chart
    cellIDChartConfig.options.onClick = ueDrillDownCellIDChartEventHandler;

    cellIDChartConfig.options.scales.yAxes[0].ticks.max = Math.max(...data);
    networkEngQueryTwoDrillDownCellIDChart = new Chart($("#networkEngQueryTwoDrillDownCellIDChart")[0], cellIDChartConfig);
    networkEngQueryTwoDrillDownCellIDChart.data.datasets[0].data = data;
    networkEngQueryTwoDrillDownCellIDChart.data.labels = labels;
    if(data.length == 1){
        networkEngQueryTwoDrillDownCellIDChart.data.datasets[0].backgroundColor = "#4e73df";
    }else{
        networkEngQueryTwoDrillDownCellIDChart.data.datasets[0].backgroundColor = d3.quantize(d3.interpolateHcl("#4e73df", "#60c96e"), data.length);
    }
    networkEngQueryTwoDrillDownCellIDChart.update();
}

const queryListOfUEEventForDrillDown = function(description){
    const startTime = new Date();
    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL2}/userEquipment/description/query?description=${description}`, // query for events here
        beforeSend: setAuthHeader1,
        success: function(EventsList){
            displayListOfFailureEventForDrillDown(EventsList);    
            cacheUEDrillDownData(EventsList);
            const endTime = new Date();
            displayResponseSummary(EventsList, startTime, endTime);
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("There is an error in queryListOfUEEventForDrillDown")
        }
    });
}

const displayCauseDetails = function(events){
    const event = events[0];
    $("#networkEngQueryTwoDrillDownChartCardDetailsEventID").val(event.eventId);
    $("#networkEngQueryTwoDrillDownChartCardDetailsCauseCode").val(event.eventCause.eventCauseId.causeCode);
    $("#networkEngQueryTwoDrillDownChartCardDetailsDescription").val(event.eventCause.description);
    $("#networkEngQueryTwoDrillDownChartCardDetailsTotalFailureCount").val(events.length);
}

const ueDrillDownLineChartEventHandler = function(event, array){
    const startTime = new Date();
    const storedData = $(this.canvas).closest(".drillDownSections").data("events");
    const activeBar = this.getElementAtEvent(event);
    let filteredData;
    if(activeBar[0]){
        const index = activeBar[0]["_index"];
        const date = this.data.datasets[0].data[index].x;
        filteredData = filterHourlyEventData(date, storedData);
        imsiLineChartConfig.options.scales.xAxes[0].ticks.minRotation = 0;
        showFilterDataBanner();
    }else{
        filteredData = storedData;
        showFullDataBanner();
    }
    displayListOfFailureEventForDrillDown(filteredData);
    const endTime = new Date();
    displayResponseSummary(filteredData, startTime, endTime);
}


const cacheUEDrillDownData = function(data){
    $("#networkEngQueryTwoDrillDown").data("id", data[0].ueType.model);
    $("#networkEngQueryTwoDrillDown").data("type", "ueType");
    $("#networkEngQueryTwoDrillDown").data("events", data);
}


const ueDrillDownCellIDChartEventHandler = function(event, array){    
    const startTime = new Date();
    let storedData = $(this.canvas).closest(".drillDownSections").data("events");
    let filteredData;
    const activeSlice = this.getElementAtEvent(event);
    if(activeSlice[0]){
        const index = activeSlice[0]["_index"];
        const cellCombo = this.data.labels[index];
        filteredData = filterCellIDDescriptionEventData(cellCombo, storedData);
        showFilterDataBanner();
    }else{
        filteredData = storedData;
        showFullDataBanner();
    }
    displayListOfFailureEventForDrillDown(filteredData);
    const endTime = new Date();
    displayResponseSummary(filteredData, startTime, endTime);
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





