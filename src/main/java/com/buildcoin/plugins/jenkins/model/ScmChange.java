package com.buildcoin.plugins.jenkins.model;

public class ScmChange {
	
	private String scmAuthor;
	
	private String scmMessage;
	
	private String scmRevision;
	
	public String getScmAuthor() {
		return scmAuthor;
	}

	public void setScmAuthor(String scmAuthor) {
		this.scmAuthor = scmAuthor;
	}
	
	public String getScmMessage() {
		return scmMessage;
	}

	public void setScmMessage(String scmMessage) {
		this.scmMessage = scmMessage;
	}
	
	public String getScmRevision() {
		return scmRevision;
	}

	public void setScmRevision(String scmMessage) {
		this.scmRevision = scmMessage;
	}
	
}
