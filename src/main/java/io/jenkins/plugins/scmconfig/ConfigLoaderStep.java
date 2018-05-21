package io.jenkins.plugins.scmconfig;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.FreeStyleProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.ListBoxModel;
import jenkins.scm.api.SCMHead;
import jenkins.scm.api.SCMSource;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ConfigLoaderStep extends Builder {

    private final String task;
    private final String source;

    @DataBoundConstructor
    public ConfigLoaderStep(String task, String source) {
        this.task = task;
        this.source = source;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        System.out.println("loader step");

        List<ConfigSource> sources = SCMConfigConfiguration.get().getSources();
        ConfigSource selectedSource= null;
        for (ConfigSource source: sources) {
            if (source.getSourceName().equals(this.source)) {
                selectedSource = source;
            }
        }
        if (selectedSource == null) {
            return false;
        }

        JenkinsConfigReader reader = new JenkinsConfigReader();

        doRetrieve(selectedSource.getScm(), listener);
        return true;
    }

    private void doRetrieve(SCMSource scmSource, BuildListener listener) throws IOException, InterruptedException {
        Set<SCMHead> heads = scmSource.fetch(listener);

        System.out.println("heads");
        System.out.println(heads);
    }

    public String getTask() {
        return task;
    }

    public String getSource() {
        return source;
    }



    @Extension
    public static class Descriptor extends BuildStepDescriptor<Builder> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return FreeStyleProject.class.isAssignableFrom(jobType);
        }

        @Override
        public String getDisplayName() {
            return "execute SCM Config task";
        }

        public ListBoxModel doFillSourceItems() {
            ListBoxModel items = new ListBoxModel();
            for (ConfigSource source : SCMConfigConfiguration.get().getSources()) {
                items.add(source.getSourceName(), source.getSourceName());
            }
            return items;
        }
    }
}