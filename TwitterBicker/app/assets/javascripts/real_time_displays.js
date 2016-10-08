// Place all the behaviors and hooks related to the matching controller here.
// All this logic will automatically be available in application.js.

// globals to get rid of warnings
var $;
var Chart; 
var d3;

window.onload = function(){
    Chart.defaults.global.tooltips = false;
    var meanTweetChart = new barChart();
    var toneAnalyzerChart = new radarChart();
    var wordCloudTest = new wordCloud();
};
var candidates = {
    HILLARY : 0,
    TRUMP : 1,
};
var wordCloud = function(){
    console.log("word cloud");
   var words = [{text: "Lorem", weight: 13},
  {text: "Ipsum", weight: 10.5},
  {text: "Dolor", weight: 9.4},
  {text: "Sit", weight: 8},
  {text: "Amet", weight: 6.2},
  {text: "Consectetur", weight: 5},
  {text: "Adipiscing", weight: 5}];
    $('#word-map').jQCloud(words);
};
var radarChart = function(){
    this.radarDiv = $("#radar-chart-trump-hillary");
    this.radarChart = this.createRadarChart();
};
radarChart.prototype.createRadarChart = function(){
    var data = {
        labels: ["Emotional Range", "Openness", "Conscientiousness", "Extraversion", "Agreeableness"],
        datasets: [
            {
                label: "Hillary",
                backgroundColor: "rgba(0,0,255,0.2)",
                borderColor: "rgba(179,181,198,1)",
                pointBackgroundColor: "rgba(179,181,198,1)",
                pointBorderColor: "#fff",
                pointHoverBackgroundColor: "#fff",
                pointHoverBorderColor: "rgba(179,181,198,1)",
                data: [65, 59, 90, 81, 56]
            },
            {
                label: "Trump",
                backgroundColor: "rgba(255,99,132,0.2)",
                borderColor: "rgba(255,99,132,1)",
                pointBackgroundColor: "rgba(255,99,132,1)",
                pointBorderColor: "#fff",
                pointHoverBackgroundColor: "#fff",
                pointHoverBorderColor: "rgba(255,99,132,1)",
                data: [28, 48, 40, 19, 96]
            }
        ]
    };
    var myRadarChart = new Chart(this.radarDiv, {
        type: 'radar',
        data:data
    });
    return myRadarChart;
};

function changeTab(option){
    $('#ratio-charts').css('display', 'none');
    $('#radar-chart').css('display', 'none');
    
    if (option === "Ratio") {
        $('#ratio-charts').css('display', 'block');
    } else {
        $('#radar-chart').css('display', 'block');
    }
    
    return false;
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
        datasets: [{
            label: data,
            data: [data],
            backgroundColor: [color],
        }]
    },
    options: {
        legend: {display:false, fontSize:40},
        //labels: {fontSize:20},
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
barChart.prototype.updateChart = function(){
   this.trumpBarChart.updateChart();
   this.hillaryBarChart.updateChart();
};