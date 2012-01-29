package com.buildcoin.plugins.jenkins;

import hudson.Extension;
import hudson.model.JobPropertyDescriptor;
import hudson.model.Job;
import hudson.util.FormValidation;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

@Extension
public final class BuildcoinPropertyDescriptor extends JobPropertyDescriptor {

	public BuildcoinPropertyDescriptor() {
		super(BuildcoinProperty.class);
		load();
	}

	private List<Endpoint> endpoints = new ArrayList<Endpoint>();

	public boolean isEnabled() {
		return !endpoints.isEmpty();
	}

	public List<Endpoint> getTargets() {
		return endpoints;
	}

	public void setEndpoints(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	@Override
	public boolean isApplicable(@SuppressWarnings("rawtypes") Class<? extends Job> jobType) {
		return true;
	}

	public String getDisplayName() {
		return "Hudson Job Notification";
	}

	@Override
	public BuildcoinProperty newInstance(StaplerRequest req, JSONObject formData) throws FormException {

		List<Endpoint> endpoints = new ArrayList<Endpoint>();
		if (formData != null && !formData.isNullObject()) {
			JSON endpointsData = (JSON) formData.get("endpoints");
			if (endpointsData != null && !endpointsData.isEmpty()) {
				if (endpointsData.isArray()) {
					JSONArray endpointsArrayData = (JSONArray) endpointsData;
					endpoints.addAll(req.bindJSONToList(Endpoint.class, endpointsArrayData));
				} else {
					JSONObject endpointsObjectData = (JSONObject) endpointsData;
					endpoints.add(req.bindJSON(Endpoint.class, endpointsObjectData));
				}
			}
		}
		BuildcoinProperty notificationProperty = new BuildcoinProperty(endpoints);
		return notificationProperty;
	}

	public FormValidation doCheckUrl(@QueryParameter(value = "url", fixEmpty = true) String url, @QueryParameter(value = "protocol") String protocolParameter) {
		Protocol protocol = Protocol.valueOf(protocolParameter);
		try {
			protocol.validateUrl(url);
			return FormValidation.ok();
		} catch (Exception e) {
			return FormValidation.error(e.getMessage());
		}
	}

	@Override
	public boolean configure(StaplerRequest req, JSONObject formData) {
		save();
		return true;
	}

}