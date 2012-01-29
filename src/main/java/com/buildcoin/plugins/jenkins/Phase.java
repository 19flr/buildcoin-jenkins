package com.buildcoin.plugins.jenkins;

import hudson.model.Run;
import hudson.model.TaskListener;
import com.buildcoin.plugins.jenkins.model.Endpoint;

import java.io.IOException;
import java.util.List;


public enum Phase {
	STARTED, COMPLETED, FINISHED;

	@SuppressWarnings({ "rawtypes" })
	public void handlePhase(Run run, String status, TaskListener listener) {
			List<Endpoint> endpoints = BuildcoinPropertyDescriptor.endpoints;
			if (!endpoints.isEmpty()) {
				for (Endpoint endpoint : endpoints) {
	                try {
	                	endpoint.getProtocol().sendNotification(endpoint.getUrl(), run.getParent(), run, this, status);
	                } catch (IOException e) {
	                    e.printStackTrace(listener.error("Failed to notify "+endpoint));
	                }
	            }
			}
	}
}