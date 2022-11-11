package com.Project;

public class Context {
	private Average average;

	public Context(Average average) {
		this.average = average;
	}

	public double executeAverage() {
		return average.getAverage();
	}
}
