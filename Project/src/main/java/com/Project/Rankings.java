package com.Project;

public class Rankings extends TeamInfo implements FormatInfo {
	private int rank;
	private int rating;
	private int points;
	private int formatId;
	private String formatName;
	private TeamInfo team;

	Rankings() {

	}

	Rankings(TeamInfo team, int rank, int rating, int points, int formatId, String formatName) {
		this.team = team;
		this.rank = rank;
		this.rating = rating;
		this.points = points;
		this.formatId = formatId;
		this.formatName = formatName;
	}

	@Override
	public int getFormatId() {
		return formatId;
	}

	@Override
	public void setFormatId(int formatId) {
		this.formatId = formatId;
	}

	@Override
	public String getFormatName() {
		return formatName;
	}

	@Override
	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public TeamInfo getTeam() {
		return team;
	}

	public void setTeam(TeamInfo team) {
		this.team = team;
	}

	public String toString() {
		return this.team.toString() + "\n" + "rank: " + this.rank + "\n rating: " + this.rating + "\n points: "
				+ this.points + "\n formatId: " + this.formatId + "\n formatName: " + this.formatName;
	}

	public boolean equals(TeamInfo team, int formatId) {
		if (this.team.getTeamId() == team.getTeamId() && this.formatId == formatId) {
			return true;
		}
		return false;
	}
}
