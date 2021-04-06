const drillDownInit = function(){
    console.log("Drill down is initialized");
}

const topTenIMSIDrillDown = function(event, array){
    let activeBar = this.getElementAtEvent(event);
    if(activeBar[0]){
        console.log(activeBar);
    }
};

