package com.Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TDG {
	private static TDG tdg;

	private TDG() {

	}

	public static TDG getInstance() {
		if (tdg == null) {
			tdg = new TDG();
		}
		return tdg;
	}

	private static Connection getConnection() throws ClassNotFoundException, SQLException {

		Connection con = null;
		Class.forName("org.sqlite.JDBC");
		con = DriverManager
				.getConnection("jdbc:sqlite:/Users/jatin/Desktop/workspace/java/APPproject/src/main/webapp/project.db");
		return con;

	}

	public ResultSet getDataFromDb(String tablename, String parameter) {
		Connection con = null;
		ResultSet rs = null;
		try {

			String query = "";

			if (tablename.equalsIgnoreCase("TEAMINFO")) {
				if (parameter.equalsIgnoreCase("ALL")) {
					query = "Select * from teaminfo";
				}
			} else if (tablename.equalsIgnoreCase("RANKINGS")) {
				query = "SELECT RANKINGS.TEAMID,RANKINGS.FORMATID,RANKINGS.RANK,RANKINGS.RATING,"
						+ "RANKINGS.POINTS,TEAMINFO.TEAMNAME,FORMATINFO.FORMATNAME FROM RANKINGS INNER JOIN "
						+ "TEAMINFO ON RANKINGS.TEAMID = TEAMINFO.TEAMID INNER JOIN FORMATINFO ON "
						+ "FORMATINFO.FORMATID=RANKINGS.FORMATID   " + "WHERE FORMATINFO.FORMATNAME ='" + parameter
						+ "' ORDER BY RANK";

			} else if (tablename.equalsIgnoreCase("PLAYERINFO")) {
				query = "SELECT PLAYERINFO.PLAYERID,PLAYERINFO.TEAMID,PLAYERINFO.PLAYERNAME,PLAYERINFO.TYPE FROM PLAYERINFO INNER JOIN "
						+ "TEAMINFO ON PLAYERINFO.TEAMID = TEAMINFO.TEAMID WHERE TEAMINFO.TEAMNAME ='" + parameter
						+ "' and (PLAYERINFO.TYPE='BATSMEN' OR PLAYERINFO.TYPE='BOWLER') order by PLAYERINFO.PLAYERNAME";

			} else if (tablename.equalsIgnoreCase("BATTINGSTATS")) {
				query = "SELECT BATTINGSTATS.FORMATID,FORMATINFO.FORMATNAME,BATTINGSTATS.INNINGS,BATTINGSTATS.RUNS,"
						+ "BATTINGSTATS.HIGHEST,BATTINGSTATS.AVERAGE,BATTINGSTATS.SR FROM PLAYERINFO INNER JOIN "
						+ "TEAMINFO ON PLAYERINFO.TEAMID = TEAMINFO.TEAMID INNER JOIN BATTINGSTATS ON "
						+ "BATTINGSTATS.PLAYERID=PLAYERINFO.PLAYERID  INNER JOIN FORMATINFO ON BATTINGSTATS.FORMATID=FORMATINFO.FORMATID "
						+ "WHERE PLAYERINFO.PLAYERID ='" + parameter + "'";
			} else if (tablename.equalsIgnoreCase("BOWLINGSTATS")) {
				query = "SELECT BOWLINGSTATS.FORMATID,FORMATINFO.FORMATNAME,BOWLINGSTATS.INNINGS,BOWLINGSTATS.WICKETS,"
						+ "BOWLINGSTATS.FIVEWICKETS,BOWLINGSTATS.ECONOMY,BOWLINGSTATS.SR FROM PLAYERINFO INNER JOIN "
						+ "TEAMINFO ON PLAYERINFO.TEAMID = TEAMINFO.TEAMID INNER JOIN BOWLINGSTATS ON "
						+ "BOWLINGSTATS.PLAYERID=PLAYERINFO.PLAYERID  INNER JOIN FORMATINFO ON BOWLINGSTATS.FORMATID=FORMATINFO.FORMATID  "
						+ "WHERE PLAYERINFO.PLAYERID ='" + parameter + "'";
			}
			con = this.getConnection();
			Statement stmnt = con.createStatement();
			rs = stmnt.executeQuery(query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	public void insertDataInDb(Object obj) {
		Connection con = null;
		try {
			String query = "";

			if (obj.getClass() == TeamInfo.class) {
				TeamInfo team = (TeamInfo) obj;
				query = "INSERT INTO TEAMINFO(TEAMID,TEAMNAME) values(" + team.getTeamId() + ",'" + team.getTeamName()
						+ "')";
			} else if (obj.getClass() == PlayerInfo.class) {
				PlayerInfo player = (PlayerInfo) obj;
				query = "INSERT INTO PLAYERINFO(PLAYERID,PLAYERNAME,TEAMID,TYPE) values(" + player.getPlayerId() + ", '"
						+ player.getPlayerName() + "'," + "'" + player.getTeam().getTeamId() + "'," + "'"
						+ player.getPlayerType() + "')";
			} else if (obj.getClass() == BattingStats.class) {
				BattingStats stat = (BattingStats) obj;
				query = "INSERT INTO BATTINGSTATS(PLAYERID,INNINGS,RUNS,HIGHEST,AVERAGE,SR,FORMATID) values("
						+ stat.getPlayer().getPlayerId() + ", " + stat.getInnings() + "," + stat.getRuns() + ","
						+ stat.getHighest() + "," + stat.getAverage() + "," + stat.getStrikeRate() + ","
						+ stat.getFormatId() + ")";
			} else if (obj.getClass() == BowlingStats.class) {
				BowlingStats stat = (BowlingStats) obj;
				query = "INSERT INTO BOWLINGSTATS(PLAYERID,INNINGS,WICKETS,FIVEWICKETS,ECONOMY,SR,FORMATID) values("
						+ stat.getPlayer().getPlayerId() + ", " + stat.getInnings() + "," + stat.getWickets() + ","
						+ stat.getFiveWickets() + "," + stat.getEconomy() + "," + stat.getStrikeRate() + ","
						+ stat.getFormatId() + ")";
			} else if (obj.getClass() == Rankings.class) {
				Rankings r = (Rankings) obj;
				query = "INSERT INTO RANKINGS(TEAMID,RANK,RATING,POINTS,FORMATID) values(" + r.getTeam().getTeamId()
						+ "," + r.getRank() + "," + r.getRating() + "," + r.getPoints() + "," + r.getFormatId() + ")";
			}

			con = this.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);
			int records = stmt.executeUpdate();
			System.out.println(records + " records inserted");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
