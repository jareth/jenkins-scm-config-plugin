package io.jenkins.plugins.scmconfig;

import jenkins.scm.impl.mock.MockSCMController;
import jenkins.scm.impl.mock.MockSCMSource;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ConfigSourceTest {

    @Rule public JenkinsRule r = new JenkinsRule();

    @Test
    public void testSourceInteraction() {
        MockSCMController controller = MockSCMController.create();
        MockSCMSource scmSource = new MockSCMSource(controller, "https://github.com/jareth/jenkins-scm-config-plugin-example.git");
        ConfigSource configSource = new ConfigSource("test", scmSource);
        SCMConfigConfiguration.get().setSources(Arrays.asList(configSource));

        assertEquals("source added to scm config", 1, SCMConfigConfiguration.get().getSources().size());
    }
}
