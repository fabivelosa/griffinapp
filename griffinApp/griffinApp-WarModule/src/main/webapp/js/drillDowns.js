/* Util Methods for the Graph*/
const getRoundedUpYAxisMaxValue = function(durations, counts){
    const durationsMaxValue = Math.max(...durations.map(data => data.y));
    const countsMaxValue = Math.max(...counts.map(data => data.y));
    const maxValue = Math.max(durationsMaxValue, countsMaxValue);
    const roundedUp =  maxValue <= 10 ? 10 : (Math.ceil(maxValue/10)) * (Math.pow(10, Math.ceil(Math.log10(maxValue)))/10);
    return roundedUp;
}

const getMinXAxisValue = function(chartData){
    return new Date(Math.min(...chartData.map(data => new Date(data.x)))).toDateString();
};


const getMaxXAxisValue = function(chartData){
    return new Date(Math.max(...chartData.map(data => new Date(data.x)))).toDateString();
};

const generateIncrementalTimeSeriesData = function(dataset){
    const durationPerTimeMap = {};
    const countPerTimeMap = {};
    dataset.forEach(data => {
        let key = new Date(data.dateTime).toISOString();
        durationPerTimeMap[key] = durationPerTimeMap[key] ? durationPerTimeMap[key] + (data.duration/1000) : (data.duration/1000);
        countPerTimeMap[key] = countPerTimeMap[key] ? countPerTimeMap[key] + 1 : 1;
    });
    return generateTimeSeriesData(countPerTimeMap, durationPerTimeMap);
};


const generateCumulativeTimeSeriesData = function(dataset){
    console.log(dataset);
    const durationPerTimeMap = {};
    const countPerTimeMap = {};
    dataset.forEach(data => {
        let key = new Date(data.dateTime).toISOString();
        if(!(key in durationPerTimeMap)){
            durationPerTimeMap[key] = (data.duration/1000);
            countPerTimeMap[key] = 1;
            let maxExistingKey = new Date(0);
            for(existingKey in durationPerTimeMap){
                if(new Date(key) > new Date(existingKey) && new Date(existingKey) > new Date(maxExistingKey)){
                    maxExistingKey = new Date(existingKey);
                } 
            }
            if(maxExistingKey > new Date(0)){
                durationPerTimeMap[key] += durationPerTimeMap[maxExistingKey.toISOString()];
                countPerTimeMap[key] += countPerTimeMap[maxExistingKey.toISOString()];
            }
        }else{
            for(existingKey in durationPerTimeMap){
                if(new Date(existingKey) >= new Date(key)){
                    durationPerTimeMap[existingKey] += (data.duration/1000);
                    countPerTimeMap[existingKey] += 1;
                } 
            }
        }
    });
    return generateTimeSeriesData(countPerTimeMap, durationPerTimeMap);
};

const generateTimeSeriesData = function(countPerTimeMap, durationPerTimeMap) {
    const timeSeriesData = {counts:[], durations:[]};
    for (key in durationPerTimeMap) {
        timeSeriesData.durations.push({
            x: key,
            y: durationPerTimeMap[key]
        });
    };

    for (key in countPerTimeMap) {
        timeSeriesData.counts.push({
            x: key,
            y: countPerTimeMap[key]
        });
    };
    return timeSeriesData;
}


const generateFailureClassDisribution = function(dataset){
    const failureMap = {};
    dataset.forEach(data => {
        failureMap[data.failureClass.failureDesc] = failureMap[data.failureClass.failureDesc] ? failureMap[data.failureClass.failureDesc] + 1 : 1;
    })
    return failureMap;
};

const hideAllSections = function(){
    $.each($("#queryNESelectors").children(), function(index, selector) {
        $(`#${$(selector).data("section")}`).hide();
    });
    $.each($("#querySESelectors").children(), function(index, selector) {
        $(`#${$(selector).data("section")}`).hide();
    });
    $.each($("#queryCSSelectors").children(), function(index, selector) {
        $(`#${$(selector).data("section")}`).hide();
    });
}

const displayListOfIMSIEventForDrillDown = function(imsiEventsList){
    hideAllSections();
    $("#networkEngQueryFourDrillDown").show();
    $("#networkEngQueryFourDrillDownTitle").text(`Drilldown : ${imsiEventsList[0].imsi}`)
    displayNetworkEngQueryFourDrillDownTable(imsiEventsList);
    displayNetworkEngQueryFourDrillDownChart(imsiEventsList);
    displayIMSIDetails(imsiEventsList[0]);
}

