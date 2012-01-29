package com.buildcoin.plugins.jenkins;

import hudson.model.JobProperty;
import hudson.model.AbstractProject;

import java.util.List;

import org.kohsuke.stapler.DataBoundConstructor;

public class BuildcoinProperty extends JobProperty<AbstractProject<?, ?>> {

	final public List<Endpoint> endpoints;

	@DataBoundConstructor
	public BuildcoinProperty(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	public List<Endpoint> getEndpoints() {
		return endpoints;
	}

	public BuildcoinPropertyDescriptor getDescriptor() {
		return (BuildcoinPropertyDescriptor) super.getDescriptor();
	}
}