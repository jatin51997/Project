package com.Project;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/servlet")
public class servlet extends HttpServlet {
	TDG tdg = TDG.getInstance();

	ArrayList<PlayerInfo> players;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String id = request.getParameter("id");
		String res = "";
		System.out.println(id);
		if (id.equalsIgnoreCase("teamName")) {
			res = setPlayerNames(request, response);
		} else if (id.equalsIgnoreCase("playerName")) {
			res = setStats(request, response);
		} else if (id.equalsIgnoreCase("formatName")) {
			res = setRankings(request, response);
		} else if (id.equalsIgnoreCase("fetchData")) {
			Mapper mapper = Mapper.getInstance();
			String path = System.getProperty("user.dir") + "/src/main/webapp/config.properties";
			path = "/Users/jatin/git/Project/Project/src/main/webapp/config.properties";
			InputStream input = new FileInputStream(path);
			Properties prop = new Properties();
			prop.load(input);
			OutputStream output = null;

			String getData = prop.getProperty("getData");

			if (getData.equalsIgnoreCase("Y")) {
				res = mapper.fetchAndStoreAPIData();
				if (res.equalsIgnoreCase("success")) {
					output = new FileOutputStream(path);
					prop.setProperty("getData", "N");
					prop.store(output, null);
				}
			} else {
				res = "No need to fetch!";
			}

		}

		response.setContentType("text/plain");

