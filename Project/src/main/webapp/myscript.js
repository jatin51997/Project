var imported = document.createElement('script');
imported.src = 'jquery.min.js';
document.head.appendChild(imported);

var teams = document.getElementById("teamName");
var players = document.getElementById("playerName");
var formats = document.getElementById("formatName");

var teamName = ["India" , "Australia"];
var formatName = ["TEST" , "ODI","T20"];


makePostCall = function (url, orgdata) { // here the data and url are not hardcoded anymore
	    return $.ajax({
			type: "POST",
			url : url,
			data : orgdata,
			success : function(responseText) {
				console.log("yes");
			},
			fail:function(){
				alert("Some error occured!");
			}
		});
	}

teams.onchange = function(e){
	document.getElementById("player").style.visibility = "visible";
	document.getElementById("statsTable").style.visibility = "hidden";
    removeOptions(players);
    var value = e.target.value;

makePostCall("servlet?id=teamName", {teamName : value})
    .success(function(data){
	console.log(data);
		var disabledOption ="<option disabled selected value> -- select an option -- </option>";
		data = disabledOption+data;
    	document.getElementById("playerName").innerHTML=data;
   })
    .fail(function(sender, message, details){
           alert("Sorry, something went wrong!");
  });
 
}

players.onchange = function(e){
document.getElementById("statsTable").style.visibility = "visible";
var value = e.target.value;

makePostCall("servlet?id=playerName", {playerName : encodeURIComponent(value)})
    .success(function(data){
	console.log(data);
    	document.getElementById("stats").innerHTML=data;
   })
    .fail(function(sender, message, details){
           alert("Sorry, something went wrong!");
  });
 
}


formats.onchange = function(e){
document.getElementById("statsTable").style.visibility = "visible";
var value = e.target.value;

makePostCall("servlet?id=formatName", {formatName : encodeURIComponent(value)})
    .success(function(data){
	console.log(data);
    	document.getElementById("stats").innerHTML=data;
   })
    .fail(function(sender, message, details){
           alert("Sorry, something went wrong!");
  });
 
}

window.onload = function onLoadFun() {
		document.getElementById("player").style.visibility = "hidden";
		document.getElementById("rankings").style.visibility = "hidden";
    for(var i =0 ; i < teamName.length ; i++){
        var option = teamName[i];
        
        var el = document.createElement("option");
        el.textContent = option; //value displayed on webpage
        el.value = option;
       
        teams.appendChild(el);
    }
    
    for(var i =0 ; i < formatName.length ; i++){
        var option = formatName[i];
        
        var el = document.createElement("option");
        el.textContent = option; //value displayed on webpage
        el.value = option;
       
        formats.appendChild(el);
    }
     $("#loading").show();
    makePostCall("servlet?id=fetchData", "")
    .success(function(data){
	console.log(data);
	 $("#loading").hide();
   })
    .fail(function(sender, message, details){
           alert("Sorry, something went wrong!");
  });
    
}


function removeOptions(selectElement) {
    var i, L = selectElement.options.length - 1;
    for(i = L; i >= 0; i--) {
       selectElement.remove(i);
    }
 }
 
 function manageVisibilityStats() {
    	
		document.getElementById("team").style.visibility = "visible";
		document.getElementById("player").style.visibility = "hidden";
		document.getElementById("rankings").style.visibility = "hidden";
		document.getElementById("statsTable").style.visibility = "hidden";
		
		document.getElementById("formatName").value='';
	
 }
 
  function manageVisibilityRankings() {
    
		document.getElementById("team").style.visibility = "hidden";
		document.getElementById("player").style.visibility = "hidden";
		document.getElementById("rankings").style.visibility = "visible";
		document.getElementById("statsTable").style.visibility = "hidden";
	
	document.getElementById("teamName").value='';
 }