package io.jenkins.plugins.scmconfig;

import jenkins.scm.impl.mock.MockSCMController;
import jenkins.scm.impl.mock.MockSCMSource;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JenkinsConfigReaderTest {

    @Rule public JenkinsRule r = new JenkinsRule();

    @Test
    public void testGetJenkinsConfigFiles() throws Exception{
        SCMConfigConfiguration.get().setSources(Arrays.asList(new ConfigSource("test", new MockSCMSource(MockSCMController.create(), "test.repo"))));

        File jenkinsRootDir = r.jenkins.getRootDir();
        assertNotNull("jenkins root directory defined", jenkinsRootDir);
        assertTrue("jenkins root directory is an existing directory", jenkinsRootDir.isDirectory());

        File jenkinsConfigFile = new File(jenkinsRootDir, "config.xml");
        assertNotNull("jenkins config file defined", jenkinsConfigFile);
        assertTrue("jenkins config file is an existing file", jenkinsConfigFile.isFile());

        System.err.println("config: " + Files.readAllLines(jenkinsConfigFile.toPath()));
    }
}
