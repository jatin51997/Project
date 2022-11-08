package com.Project;

public class BattingStats extends PlayerInfo implements FormatInfo, Average {
	private int innings;
	private int runs;
	private int highest;
	private double average;
	private double strikeRate;
	private PlayerInfo player;
	private int formatId;
	private String formatName;
	private int notOuts;

	BattingStats() {

	}

	BattingStats(PlayerInfo player, int innings, int runs, int highest, double strikeRate, int formatId,
			String formatName, int notOuts) {
		this.player = player;
		this.innings = innings;
		this.runs = runs;
		this.highest = highest;
		this.strikeRate = strikeRate;
		this.formatId = formatId;
		this.formatName = formatName;
		this.notOuts = notOuts;
	}

	public int getNotOuts() {
		return notOuts;
	}

	public void setNotOuts(int notOuts) {
		this.notOuts = notOuts;
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

	public int getRuns() {
		return runs;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public int getHighest() {
		return highest;
	}

	public void setHighest(int highest) {
		this.highest = highest;
	}

	public double getAverage() {
		this.average = (this.runs) / ((this.innings) - (this.notOuts));
		return this.average;
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
		return this.player.toString() + "\n" + "innings: " + this.innings + "\n runs: " + this.runs + "\n highest: "
				+ this.highest + "\n average: " + this.average + "\n strikeRate: " + this.strikeRate + "\n formatId: "
				+ this.formatId + "\n formatName: " + this.formatName;
	}

}