		try {
			response.getWriter().write(res);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String setPlayerNames(HttpServletRequest request, HttpServletResponse response) {
		String teamName = request.getParameter("teamName");
		players = getPlayers(teamName);
		String res = "";
		if (players.size() != 0) {
			for (PlayerInfo player : players) {
				res += "<option value=" + player.getPlayerName().replace(" ", "&nbsp;") + ">" + player.getPlayerName()
						+ "</option>";
			}
		}
		return res;

	}

	public String setStats(HttpServletRequest request, HttpServletResponse response) {
		String playerName = request.getParameter("playerName");
		playerName = playerName.replace("%C2%A0", " ");
		String res = "";
		System.out.println(playerName);
		for (PlayerInfo player : players) {
			if (player.getPlayerName().equalsIgnoreCase(playerName)) {

				String playerType = player.getPlayerType();

				if (playerType.equalsIgnoreCase("BATSMEN")) {
					ArrayList<BattingStats> stats = getBattingStats(player);
					res = "<tr>" + "    <th>Player Name</th>" + "    <th>Format Name</th>" + "    <th>Player Type</th>"
							+ "    <th>Innings</th>" + "    <th>Runs</th>" + "    <th>Average</th>"
							+ "    <th>Highest</th>" + "    <th>StrikeRate</th>" + "  </tr>";
					if (stats.size() != 0) {
						for (BattingStats stat : stats) {
							Context context = new Context(stat);
							res += "<tr>" + "<td>" + stat.getPlayer().getPlayerName() + "</td>" + "<td>"
									+ stat.getFormatName() + "</td>" + "<td>" + stat.getPlayer().getPlayerType()
									+ "</td>" + "<td>" + stat.getInnings() + "</td>" + "<td>" + stat.getRuns() + "</td>"
									+ "<td>" + context.executeAverage() + "</td>" + "<td>" + stat.getHighest() + "</td>"
									+ "<td>" + stat.getStrikeRate() + "</td>" + "</tr>";
						}
					}

				} else if (playerType.equalsIgnoreCase("BOWLER")) {

					ArrayList<BowlingStats> stats = getBowlingStats(player);
					res = "<tr>" + "    <th>Player Name</th>" + "    <th>Format Name</th>" + "    <th>Player Type</th>"
							+ "    <th>Innings</th>" + "    <th>Wickets</th>" + "    <th>Five Wickets</th>"
							+ "    <th>Average</th>" + "    <th>StrikeRate</th>" + "  </tr>";
					if (stats.size() != 0) {
						for (BowlingStats stat : stats) {
							Context context = new Context(stat);
							res += "<tr>" + "<td>" + stat.getPlayer().getPlayerName() + "</td>" + "<td>"
									+ stat.getFormatName() + "</td>" + "<td>" + stat.getPlayer().getPlayerType()
									+ "</td>" + "<td>" + stat.getInnings() + "</td>" + "<td>" + stat.getWickets()
									+ "</td>" + "<td>" + stat.getFiveWickets() + "</td>" + "<td>"
									+ context.executeAverage() + "</td>" + "<td>" + stat.getStrikeRate() + "</td>"
									+ "</tr>";
						}
					}

				}

			}
		}

		return res;
	}

	public String setRankings(HttpServletRequest request, HttpServletResponse response) {
		String formatName = request.getParameter("formatName");
		String res = "";
		System.out.println(formatName);

		ArrayList<Rankings> ranks = getRankings(formatName);
		if (formatName.equalsIgnoreCase("t20")) {
			res = "<tr>" + "    <th>Team Name</th>" + "    <th>Rank</th>" + "    <th>Points</th>" + "  </tr>";
			if (ranks.size() != 0) {
				for (Rankings rank : ranks) {
					res += "<tr>" + "<td>" + rank.getTeam().getTeamName() + "</td>" + "<td>" + rank.getRank() + "</td>"
							+ "<td>" + rank.getPoints() + "</td>" + "</tr>";
				}
			}
		} else {
			res = "<tr>" + "    <th>Team Name</th>" + "    <th>Rank</th>" + "    <th>Rating</th>"
					+ "    <th>Points</th>" + "  </tr>";
			if (ranks.size() != 0) {
				for (Rankings rank : ranks) {
					res += "<tr>" + "<td>" + rank.getTeam().getTeamName() + "</td>" + "<td>" + rank.getRank() + "</td>"
							+ "<td>" + rank.getRating() + "</td>" + "<td>" + rank.getPoints() + "</td>" + "</tr>";
				}
			}
		}

		return res;
	}

	public ArrayList<Rankings> getRankings(String formatName) {
		ArrayList<Rankings> ranks = new ArrayList<>();

		try {
			ResultSet rs = tdg.getDataFromDb("RANKINGS", formatName);
			while (rs.next()) {

				int teamId = rs.getInt("TEAMID");
				String teamName = rs.getString("TEAMNAME");
				TeamInfo team = new TeamInfo(teamId, teamName);

				int formatId = rs.getInt("FORMATID");

				int rank = rs.getInt("RANK");
				int rating = rs.getInt("RATING");
				int points = rs.getInt("POINTS");

				Rankings r = new Rankings(team, rank, rating, points, formatId, formatName);

				ranks.add(r);
				System.out.println(ranks.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ranks;
	}

	public ArrayList<PlayerInfo> getPlayers(String teamName) {
		players = new ArrayList<>();

		try {
			ResultSet rs = tdg.getDataFromDb("PLAYERINFO", teamName);
			while (rs.next()) {
				String playerName = rs.getString("PLAYERNAME");
				int playerID = rs.getInt("PLAYERID");
				int teamId = rs.getInt("TEAMID");
				String type = rs.getString("TYPE");

				TeamInfo team = new TeamInfo(teamId, teamName);
				PlayerInfo player = new PlayerInfo(team, playerID, playerName, type);

				players.add(player);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return players;
	}

	public ArrayList<BattingStats> getBattingStats(PlayerInfo player) {
		ArrayList<BattingStats> stats = new ArrayList<>();

		try {
			ResultSet rs = tdg.getDataFromDb("BATTINGSTATS", player.getPlayerId() + "");
			while (rs.next()) {

				int formatId = rs.getInt("FORMATID");
				String formatName = rs.getString("FORMATNAME");
				int innings = rs.getInt("INNINGS");
				int runs = rs.getInt("RUNS");
				int highest = rs.getInt("HIGHEST");
				int sr = rs.getInt("SR");
				int notOuts = rs.getInt("NOTOUTS");
				BattingStats stat = new BattingStats(player, innings, runs, highest, sr, formatId, formatName, notOuts);

				stats.add(stat);
				System.out.println(stats.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stats;
	}

	public ArrayList<BowlingStats> getBowlingStats(PlayerInfo player) {
		ArrayList<BowlingStats> stats = new ArrayList<>();

		try {
			ResultSet rs = tdg.getDataFromDb("BOWLINGSTATS", player.getPlayerId() + "");
			while (rs.next()) {

				int formatId = rs.getInt("FORMATID");
				String formatName = rs.getString("FORMATNAME");
				int innings = rs.getInt("INNINGS");
				int wickets = rs.getInt("WICKETS");
				int fiveWickets = rs.getInt("FIVEWICKETS");
				int sr = rs.getInt("SR");
				int runs = rs.getInt("RUNS");
				BowlingStats stat = new BowlingStats(player, innings, wickets, fiveWickets, sr, formatId, formatName,
						runs);

				stats.add(stat);
				System.out.println(stats.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stats;
	}

}
