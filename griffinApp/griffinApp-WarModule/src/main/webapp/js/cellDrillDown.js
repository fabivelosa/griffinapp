const displayListOfCellEventForDrillDown = function(eventsList){
    hideAllSections();
    $("#networkEngQueryThreeDrillDown").show();
    $("#networkEngQueryThreeDrillDownTitle").text(`Drilldown : Cell ${eventsList[0].cellId} of ${eventsList[0].marketOperator.operatorDesc} | ${eventsList[0].marketOperator.countryDesc} `)
    displayCellDetails(eventsList[0]);
    displayNetworkEngQueryThreeDrillDownTable(eventsList);
    displayNetworkEngQueryThreeDrillDownCharts(eventsList);
}

const displayNetworkEngQueryThreeDrillDownTable = function(eventsList){
    const table = $('#networkEngQueryThreeDrillDownTable').DataTable();
    table.clear();
    $(eventsList).each(function(index, event){
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

const displayNetworkEngQueryThreeDrillDownCharts = function(eventsList){
    generateCellFailuresBarLineChart(eventsList);
    generateCellFailureClassPieChart(eventsList);
    generateCellIMSIBarChart(eventsList);
    generateCellPhoneBarChart(eventsList);
}

let networkEngQueryThreeDrillDownBarChart = new Chart($("#networkEngQueryThreeDrillDownChart")[0], imsiLineChartConfig);
const generateCellFailuresBarLineChart = function(eventsList){
    networkEngQueryThreeDrillDownBarChart.destroy();
    networkEngQueryThreeDrillDownBarChart = new Chart($("#networkEngQueryThreeDrillDownChart")[0], imsiLineChartConfig);
    const incrementalDurations = generateIncrementalTimeSeriesData(eventsList)["durations"];
    const incrementalCounts = generateIncrementalTimeSeriesData(eventsList)["counts"];
    
    imsiLineChartConfig.options.scales.yAxes[0].ticks.max = getRoundedUpYAxisMaxValue(incrementalDurations, incrementalCounts);
    imsiLineChartConfig.options.onClick = drillDownLineChartEventHandler;
    
    networkEngQueryThreeDrillDownBarChart.data.datasets[0].data = incrementalDurations;
    networkEngQueryThreeDrillDownBarChart.data.datasets[1].data = incrementalCounts;
    networkEngQueryThreeDrillDownBarChart.update();
};

let networkEngQueryThreeDrillDownPieChart = new Chart($("#networkEngQueryThreeDrillDownFailureClassChart")[0], failureClassPieChartConfig);
const generateCellFailureClassPieChart = function(eventsList){
    const dataset = generateFailureClassDisribution(eventsList);
    const labels =  Object.keys(dataset);
    const data = [];
    for(let i = 0; i < labels.length; i++){
        data[i] = dataset[labels[i]];
    }
    networkEngQueryThreeDrillDownPieChart.destroy();
    networkEngQueryThreeDrillDownPieChart = new Chart($("#networkEngQueryThreeDrillDownFailureClassChart")[0], failureClassPieChartConfig);
    networkEngQueryThreeDrillDownPieChart.data.datasets[0].data = data;
    networkEngQueryThreeDrillDownPieChart.data.labels = labels;
    networkEngQueryThreeDrillDownPieChart.data.datasets[0].backgroundColor = colorPalette.slice(0, data.length);
    networkEngQueryThreeDrillDownPieChart.update();
}

let networkEngQueryThreeDrillDownIMSIBarChart = new Chart($("#networkEngQueryThreeDrillDownIMSIChart")[0], top10IMSIHorizontalBarConfig);
const generateCellIMSIBarChart = function(eventsList){
    const dataset = generateTop10IMSIDistribution(eventsList);
    const labels =  Object.keys(dataset);
    const data = [];
    for(let i = 0; i < labels.length; i++){
        data[i] = dataset[labels[i]];
    }
    networkEngQueryThreeDrillDownIMSIBarChart.destroy();
    top10IMSIHorizontalBarConfig.options.scales.xAxes[0].ticks.max = Math.max(...data);
    networkEngQueryThreeDrillDownIMSIBarChart = new Chart($("#networkEngQueryThreeDrillDownIMSIChart")[0], top10IMSIHorizontalBarConfig);
    networkEngQueryThreeDrillDownIMSIBarChart.data.datasets[0].data = data;
    networkEngQueryThreeDrillDownIMSIBarChart.data.labels = labels;
    networkEngQueryThreeDrillDownIMSIBarChart.data.datasets[0].backgroundColor = "#4e73df";
    networkEngQueryThreeDrillDownIMSIBarChart.update();
}


let networkEngQueryThreeDrillDownPhoneModelBarChart = new Chart($("#networkEngQueryThreeDrillDownPhoneModelChart")[0], top10PhoneModelHorizontalBarConfig);
const generateCellPhoneBarChart = function(eventsList){
    const dataset = generateTop10PhoneModelDistribution(eventsList);
    console.log(dataset);
    const labels =  Object.keys(dataset);
    const data = [];
    for(let i = 0; i < labels.length; i++){
        data[i] = dataset[labels[i]];
    }
    networkEngQueryThreeDrillDownPhoneModelBarChart.destroy();
    top10PhoneModelHorizontalBarConfig.options.scales.xAxes[0].ticks.max = Math.max(...data);
    networkEngQueryThreeDrillDownPhoneModelBarChart = new Chart($("#networkEngQueryThreeDrillDownPhoneModelChart")[0], top10PhoneModelHorizontalBarConfig);
    networkEngQueryThreeDrillDownPhoneModelBarChart.data.datasets[0].data = data;
    networkEngQueryThreeDrillDownPhoneModelBarChart.data.labels = labels;
    networkEngQueryThreeDrillDownPhoneModelBarChart.data.datasets[0].backgroundColor = d3.quantize(d3.interpolateHcl("#4e73df", "#60c96e"), data.length);
    networkEngQueryThreeDrillDownPhoneModelBarChart.update();
}


const displayCellDetails = function(event){
    $("#networkEngQueryThreeDrillDownChartCardDetailsCellID").val(event.cellId);
    $("#networkEngQueryThreeDrillDownChartCardDetailsCountry").val(event.marketOperator.countryDesc);
    $("#networkEngQueryThreeDrillDownChartCardDetailsOperator").val(event.marketOperator.operatorDesc);
}

const cacheDrillDownData = function(data){
    $("#networkEngQueryThreeDrillDown").data("id", data[0].cellId);
    $("#networkEngQueryThreeDrillDown").data("type", "cell");
    $("#networkEngQueryThreeDrillDown").data("events", data);
}

const queryListOfCellEventForDrillDown = function(cellId, country, operator){
    const startTime = new Date();
    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL2}/cells/query?cellId=${cellId}&country=${country}&operator=${operator}`,
        beforeSend: setAuthHeader1,
        success: function(eventsList){
            displayListOfCellEventForDrillDown(eventsList);
            cacheDrillDownData(eventsList);
            const endTime = new Date();
            displayResponseSummary(eventsList, startTime, endTime);
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("There is an error in queryListOfIMSIEventForDrillDown")
        }
    });
}


const filterHourlyEventData = function(date, storedData){    
    return storedData.filter(data => {
        return roundDateToNearestHour(data).toISOString() == new Date(date).toISOString();
    });
}

const drillDownLineChartEventHandler = function(event, array){    
    const storedData = $(this.canvas).closest(".drillDownSections").data("events");
    const activeBar = this.getElementAtEvent(event);
    if(activeBar[0]){
        const index = activeBar[0]["_index"];
        const date = this.data.datasets[0].data[index].x;
        const filteredData = filterHourlyEventData(date, storedData);
        imsiLineChartConfig.options.scales.xAxes[0].ticks.minRotation = 0;
        displayListOfCellEventForDrillDown(filteredData);
    }else{
        displayListOfCellEventForDrillDown(storedData);
    }
}

const cellDrillDownEventHandler = function(event, array){
    $("#cellDrillDownBackIcon").data("target", "networkEngQueryThree");
    imsiLineChartConfig.options.scales.xAxes[0].ticks.minRotation = 45;
    const activeBar = this.getElementAtEvent(event);
    if(activeBar[0]){
        const index = activeBar[0]["_index"];
        const marketOperatorCombo = this.data.labels[index];
        const marketOperatorComboArr = marketOperatorCombo.split(", ");  
        const country = marketOperatorComboArr[0];
        const operator = marketOperatorComboArr[1];
        const cellId = marketOperatorComboArr[2].split(" ")[1];
        queryListOfCellEventForDrillDown(cellId, country, operator);
    }
};

