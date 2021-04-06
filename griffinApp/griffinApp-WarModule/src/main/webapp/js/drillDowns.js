const drillDownInit = function(){
    console.log("hello");
}

const topTenIMSIDrillDown = function(event, array){
    let activeBar = this.getElementAtEvent(event);
    if(activeBar[0]){
        console.log(activeBar);
    }

    // if (activePoints[0]) {
    //   console.log(activePoints.length);
    //   console.log(activePoints[0]);

    //   var chartData = activePoints[0]['_chart'].config.data;
    //   var idx = activePoints[0]['_index'];

    //   var label = chartData.labels[idx];
    //   var value = chartData.datasets[0].data[idx];
    //   var color = chartData.datasets[0].backgroundColor[idx]; //Or any other data you wish to take from the clicked slice

    //   console.log(chartData.datasets[0]);


    //   alert(label + ' ' + value + ' ' + color);
    // }
};

