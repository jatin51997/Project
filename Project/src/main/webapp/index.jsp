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

  
    
    <div id="statsTableTest">
    	<table id="stats" class="stats">
    		<tbody></tbody>
		</table>
    </div>
    

    
</body>
</html>