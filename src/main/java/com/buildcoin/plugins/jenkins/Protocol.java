package com.buildcoin.plugins.jenkins;

import hudson.EnvVars;
import hudson.model.AbstractBuild;
import hudson.model.Job;
import hudson.model.Cause;
import hudson.model.Cause.*;
import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import hudson.model.Run;
import hudson.model.Hudson;
import hudson.triggers.SCMTrigger.SCMTriggerCause;
import hudson.triggers.TimerTrigger.TimerTriggerCause;
import hudson.scm.ChangeLogSet.Entry;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.HttpConnectionManager;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.buildcoin.plugins.jenkins.model.*;

@SuppressWarnings("rawtypes")
public enum Protocol {
	
	HTTP {
		@Override
		protected void send(String url, String data) throws IOException {
			// initialize the POST method
			PostMethod post = new PostMethod(url);
			post.addParameter("payload", data);

			HttpConnectionManagerParams connectionParams = new HttpConnectionManagerParams();
			int connTimeout = 10000;
			int sockTimeout = 10000;
			connectionParams.setConnectionTimeout(connTimeout);
			connectionParams.setSoTimeout(sockTimeout);
			
			// execute the POST
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().setParams(connectionParams);
			int status = client.executeMethod(post); 
			String response = post.getResponseBodyAsString();
		}
	};

	private Gson gson = new GsonBuilder().setFieldNamingPolicy(
			FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

	public void sendNotification(String url, Job job, Run run, Phase phase, String status) throws IOException {
		send(url, buildMessage(job, run, phase, status));
	}

	@SuppressWarnings("deprecation")
	private String buildMessage(Job job, Run run, Phase phase, String status) {
		String rootUrl = Hudson.getInstance().getRootUrl();
		JobState jobState = new JobState();
		jobState.setJobName(job.getName());
		jobState.setJobUrl(rootUrl + job.getUrl());
		BuildState buildState = new BuildState();
		buildState.setBuildNumber(run.getNumber());
		buildState.setBuildUrl(rootUrl + run.getUrl());
		buildState.setPhase(phase);
		buildState.setStatus(status);
		jobState.setBuild(buildState);

		ParametersAction paramsAction = run.getAction(ParametersAction.class);
		if (paramsAction != null && run instanceof AbstractBuild) {
			AbstractBuild build = (AbstractBuild) run;
			EnvVars env = new EnvVars();
			for (ParameterValue value : paramsAction.getParameters())
				if (!value.isSensitive())
					value.buildEnvVars(build, env);
			buildState.setParameters(env);
		}
		if (run instanceof AbstractBuild) {
			AbstractBuild<?, ?> build = (AbstractBuild) run;
			List<ScmChange> scmChanges = new LinkedList<ScmChange>();
			for (Entry change : build.getChangeSet()) {
				ScmChange scmChange = new ScmChange();
				scmChange.setScmAuthor(change.getAuthor().getDisplayName());
				scmChange.setScmMessage(change.getMsg());
				scmChange.setScmRevision(change.getCommitId());
				scmChanges.add(scmChange);
			}
			buildState.setScmChanges(scmChanges);
			List<BuildCause> buildCauses = new LinkedList<BuildCause>();
			for (Cause cause : build.getCauses()) {
				BuildCause buildCause = new BuildCause();
				buildCause.setShortDescription(cause.getShortDescription());
				if (cause.getClass() == Cause.RemoteCause.class) {
					buildCause.setCauseType(CauseType.REMOTE);
				} else if (cause.getClass() == Cause.UpstreamCause.class) {
					UpstreamCause upstreamCause = (UpstreamCause)cause;
					UpstreamBuild upstreamBuild = new UpstreamBuild();
					buildCause.setCauseType(CauseType.UPSTREAM);
					upstreamBuild.setBuildNumber(upstreamCause.getUpstreamBuild());
					upstreamBuild.setBuildUrl(rootUrl + upstreamCause.getUpstreamUrl() + upstreamBuild.getBuildNumber());
					upstreamBuild.setJobName(upstreamCause.getUpstreamProject());
					upstreamBuild.setJobUrl(rootUrl + upstreamCause.getUpstreamUrl());
					buildCause.setUpstreamBuild(upstreamBuild);
				} else if (cause.getClass() == Cause.UserCause.class || cause.getClass() == Cause.UserIdCause.class) {
					buildCause.setCauseType(CauseType.MANUAL);
					if (cause.getClass() == Cause.UserCause.class) { // for backward compatibility
						UserCause userCause = (UserCause)cause;
						buildCause.setUserName(userCause.getUserName());
					} else {
						UserIdCause userCause = (UserIdCause)cause;
						buildCause.setUserName(userCause.getUserName());
					}
				} else if(cause.getClass() == SCMTriggerCause.class) {
					buildCause.setCauseType(CauseType.SCMTRIGGERED);
				} else if(cause.getClass() == TimerTriggerCause.class) {
					buildCause.setCauseType(CauseType.TIMERTRIGGERED);
				} else {
					buildCause.setCauseType(CauseType.UNKNOWN);
				}
				buildCauses.add(buildCause);
			}
			buildState.setCauses(buildCauses);
		}

		return gson.toJson(jobState);
	}

	abstract protected void send(String url, String data) throws IOException;

}