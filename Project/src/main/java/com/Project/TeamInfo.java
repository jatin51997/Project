package com.Project;

public class TeamInfo {
	private int teamId;
	private String teamName;

	TeamInfo() {

	}

	TeamInfo(int teamId, String teamName) {
		this.teamId = teamId;
		this.teamName = teamName;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String toString() {
		return "TeamId: " + this.teamId + "\n TeamName: " + this.teamName;
	}

	public boolean equals(int teamId) {
		if (this.teamId == teamId) {
			return true;
		}
		return false;
	}

}
