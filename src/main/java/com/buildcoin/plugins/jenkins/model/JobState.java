package com.buildcoin.plugins.jenkins.model;

public class JobState {

	private String jobName;

	private String jobUrl;

	private BuildState build;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobUrl() {
		return jobUrl;
	}

	public void setJobUrl(String jobUrl) {
		this.jobUrl = jobUrl;
	}

	public BuildState getBuild() {
		return build;
	}

	public void setBuild(BuildState build) {
		this.build = build;
	}
}
