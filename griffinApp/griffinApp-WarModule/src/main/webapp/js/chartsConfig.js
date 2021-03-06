const imsiLineChartConfig = {
    type: "line",
    data: {
      labels: [],
      datasets: [{
        data: [],
        label: "Hourly Failures Duration (seconds)",
        borderColor: "#FFA500",        
        borderDash: [2,2],
        borderWidth:2,
        fill:false,
     },{
        type: 'bar',
        label: "Hourly Failures Count",
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
          time: {
            displayFormats: {
               'millisecond': 'MMM DD HH:mm',
               'second': 'MMM DD HH:mm',
               'minute': 'MMM DD HH:mm',
               'hour': 'MMM DD HH:mm',
               'day': 'MMM DD HH:mm',
               'week': 'MMM DD HH:mm',
               'month': 'MMM DD HH:mm',
               'quarter': 'MMM DD HH:mm',
               'year': 'MMM DD HH:mm',
            },
          },
          min:0,
          distribution: "linear",
          ticks:{
            source:'data',
            autoSkip: false,
            maxRotation: 90,
            minRotation: 0
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
            max: 5,     
            padding: 10,
         },
        scaleLabel: {
          display: true,
          labelString: 'Hourly Failures Count/Duration(s)'
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
      plugins: {
        datalabels: {
            display: false,
        },
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
      },
      legend: {
        display: true,
        position:'bottom',
      }
    }
};

const failureClassPieChartConfig = {
    type: "doughnut",
    data: {
        labels: [],
        datasets: [{
            data: [],
            backgroundColor: [],
            hoverBackgroundColor: [],
            hoverBorderColor: "rgba(234, 236, 244, 1)"
        }]
    },
    options: {
        maintainAspectRatio: false,
        tooltips: {
            backgroundColor: "rgb(255,255,255)",
            bodyFontColor: "#858796",
            borderColor: "#dddfeb",
            borderWidth: 1,
            xPadding: 15,
            yPadding: 15,
            displayColors: false,
            caretPadding: 10
        },
        plugins: {
          datalabels: {
              formatter: (value, ctx) => {
                  let sum = 0;
                  let dataArr = ctx.chart.data.datasets[0].data;
                  dataArr.map(data => {
                      sum += data;
                  });
                  let percentage = (value*100 / sum).toFixed(2)+"%";
                  return percentage;
              },
              color: '#fff',
          }
        },
        layout: {
          padding: {
            left: 10,
            right: 25,
            top: 25,
            bottom: 0
          }
        },
        legend: {
            display: true,
            position:'right',
        },
        cutoutPercentage: 60
    }
};


const cellIDChartConfig = {
  type: 'bar',
  data: {
    labels: [],
    datasets: [{
      backgroundColor: [],
      hoverBackgroundColor: "#4e73df",
      borderColor: "#4e73df",
      barPercentage:0.5,
      categoryPercentage:1.0,
      maxBarThickness:40,
      data: [],
    }],
  },
  options: {
    maintainAspectRatio: false,
    layout: {
      padding: {
        left: 10,
        right: 25,
        top: 25,
        bottom: 0
      }
    },
    scales: {
      xAxes: [{
        type:"category",
        gridLines: {
          display: false,
          drawBorder: false
        },
        ticks: {
          maxTicksLimit: 12
        }
      }],
      yAxes: [{
        ticks: {
          min: 0,
          max: 30,       
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
    },
    legend: {
      display: false
    },
    plugins: {
      datalabels: {
          display: false,
      },
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
    },
  }
}


const top10PhoneModelHorizontalBarConfig = {
  type: 'horizontalBar',
  data: {
    labels: [],
    datasets: [{
      label: "Total Count",
      backgroundColor: [],
      hoverBackgroundColor: "#4e73df",
      borderColor: "#4e73df",
      barPercentage:0.5,
      categoryPercentage:1.0,
      maxBarThickness:40,
      data: [],
    }],
  },
  options: {
    maintainAspectRatio: false,
    layout: {
      padding: {
        left: 10,
        right: 25,
        top: 25,
        bottom: 0
      }
    },
    scales: {
      yAxes: [{
        type:"category",
        gridLines: {
          display: false,
          drawBorder: false
        },
        ticks: {
          maxTicksLimit: 10
        }
      }],
      xAxes: [{
        ticks: {
          min: 0,
          max: 20,
          maxTicksLimit: 10,
          padding: 10
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
    },
    legend: {
      display: false
    },
    plugins: {
      datalabels: {
          display: false,
      },
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
          return datasetLabel + ': ' + tooltipItem.xLabel;
        }
      }
    },
  }
};

const top10IMSIHorizontalBarConfig = {
  type: 'horizontalBar',
  data: {
    labels: [],
    datasets: [{
      label: "Total Count",
      backgroundColor: [],
      hoverBackgroundColor: "#4e73df",
      borderColor: "#4e73df",
      barPercentage:0.5,
      categoryPercentage:1.0,
      maxBarThickness:40,
      data: [],
    }],
  },
  options: {
    maintainAspectRatio: false,
    layout: {
      padding: {
        left: 10,
        right: 25,
        top: 25,
        bottom: 0
      }
    },
    scales: {
      yAxes: [{
        type:"category",
        gridLines: {
          display: false,
          drawBorder: false
        },
        ticks: {
          maxTicksLimit: 10
        }
      }],
      xAxes: [{
        ticks: {
          min: 0,
          max: 20,
          maxTicksLimit: 10,
          padding: 10
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
    },
    legend: {
      display: false
    },
    plugins: {
      datalabels: {
          display: false,
      },
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
          return datasetLabel + ': ' + tooltipItem.xLabel;
        }
      }
    },
  }
};
