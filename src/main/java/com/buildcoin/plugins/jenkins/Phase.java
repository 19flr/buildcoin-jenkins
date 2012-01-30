package com.buildcoin.plugins.jenkins;

import hudson.model.Run;
import hudson.model.TaskListener;

import java.io.IOException;
import java.util.List;

import com.buildcoin.plugins.jenkins.BuildcoinProperty.BuildcoinPropertyDescriptor;
import com.buildcoin.plugins.jenkins.model.Endpoint;


public enum Phase {
	STARTED, COMPLETED, FINISHED;
	@SuppressWarnings({ "rawtypes" })
	public void handlePhase(Run run, String status, TaskListener listener) {
		BuildcoinPropertyDescriptor defaultProperty = new BuildcoinPropertyDescriptor();
		List<Endpoint> targets = defaultProperty.getEndpoints();
		if (defaultProperty.isEnabled()) {
			for (Endpoint target : targets) {
				try {
                    target.getProtocol().sendNotification(target.getUrl(), run.getParent(), run, this, status);
                } catch (IOException e) {
                    e.printStackTrace(listener.error("Failed to notify buildcoin"));
                }
            }
		}
	}
}