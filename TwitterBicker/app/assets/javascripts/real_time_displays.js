// Place all the behaviors and hooks related to the matching controller here.
// All this logic will automatically be available in application.js.

// globals to get rid of warnings
var $;
var Chart; 
var d3;
var Firebase;
// objects we need
var rChart;
var bChart;
var wCloud;
var myFirebaseRef = new Firebase("https://twitterbicker.firebaseio.com/");
var candidates = {
    HILLARY : 0,
    TRUMP : 1,
};
window.onload = function(){
    Chart.defaults.global.tooltips = false;
    // construct all objects on load
    bChart = new barChart();
    rChart = new radarChart();
    wCloud = new wordCloud();
    bChart.trumpChart = bChart.create(candidates.TRUMP);
    bChart.hillaryChart = bChart.create(candidates.HILLARY);
};
// Update the state of the sensor everytime that firebase updates 
myFirebaseRef.child('Hillary').on('value', function(dataSnapshot) {
    var newData = dataSnapshot.val();
    rChart.update(newData, true);
    console.log(dataSnapshot.val());
    
});
myFirebaseRef.child('Trump').on('value', function(dataSnapshot) {
    var newData = dataSnapshot.val();
    rChart.update(newData, false);
    console.log(dataSnapshot.val());
    
});

var wordCloud = function(){
    this.wordCloudDiv = $('#word-map');
    this.data = [{text: "Lorem", weight: 13},
                {text: "Ipsum", weight: 10.5},
                {text: "Dolor", weight: 9.4},
                {text: "Sit", weight: 8},
                {text: "Amet", weight: 6.2},
                {text: "Consectetur", weight: 5},
                {text: "Adipiscing", weight: 5}];
};
wordCloud.prototype.create = function(){
    this.wordCloudDiv.jQCloud(this.data, {
            autoResize: true,
            //shape: 'rectangular',
            width: 1500,
            height: 500
    });
};
wordCloud.prototype.update = function(){
    
};
var radarChart = function(){
    this.hillaryData = [];
    this.trumpData = [];
    this.radarDiv = $("#radar-chart-trump-hillary");
    this.radarChart = null;
};
radarChart.prototype.create = function(){
    console.log(this.hillaryData);
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
radarChart.prototype.update = function(newData, isHillary){
   var tempData = [newData["Emotional Range"], newData.Openness, newData.Conscientiousness, newData.Extraversion, newData.Agreeableness];
   if(isHillary){
    this.hillaryData = tempData;
   }
   else{
       this.trumpData = tempData;
   }
   if(this.radarChart){
        console.log("can update");
        this.radarChart.update();
       
   }
   else{
       console.log("can't update");
   }
    
};
var barChart = function(){
    this.trumpData = 100;
    this.hillaryData = 50;
    this.trumpDiv = $("#ratio-chart-trump");
    this.hillaryDiv = $("#ratio-chart-hillary");
    this.trumpChart = null;
    this.hillaryChart = null;
};
barChart.prototype.create = function(candidate){
    console.log("create");
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
barChart.prototype.update = function(){
//   this.trumpBarChart.updateChart();
//   this.hillaryBarChart.updateChart();
};
function changeTab(option){
    $('#ratio-charts').css('display', 'none');
    $('#radar-chart').css('display', 'none');
    $('#word-map').css('display', 'none');
    
    if (option === "Ratio") {
        $('#ratio-charts').css('display', 'block');
        bChart.hillaryChart = bChart.create(candidates.TRUMP);
        bChart.trumpChart = bChart.create(candidates.HILLARY);
        
    } else if (option === "Radar") {
        $('#radar-chart').css('display', 'block');
        rChart.radarChart = rChart.create();
    } else {
        $('#word-map').fadeIn("slow");
        wCloud.create();
    }
    return false;
};