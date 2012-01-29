package com.buildcoin.plugins.jenkins;

import hudson.model.Run;
import hudson.model.TaskListener;

import java.io.IOException;
import java.util.List;

import com.buildcoin.plugins.jenkins.BuildcoinProperty.BuildcoinPropertyDescriptor;
import com.buildcoin.plugins.jenkins.model.Endpoint;


public enum Phase {
	STARTED, COMPLETED, FINISHED;
fdsasdfds
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void handlePhase(Run run, String status, TaskListener listener) {
		BuildcoinProperty property = (BuildcoinProperty) run.getParent().getProperty(BuildcoinProperty.class);
		if (property != null) {
			List<Endpoint> targets = property.getDescriptor().getEndpoints();
			if (property.getDescriptor().isEnabled() && property.isProjectDisabled() == false) {
				for (Endpoint target : targets) {
	                try {
	                    target.getProtocol().sendNotification(target.getUrl(), run.getParent(), run, this, status);
	                } catch (IOException e) {
	                    e.printStackTrace(listener.error("Failed to notify buildcoin"));
	                }
	            }
			}
			return;
		}
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