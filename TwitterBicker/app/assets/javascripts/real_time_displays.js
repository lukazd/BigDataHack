// Place all the behaviors and hooks related to the matching controller here.
// All this logic will automatically be available in application.js.
var Chart = require('src/chart.js');
// document.onLoad = function(){

    
// }
var barChart = function(){
    this.trumpBarChart = $("#ratio-chart-trump");
    this.hillaryBarChart = $("#ratio-chart-hillary");
};

barChart.prototype.createCharts = function(){
    var trumpChart = new Chart(this.trumpBarChart, {
    type: 'bar',
    data: {
        labels: ["Red"],
        datasets: [{
            label: '# of mean tweets',
            data: [50],
            backgroundColor: [
                'rgba(255, 0, 0, 1)',
            ],
            borderColor: [
                'rgba(255,0,0,1)',
            ],
            borderWidth: 1
        }]
    },
    options: {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero:true
                }
            }]
        }
    }});
}

barChart.prototype.updateChart = function(){
   
}
