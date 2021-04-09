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
    console.log(storedData);
    const activeBar = this.getElementAtEvent(event);
    if(activeBar[0]){
        const index = activeBar[0]["_index"];
        const date = this.data.datasets[0].data[index].x;
        const filteredData = filterHourlyEventData(date, storedData);
        
    
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

