package com.Project;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;

import org.json.JSONArray;
import org.json.JSONObject;

public class ManageData {
	private static int count = 0;
	private static String jdbcUrl = "jdbc:sqlite:/Users/jatin/Desktop/workspace/java/APPproject/src/main/webapp/project.db";
	private static Connection con;
	TDG tdg = new TDG();

	public String fetchAndStoreAPIData() {

		String url = "";
		String response = "";
		String query = "";

		try {
			url = "teams/v1/international";
			response = getAPIResponse(url);

			JSONObject objTeams = new JSONObject(response);
			JSONArray arrTeams = objTeams.getJSONArray("list");

			for (int i = 1; i < arrTeams.length(); i++) {
				String teamName = arrTeams.getJSONObject(i).getString("teamName");
				if (teamName.equalsIgnoreCase("Associate Teams")) {
					break;
				}
				int teamId = arrTeams.getJSONObject(i).getInt("teamId");

				TeamInfo team = new TeamInfo(teamId, teamName);

				query = "INSERT INTO TEAMINFO(TEAMID,TEAMNAME) values(" + team.getTeamId() + ",'" + team.getTeamName()
						+ "')";
				tdg.insertDataInDb(query);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "Success";
	}

	public static String getAPIResponse(String url) {
		try {
			count++;
			if (count == 5) {
				Thread.sleep(2000);
				count = 0;
			}

		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://cricbuzz-cricket.p.rapidapi.com/" + url))
				.header("X-RapidAPI-Key", "8a3a579825mshd663133c8f17321p10734ajsn3f05a0ccd612")
				.header("X-RapidAPI-Host", "cricbuzz-cricket.p.rapidapi.com")
				.method("GET", HttpRequest.BodyPublishers.noBody()).build();
		HttpResponse<String> response = null;

		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(response.body());
		return response.body().toString();
	}

}
