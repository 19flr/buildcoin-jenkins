package com.buildcoin.plugins.jenkins.model;

import com.buildcoin.plugins.jenkins.CauseType;

public class BuildCause {
	
	private CauseType causeType;
	
	private UpstreamBuild upstreamBuild;
	
	private String shortDescription;
	
	private String userName;
	
	public CauseType getCauseType() {
		return causeType;
	}

	public void setCauseType(CauseType causeType) {
		this.causeType = causeType;
	}
	
	public UpstreamBuild getUpstreamBuild() {
		return upstreamBuild;
	}

	public void setUpstreamBuild(UpstreamBuild upstreamBuild) {
		this.upstreamBuild = upstreamBuild;
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
	
}
