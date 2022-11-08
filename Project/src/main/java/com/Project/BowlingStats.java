package com.Project;

public class BowlingStats extends PlayerInfo implements FormatInfo, Average {
	private int innings;
	private int wickets;
	private int fiveWickets;
	private double average;
	private double strikeRate;
	private PlayerInfo player;
	private int formatId;
	private String formatName;
	private int runs;

	BowlingStats() {

	}

	BowlingStats(PlayerInfo player, int innings, int wickets, int fiveWickets, double strikeRate, int formatId,
			String formatName, int runs) {
		this.player = player;
		this.innings = innings;
		this.wickets = wickets;
		this.fiveWickets = fiveWickets;
		this.strikeRate = strikeRate;
		this.formatId = formatId;
		this.formatName = formatName;
		this.runs = runs;
	}

	public int getRuns() {
		return runs;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	@Override
	public int getFormatId() {
		return formatId;
	}

	@Override
	public String getFormatName() {
		return formatName;
	}

	@Override
	public void setFormatId(int formatId) {
		this.formatId = formatId;
	}

	@Override
	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}

	public int getInnings() {
		return innings;
	}

	public void setInnings(int innings) {
		this.innings = innings;
	}

	public int getWickets() {
		return wickets;
	}

	public void setWickets(int wickets) {
		this.wickets = wickets;
	}

	public int getFiveWickets() {
		return fiveWickets;
	}

	public void setFiveWickets(int fiveWickets) {
		this.fiveWickets = fiveWickets;
	}

	public double getStrikeRate() {
		return strikeRate;
	}

	public void setStrikeRate(double strikeRate) {
		this.strikeRate = strikeRate;
	}

	public PlayerInfo getPlayer() {
		return player;
	}

	public void setPlayer(PlayerInfo player) {
		this.player = player;
	}

	public String toString() {
		return this.player.toString() + "\n" + "innings: " + this.innings + "\n average: " + this.average
				+ "\n wickets: " + this.wickets + "\n fiveWickets: " + this.fiveWickets + "\n strikeRate: "
				+ this.strikeRate + "\n formatId: " + this.formatId + "\n formatName: " + this.formatName;
	}

	@Override
	public double getAverage() {
		this.average = (this.runs) / (this.wickets);
		return this.average;

	}

}
