// Place all the behaviors and hooks related to the matching controller here.
// All this logic will automatically be available in application.js.

// globals to get rid of warnings
var $;
var Chart; 
var d3;

window.onload = function(){
    Chart.defaults.global.tooltips = false;
    new barChart();
};
var candidates = {
    HILLARY : 0,
    TRUMP : 1,
};
var wordCloud = function(){
    this.wordMapDiv = $("#word-map");
    this.words =  [{text: "Lorem", weight: 13},
                {text: "Ipsum", weight: 10.5},
                {text: "Dolor", weight: 9.4},
                {text: "Sit", weight: 8},
                {text: "Amet", weight: 6.2},
                {text: "Consectetur", weight: 5},
                {text: "Adipiscing", weight: 5}];
    this.create();
};
wordCloud.prototype.create = function(){
     this.wordMapDiv.jQCloud(this.words, {
            autoResize: true,
            //shape: 'rectangular',
           width: 1500,
           height: 500
    });
};
wordCloud.prototype.update = function(){
    
};
var radarChart = function(){
    this.hillaryData = [65, 59, 90, 81, 56];
    this.trumpData = [28, 48, 40, 19, 96];
    this.radarDiv = $("#radar-chart-trump-hillary");
    this.radarChart = this.create();
};
radarChart.prototype.create = function(){
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
                data: this.hillaryData
            },
            {
                label: "Trump",
                backgroundColor: "rgba(255,99,132,0.2)",
                borderColor: "rgba(255,99,132,1)",
                pointBackgroundColor: "rgba(255,99,132,1)",
                pointBorderColor: "#fff",
                pointHoverBackgroundColor: "#fff",
                pointHoverBorderColor: "rgba(255,99,132,1)",
                data: this.trumpData
            }
        ]
    };
    var myRadarChart = new Chart(this.radarDiv, {
        type: 'radar',
        data:data
    });
    return myRadarChart;
};
var barChart = function(){
    this.hillaryData = 50;
    this.trumpData = 100;
    this.trumpDiv = $("#ratio-chart-trump");
    this.hillaryDiv = $("#ratio-chart-hillary");
    // create the chart
    this.trumpBarChart = this.create(candidates.TRUMP);
    this.hillaryBarChart = this.create(candidates.HILLARY);
};
barChart.prototype.create = function(candidate){
    // getting rid of the top legend
    Chart.defaults.global.defaultFontSize = 20;
    var maxData = 100;
    // these will be different for each candidate
    var candidateEl, label, color, data;
    if(candidate == 0){
        candidateEl = this.hillaryDiv;
        color = 'rgba(0, 0, 255, 1)';
        data = this.hillaryData;
    }
    else{
        candidateEl = this.trumpDiv;
        color = 'rgba(255, 0, 0, 1)';
        data = this.trumpData;
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
barChart.prototype.update = function(){
   this.trumpBarChart.updateChart();
   this.hillaryBarChart.updateChart();
};
barChart.prototype.update = function(){
   this.trumpBarChart.updateChart();
   this.hillaryBarChart.updateChart();
};
function changeTab(option){
    $('#ratio-charts').css('display', 'none');
    $('#radar-chart').css('display', 'none');
    $('#word-map').css('display', 'none');
    
    if (option === "Ratio") {
        $('#ratio-charts').css('display', 'block');
        new barChart();
        
    } else if (option === "Radar") {
        $('#radar-chart').css('display', 'block');
        new radarChart();
    } else {
        $('#word-map').fadeIn("slow");
        new wordCloud();
    }
    
    return false;
};