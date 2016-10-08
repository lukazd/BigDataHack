// Place all the behaviors and hooks related to the matching controller here.
// All this logic will automatically be available in application.js.

// globals to get rid of warnings
var $;
var Chart; 
window.onload = function(){
    var meanTweetChart = new barChart();
};
var barChart = function(){
    this.candidates = {
        HILLARY : 0,
        TRUMP : 1,
    }
    this.trumpBarChart = $("#ratio-chart-trump");
    this.hillaryBarChart = $("#ratio-chart-hillary");
    // create the chart
    this.createChart(this.candidates.TRUMP);
    this.createChart(this.candidates.HILLARY);
};
barChart.prototype.createChart = function(candidate){
    // these will be different for each candidate
    var candidateEl, label, color;
    if(candidate == 0){
        candidateEl = this.hillaryBarChart;
        label = 'Blue';
        color = 'rgba(0, 0, 255, 1)';
    }
    else{
        candidateEl = this.trumpBarChart;
        label = 'Red';
        color = 'rgba(255, 0, 0, 1)';
    }
    var chart = new Chart(candidateEl, {
    type: 'bar',
    data: {
        labels: [label],
        datasets: [{
            label: '# of mean tweets',
            data: [50],
            backgroundColor: [
                color
            ],
            borderColor: [
                color
            ],
            borderWidth: 0
        }]
    },
    options: {
        scales: {
            yAxes: [{
                display: false,
                ticks: {
                    beginAtZero:true
                }
            }]
        }
    }
    });
};
barChart.prototype.updateChart = function(){
   
}
