package com.buildcoin.plugins.jenkins.model;

import com.buildcoin.plugins.jenkins.CauseType;

public class BuildCause {
	
	private CauseType causeType;
	
	private String shortDescription;
	
	private int upstreamBuildNumber;
	
	private String upstreamProject;
	
	private String userName;
	
	private String upstreamBuildUrl;
	
	public CauseType getCauseType() {
		return causeType;
	}

	public void setCauseType(CauseType causeType) {
		this.causeType = causeType;
	}
	
	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUpstreamProject() {
		return upstreamProject;
	}

	public void setUpstreamProject(String upstreamProject) {
		this.upstreamProject = upstreamProject;
	}
	
	public int getUpstreamBuildNumber() {
		return upstreamBuildNumber;
	}
	
	public void setUpstreamBuildNumber(int upstreamBuildNumber) {
		this.upstreamBuildNumber = upstreamBuildNumber;
	}

	public String getUpstreamBuildUrl() {
		return upstreamBuildUrl;
	}

	public void setUpstreamBuildUrl(String upstreamBuildUrl) {
		this.upstreamBuildUrl = upstreamBuildUrl;
	}
	
}
