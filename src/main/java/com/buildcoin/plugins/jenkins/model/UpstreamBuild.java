package com.buildcoin.plugins.jenkins.model;

public class UpstreamBuild {
	
	private String jobName;
	
	private String jobUrl;
	
	private int buildNumber;
	
	private String buildUrl;
	
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
	
	public int getBuildNumber() {
		return buildNumber;
	}
	
	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}

	public String getBuildUrl() {
		return buildUrl;
	}

	public void setBuildUrl(String buildUrl) {
		this.buildUrl = buildUrl;
	}
	
}
