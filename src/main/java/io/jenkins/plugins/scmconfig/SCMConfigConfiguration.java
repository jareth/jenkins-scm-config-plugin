package io.jenkins.plugins.scmconfig;

import hudson.Extension;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Global configuration plugin that manages a list of SCM sources for Jenkins configuration.
 */
@Extension
public class SCMConfigConfiguration extends GlobalConfiguration {

    /** @return the singleton instance */
    public static @Nonnull  SCMConfigConfiguration get() {
        SCMConfigConfiguration instance = GlobalConfiguration.all().get(SCMConfigConfiguration.class);
        if (instance == null) {
            throw new IllegalStateException();
        }
        return instance;
    }

    private List<ConfigSource> sources = new ArrayList<>();

    public SCMConfigConfiguration() {
        // When Jenkins is restarted, load any saved configuration from disk.
        load();
    }

    public List<ConfigSource> getSources() {
        return sources;
    }

    public void setSources(List<ConfigSource> sources) {
        this.sources = sources;
        save();
    }

    @Override public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        setSources(Collections.<ConfigSource>emptyList()); // allow last library to be deleted
        return super.configure(req, json);
    }

}
