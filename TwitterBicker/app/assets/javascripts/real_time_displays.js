// Place all the behaviors and hooks related to the matching controller here.
// All this logic will automatically be available in application.js.

// globals to get rid of warnings
var $;
var Chart; 

window.onload = function(){
    var meanTweetChart = new barChart();
};
var candidates = {
    HILLARY : 0,
    TRUMP : 1,
};
var radarChart = function(){
    
};
radarChart.prototype.createRadarChart = function(){
//     var myRadarChart = new Chart(ctx, {
//     type: 'radar',
//     data: data,
//     options: options
// });
};
var barChart = function(){
   
    this.trumpDiv = $("#ratio-chart-trump");
    this.hillaryDiv = $("#ratio-chart-hillary");
    // create the chart
    this.trumpBarChart = this.createBarChart(candidates.TRUMP);
    this.hillaryBarChart = this.createBarChart(candidates.HILLARY);
};
barChart.prototype.createBarChart = function(candidate){
    // getting rid of the top legend
    Chart.defaults.global.legend.display = false;
    Chart.defaults.global.tooltips = false;
    Chart.defaults.global.defaultFontSize = 20;
    var maxData = 100;
    // these will be different for each candidate
    var candidateEl, label, color, data;
    if(candidate == 0){
        candidateEl = this.hillaryDiv;
        color = 'rgba(0, 0, 255, 1)';
        data = 20;
    }
    else{
        candidateEl = this.trumpDiv;
        color = 'rgba(255, 0, 0, 1)';
        data = 100;
    }
    var barChart = new Chart(candidateEl, {
    type: 'bar',
    data: {
        labels: [data],
        fontSize:20,
        datasets: [{
            label: data,
            data: [data],
            backgroundColor: [color],
        }]
    },
    options: {
        scales: {
            yAxes: [{
                display:false,
                ticks: {
                    beginAtZero:true,
                    max:maxData
                }
            }],
            xAxes: [{
                gridLines : {
                    display : false
                },
                
            }]
        }
    }});
    return barChart;
};