package io.jenkins.plugins.scmconfig;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.RestartableJenkinsRule;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class SCMConfigConfigurationTest {

    @Rule
    public RestartableJenkinsRule rr = new RestartableJenkinsRule();

    /**
     * Tries to exercise enough code paths to catch common mistakes:
     * <ul>
     * <li>missing {@code load}
     * <li>missing {@code save}
     * <li>misnamed or absent getter/setter
     * <li>misnamed {@code textbox}
     * </ul>
     */
    @Test
    public void uiAndStorage() {
        rr.then(r -> {
            SCMConfigConfiguration scmConfig = SCMConfigConfiguration.get();
            assertEquals("not set initially", 0, scmConfig.getSources().size());

            // Interacting with the 'add' button of stapler's repeatableProperty doesn't work well in test suites,
            // so we add the test ConfigSource manually.
            ConfigSource testSource = new ConfigSource("initialValue", null);
            scmConfig.setSources(Arrays.asList(testSource));

            HtmlPage configurePage = r.createWebClient().goTo("configure");
            HtmlForm config = configurePage.getFormByName("config");
            HtmlTextInput textbox = config.getInputByName("_.sourceName");

            assertEquals("UI shows stored value", "initialValue", textbox.getText());
            assertEquals("Source is populated", 1, scmConfig.getSources().size());

            textbox.setText("updatedValue");
            r.submit(config);
        });
        rr.then(r -> {
            SCMConfigConfiguration scmConfig = SCMConfigConfiguration.get();
            assertEquals("source is still there after restart of Jenkins", 1, scmConfig.getSources().size());
            assertEquals("source values are still there after restart of Jenkins", "updatedValue", scmConfig.getSources().get(0).getSourceName());
        });
    }
}
