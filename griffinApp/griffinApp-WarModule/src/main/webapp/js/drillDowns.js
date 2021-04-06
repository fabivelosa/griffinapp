const drillDownInit = function(){
    console.log("Drill down is initialized");
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


const displayListOfIMSIEventForDrillDown = function(imsiEventsList){
    hideAllSections();
    $("#networkEngQueryFourDrillDown").show();
    console.log(imsiEventsList);
}

const queryListOfIMSIEventForDrillDown = function(imsi, fromTime, toTime){
    console.log(`${imsi} ${fromTime} ${toTime}`);
    $.ajax({
        type:'GET',
        dataType:'json',
        url:`${rootURL2}/IMSIs/query?imsi=${imsi}&from=${fromTime}&to=${toTime}`,
        beforeSend: setAuthHeader1,
        success: function(imsiEventsList){
            displayListOfIMSIEventForDrillDown(imsiEventsList);
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.log("There is an error in queryListOfIMSIEventForDrillDown")
        }
    });
}

const topTenIMSIDrillDown = function(event, array){
    let activeBar = this.getElementAtEvent(event);
    if(activeBar[0]){
        let index = activeBar[0]["_index"];
        let imsi = this.data.labels[index];
        let fromTime = new Date($('#startDateOnIMSITopSummaryForm').val()).valueOf();
        let toTime = new Date($('#endDateOnIMSITopSummaryForm').val()).valueOf();        
        queryListOfIMSIEventForDrillDown(imsi, fromTime, toTime);
    }
};

