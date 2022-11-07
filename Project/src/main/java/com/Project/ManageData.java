package com.Project;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class ManageData {
	private static int count = 0;
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
				tdg.insertDataInDb(team);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			ResultSet result = tdg.getDataFromDb("TEAMINFO", "ALL");

			while (result.next()) {
				String teamname = result.getString("teamname");
				int teamId = result.getInt("teamId");
				if (teamId == 2 || teamId == 4) {

					url = "teams/v1/" + teamId + "/players";
					response = getAPIResponse(url);

					JSONObject objPlayers = new JSONObject(response);
					JSONArray arrPlayers = objPlayers.getJSONArray("player");

					String type = "";
					for (int i = 0; i < arrPlayers.length(); i++) {

						String name = arrPlayers.getJSONObject(i).getString("name");

						if (name.equalsIgnoreCase("BATSMEN")) {
							type = name;
						} else if (name.equalsIgnoreCase("BOWLER")) {
							type = name;
						} else if (name.equalsIgnoreCase("WICKET KEEPER")) {
							type = name;
						} else if (name.equalsIgnoreCase("ALL ROUNDER")) {
							type = name;
						} else {

							int playerId = arrPlayers.getJSONObject(i).getInt("id");

							TeamInfo team = new TeamInfo(teamId, teamname);

							PlayerInfo player = new PlayerInfo(team, playerId, name, type);

							tdg.insertDataInDb(player);

							if (type.equalsIgnoreCase("BATSMEN")) {

								url = "stats/v1/player/" + playerId + "/batting";
								response = getAPIResponse(url);

								JSONObject objBatsmen = new JSONObject(response);
								JSONArray arrBatsmen = objBatsmen.getJSONArray("values");

								ArrayList<String> toInsertTest = new ArrayList<String>();
								ArrayList<String> toInsertOdi = new ArrayList<String>();
								ArrayList<String> toInsertT20 = new ArrayList<String>();

								for (int j = 0; j < arrBatsmen.length(); j++) {
									Iterator<String> keys = arrBatsmen.getJSONObject(j).keys();
									while (keys.hasNext()) {
										String key = keys.next();
										Object input = arrBatsmen.getJSONObject(j).get(key);
										String input1 = input.toString();
										input1 = input1.replaceAll("[\\[\\]\"]", "");
										ArrayList<String> parts = new ArrayList<>(Arrays.asList(input1.split(",")));
										String val = parts.get(0);
										if (val.equalsIgnoreCase("Innings") || val.equalsIgnoreCase("Runs")
												|| val.equalsIgnoreCase("Average") || val.equalsIgnoreCase("Highest")
												|| val.equalsIgnoreCase("SR")) {
											toInsertTest.add(parts.get(1));
											toInsertOdi.add(parts.get(2));
											toInsertT20.add(parts.get(3));
										}
									}

								}

								BattingStats testStat = new BattingStats(player, Integer.parseInt(toInsertTest.get(0)),
										Integer.parseInt(toInsertTest.get(1)), Integer.parseInt(toInsertTest.get(2)),
										Double.parseDouble(toInsertTest.get(3)),
										Double.parseDouble(toInsertTest.get(4)), 0, "TEST");
								tdg.insertDataInDb(testStat);

								BattingStats odiStat = new BattingStats(player, Integer.parseInt(toInsertOdi.get(0)),
										Integer.parseInt(toInsertOdi.get(1)), Integer.parseInt(toInsertOdi.get(2)),
										Double.parseDouble(toInsertOdi.get(3)), Double.parseDouble(toInsertOdi.get(4)),
										1, "ODI");
								tdg.insertDataInDb(odiStat);

								BattingStats t20Stat = new BattingStats(player, Integer.parseInt(toInsertT20.get(0)),
										Integer.parseInt(toInsertT20.get(1)), Integer.parseInt(toInsertT20.get(2)),
										Double.parseDouble(toInsertT20.get(3)), Double.parseDouble(toInsertT20.get(4)),
										2, "T20");
								tdg.insertDataInDb(t20Stat);

							} else if (type.equalsIgnoreCase("BOWLER")) {
								url = "stats/v1/player/" + playerId + "/bowling";
								response = getAPIResponse(url);

								JSONObject objBowler = new JSONObject(response);
								JSONArray arrBowler = objBowler.getJSONArray("values");

								ArrayList<String> toInsertTest = new ArrayList<String>();
								ArrayList<String> toInsertOdi = new ArrayList<String>();
								ArrayList<String> toInsertT20 = new ArrayList<String>();

								for (int i1 = 0; i1 < arrBowler.length(); i1++) {
									Iterator<String> keys = arrBowler.getJSONObject(i1).keys();
									while (keys.hasNext()) {
										String key = keys.next();
										Object input = arrBowler.getJSONObject(i1).get(key);
										String input1 = input.toString();
										input1 = input1.replaceAll("[\\[\\]\"]", "");
										ArrayList<String> parts = new ArrayList<>(Arrays.asList(input1.split(",")));
										String val = parts.get(0);
										if (val.equalsIgnoreCase("Innings") || val.equalsIgnoreCase("Wickets")
												|| val.equalsIgnoreCase("Eco") || val.equalsIgnoreCase("SR")
												|| val.equalsIgnoreCase("5w")) {
											toInsertTest.add(parts.get(1));
											toInsertOdi.add(parts.get(2));
											toInsertT20.add(parts.get(3));
										}
									}

								}

								BowlingStats testStat = new BowlingStats(player, Integer.parseInt(toInsertTest.get(0)),
										Integer.parseInt(toInsertTest.get(1)), Integer.parseInt(toInsertTest.get(4)),
										Double.parseDouble(toInsertTest.get(2)),
										Double.parseDouble(toInsertTest.get(3)), 0, "TEST");
								tdg.insertDataInDb(testStat);

								BowlingStats odiStat = new BowlingStats(player, Integer.parseInt(toInsertOdi.get(0)),
										Integer.parseInt(toInsertOdi.get(1)), Integer.parseInt(toInsertOdi.get(4)),
										Double.parseDouble(toInsertOdi.get(2)), Double.parseDouble(toInsertOdi.get(3)),
										1, "ODI");
								tdg.insertDataInDb(odiStat);

								BowlingStats t20Stat = new BowlingStats(player, Integer.parseInt(toInsertT20.get(0)),
										Integer.parseInt(toInsertT20.get(1)), Integer.parseInt(toInsertT20.get(4)),
										Double.parseDouble(toInsertT20.get(2)), Double.parseDouble(toInsertT20.get(3)),
										2, "T20");
								tdg.insertDataInDb(t20Stat);

							}

						}

					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			if (con != null) {
				con.close();
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
