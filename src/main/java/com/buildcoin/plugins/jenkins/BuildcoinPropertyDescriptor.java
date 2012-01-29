package com.buildcoin.plugins.jenkins;

import hudson.Extension;
import hudson.model.JobPropertyDescriptor;
import com.buildcoin.plugins.jenkins.model.Endpoint;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

@Extension
public final class BuildcoinPropertyDescriptor extends JobPropertyDescriptor {
	
	public static String buildcoinUrl;
	
	public static boolean isDebugMode;
	
	public String getBuildcoinUrl() {
		return buildcoinUrl;
	}
	
	public BuildcoinPropertyDescriptor() {
		super(BuildcoinProperty.class);
		load();
	}

	public static List<Endpoint> endpoints = new ArrayList<Endpoint>();
	
	public boolean isEnabled() {
		return !endpoints.isEmpty();
	}
	
	public boolean isDebugMode() {
		return isDebugMode;
	}

	public String getDisplayName() {
		return "Buildcoin integration plugin";
	}
	
	@Override
	public boolean configure(StaplerRequest req, JSONObject formData) {
		buildcoinUrl = formData.getString("buildcoinUrl");
		isDebugMode = formData.getBoolean("buildcoinDebugMode");
		endpoints.clear();
		if (!buildcoinUrl.isEmpty() && !isDebugMode) {
			Endpoint endpoint = new Endpoint();
			endpoint.setProtocol(Protocol.HTTP);
			endpoint.setUrl(formData.getString("buildcoinUrl"));
			endpoints.add(endpoint);
		} else if (isDebugMode) {
			Endpoint endpoint = new Endpoint();
			endpoint.setProtocol(Protocol.HTTP);
			endpoint.setUrl("http://localhost:8080/hooks/" + formData.getString("buildcoinUrl") + "/jenkins");
			endpoints.add(endpoint);
		}
		save();
		return true;
	}

}