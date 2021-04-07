/* Util Methods for the Graph*/
const getRoundedUpYAxisMaxValue = function(chartData){
    const maxValue = Math.max(...chartData.map(data => data.y));
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
    const durationPerTimeMap = {};
    const countPerTimeMap = {};
    dataset.forEach(data => {
        let key = new Date(data.dateTime).toISOString();
        if(!(key in durationPerTimeMap)){
            durationPerTimeMap[key] = (data.duration/1000);
            countPerTimeMap[key] += 1;
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
                    countPerTimeMap[key] += 1;
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


/* Line Chart Config*/
const imsiLineChartConfig = {
    type: "bar",
    data: {
      labels: [],
      datasets: [{
        type: 'line',
        data: [],
        label: "Call Failures Duration",
        borderColor: "#FFA500",        
        borderDash: [2,2],
        borderWidth:2
     },{
        label: "Call Failures Count",
        backgroundColor: "#4e73df",
        hoverBackgroundColor: "#002593",
        borderColor: "#4e73df",
        barPercentage:0.5,
        categoryPercentage:1.0,
        maxBarThickness:50,
        data: [],
      }]
    },
    options: {
      scales: {
        xAxes: [{
          type: "time",
          min:0,
          distribution: "linear",
          ticks:{
            source:'data'
          },
          gridLines: {
            display: false,
            drawBorder: false
          },
          offset: true
        }],
        yAxes: [{
          ticks: {
            min: 0,
            max: 100,     
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
        title: {
          display: false
        }
      },
      maintainAspectRatio: false,
      layout: {
        padding: {
          left: 10,
          right: 25,
          top: 25,
          bottom: 0
        }
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
      }
    }
};

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
    const context = $("#networkEngQueryFourDrillDownChart")[0];
    const networkEngQueryFourDrillDownChart = new Chart(context, imsiLineChartConfig);
    const cumulativeDurations = generateCumulativeTimeSeriesData(imsiEventsList)["durations"];
    const incrementalDurations = generateIncrementalTimeSeriesData(imsiEventsList)["durations"];
    const cumulativeCounts = generateCumulativeTimeSeriesData(imsiEventsList)["durations"];
    const incrementalCounts = generateIncrementalTimeSeriesData(imsiEventsList)["durations"];
    networkEngQueryFourDrillDownChart.data.datasets[0].data = cumulativeDurations;
    networkEngQueryFourDrillDownChart.data.datasets[1].data = cumulativeCounts;
    imsiLineChartConfig.options.scales.yAxes[0].ticks.max = getRoundedUpYAxisMaxValue(cumulativeDurations);
    networkEngQueryFourDrillDownChart.update();
    $("#networkEngQueryFourDrillDownChartTitleType").text("Cumulative")
    $("#networkEngQueryFourDrillDownChartTitleDescription").text(`IMSI ${imsiEventsList[0].imsi} Failures`);
    $("#networkEngQueryFourIncrementalBtn").show();
    initDrillDownButtons(networkEngQueryFourDrillDownChart, cumulativeDurations, incrementalDurations, cumulativeCounts, incrementalCounts);
}

const initDrillDownButtons = function(chart, cumulativeDurations, incrementalDurations, cumulativeCounts, incrementalCounts){
    $("#networkEngQueryFourIncrementalBtn").click(function (event) {
        $("#networkEngQueryFourIncrementalBtn").hide();
        $("#networkEngQueryFourCumulativeBtn").show();
        $("#networkEngQueryFourDrillDownChartTitleType").text("Incremental ")
        chart.data.datasets[0].data = incrementalDurations;
        chart.data.datasets[1].data = incrementalCounts;
        imsiLineChartConfig.options.scales.yAxes[0].ticks.max = getRoundedUpYAxisMaxValue(incrementalDurations)["durations"];
        chart.update();
        console.log(imsiLineChartConfig);
    });

    $("#networkEngQueryFourCumulativeBtn").click(function (event) {
        $("#networkEngQueryFourIncrementalBtn").show();
        $("#networkEngQueryFourCumulativeBtn").hide();
        $("#networkEngQueryFourDrillDownChartTitleType").text("Cumulative ")
        chart.data.datasets[0].data = cumulativeDurations;
        chart.data.datasets[1].data = cumulativeCounts;
        imsiLineChartConfig.options.scales.yAxes[0].ticks.max = getRoundedUpYAxisMaxValue(cumulativeDurations)["durations"];
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

