package com.buildcoin.plugins.jenkins;

import hudson.model.TaskListener;
import hudson.model.Run;

import java.io.IOException;

import com.buildcoin.plugins.jenkins.BuildcoinProperty.BuildcoinPropertyDescriptor;

public enum Phase {
    COMPLETED;
    Message message = new Message();

    @SuppressWarnings({ "rawtypes" })
    public void handlePhase(Run run, String status, TaskListener listener) {
	BuildcoinProperty jobProperty = null;
	jobProperty = (BuildcoinProperty) run.getParent().getProperty(BuildcoinProperty.class);
	BuildcoinPropertyDescriptor defaultProperty = new BuildcoinPropertyDescriptor();
	
	String url;
	String key = null;
	
	if (defaultProperty.isEnabled() && jobProperty == null) {
	    key = defaultProperty.getBuildcoinKey();
	} else if ((defaultProperty.isEnabled() && !jobProperty.isBuildcoinJobDisabled()) || jobProperty.isEnabled()) {
	    
	    if (jobProperty.getBuildcoinJobKey() != null && !jobProperty.getBuildcoinJobKey().isEmpty()) {
		key = jobProperty.getBuildcoinJobKey();
	    } else {
		// use global config if job does not have a specific Buildcoin key specified
		key = defaultProperty.getBuildcoinKey();
	    }
	}
	
	if (key != null) {
	    if (defaultProperty.isDebugMode()) {
		url = key;
	    } else {
		url = "https://buildcoin.com/hooks/" + key + "/jenkins";
	    }

	    try {
		message.sendNotification(url, run.getParent(), run, this,
			status);
	    } catch (IOException e) {
		e.printStackTrace(listener.error("Failed to notify buildcoin"));
	    }
	}
    }
}