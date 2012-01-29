package com.buildcoin.plugins.jenkins.model;

import com.buildcoin.plugins.jenkins.Protocol;

public class Endpoint {

	private Protocol protocol;

	private String url;

	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

    @Override
    public String toString() {
        return protocol+":"+url;
    }
}