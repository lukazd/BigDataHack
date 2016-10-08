// Place all the behaviors and hooks related to the matching controller here.
// All this logic will automatically be available in application.js.

// globals to get rid of warnings
var $;
var Chart; 

window.onload = function(){
    var meanTweetChart = new barChart();
};
var barChart = function(){
    // properties
    this.candidates = {
        HILLARY : 0,
        TRUMP : 1,
    }
    this.trumpDiv = $("#ratio-chart-trump");
    this.hillaryDiv = $("#ratio-chart-hillary");
    // create the chart
    this.trumpBarChart = this.createChart(this.candidates.TRUMP);
    this.hillaryBarChart = this.createChart(this.candidates.HILLARY);
};
barChart.prototype.createChart = function(candidate){
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
    var chart = new Chart(candidateEl, {
    type: 'bar',
    data: {
        label: '# of mean tweets',
        labels: [data],
        datasets: [{
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
    return chart;
};
barChart.prototype.updateChart = function(){
   
}
