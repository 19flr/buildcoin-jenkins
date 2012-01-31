package com.buildcoin.plugins.jenkins;

import hudson.EnvVars;
import hudson.model.AbstractBuild;
import hudson.model.Job;
import hudson.model.Cause;
import hudson.model.Cause.*;
import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import hudson.model.Run;
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

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.buildcoin.plugins.jenkins.model.BuildState;
import com.buildcoin.plugins.jenkins.model.BuildCause;
import com.buildcoin.plugins.jenkins.model.JobState;
import com.buildcoin.plugins.jenkins.model.ScmChange;

@SuppressWarnings("rawtypes")
public enum Protocol {

	UDP {
		@Override
		protected void send(String url, byte[] data) throws IOException {
            HostnamePort hostnamePort = HostnamePort.parseUrl(url);
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(hostnamePort.hostname), hostnamePort.port);
            socket.send(packet);
		}
		
	},
	TCP {
		@Override
		protected void send(String url, byte[] data) throws IOException {
            HostnamePort hostnamePort = HostnamePort.parseUrl(url);
            SocketAddress endpoint = new InetSocketAddress(InetAddress.getByName(hostnamePort.hostname), hostnamePort.port);
            Socket socket = new Socket();
            socket.connect(endpoint);
            OutputStream output = socket.getOutputStream();
            output.write(data);
            output.flush();
            output.close();
		}
	},
	HTTP {
		@Override
		protected void send(String url, byte[] data) throws IOException {
            URL targetUrl = new URL(url);
            URLConnection connection = targetUrl.openConnection();
            if (connection instanceof HttpURLConnection)
                ((HttpURLConnection) connection)
                        .setFixedLengthStreamingMode(data.length);
            connection.setDoInput(false);
            connection.setDoOutput(true);
            OutputStream output = connection.getOutputStream();
            output.write(data);
            output.flush();
            output.close();
		}
	};

	private Gson gson = new GsonBuilder().setFieldNamingPolicy(
			FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

	public void sendNotification(String url, Job job, Run run, Phase phase, String status) throws IOException {
		send(url, buildMessage(job, run, phase, status));
	}

	@SuppressWarnings("deprecation")
	private byte[] buildMessage(Job job, Run run, Phase phase, String status) {
		JobState jobState = new JobState();
		jobState.setName(job.getName());
		jobState.setUrl(job.getUrl());
		BuildState buildState = new BuildState();
		buildState.setNumber(run.number);
		buildState.setUrl(run.getUrl());
		buildState.setPhase(phase);
		buildState.setStatus(status);
		
		try {
			buildState.setFullUrl(run.getAbsoluteUrl());
		} catch (IllegalStateException ignored) {
			// Ignored
		}
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
					buildCause.setCauseType(CauseType.UPSTREAM);
					buildCause.setUpstreamBuildNumber(upstreamCause.getUpstreamBuild());
					buildCause.setUpstreamBuildUrl(upstreamCause.getUpstreamUrl());
					buildCause.setUpstreamProject(upstreamCause.getUpstreamProject());
				} else if (cause.getClass() == Cause.UserCause.class || cause.getClass() == Cause.UserIdCause.class) {
					buildCause.setCauseType(CauseType.MANUAL);
					if (cause.getClass() == Cause.UserCause.class) {
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

		return gson.toJson(jobState).getBytes();
	}

	abstract protected void send(String url, byte[] data) throws IOException;

}