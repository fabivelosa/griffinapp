/* Util Methods for the Graph*/
const getRoundedUpYAxisMaxValue = function(durations, counts){
    const durationsMaxValue = Math.max(...durations.map(data => data.y));
    const countsMaxValue = Math.max(...counts.map(data => data.y));
    const maxValue = Math.max(durationsMaxValue, countsMaxValue);
    return maxValue < 5 ? 5 : Math.ceil(maxValue) + (10 - (Math.ceil(maxValue) % 10));
}

const getMinXAxisValue = function(chartData){
    return new Date(Math.min(...chartData.map(data => new Date(data.x)))).toDateString();
};

const getMaxXAxisValue = function(chartData){
    return new Date(Math.max(...chartData.map(data => new Date(data.x)))).toDateString();
};

const roundDateToNearestHour = function(data) {
    let date = new Date(data.dateTime);
    date.setMinutes(0);
    return date;
};

const generateIncrementalTimeSeriesData = function(dataset){
    const durationPerTimeMap = {};
    const countPerTimeMap = {};
    dataset.forEach(data => {
        let date = roundDateToNearestHour(data);

        let key = new Date(date).toISOString();
        durationPerTimeMap[key] = durationPerTimeMap[key] ? durationPerTimeMap[key] + (data.duration/1000) : (data.duration/1000);
        countPerTimeMap[key] = countPerTimeMap[key] ? countPerTimeMap[key] + 1 : 1;
    });
    return generateTimeSeriesData(countPerTimeMap, durationPerTimeMap);
};


