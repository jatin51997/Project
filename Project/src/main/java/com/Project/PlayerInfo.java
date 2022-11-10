package com.Project;

public class PlayerInfo {

	public static void main(String[] args) {
		System.out.println("Working Directory = " + System.getProperty("user.dir"));

	}

	private int playerId;
	private String playerName;
	private String playerType;
	private TeamInfo team;

	PlayerInfo() {

	}

	PlayerInfo(TeamInfo team, int playerId, String playerName, String playerType) {
		this.team = team;
		this.playerId = playerId;
		this.playerName = playerName;
		this.playerType = playerType;
	}

	public TeamInfo getTeam() {
		return team;
	}

	public void setTeam(TeamInfo team) {
		this.team = team;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerType() {
		return playerType;
	}

	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}

	public String toString() {
		return this.team.toString() + "\n" + "playerId: " + this.playerId + "\n playerName: " + this.playerName
				+ "\n playerType: " + this.playerType;
	}

	public boolean equals(int playerId) {
		if (this.playerId == playerId) {
			return true;
		}
		return false;
	}

}
