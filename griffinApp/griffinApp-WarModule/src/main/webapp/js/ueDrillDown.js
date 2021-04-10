


const UEDrillDownEventHandler = function(event, array){
    $("#drillDownBackIcon").data("target", "networkEngQueryTwo");
    imsiLineChartConfig.options.scales.xAxes[0].ticks.minRotation = 0;
    let activeBar = this.getElementAtEvent(event);
    if(activeBar[0]){
        let index = activeBar[0]["_index"];
        let description = this.data.labels[index];    
        queryListOfUEEventForDrillDown(description);
    }
}

const queryListOfUEEventForDrillDown = function(description){
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

