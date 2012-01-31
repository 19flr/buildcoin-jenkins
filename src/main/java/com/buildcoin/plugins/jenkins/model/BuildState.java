package com.buildcoin.plugins.jenkins.model;

import com.buildcoin.plugins.jenkins.Phase;

import java.util.List;
import java.util.Map;

public class BuildState {

	private int buildNumber;

	private Phase phase;

	private String status;

	private String buildUrl;
	
	private List<BuildCause> causes;
	
	private List<ScmChange> scmChanges;
	
	private Map<String, String> parameters;

	public int getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}

	public Phase getPhase() {
		return phase;
	}

	public void setPhase(Phase phase) {
		this.phase = phase;
	}
	
	public List<BuildCause> getCauses() {
		return causes;
	}

	public void setCauses(List<BuildCause> causes) {
		this.causes = causes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBuildUrl() {
		return buildUrl;
	}

	public void setBuildUrl(String buildUrl) {
		this.buildUrl = buildUrl;
	}
	
	public List<ScmChange> getScmChanges() {
		return scmChanges;
	}

	public void setScmChanges(List<ScmChange> scmChanges) {
		this.scmChanges = scmChanges;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> params) {
		this.parameters = params;
	}
}
