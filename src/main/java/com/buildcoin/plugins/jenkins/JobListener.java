package com.buildcoin.plugins.jenkins;

import hudson.Extension;
import hudson.model.Result;
import hudson.model.TaskListener;
import hudson.model.Run;
import hudson.model.listeners.RunListener;

@Extension
@SuppressWarnings("rawtypes")
public class JobListener extends RunListener<Run> {

	public JobListener() {
		super(Run.class);
	}

	@Override
	public void onCompleted(Run r, TaskListener listener) {
		Phase.COMPLETED.handlePhase(r, getStatus(r), listener);
	}

	private String getStatus(Run r) {
		Result result = r.getResult();
		String status = null;
		if (result != null) {
			status = result.toString();
		}
		return status;
	}
}