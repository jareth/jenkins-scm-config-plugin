package io.jenkins.plugins.scmconfig;

import hudson.Extension;
import hudson.ExtensionList;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import jenkins.scm.api.SCMSource;
import jenkins.scm.api.SCMSourceDescriptor;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.util.Collection;

/**
 * A source location for loading and storing Jenkins configuration files.
 */
public class ConfigSource extends AbstractDescribableImpl<ConfigSource> {

    private final String sourceName;
    private final SCMSource scm;

    @DataBoundConstructor
    public ConfigSource(String sourceName, SCMSource scm) {

        this.sourceName = sourceName;
        this.scm = scm;
    }

    public String getSourceName() {
        return sourceName;
    }

    public SCMSource getScm() {
        return scm;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<ConfigSource> {

        public FormValidation doCheckName(@QueryParameter String sourceName) {
            if (sourceName.isEmpty()) {
                return FormValidation.error("You must enter a source name.");
            }
            // Currently no character restrictions.
            return FormValidation.ok();
        }

        @Restricted(NoExternalUse.class) // Jelly, Hider
        public Collection<SCMSourceDescriptor> getSCMDescriptors() {
            return ExtensionList.lookup(SCMSourceDescriptor.class);
        }

    }
}
