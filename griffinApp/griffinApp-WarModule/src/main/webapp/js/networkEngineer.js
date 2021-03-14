const rootURL = "http://localhost:8080/callfailures/api";


const displayIMSISummary = function(data, textStatus, jqXHR){
    $("#imsiSummaryFormResult").empty();
    






    console.log(data);
}


const displayErrorOnIMSISummary = function(jqXHR, textStatus, errorThrown){
    console.log(data);
}

const queryIMSISUmmary = function(imsi, from, to){
    $.ajax({
        type: "GET",
        dataType: "json",
        url: `${rootURL}/events/query?imsi=${imsi}&from=${from}&to=${to}&summary=true`,
        success: displayIMSISummary,
        error: displayErrorOnIMSISummary
    })
}

$(document).ready(function(){		
    
    $('#imsiSummaryForm').submit(function(event){
        event.preventDefault();
        const imsi = $('#imsiOnIMSISummaryForm').val();
        const from = new Date($('#startDateOnIMSISummaryForm').val()).valueOf();
        const to = new Date($('#endDateOnIMSISummaryForm').val()).valueOf();
        queryIMSISUmmary(imsi, from, to);
    });

    
});