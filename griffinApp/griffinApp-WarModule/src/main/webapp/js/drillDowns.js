/**
 * @author Wilmir Nicanor
 * @email wbnicanor01@gmail.com
 */



/* Util Methods for the Graph*/
const getRoundedUpYAxisMaxValue = function(chartData){
    const maxValue = Math.max(...chartData.map(data => data.y));
    return Math.pow(10, Math.ceil(Math.log10(maxValue)));
}

const getMinXAxisValue = function(chartData){
    return new Date(Math.min(...chartData.map(data => new Date(data.x)))).toDateString();
};

const getMaxXAxisValue = function(chartData){
    return new Date(Math.max(...chartData.map(data => new Date(data.x)))).toDateString();
};

const generateIncrementalTimeSeriesData = function(dataset){
    const countPerTimeMap = dataset.reduce((map,event) => {
        let key = new Date(event.dateTime).toISOString();
        if(key in map){
            map[key] += 1;
        }else{
            map[key] = 1;
        }        
        return map;
    }, {});
    const timeSeriesData = [];
    for(key in countPerTimeMap){
        timeSeriesData.push({
            x:key,
            y: countPerTimeMap[key]
        })
    };
    return timeSeriesData;
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

/* Line Chart Default Config*/
const imsiLineChartConfig = {
    type: "line",
    data: {
      labels: [],
      datasets: [{
        data: [],
        label: "Call Failures",
        borderColor: "#3e95cd",
        fill: false
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
          }
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
      }
    }
};


const displayListOfIMSIEventForDrillDown = function(imsiEventsList){
    hideAllSections();
    $("#networkEngQueryFourDrillDown").show();
    displayNetworkEngQueryFourDrillDownTable(imsiEventsList);
    displayNetworkEngQueryFourDrillDownChart(imsiEventsList);
}

const displayNetworkEngQueryFourDrillDownTable = function(imsiEventsList){
    const table = $('#networkEngQueryFourDrillDownTable').DataTable();
    table.clear();
    $(imsiEventsList).each(function(index, event){
        table.row.add([event.dateTime, 
            event.imsi,
            event.eventCause.description
        ]);
    });
    table.draw();
}

const displayNetworkEngQueryFourDrillDownChart = function(imsiEventsList){    
    const context = $("#networkEngQueryFourDrillDownChart")[0];
    const networkEngQueryFourDrillDownChart = new Chart(context, imsiLineChartConfig);
    let chartData = generateIncrementalTimeSeriesData(imsiEventsList);
    networkEngQueryFourDrillDownChart.data.datasets[0].data = chartData;
    imsiLineChartConfig.options.scales.yAxes[0].ticks.max = getRoundedUpYAxisMaxValue(chartData);
    networkEngQueryFourDrillDownChart.update();

    $("#networkEngQueryFourDrillDownChartTitle").text(`IMSI ${imsiEventsList[0].imsi} Failures`)


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
            console.log(imsiLineChartConfig);
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("There is an error in queryListOfIMSIEventForDrillDown")
        }
    });
}

const topTenIMSIDrillDownEventHandler = function(event, array){
    let activeBar = this.getElementAtEvent(event);
    if(activeBar[0]){
        let index = activeBar[0]["_index"];
        let imsi = this.data.labels[index];
        let fromTime = new Date($('#startDateOnIMSITopSummaryForm').val()).valueOf();
        let toTime = new Date($('#endDateOnIMSITopSummaryForm').val()).valueOf();        
        queryListOfIMSIEventForDrillDown(imsi, fromTime, toTime);
    }
};