package com.buildcoin.plugins.jenkins;

import hudson.Extension;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.model.AbstractProject;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class BuildcoinProperty extends JobProperty<AbstractProject<?, ?>> {
    
    private final String buildcoinJobKey;
    private final boolean buildcoinJobDisabled;
    
    @DataBoundConstructor
    public BuildcoinProperty(String buildcoinJobKey, boolean buildcoinJobDisabled) {
        this.buildcoinJobKey = buildcoinJobKey;
        this.buildcoinJobDisabled = buildcoinJobDisabled;
    }
    
    public String getBuildcoinJobKey() {
        return buildcoinJobKey;
    }
    
    public boolean isBuildcoinJobDisabled() {
        return buildcoinJobDisabled;
    }
    
    public boolean isEnabled() {
	    if (buildcoinJobKey == null || buildcoinJobKey.isEmpty() || buildcoinJobDisabled) {
		return false;
	    }
	    return true;
	}

    @Override
    public BuildcoinPropertyDescriptor getDescriptor() {
	return (BuildcoinPropertyDescriptor) super.getDescriptor();
    }

    @Extension
    public static final class BuildcoinPropertyDescriptor extends JobPropertyDescriptor {

	public BuildcoinPropertyDescriptor() {
	    super(BuildcoinProperty.class);
	    load();
	}

	private String buildcoinKey;
	private boolean isDebugMode;

	public void setDebugMode(boolean isDebugMode) {
	    this.isDebugMode = isDebugMode;
	}

	public boolean isDebugMode() {
	    return isDebugMode;
	}

	public void setBuildcoinKey(String buildcoinKey) {
	    this.buildcoinKey = buildcoinKey;
	}

	public String getBuildcoinKey() {
	    return buildcoinKey;
	}

	public boolean isEnabled() {
	    if (buildcoinKey == null || buildcoinKey.isEmpty()) {
		return false;
	    }
	    return true;
	}

	public boolean isEnabledByDefault() {
	    return true;
	}

	public String getDisplayName() {
	    return "Buildcoin integration plugin";
	}

	@Override
	public boolean configure(StaplerRequest req, JSONObject formData)
		throws FormException {

	    setBuildcoinKey(formData.getString("buildcoinKey"));
	    setDebugMode(formData.getBoolean("buildcoinDebugMode"));
	    
	    save();

	    return super.configure(req, formData);
	}

    }

}