const generateCumulativeTimeSeriesData = function(dataset){
    const durationPerTimeMap = {};
    const countPerTimeMap = {};
    dataset.forEach(data => {
        let date = roundDateToNearestHour(data);
        let key = new Date(date).toISOString();
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

    const dateComparator = (current, next) => new Date(current.x) - new Date(next.x);
    timeSeriesData["counts"].sort(dateComparator);
    timeSeriesData["durations"].sort(dateComparator);
    return timeSeriesData;
}


const generateFailureClassDisribution = function(dataset){
    const failureMap = {};
    dataset.forEach(data => {
        failureMap[data.failureClass.failureDesc] = failureMap[data.failureClass.failureDesc] ? failureMap[data.failureClass.failureDesc] + 1 : 1;
    })
    return failureMap;
};

//MARKET OP
const generateMarketOperatorCell = function(dataset){
	const marketOperatorCell ={};
	dataset.forEach(data => {
		let combo = 'Cell: '+ data.cellId + ' - '  + data.marketOperator.operatorDesc+ ' ' + data.marketOperator.countryDesc;
		marketOperatorCell[combo] = marketOperatorCell[combo] ? marketOperatorCell[combo] + 1 : 1;
	})
    const top10Array =  Object.entries(marketOperatorCell).sort((a,b)=>b[1]-a[1])  
    const top10Object ={};
    top10Array.forEach(item => {
        top10Object[item[0]] = item[1];
    });
    return top10Object;
};

const generateCellDistribution = function(dataset){
    const cells = {};
    dataset.forEach(data => {
        cells[data.cellId] = cells[data.cellId] ? cells[data.cellId] + 1 : 1;
    })
    return cells;
};

const generateTop10IMSIDistribution = function(dataset){
    const imsis = {};
    dataset.forEach(data => {
        imsis[data.imsi] = imsis[data.imsi] ? imsis[data.imsi] + 1 : 1;
    });
    const top10IMSIsArray =  Object.entries(imsis).sort((a,b)=>b[1]-a[1]).slice(0,10);    
    const top10IMSISObject ={};
    top10IMSIsArray.forEach(imsi => {
        top10IMSISObject[imsi[0]] = imsi[1];
    });
    return top10IMSISObject;
};

const generateTop10PhoneModelDistribution = function(dataset){
    const items = {};
    dataset.forEach(data => {
        items[data.ueType.model] = items[data.ueType.model] ? items[data.ueType.model] + 1 : 1;
    });

    
    const top10Array =  Object.entries(items).sort((a,b)=>b[1]-a[1]).slice(0,10);    
    const top10Object ={};
    top10Array.forEach(item => {
        top10Object[item[0]] = item[1];
    });
    return top10Object;
};

const generateCauseCodeDescriptionDistribution = function(dataset){
    const items = {};
    dataset.forEach(data => {
        let key = data.eventCause.description.split("-")[1].trim();
        items[key] = items[key] ? items[key] + 1 : 1;
    });
    const top10Array =  Object.entries(items).sort((a,b)=>b[1]-a[1]).slice(0,10);    
    const top10Object ={};
    top10Array.forEach(item => {
        top10Object[item[0]] = item[1];
    });
    return top10Object;
}

const countUniqueIMSI = function(dataset){
    const imsis = new Set();
    dataset.forEach(data => {
        imsis.add(data.imsi);
    });
    return imsis.size;
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
    displayNetworkEngQueryFourDrillDownCharts(imsiEventsList);
    displayIMSIDetails(imsiEventsList);
}

const displayListOfIMSIEventForDrillDownByPhoneModel = function(imsiEventsList){
    hideAllSections();
    $("#supportEngQueryDrillDown").show();
    $("#supportEngQueryDrillDownTitle").text(`Drilldown : ${imsiEventsList[0].ueType.model}`);
    displayIMSIDetailsPhoneModel(imsiEventsList);
    displayNetworkEngQueryFourDrillDownTable(imsiEventsList);
    displayNetworkEngQueryFourDrillDownChartsPhoneModel(imsiEventsList);
}

const displayNetworkEngQueryFourDrillDownTable = function(imsiEventsList){
    const table = $('#supportEngQueryDrillDownTable').DataTable();
    table.clear();
    $(imsiEventsList).each(function(index, event){
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


const displayNetworkEngQueryFourDrillDownCharts = function(imsiEventsList){    
    generateBarLineChart(imsiEventsList);
    generatePhoneModelPieChart(imsiEventsList);
    generateCauseCodeBarChart(imsiEventsList);
    generateCellIDBarChart(imsiEventsList);
}

const displayNetworkEngQueryFourDrillDownChartsPhoneModel = function(imsiEventsList){    
    generateBarLineChartPhoneModel(imsiEventsList);
    generateCauseCodeBarChart(imsiEventsList);
    generateCellIDBarChart(imsiEventsList);
}

let networkEngQueryFourDrillDownBarChart = new Chart($("#networkEngQueryFourDrillDownChart")[0], imsiLineChartConfig);
let colorPalette = ["#4e73df", "#ff7f0e", "#2ca02c", "#d62728", "#9467bd", "#8c564b", "#e377c2", "#7f7f7f", "#bcbd22", "#17becf"];

const generateBarLineChart = function(imsiEventsList) {
    networkEngQueryFourDrillDownBarChart.destroy();
    networkEngQueryFourDrillDownBarChart = new Chart($("#networkEngQueryFourDrillDownChart")[0], imsiLineChartConfig);
    const incrementalDurations = generateIncrementalTimeSeriesData(imsiEventsList)["durations"];
    const incrementalCounts = generateIncrementalTimeSeriesData(imsiEventsList)["counts"];
    networkEngQueryFourDrillDownBarChart.data.datasets[0].data = incrementalDurations;
    networkEngQueryFourDrillDownBarChart.data.datasets[1].data = incrementalCounts;
    imsiLineChartConfig.options.scales.yAxes[0].ticks.max = getRoundedUpYAxisMaxValue(incrementalDurations, incrementalCounts);

    // add the event handler to the chart
    imsiLineChartConfig.options.onClick = imsiDrillDownLineChartEventHandler;

    networkEngQueryFourDrillDownBarChart.update();
    $("#networkEngQueryFourDrillDownChartTitleType").text("");
    $("#networkEngQueryFourDrillDownChartTitleDescription").text(`IMSI ${imsiEventsList[0].imsi} Failures`);
};

let networkEngQueryFourDrillDownPhoneModelChart = new Chart($("#networkEngQueryFourDrillDownPhoneModelChart")[0], top10PhoneModelHorizontalBarConfig);
const generatePhoneModelPieChart = function(imsiEventsList){
    const dataset = generateTop10PhoneModelDistribution(imsiEventsList);
    const labels =  Object.keys(dataset);
    const data = [];
    for(let i = 0; i < labels.length; i++){
        data[i] = dataset[labels[i]];
    }
    networkEngQueryFourDrillDownPhoneModelChart.destroy();
    
    // add the event handler to the phone model chart
    top10PhoneModelHorizontalBarConfig.options.onClick = imsiDrillDownPhoneModelChartEventHandler;
    
    top10PhoneModelHorizontalBarConfig.options.scales.xAxes[0].ticks.max = Math.max(...data);

    networkEngQueryFourDrillDownPhoneModelChart = new Chart($("#networkEngQueryFourDrillDownPhoneModelChart")[0], top10PhoneModelHorizontalBarConfig);
    networkEngQueryFourDrillDownPhoneModelChart.data.datasets[0].data = data;
    networkEngQueryFourDrillDownPhoneModelChart.data.labels = labels;
    if(data.length == 1){
        networkEngQueryFourDrillDownPhoneModelChart.data.datasets[0].backgroundColor = "#4e73df";
    }else{
        networkEngQueryFourDrillDownPhoneModelChart.data.datasets[0].backgroundColor = d3.quantize(d3.interpolateHcl("#4e73df", "#60c96e"), data.length);
    }
    networkEngQueryFourDrillDownPhoneModelChart.update();
}

let supportEngQueryDrillDownBarChart = new Chart($("#supportEngQueryDrillDownChart")[0], imsiLineChartConfig);

const generateBarLineChartPhoneModel = function(imsiEventsList) {
    supportEngQueryDrillDownBarChart.destroy();
    supportEngQueryDrillDownBarChart = new Chart($("#supportEngQueryDrillDownChart")[0], imsiLineChartConfig);
    const incrementalDurations = generateIncrementalTimeSeriesData(imsiEventsList)["durations"];
    const incrementalCounts = generateIncrementalTimeSeriesData(imsiEventsList)["counts"];
    supportEngQueryDrillDownBarChart.data.datasets[0].data = incrementalDurations;
    supportEngQueryDrillDownBarChart.data.datasets[1].data = incrementalCounts;
    imsiLineChartConfig.options.scales.yAxes[0].ticks.max = getRoundedUpYAxisMaxValue(incrementalDurations, incrementalCounts);
    supportEngQueryDrillDownBarChart.update();
    $("#supportEngQueryDrillDownChartTitleType").text("Cumulative");
    $("#supportEngQueryDrillDownChartTitleDescription").text(`Model ${imsiEventsList[0].ueType.model} Failures`);
};

let networkEngQueryFourDrillDownCauseCodeChart = new Chart($("#networkEngQueryFourDrillDownCauseCodeChart")[0], failureClassPieChartConfig);
const generateCauseCodeBarChart = function(imsiEventsList){
    const dataset = generateCauseCodeDescriptionDistribution(imsiEventsList);
    const labels =  Object.keys(dataset);
    const data = [];
    for(let i = 0; i < labels.length; i++){
        data[i] = dataset[labels[i]];
    }
    networkEngQueryFourDrillDownCauseCodeChart.destroy();

    // add the event handler to the chart
    failureClassPieChartConfig.options.onClick = imsiDrillDownCauseCodeChartEventHandler;

    networkEngQueryFourDrillDownCauseCodeChart = new Chart($("#networkEngQueryFourDrillDownCauseCodeChart")[0], failureClassPieChartConfig);
    networkEngQueryFourDrillDownCauseCodeChart.data.datasets[0].data = data;
    networkEngQueryFourDrillDownCauseCodeChart.data.labels = labels;
    networkEngQueryFourDrillDownCauseCodeChart.data.datasets[0].backgroundColor = colorPalette.slice(0, data.length);
    networkEngQueryFourDrillDownCauseCodeChart.update();
}

let supportEngQueryDrillDownCauseCodeChart = new Chart($("#supportEngQueryDrillDownCauseCodeChart")[0], failureClassPieChartConfig);
const generateCauseCodeBarChartPhoneModel = function(imsiEventsList){
    const dataset = generateCauseCodeDescriptionDistribution(imsiEventsList);
    const labels =  Object.keys(dataset);
    const data = [];
    for(let i = 0; i < labels.length; i++){
        data[i] = dataset[labels[i]];
    }
    supportEngQueryDrillDownCauseCodeChart.destroy();

    // add the event handler to the chart
    failureClassPieChartConfig.options.onClick = imsiDrillDownCauseCodeChartEventHandler;

    supportEngQueryDrillDownCauseCodeChart = new Chart($("#supportEngQueryDrillDownCauseCodeChart")[0], failureClassPieChartConfig);
    supportEngQueryDrillDownCauseCodeChart.data.datasets[0].data = data;
    supportEngQueryDrillDownCauseCodeChart.data.labels = labels;
    supportEngQueryDrillDownCauseCodeChart.data.datasets[0].backgroundColor = colorPalette.slice(0, data.length);
    supportEngQueryDrillDownCauseCodeChart.update();
}


let networkEngQueryFourDrillDownCellIDChart = new Chart($("#networkEngQueryFourDrillDownCellIDChart")[0], cellIDChartConfig);
const generateCellIDBarChart = function(imsiEventsList){
    const dataset = generateMarketOperatorCell(imsiEventsList);
    const labels =  Object.keys(dataset);
    const data = [];
    for(let i = 0; i < labels.length; i++){
        data[i] = dataset[labels[i]];
    }
    networkEngQueryFourDrillDownCellIDChart.destroy();

    // add the event handler to the chart
    cellIDChartConfig.options.onClick = imsiDrillDownCellIDChartEventHandler;

    cellIDChartConfig.options.scales.yAxes[0].ticks.max = Math.max(...data);
    networkEngQueryFourDrillDownCellIDChart = new Chart($("#networkEngQueryFourDrillDownCellIDChart")[0], cellIDChartConfig);
    networkEngQueryFourDrillDownCellIDChart.data.datasets[0].data = data;
    networkEngQueryFourDrillDownCellIDChart.data.labels = labels;
    networkEngQueryFourDrillDownCellIDChart.data.datasets[0].backgroundColor = colorPalette.slice(0, data.length);
    networkEngQueryFourDrillDownCellIDChart.update();
}

let supportEngQueryDrillDownCellIDChart = new Chart($("#supportEngQueryDrillDownCellIDChart")[0], cellIDChartConfig);
const generateCellIDBarChartPhoneModel = function(imsiEventsList){
    const dataset = generateMarketOperatorCell(imsiEventsList);
    const labels =  Object.keys(dataset);
    const data = [];
    for(let i = 0; i < labels.length; i++){
        data[i] = dataset[labels[i]];
    }
    supportEngQueryDrillDownCellIDChart.destroy();

    // add the event handler to the chart
    cellIDChartConfig.options.onClick = imsiDrillDownCellIDChartEventHandler;

    cellIDChartConfig.options.scales.yAxes[0].ticks.max = Math.max(...data);
    supportEngQueryDrillDownCellIDChart = new Chart($("#supportEngQueryDrillDownCellIDChart")[0], cellIDChartConfig);
    supportEngQueryDrillDownCellIDChart.data.datasets[0].data = data;
    supportEngQueryDrillDownCellIDChart.data.labels = labels;
    supportEngQueryDrillDownCellIDChart.data.datasets[0].backgroundColor = colorPalette.slice(0, data.length);
    supportEngQueryDrillDownCellIDChart.update();
}

const displayIMSIDetails = function(imsiEventsList){
    const imsi = imsiEventsList[0];
    $("#networkEngQueryFourDrillDownChartCardIMSIDetailsIMSI").val(imsi.imsi);
    $("#networkEngQueryFourDrillDownChartCardIMSIDetailsTotalFailureCount").val(imsiEventsList.length);
}

const displayIMSIDetailsPhoneModel = function(imsiEventsList){
    $("#supportEngQueryDrillDownChartCardIMSIDetailsTotalFailureCount").val(imsiEventsList.length);
}

const cacheIMSIDrillDownData = function(data){
    $("#networkEngQueryFourDrillDown").data("id", data[0].imsi);
    $("#networkEngQueryFourDrillDown").data("type", "imsi");
    $("#networkEngQueryFourDrillDown").data("events", data);
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
            cacheIMSIDrillDownData(imsiEventsList);
            const endTime = new Date();
            displayResponseSummary(imsiEventsList, startTime, endTime);
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("There is an error in queryListOfIMSIEventForDrillDown")
        }
    });
}

const topTenIMSIDrillDownEventHandler = function(event, array){
    $("#imsiDrillDownBackIcon").data("target", "networkEngQueryFour");
    imsiLineChartConfig.options.scales.xAxes[0].ticks.minRotation = 45;
    let activeBar = this.getElementAtEvent(event);
    if(activeBar[0]){
        let index = activeBar[0]["_index"];
        let imsi = this.data.labels[index];
        let fromTime = new Date($('#startDateOnIMSITopSummaryForm').val()).valueOf();
        let toTime = new Date($('#endDateOnIMSITopSummaryForm').val()).valueOf();        
        queryListOfIMSIEventForDrillDown(imsi, fromTime, toTime);
        showFullDataBanner();
    }
};

const imsiSummaryDrillDownEventHandler = function(event, array){
    $("#imsiDrillDownBackIcon").data("target", "networkEngQueryOne");
    imsiLineChartConfig.options.scales.xAxes[0].ticks.minRotation = 0;
    let activeBar = this.getElementAtEvent(event);
    if(activeBar[0]){
        let index = activeBar[0]["_index"];
        let imsi = this.data.labels[index]; // This captures the description of the bar
        let fromTime = new Date($('#startDateOnIMSISummaryFormNE').val()).valueOf();
        let toTime = new Date($('#endDateOnIMSISummaryFormNE').val()).valueOf();        
        queryListOfIMSIEventForDrillDown(imsi, fromTime, toTime);
        showFullDataBanner();
    }
}

const imsiFailuresDrillDownEventHandler = function(event, array){
    $("#imsiDrillDownBackIcon").data("target", "imsiFailuresCountQuery");
    imsiLineChartConfig.options.scales.xAxes[0].ticks.minRotation = 0;
    let activeBar = this.getElementAtEvent(event);
    if(activeBar[0]){
        let index = activeBar[0]["_index"];
        let imsi = this.data.labels[index];
        let fromTime = new Date($('#startDateOnIMSISummaryForm').val()).valueOf();
        let toTime = new Date($('#endDateOnIMSISummaryForm').val()).valueOf();        
        queryListOfIMSIEventForDrillDown(imsi, fromTime, toTime);
        showFullDataBanner();
    }
};

const imsiDrillDownLineChartEventHandler = function(event, array){
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
    displayListOfIMSIEventForDrillDown(filteredData);
    const endTime = new Date();
    displayResponseSummary(filteredData, startTime, endTime);
}

const imsiDrillDownPhoneModelChartEventHandler = function(event, array){
    const startTime = new Date();
    const storedData = $(this.canvas).closest(".drillDownSections").data("events");
    const activeBar = this.getElementAtEvent(event);
    let filteredData;
    if(activeBar[0]){
        const index = activeBar[0]["_index"];
        const phoneModel = this.data.labels[index];
        filteredData = filterPhoneModelEventData(phoneModel, storedData);
        showFilterDataBanner();
    }else{
        filteredData = storedData;
        showFullDataBanner();
    }
    displayListOfIMSIEventForDrillDown(filteredData);
    const endTime = new Date();
    displayResponseSummary(filteredData, startTime, endTime);
}

const imsiDrillDownCauseCodeChartEventHandler = function(event, array){    
    const startTime = new Date();
    let storedData = $(this.canvas).closest(".drillDownSections").data("events");
    let filteredData;
    const activeSlice = this.getElementAtEvent(event);
    if(activeSlice[0]){
        const index = activeSlice[0]["_index"];
        const failureClass = this.data.labels[index];
        filteredData = filterCauseCodeDescriptionEventData(failureClass, storedData);
        showFilterDataBanner();
    }else{
        filteredData = storedData;
        showFullDataBanner();
    }
    displayListOfIMSIEventForDrillDown(filteredData);
    const endTime = new Date();
    displayResponseSummary(filteredData, startTime, endTime);
}

const imsiDrillDownCellIDChartEventHandler = function(event, array){    
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
    displayListOfIMSIEventForDrillDown(filteredData);
    const endTime = new Date();
    displayResponseSummary(filteredData, startTime, endTime);
}


const imsiClickFromTableEventHandler = function(imsi, parentContainer){
    $("#imsiDrillDownBackIcon").data("target", parentContainer);
    queryListOfIMSIEventForDrillDown(imsi, 0, 8640000000000000);    
    showFullDataBanner();
}

const countCallFailuresDrillDownEventHandler = function(event, array){
    $("#imsiDrillDownBackIconSup").data("target", "supportEngQueryOne");
    imsiLineChartConfig.options.scales.xAxes[0].ticks.minRotation = 0;
    let activeBar = this.getElementAtEvent(event);
    if(activeBar[0]){
        let index = activeBar[0]["_index"];
        let model = this.data.labels[index];
        let fromTime = new Date($('#startDateOnCallFailureForm').val()).valueOf();
        let toTime = new Date($('#endDateOnCallFailureForm').val()).valueOf();      
        queryListOfIMSIEventForDrillDownByPhoneModel(model, fromTime, toTime);
    }
};

const queryListOfIMSIEventForDrillDownByPhoneModel = function(model, fromTime, toTime){
    const startTime = new Date();
    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL2}/events/query/ue/imsi?model=${model}&from=${fromTime}&to=${toTime}`,
        beforeSend: setAuthHeader1,
        success: function(imsiEventsList){
            displayListOfIMSIEventForDrillDownByPhoneModel(imsiEventsList);
            const endTime = new Date();
            displayResponseSummary(imsiEventsList, startTime, endTime);
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("There is an error in queryListOfIMSIEventForDrillDownByPhoneModel")
        }
    });
};

