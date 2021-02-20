function filePicked() {  
    var regex = /^([a-zA-Z0-9\s_\\.\-:])+(.xlsx|.xls)$/;    
    if (regex.test($("#file").val().toLowerCase())) {  
        var xlsxflag = false;  
        if ($("#file").val().toLowerCase().indexOf(".xlsx") > 0) {  
            xlsxflag = true;  
        }  
        if (typeof (FileReader) != "undefined") {  
            var reader = new FileReader();  
            reader.onload = function (e) {  
                var data = e.target.result;    
                if (xlsxflag) {  
                    var workbook = XLSX.read(data, { type: 'binary' });  
                }  
                else {  
                    var workbook = XLS.read(data, { type: 'binary' });  
                }    
                var sheet_name_list = workbook.SheetNames;  
 
                var cnt = 0;  
                sheet_name_list.forEach(function (y) {  
                    if (xlsxflag) {  
                        var exceljson = XLSX.utils.sheet_to_json(workbook.Sheets[y]);  
                    }  
                    else {  
                        var exceljson = XLS.utils.sheet_to_row_object_array(workbook.Sheets[y]);  
                    }
                    console.log(exceljson);
                });  
            }  
            if (xlsxflag) {  
                reader.readAsArrayBuffer($("#file")[0].files[0]);  
            }  
            else {  
                reader.readAsBinaryString($("#file")[0].files[0]);  
            }  
        }  
        else {  
            console.log("Sorry! Your browser does not support HTML5!");  
        }  
    }  
    else {  
        console.log("Please upload a valid Excel file!");  
    }  
}

$(function() {
    oFileIn = document.getElementById('file');
    if(oFileIn.addEventListener) {
        oFileIn.addEventListener('change', filePicked, false);
    }
});