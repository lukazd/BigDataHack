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
//var asset_path_trump;
//var asset_path_hillary;

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
};
// Update the state of the sensor everytime that firebase updates 
myFirebaseRef.child('Hillary').on('value', function(dataSnapshot) {
    var newData = dataSnapshot.val();
    rChart.update(newData, true);
    
});
myFirebaseRef.child('Trump').on('value', function(dataSnapshot) {
    var newData = dataSnapshot.val();
    rChart.update(newData, false);
    console.log(dataSnapshot.val());
    
});
myFirebaseRef.child("AngerScore").on('value', function(dataSnapshot){
    var newData = dataSnapshot.val();
    bChart.update(newData);
    var trumpImage = $("#trump-img");
    var hillaryImage = $("#hillary-img");
    
    if(newData.Hillary >= 80){
        trumpImage.attr('src', asset_path_trump[3]);
    }
    else if(newData.Hillary >= 70){
     trumpImage.attr('src', asset_path_trump[2]);
    }
    else if(newData.Hillary >= 60){
        trumpImage.attr('src', asset_path_trump[1]);
    }
    else{
        trumpImage.attr('src', asset_path_trump[0]);
    }
    if(newData.Trump >= 80){
        hillaryImage.attr('src', asset_path_hillary[3]);
    }
    else if(newData.Trump >= 70){
     hillaryImage.attr('src', asset_path_hillary[2]);
    }
    else if(newData.Trump >= 60){
        hillaryImage.attr('src', asset_path_hillary[1]);
    }
    else{
        hillaryImage.attr('src', asset_path_hillary[0]);
    }
    
});
myFirebaseRef.child("WordFrequency").on('value', function(dataSnapshot){
    console.log("word freq: ");
    console.log(dataSnapshot.val());
    wCloud.update(dataSnapshot.val());
    
    
});
myFirebaseRef.child('AngriestTweet').on('value', function(dataSnapshot) {
    var newData = dataSnapshot.val();
    $('#tweet').text(newData["Tweet"]);
    $('#user').text(newData["User"]);
});

var wordCloud = function(){
    this.created = false;
    this.wordCloudDiv = $('#word-map');
    this.data = [];
};
wordCloud.prototype.create = function(){
    this.created = true;
    this.wordCloudDiv.jQCloud(this.data, {
            autoResize: true,
            //shape: 'rectangular',
            width: 1500,
            height: 500
    });
};
wordCloud.prototype.update = function(newData){
    for(var key in newData){
        if(newData.hasOwnProperty(key)){
            console.log(key + " -> " + newData[key]);
            var json = {text: key, weight: parseInt(newData[key])};
            this.data.push(json);
        }
    }
    if(this.created){
        this.wordCloudDiv.jQCloud('update', this.data);
    }
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
                borderColor: "rgba(0,0,255,1)",
                pointBackgroundColor: "rgba(179,181,198,1)",
                pointBorderColor: "#fff",
                pointHoverBackgroundColor: "#fff",
                pointHoverBorderColor: "rgba(179,181,198,1)",
                data: this.hillaryData
            },
            {
                label: "Trump",
                backgroundColor: "rgba(255,0,0,0.2)",
                borderColor: "rgba(255,0,0,1)",
                pointBackgroundColor: "rgba(255,0,0,1)",
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
   if(isHillary){
        this.hillaryData[0] = newData["Emotional Range"];
        this.hillaryData[1] = newData["Openness"];
        this.hillaryData[2] = newData["Conscientiousness"];
        this.hillaryData[3] = newData["Extraversion"];
        this.hillaryData[4] = newData["Agreeableness"];
    }
   else{
        this.trumpData[0] = newData["Emotional Range"];
        this.trumpData[1] = newData["Openness"];
        this.trumpData[2] = newData["Conscientiousness"];
        this.trumpData[3] = newData["Extraversion"];
        this.trumpData[4] = newData["Agreeableness"];
   }
   if(this.radarChart){
        this.radarChart.update();
    }
};
var barChart = function(){
    this.trumpData = [];
    this.hillaryData = [];
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
            data: data,
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
barChart.prototype.update = function(newData){
    this.trumpData[0] = newData.Trump;
    this.hillaryData[0] = newData.Hillary;
    if(this.trumpChart && this.hillaryChart){
        this.trumpChart.update();
        this.hillaryChart.update();
    }
    else{
    this.trumpChart = bChart.create(candidates.TRUMP);
    this.hillaryChart = bChart.create(candidates.HILLARY);
    }
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