const displayListOfCellEventForDrillDown = function(eventsList){
    hideAllSections();
    $("#networkEngQueryThreeDrillDown").show();
    $("#networkEngQueryThreeDrillDownTitle").text(`Drilldown : Cell ${eventsList[0].cellId} of ${eventsList[0].marketOperator.operatorDesc} | ${eventsList[0].marketOperator.countryDesc} `)
    console.log(eventsList);
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
            const endTime = new Date();
            displayResponseSummary(eventsList, startTime, endTime);
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("There is an error in queryListOfIMSIEventForDrillDown")
        }
    });
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