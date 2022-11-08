<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="style.css"> 
    <script src="myscript.js" defer></script>
    <title>Cricbuzz - Get Stats</title>
</head>
<body>
    <nav class="navbar background">
        <ul class="nav-list">
            <div class="logo"><img src="logo.jpg" alt="logo"></div>
            <li><a href="javascript:manageVisibilityStats();" >Player Stats</a></li>
            <li><a href="javascript:manageVisibilityRankings();" >Team Rankings</a></li>
        </ul>
        <div class="rightnav">
            <input type="text" name="search" id="search">
            <button class="btn btn-sm">Search</button> 
        </div>
    </nav>

    <div id="team">
        <label for="teamName">Select Team </label>
        <select id="teamName">
            <option disabled selected value> -- select an option -- </option>
        </select>
    </div>

    <div id="player">
        </select>
        <label for="playerName">Select player </label>
        <select id="playerName">
           <option disabled selected value> -- select an option -- </option>
        </select>
    </div>
    
    <div id="rankings">
        </select>
        <label for="formatName">Select format </label>
        <select id="formatName">
           <option disabled selected value> -- select an option -- </option>
        </select>
    </div>
    
    <div id="statsTable">
    	<table id="stats" class="stats">
    		<tbody></tbody>
		</table>
    </div>
    

    
</body>
</html>