const displayNetworkEngQueryFourDrillDownTable = function(imsiEventsList){
    console.log(imsiEventsList);
    const table = $('#networkEngQueryFourDrillDownTable').DataTable();
    table.clear();
    $(imsiEventsList).each(function(index, event){
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


const displayNetworkEngQueryFourDrillDownChart = function(imsiEventsList){    
    generateBarLineChart(imsiEventsList);
    generateFailureClassPieChart(imsiEventsList);
}

let networkEngQueryFourDrillDownBarChart = new Chart($("#networkEngQueryFourDrillDownChart")[0], imsiLineChartConfig);

const generateBarLineChart = function(imsiEventsList) {
    const cumulativeDurations = generateCumulativeTimeSeriesData(imsiEventsList)["durations"];
    const incrementalDurations = generateIncrementalTimeSeriesData(imsiEventsList)["durations"];
    const cumulativeCounts = generateCumulativeTimeSeriesData(imsiEventsList)["counts"];
    const incrementalCounts = generateIncrementalTimeSeriesData(imsiEventsList)["counts"];
    networkEngQueryFourDrillDownBarChart.data.datasets[0].data = cumulativeDurations;
    networkEngQueryFourDrillDownBarChart.data.datasets[1].data = cumulativeCounts;
    imsiLineChartConfig.options.scales.yAxes[0].ticks.max = getRoundedUpYAxisMaxValue(cumulativeDurations, cumulativeCounts);
    networkEngQueryFourDrillDownBarChart.update();
    $("#networkEngQueryFourDrillDownChartTitleType").text("Cumulative");
    $("#networkEngQueryFourDrillDownChartTitleDescription").text(`IMSI ${imsiEventsList[0].imsi} Failures`);
    $("#networkEngQueryFourIncrementalBtn").show();
    $("#networkEngQueryFourCumulativeBtn").hide();
    initDrillDownButtons(networkEngQueryFourDrillDownBarChart, cumulativeDurations, incrementalDurations, cumulativeCounts, incrementalCounts);
};

let networkEngQueryFourDrillDownPieChart = new Chart($("#networkEngQueryFourDrillDownFailureClassChart")[0], failureClassPieChartConfig);

const generateFailureClassPieChart = function(imsiEventsList){
    const dataset = generateFailureClassDisribution(imsiEventsList);
    const labels =  Object.keys(dataset);
    const data = [];
    for(let i = 0; i < labels.length; i++){
        data[i] = dataset[labels[i]];
    }
    networkEngQueryFourDrillDownPieChart.destroy();
    networkEngQueryFourDrillDownPieChart = new Chart($("#networkEngQueryFourDrillDownFailureClassChart")[0], failureClassPieChartConfig);
    networkEngQueryFourDrillDownPieChart.data.datasets[0].data = data;
    networkEngQueryFourDrillDownPieChart.data.labels = labels;
    networkEngQueryFourDrillDownPieChart.data.datasets[0].backgroundColor = d3.schemeCategory10.slice(0, data.length);
    networkEngQueryFourDrillDownPieChart.update();
}

const initDrillDownButtons = function(chart, cumulativeDurations, incrementalDurations, cumulativeCounts, incrementalCounts){
    $("#networkEngQueryFourIncrementalBtn").click(function (event) {
        $("#networkEngQueryFourIncrementalBtn").hide();
        $("#networkEngQueryFourCumulativeBtn").show();
        $("#networkEngQueryFourDrillDownChartTitleType").text("Incremental ")
        chart.data.datasets[0].data = incrementalDurations;
        chart.data.datasets[1].data = incrementalCounts;
        imsiLineChartConfig.options.scales.yAxes[0].ticks.max = getRoundedUpYAxisMaxValue(incrementalDurations, incrementalCounts);
        chart.update();
    });

    $("#networkEngQueryFourCumulativeBtn").click(function (event) {
        $("#networkEngQueryFourIncrementalBtn").show();
        $("#networkEngQueryFourCumulativeBtn").hide();
        $("#networkEngQueryFourDrillDownChartTitleType").text("Cumulative ")
        chart.data.datasets[0].data = cumulativeDurations;
        chart.data.datasets[1].data = cumulativeCounts;
        imsiLineChartConfig.options.scales.yAxes[0].ticks.max = getRoundedUpYAxisMaxValue(cumulativeDurations, cumulativeCounts);
        chart.update();
    });
};

const displayIMSIDetails = function(imsi){
    console.log("Updating IMSI Details")
    $("#networkEngQueryFourDrillDownChartCardIMSIDetailsIMSI").val(imsi.imsi);
    $("#networkEngQueryFourDrillDownChartCardIMSIDetailsCountry").val(imsi.marketOperator.countryDesc);
    $("#networkEngQueryFourDrillDownChartCardIMSIDetailsOperator").val(imsi.marketOperator.operatorDesc);
    $("#networkEngQueryFourDrillDownChartCardIMSIDetailsPhoneModel").val(imsi.ueType.model);
    $("#networkEngQueryFourDrillDownChartCardIMSIDetailsPhoneVendor").val(imsi.ueType.vendorName);
}

const queryListOfIMSIEventForDrillDown = function(imsi, fromTime, toTime){
    const startTime = new Date();
    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL2}/IMSIs/query?imsi=${imsi}&from=${fromTime}&to=${toTime}`,
        beforeSend: setAuthHeader1,
        success: function(imsiEventsList){
            displayListOfIMSIEventForDrillDown(imsiEventsList);
            const endTime = new Date();
            displayResponseSummary(imsiEventsList, startTime, endTime);
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("There is an error in queryListOfIMSIEventForDrillDown")
        }
    });
}

const topTenIMSIDrillDownEventHandler = function(event, array){
    $("#drillDownBackIcon").data("target", "networkEngQueryFour");

    let activeBar = this.getElementAtEvent(event);
    if(activeBar[0]){
        let index = activeBar[0]["_index"];
        let imsi = this.data.labels[index];
        let fromTime = new Date($('#startDateOnIMSITopSummaryForm').val()).valueOf();
        let toTime = new Date($('#endDateOnIMSITopSummaryForm').val()).valueOf();        
        queryListOfIMSIEventForDrillDown(imsi, fromTime, toTime);
    }
};

const imsiSummaryDrillDownEventHandler = function(event, array){
    $("#drillDownBackIcon").data("target", "networkEngQueryOne");

    let activeBar = this.getElementAtEvent(event);
    if(activeBar[0]){
        let index = activeBar[0]["_index"];
        let imsi = this.data.labels[index];
        let fromTime = new Date($('#startDateOnIMSISummaryFormNE').val()).valueOf();
        let toTime = new Date($('#endDateOnIMSISummaryFormNE').val()).valueOf();        
        queryListOfIMSIEventForDrillDown(imsi, fromTime, toTime);
    }
}



