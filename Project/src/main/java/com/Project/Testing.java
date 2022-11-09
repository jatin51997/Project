package com.Project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class Testing {
	private static int count = 0;
	private static Connection con = null;

	Mapper mapper = Mapper.getInstance();
	TDG tdg = TDG.getInstance();

	@Test
	void checkStatusCode200() throws Exception {

		ArrayList<String> list = new ArrayList<>();
		list.add("https://cricbuzz-cricket.p.rapidapi.com/teams/v1/international");
		list.add("https://cricbuzz-cricket.p.rapidapi.com/teams/v1/2/players");
		list.add("https://cricbuzz-cricket.p.rapidapi.com/stats/v1/player/" + 1413 + "/batting");
		list.add("https://cricbuzz-cricket.p.rapidapi.com/stats/v1/player/" + 9311 + "/bowling");
		list.add("https://cricbuzz-cricket.p.rapidapi.com/stats/v1/rankings/teams?formatType=test");

		for (String url : list) {
			System.out.println("Testing endpoint: " + url);
			HttpResponse<String> response = getAPIResponse(url);
			System.out.println("Response status Code: " + response.statusCode());
			assertThat(response.statusCode()).isEqualTo(200);
		}

	}

	@Test
	void checkContentTypeJSON() throws Exception {
		ArrayList<String> list = new ArrayList<>();
		list.add("https://cricbuzz-cricket.p.rapidapi.com/teams/v1/international");
		list.add("https://cricbuzz-cricket.p.rapidapi.com/teams/v1/2/players");
		list.add("https://cricbuzz-cricket.p.rapidapi.com/stats/v1/player/" + 1413 + "/batting");
		list.add("https://cricbuzz-cricket.p.rapidapi.com/stats/v1/player/" + 9311 + "/bowling");
		list.add("https://cricbuzz-cricket.p.rapidapi.com/stats/v1/rankings/teams?formatType=test");

		for (String url : list) {
			System.out.println("Testing endpoint: " + url);
			HttpResponse<String> response = getAPIResponse(url);
			java.util.Optional<String> firstValue = response.headers().firstValue("Content-Type");
			String string = firstValue.get();
			System.out.println("Response content type: " + string);
			assertThat(string).startsWith("application/json");
		}

	}

	@Test
	void checkAppropriateJSONkeys() throws Exception {

		ArrayList<String> list = new ArrayList<>();
		list.add("https://cricbuzz-cricket.p.rapidapi.com/teams/v1/international");
		list.add("https://cricbuzz-cricket.p.rapidapi.com/teams/v1/2/players");
		list.add("https://cricbuzz-cricket.p.rapidapi.com/stats/v1/player/" + 1413 + "/batting");
		list.add("https://cricbuzz-cricket.p.rapidapi.com/stats/v1/player/" + 9311 + "/bowling");
		list.add("https://cricbuzz-cricket.p.rapidapi.com/stats/v1/rankings/teams?formatType=test");

		for (String url : list) {
			System.out.println("Testing endpoint: " + url);
			HttpResponse<String> response = getAPIResponse(url);
			String body = response.body();
			System.out.println("Response: " + body);
			if (url.equals("https://cricbuzz-cricket.p.rapidapi.com/teams/v1/international")) {
				assertTrue(body.contains("teamName\":\"India\"") && body.contains("teamName\":\"Australia\""));
			} else if (url.equals("https://cricbuzz-cricket.p.rapidapi.com/teams/v1/2/players")) {
				assertTrue(body.contains("name\":\"BATSMEN\"") && body.contains("name\":\"BOWLER\""));
			} else if (url.equals("https://cricbuzz-cricket.p.rapidapi.com/stats/v1/player/" + 1413 + "batting")) {
				assertTrue(body.contains("Innings") && body.contains("Runs") && body.contains("Highest")
						&& body.contains("SR") && body.contains("Not Out"));
			} else if (url.equals("https://cricbuzz-cricket.p.rapidapi.com/stats/v1/player/" + 9311 + "/bowling")) {
				assertTrue(body.contains("Innings") && body.contains("Runs") && body.contains("Wickets")
						&& body.contains("SR") && body.contains("5w"));
			} else if (url.equals("https://cricbuzz-cricket.p.rapidapi.com/stats/v1/rankings/teams?formatType=test")) {
				assertTrue(body.contains("rank") && body.contains("points"));
			}

		}

	}

	@Test
	void checkDataBaseConnection() throws Exception {
		Connection con = getConnection();
		assertTrue(con != null);
	}

	@Test
	void checkDataBaseQueries() throws Exception {
		mapper.fetchAndStoreAPIData();
		ResultSet rs = null;
		rs = tdg.getDataFromDb("TEAMINFO", "ALL");
		while (rs.next()) {
			String teamname = rs.getString("teamname");
			int teamId = rs.getInt("teamId");
			if (teamId == 2) {
				assertTrue(teamname.equalsIgnoreCase("INDIA"));
			}
		}
		rs = tdg.getDataFromDb("RANKINGS", "TEST");
		while (rs.next()) {
			int formatID = rs.getInt("FORMATID");
			if (formatID == 0 && rs.getInt("TEAMID") == 4) {
				assertTrue(rs.getInt("RANK") == 1);
			}
		}

		rs = tdg.getDataFromDb("BATTINGSTATS", "1413");
		while (rs.next()) {
			int formatID = rs.getInt("FORMATID");
			if (formatID == 0 && rs.getInt("PLAYERID") == 1413) {
				assertTrue(rs.getInt("INNINGS") == 173 && rs.getInt("RUNS") == 8074 && rs.getInt("HIGHEST") == 254
						&& rs.getInt("SR") == 55.69 && rs.getInt("NOTOUTS") == 10);
			}
		}
		rs = tdg.getDataFromDb("BOWLINGSTATS", "10808");
		while (rs.next()) {
			int formatID = rs.getInt("FORMATID");
			if (formatID == 0 && rs.getInt("PLAYERID") == 10808) {
				assertTrue(rs.getInt("INNINGS") == 25 && rs.getInt("RUNS") == 1231 && rs.getInt("WICKETS") == 40
						&& rs.getInt("SR") == 55.75 && rs.getInt("FIVEWICKETS") == 1);
			}
		}

	}

	public static HttpResponse<String> getAPIResponse(String url) {
		try {
			count++;
			if (count == 5) {
				Thread.sleep(2000);
				count = 0;
			}

		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
				.header("X-RapidAPI-Key", "8a3a579825mshd663133c8f17321p10734ajsn3f05a0ccd612")
				.header("X-RapidAPI-Host", "cricbuzz-cricket.p.rapidapi.com")
				.method("GET", HttpRequest.BodyPublishers.noBody()).build();
		HttpResponse<String> response = null;

		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	private static Connection getConnection() throws ClassNotFoundException, SQLException {
		String path = System.getProperty("user.dir") + "/src/main/webapp/project.db";
		path = "/Users/jatin/git/APPProject/APPproject/src/main/webapp/project.db";
		if (con == null) {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" + path);
		}

		return con;

	}

}
