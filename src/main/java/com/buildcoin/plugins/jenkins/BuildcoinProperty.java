package com.buildcoin.plugins.jenkins;

import hudson.Extension;
import hudson.model.JobProperty;
import hudson.model.AbstractProject;
import hudson.model.JobPropertyDescriptor;

import com.buildcoin.plugins.jenkins.model.Endpoint;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

public class BuildcoinProperty extends JobProperty<AbstractProject<?, ?>> {
    
    @Override
    public BuildcoinPropertyDescriptor getDescriptor() {
        return (BuildcoinPropertyDescriptor)super.getDescriptor();
    }

	@Extension
	public static final class BuildcoinPropertyDescriptor extends JobPropertyDescriptor {
		
		private String buildcoinKey;
		
		private boolean isDebugMode;
		
		public String getBuildcoinKey() {
			return buildcoinKey;
		}
		
		public BuildcoinPropertyDescriptor() {
			super(BuildcoinProperty.class);
			load();
		}

		private List<Endpoint> endpoints = new ArrayList<Endpoint>();
		
		public List<Endpoint> getEndpoints() {
			return endpoints;
		}
		
		public boolean isEnabled() {
			return !endpoints.isEmpty();
		}
		
		public boolean isEnabledByDefault() {
			return true;
		}
		
		public boolean isDebugMode() {
			return isDebugMode;
		}

		public String getDisplayName() {
			return "Buildcoin integration plugin";
		}
		
		@Override
		public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
			buildcoinKey = formData.getString("buildcoinKey");
			isDebugMode = formData.getBoolean("buildcoinDebugMode");
			endpoints.clear();
			if (!buildcoinKey.isEmpty() && !isDebugMode) {
				Endpoint endpoint = new Endpoint();
				endpoint.setProtocol(Protocol.HTTP);
				endpoint.setUrl("http://buildcoin.com/hooks/" + formData.getString("buildcoinKey") + "/jenkins");
				endpoints.add(endpoint);
			} else if (!buildcoinKey.isEmpty() && isDebugMode) {
				Endpoint endpoint = new Endpoint();
				endpoint.setProtocol(Protocol.HTTP);
				endpoint.setUrl(formData.getString("buildcoinKey"));
				endpoints.add(endpoint);
			}
			save();
			return super.configure(req,formData);
		}

	}

}